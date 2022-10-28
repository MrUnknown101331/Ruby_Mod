package net.mrunknown.rubymod;

import net.fabricmc.api.ModInitializer;
import net.mrunknown.rubymod.items.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//Very important comment
public class RubyMod implements ModInitializer {
	public static final String MOD_ID = "rubymod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}
}
