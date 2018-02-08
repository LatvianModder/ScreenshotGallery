package com.latmod.screenshotsgallery;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = ScreenshotGallery.MOD_ID,
		name = ScreenshotGallery.MOD_NAME,
		version = ScreenshotGallery.VERSION,
		acceptedMinecraftVersions = "[1.12,)",
		clientSideOnly = true,
		guiFactory = "com.latmod.screenshotsgallery.ScreenshotGalleryGuiFactory"
)
public class ScreenshotGallery
{
	public static final String MOD_ID = "screenshotgallery";
	public static final String MOD_NAME = "Screenshot Gallery";
	public static final String VERSION = "@VERSION@";

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		ScreenshotGalleryConfig.init();
	}
}