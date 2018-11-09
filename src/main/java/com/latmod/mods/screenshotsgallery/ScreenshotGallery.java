package com.latmod.mods.screenshotsgallery;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
		modid = ScreenshotGallery.MOD_ID,
		name = ScreenshotGallery.MOD_NAME,
		version = ScreenshotGallery.VERSION,
		clientSideOnly = true,
		guiFactory = "com.latmod.mods.screenshotsgallery.ScreenshotGalleryGuiFactory"
)
public class ScreenshotGallery
{
	public static final String MOD_ID = "screenshotgallery";
	public static final String MOD_NAME = "Screenshot Gallery";
	public static final String VERSION = "0.0.0.screenshotgallery";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static File dataDirectory;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		dataDirectory = new File(System.getProperty("user.home"), ".mcscreenshotgallery");

		if (!dataDirectory.exists() && !dataDirectory.mkdirs())
		{
			throw new RuntimeException("Couldn't create Screenshots Gallery data folder at " + dataDirectory.getAbsolutePath());
		}

		ScreenshotGalleryConfig.init();
		ClientCommandHandler.instance.registerCommand(new CommandGallery());
	}
}