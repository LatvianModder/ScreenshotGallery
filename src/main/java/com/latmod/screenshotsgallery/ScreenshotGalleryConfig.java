package com.latmod.screenshotsgallery;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ScreenshotGalleryConfig
{
	private static Configuration config;

	public static Property folder, formatting, autoUpload;
	public static File folderFile;
	public static String folderFileString;

	public static class FTP
	{
		public static Property host, user, pass, path, url;
	}

	public static void init()
	{
		File file = new File(ScreenshotGallery.dataDirectory, "config.cfg");

		if (!file.exists())
		{
			File oldFile = new File(System.getProperty("user.home") + File.separatorChar + "Pictures", "ScreenshotGalleryMod.cfg");

			if (oldFile.exists() && !oldFile.renameTo(file))
			{
				throw new RuntimeException("Couldn't rename old config file " + oldFile.getAbsolutePath() + " to " + file.getAbsolutePath());
			}
		}

		config = new Configuration(file);
		config.load();

		folder = get("config", "folder", "", "Folder where all screenshots will be stored. Blank means the default screenshots folder, which is ~/Pictures/Minecraft Screenshots.");
		formatting = get("config", "formatting", "{year}-{month}-{day}_{hour}.{minute}.{second}", "Available variables:" +
				"\n{id} - random, 8 symbol [a-z0-9] string, e.g. f2a9hz5j" +
				"\n{year} - 2018+" +
				"\n{month} - 01-12" +
				"\n{day} - 01-31" +
				"\n{hour} - 00-23" +
				"\n{minute} - 00-59" +
				"\n{second} - 00-59" +
				"\n{millis} - 000-999");
		autoUpload = get("config", "auto_upload", true, "Will upload screenshot right after taking it.");

		config.getCategory("ftp").setLanguageKey("screenshotgallery.ftp");
		FTP.host = get("ftp", "host", "", "Host address.");
		FTP.user = get("ftp", "user", "", "Username.");
		FTP.pass = get("ftp", "password", "", "Password. It's not recommended to set it here, because it's saved in plain text!");
		FTP.path = get("ftp", "path", "", "Upload path. Recommended to change it to something else, so it doesn't upload in the default directory.");
		FTP.url = get("ftp", "url", "", "URL from upload path, in case FTP is linked with website, e.g. https://ss.latmod.com/mc/{variables...}. See path comment for variables.");

		saveConfig();
		ScreenshotManager.INSTANCE.loadIndex();
		ScreenshotManager.INSTANCE.saveIndex();
	}

	private static Property get(String category, String name, String def, String comment)
	{
		return config.get(category, name, def, comment).setLanguageKey("screenshotgallery." + category + "." + name);
	}

	private static Property get(String category, String name, boolean def, String comment)
	{
		return config.get(category, name, def, comment).setLanguageKey("screenshotgallery." + category + "." + name);
	}

	public static void saveConfig()
	{
		config.save();
		folderFile = ScreenshotGalleryConfig.folder.getString().isEmpty() ? new File(System.getProperty("user.home") + File.separatorChar + "Pictures" + File.separatorChar + "Minecraft Screenshots") : new File(ScreenshotGalleryConfig.folder.getString());

		if (!folderFile.exists() && !folderFile.mkdirs())
		{
			throw new RuntimeException("Couldn't create screenshots folder " + folderFile.getAbsolutePath());
		}

		folderFileString = folderFile.getAbsolutePath();
	}

	public static List<IConfigElement> getElements()
	{
		List<IConfigElement> elements = new ArrayList<>();
		elements.add(new ConfigElement(folder));
		elements.add(new ConfigElement(formatting));
		elements.add(new ConfigElement(autoUpload));
		elements.add(new ConfigElement(config.getCategory("ftp")));
		return elements;
	}
}