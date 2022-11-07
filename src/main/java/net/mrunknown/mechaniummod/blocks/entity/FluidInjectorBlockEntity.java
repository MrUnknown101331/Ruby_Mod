package net.mrunknown.mechaniummod.blocks.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.mrunknown.mechaniummod.blocks.Custom.FluidInjectorBlock;
import net.mrunknown.mechaniummod.fluid.ModFluids;
import net.mrunknown.mechaniummod.items.ModItems;
import net.mrunknown.mechaniummod.networking.ModMessages;
import net.mrunknown.mechaniummod.recipe.FluidInjectingRecipe;
import net.mrunknown.mechaniummod.recipe.GemInfusingRecipe;
import net.mrunknown.mechaniummod.screens.FluidInjectorScreenHandler;
import net.mrunknown.mechaniummod.utils.FluidStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")

public class FluidInjectorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(30000, 64, 64) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                sendEnergyPacket();
            }
        }
    };


    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 20; //20k MB
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                sendFluidPacket();
            }
        }
    };

    private void sendEnergyPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(energyStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC, data);
        }
    }

    private void sendFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        fluidStorage.variant.toPacket(data);
        data.writeLong(fluidStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, data);
        }
    }

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public FluidInjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_INJECTOR, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluidInjectorBlockEntity.this.progress;
                    case 1 -> FluidInjectorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluidInjectorBlockEntity.this.progress = value;
                    case 1 -> FluidInjectorBlockEntity.this.maxProgress = value;
                }
            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Fluid Injector");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        sendEnergyPacket();
        sendFluidPacket();
        return new FluidInjectorScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public void setEnergyLevel(long energyLevel) {
        this.energyStorage.amount = energyLevel;
    }

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("fluid_injector.progress", progress);
        nbt.putLong("fluid_injector.energy", energyStorage.amount);
        nbt.put("fluid_injector.variant", fluidStorage.variant.toNbt());
        nbt.putLong("fluid_injector.fluid", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("fluid_injector.progress");
        energyStorage.amount = nbt.getLong("fluid_injector.energy");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("fluid_injector.variant"));
        fluidStorage.amount = nbt.getLong("fluid_injector.fluid");
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return switch (slot) {
            case 0 -> stack.getItem() == ModFluids.MECHAFRUIT_JUICE_BUCKET;
            case 1 -> stack.getItem() != ModFluids.MECHAFRUIT_JUICE_BUCKET;
            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        Direction localDir = this.getWorld().getBlockState(this.pos).get(FluidInjectorBlock.FACING);

        if (side == Direction.UP) {
            return false;
        }

        // Down extract 2
        if (side == Direction.DOWN) {
            return (slot == 2 || (slot == 0 && stack.getItem() == Items.BUCKET));
        }

        // Right extract 2
        return switch (localDir) {
            default ->
                    side.getOpposite() == Direction.EAST && (slot == 2 || (slot == 0 && stack.getItem() == Items.BUCKET));
            case EAST ->
                    side.rotateYClockwise() == Direction.EAST && (slot == 2 || (slot == 0 && stack.getItem() == Items.BUCKET));
            case SOUTH -> side == Direction.EAST && (slot == 2 || (slot == 0 && stack.getItem() == Items.BUCKET));
            case WEST ->
                    side.rotateYCounterclockwise() == Direction.EAST && (slot == 2 || (slot == 0 && stack.getItem() == Items.BUCKET));
        };
    }

    private void resetProgress() {
        this.progress = 0;
    }


    public static void tick(World world, BlockPos blockPos, BlockState state, FluidInjectorBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (hasSourceInSlot(entity)) {
            transferFluidToFluidTank(entity);
        }

        if (hasRecipe(entity) && hasEnoughEnergy(entity) && hasEnoughFluid(entity)) {
            entity.progress++;
            extractEnergy(entity);
            markDirty(world, blockPos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private static void transferFluidToFluidTank(FluidInjectorBlockEntity entity) {
        if (canInsertFluidAmountIntoOutputSlot(entity) && canInsertFluidIntoOutputSlot(entity, ModFluids.STILL_MECHAFRUIT_JUICE)) {
            try (Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_MECHAFRUIT_JUICE),
                        FluidStack.convertDropletsToMb(FluidConstants.BUCKET), transaction);
                transaction.commit();
            }
            entity.setStack(0, new ItemStack(Items.BUCKET));
        }
    }

    private static boolean hasSourceInSlot(FluidInjectorBlockEntity entity) {
        return entity.getStack(0).getItem() == ModFluids.MECHAFRUIT_JUICE_BUCKET;
    }

    private static boolean hasEnoughFluid(FluidInjectorBlockEntity entity) {
        return entity.fluidStorage.amount >= 500;
    }

    private static void insertFluidToFluidTank(FluidInjectorBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_MECHAFRUIT_JUICE),
                    500, transaction);
            transaction.commit();
        }
    }

    private static void extractFluid(FluidInjectorBlockEntity entity, long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_MECHAFRUIT_JUICE),
                    amount, transaction);
            transaction.commit();
        }
    }

    private static void extractEnergy(FluidInjectorBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.energyStorage.extract(16, transaction);
            transaction.commit();
        }
    }

    private static boolean hasEnoughEnergy(FluidInjectorBlockEntity entity) {
        return entity.energyStorage.amount >= 16;
    }

    private static void craftItem(FluidInjectorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        if (hasRecipe(entity)) {
            entity.removeStack(1, 1);

            Optional<FluidInjectingRecipe> recipe = entity.getWorld().getRecipeManager()
                    .getFirstMatch(FluidInjectingRecipe.Type.INSTANCE, inventory, entity.getWorld());

            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(), entity.getStack(2).getCount() + 1));
            extractFluid(entity, 500);

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(FluidInjectorBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<FluidInjectingRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(FluidInjectingRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

    private static boolean canInsertFluidIntoOutputSlot(FluidInjectorBlockEntity entity, FlowableFluid stillMechafruitJuice) {
        return entity.fluidStorage.variant.getFluid() == stillMechafruitJuice || entity.fluidStorage.variant.isBlank();
    }

    private static boolean canInsertFluidAmountIntoOutputSlot(FluidInjectorBlockEntity entity) {
        return entity.fluidStorage.getCapacity() > entity.fluidStorage.amount;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }


}