package com.latmod.screenshotsgallery;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

/**
 * @author LatvianModder
 */
public class ScreenshotGalleryConfig
{
	public static Configuration config;
	public static Property propertyFolder, propertyOutputURLPrefix, propertyGenerateThumbnails;
	public static String folder, outputURLPrefix;
	public static boolean generateThumbnails;

	public static void init()
	{
		config = new Configuration(new File(System.getProperty("user.home") + File.separatorChar + "Pictures", "ScreenshotGalleryMod.cfg"));
		config.load();

		propertyFolder = config.get("config", "folder", System.getProperty("user.home") + File.separatorChar + "Pictures" + File.separatorChar + "Minecraft Screenshots");
		propertyFolder.setComment("Folder where all screenshots will be stored. Blank means the default screenshots folder.");
		propertyFolder.setLanguageKey(ScreenshotGallery.MOD_ID + ".config.folder");

		propertyOutputURLPrefix = config.get("config", "output_url_prefix", "");
		propertyOutputURLPrefix.setComment("What URL will be printed in chat. If blank, it will print file URL.");
		propertyOutputURLPrefix.setLanguageKey(ScreenshotGallery.MOD_ID + ".config.output_url_prefix");

		propertyGenerateThumbnails = config.get("config", "generate_thumbnails", true);
		propertyGenerateThumbnails.setComment("Generates jpg thumbnails in folder/thumbs directory. Loads gallery faster.");
		propertyGenerateThumbnails.setLanguageKey(ScreenshotGallery.MOD_ID + ".config.generate_thumbnails");

		reloadProperties();
		config.save();
	}

	public static void reloadProperties()
	{
		folder = propertyFolder.getString();
		outputURLPrefix = propertyOutputURLPrefix.getString();
		generateThumbnails = propertyGenerateThumbnails.getBoolean();
	}
}