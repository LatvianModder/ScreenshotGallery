package com.latmod.screenshotsgallery;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ScreenshotGallery.MOD_ID)
@Config(modid = ScreenshotGallery.MOD_ID, category = "", name = "../local/client/" + ScreenshotGallery.MOD_ID)
public class ScreenshotGalleryConfig
{
	public static final General general = new General();

	public static class General
	{
		@Config.Comment("Folder where all screenshots will be stored. Blank means the default screenshots folder")
		public String folder = System.getProperty("user.home") + File.separatorChar + "Pictures" + File.separatorChar + "Minecraft Screenshots";

		@Config.Comment("What URL will be printed in chat. If blank, it will print file URL.")
		public String output_url_prefix = "";

		//@Config.Comment("Generates jpg thumbnails in folder/thumbs directory. Loads gallery faster.")
		//public boolean generate_thumbnails = true;
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ScreenshotGallery.MOD_ID))
		{
			ConfigManager.sync(ScreenshotGallery.MOD_ID, Config.Type.INSTANCE);
		}
	}
}