package net.mrunknown.mechaniummod;

import net.fabricmc.api.ModInitializer;
import net.mrunknown.mechaniummod.items.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//Very important comment
public class MechaniumMod implements ModInitializer {
	public static final String MOD_ID = "mechaniummod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}
}
