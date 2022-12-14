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
import net.mrunknown.mechaniummod.blocks.Custom.FruitCrusherBlock;
import net.mrunknown.mechaniummod.fluid.ModFluids;
import net.mrunknown.mechaniummod.items.ModItems;
import net.mrunknown.mechaniummod.networking.ModMessages;
import net.mrunknown.mechaniummod.screens.FruitCrusherScreenHandler;
import net.mrunknown.mechaniummod.utils.FluidStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("UnstableApiUsage")

public class FruitCrusherBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
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

    public FruitCrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRUIT_CRUSHER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> FruitCrusherBlockEntity.this.progress;
                    case 1 -> FruitCrusherBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FruitCrusherBlockEntity.this.progress = value;
                    case 1 -> FruitCrusherBlockEntity.this.maxProgress = value;
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
        return Text.literal("Fruit Crusher");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        sendEnergyPacket();
        sendFluidPacket();
        return new FruitCrusherScreenHandler(syncId, inv, this, this.propertyDelegate);
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
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("fruit_crusher.progress", progress);
        nbt.putLong("fruit_crusher.energy", energyStorage.amount);
        nbt.put("fruit_crusher.variant", fluidStorage.variant.toNbt());
        nbt.putLong("fruit_crusher.fluid", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("fruit_crusher.progress");
        energyStorage.amount = nbt.getLong("fruit_crusher.energy");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("fruit_crusher.variant"));
        fluidStorage.amount = nbt.getLong("fruit_crusher.fluid");
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return switch (slot) {
            case 0 -> stack.getItem() != Items.BUCKET;
            case 1 -> stack.getItem() == Items.BUCKET;
            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        Direction localDir = this.getWorld().getBlockState(this.pos).get(FruitCrusherBlock.FACING);

        if (side == Direction.UP) {
            return false;
        }

        // Down extract 2
        if (side == Direction.DOWN) {
            return slot == 2;
        }

        // Right extract 2
        return switch (localDir) {
            default -> side.getOpposite() == Direction.EAST && slot == 2;
            case EAST -> side.rotateYClockwise() == Direction.EAST && slot == 2;
            case SOUTH -> side == Direction.EAST && slot == 2;
            case WEST -> side.rotateYCounterclockwise() == Direction.EAST && slot == 2;
        };
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, FruitCrusherBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (hasBucketInSlot(entity)) {
            transferFluidFromFluidTank(entity);
        }

        if (hasRecipe(entity) && hasEnoughEnergy(entity)) {
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

    private static void transferFluidFromFluidTank(FruitCrusherBlockEntity entity) {
        if (hasEnoughFluid(entity) && entity.getStack(2).isEmpty()) {
            extractFluid(entity, FluidStack.convertDropletsToMb(FluidConstants.BUCKET));
            entity.setStack(2, new ItemStack(ModFluids.MECHAFRUIT_JUICE_BUCKET));
            entity.removeStack(1, 1);
        }
    }

    private static boolean hasEnoughFluid(FruitCrusherBlockEntity entity) {
        return entity.fluidStorage.amount >= FluidStack.convertDropletsToMb(FluidConstants.BUCKET);
    }


    private static boolean hasBucketInSlot(FruitCrusherBlockEntity entity) {
        return entity.getStack(1).getItem() == Items.BUCKET;
    }

    private static void insertFluidToFluidTank(FruitCrusherBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_MECHAFRUIT_JUICE),
                    500, transaction);
            transaction.commit();
        }
    }

    private static void extractFluid(FruitCrusherBlockEntity entity, long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_MECHAFRUIT_JUICE),
                    amount, transaction);
            transaction.commit();
        }
    }

    private static void extractEnergy(FruitCrusherBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.energyStorage.extract(16, transaction);
            transaction.commit();
        }
    }

    private static boolean hasEnoughEnergy(FruitCrusherBlockEntity entity) {
        return entity.energyStorage.amount >= 16;
    }

    private static void craftItem(FruitCrusherBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        if (hasRecipe(entity)) {
            entity.removeStack(0, 1);

            insertFluidToFluidTank(entity);

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(FruitCrusherBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        boolean hasFruitInSlot = inventory.getStack(0).getItem() == ModItems.MECHAFRUIT;

        return hasFruitInSlot && canInsertFluidAmountIntoOutputSlot(entity)
                && canInsertFluidIntoOutputSlot(entity, ModFluids.STILL_MECHAFRUIT_JUICE);
    }

    private static boolean canInsertFluidIntoOutputSlot(FruitCrusherBlockEntity entity, FlowableFluid stillMechafruitJuice) {
        return entity.fluidStorage.variant.getFluid() == stillMechafruitJuice || entity.fluidStorage.variant.isBlank();
    }

    private static boolean canInsertFluidAmountIntoOutputSlot(FruitCrusherBlockEntity entity) {
        return entity.fluidStorage.getCapacity() > entity.fluidStorage.amount;
    }

}