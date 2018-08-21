package com.latmod.screenshotsgallery;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.ThreadedFileIOBase;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class ScreenshotManager implements Iterable<Screenshot>
{
	private static final char[] ID_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final Random RANDOM = new Random();
	public static final ScreenshotManager INSTANCE = new ScreenshotManager();

	private final LinkedHashMap<String, Screenshot> map = new LinkedHashMap<>();
	private final ArrayList<Screenshot> slist = new ArrayList<>();
	public final List<Screenshot> list = Collections.unmodifiableList(slist);

	private String newID()
	{
		char[] c = new char[8];

		for (int i = 0; i < c.length; i++)
		{
			c[i] = ID_CHARS[RANDOM.nextInt(ID_CHARS.length)];
		}

		String s = new String(c);
		return map.containsKey(s) ? newID() : s;
	}

	public Screenshot newScreenshot()
	{
		Screenshot screenshot = new Screenshot(this, newID());
		map.put(screenshot.id, screenshot);
		updateList();
		return screenshot;
	}

	@Nullable
	public Screenshot get(String id)
	{
		return map.get(id);
	}

	public boolean delete(String id)
	{
		Screenshot screenshot = map.remove(id);

		if (screenshot != null)
		{
			updateList();

			ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
				File file;

				if (!screenshot.localFile.isEmpty())
				{
					file = new File(screenshot.localFile);

					if (file.exists())
					{
						file.delete();
					}
				}

				file = new File(new File(ScreenshotGallery.dataDirectory, "thumbnails"), id + ".jpg");

				if (file.exists())
				{
					file.delete();
				}

				return false;
			});

			return true;
		}

		return false;
	}

	public void loadIndex()
	{
		File file = new File(ScreenshotGallery.dataDirectory, "index.nbt");

		if (file.exists())
		{
			try (FileInputStream input = new FileInputStream(file))
			{
				NBTTagCompound nbt = CompressedStreamTools.readCompressed(input);

				for (String syear : nbt.getKeySet())
				{
					int year = Integer.parseInt(syear);

					NBTTagCompound nbt1 = nbt.getCompoundTag(syear);

					for (String id : nbt1.getKeySet())
					{
						Screenshot screenshot = new Screenshot(this, id);
						screenshot.deserializeNBT(nbt1.getCompoundTag(id));
						screenshot.year = year;
						map.put(screenshot.id, screenshot);
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			ScreenshotGallery.LOGGER.info("Found " + map.size() + " screenshots in " + ScreenshotGalleryConfig.folderFile.getAbsolutePath());
			return;
		}

		updateList();

		File[] files = ScreenshotGalleryConfig.folderFile.listFiles();

		if (files != null && files.length > 0)
		{
			for (File file1 : files)
			{
				if (file1 != null && file1.isFile() && file1.getName().endsWith(".png"))
				{
					Screenshot screenshot = new Screenshot(this, newID());
					long size = file1.length();

					if (size > Integer.MAX_VALUE)
					{
						continue;
					}

					screenshot.size = (int) size;

					try
					{
						BufferedImage image = ImageIO.read(file1);
						String fileName = file1.getName();
						screenshot.year = Integer.parseInt(fileName.substring(0, 4));
						screenshot.month = Integer.parseInt(fileName.substring(5, 7));
						screenshot.day = Integer.parseInt(fileName.substring(8, 10));
						screenshot.hour = Integer.parseInt(fileName.substring(11, 13));
						screenshot.minute = Integer.parseInt(fileName.substring(14, 16));
						screenshot.second = Integer.parseInt(fileName.substring(17, 19));
						screenshot.millis = 0;
						screenshot.localFile = file1.getAbsolutePath();
						screenshot.width = image.getWidth();
						screenshot.height = image.getHeight();
						map.put(screenshot.id, screenshot);
					}
					catch (Exception ex)
					{
						ScreenshotGallery.LOGGER.error("Error loading " + file);
						ex.printStackTrace();
					}
				}
			}

			ScreenshotGallery.LOGGER.info("Loaded " + map.size() + " screenshots in " + ScreenshotGalleryConfig.folderFile.getAbsolutePath());
			saveIndex();
		}
	}

	public boolean saveIndex()
	{
		try (FileOutputStream out = new FileOutputStream(new File(ScreenshotGallery.dataDirectory, "index.nbt")))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			Int2ObjectOpenHashMap<NBTTagCompound> yearMap = new Int2ObjectOpenHashMap<>();

			for (Screenshot screenshot : map.values())
			{
				NBTTagCompound nbt1 = yearMap.get(screenshot.year);

				if (nbt1 == null)
				{
					nbt1 = new NBTTagCompound();
					yearMap.put(screenshot.year, nbt1);
					nbt.setTag(Integer.toString(screenshot.year), nbt1);
				}

				nbt1.setTag(screenshot.id, screenshot.serializeNBT());
			}

			CompressedStreamTools.writeCompressed(nbt, out);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return false;
	}

	private void updateList()
	{
		slist.clear();
		slist.addAll(map.values());
	}

	@Override
	public Iterator<Screenshot> iterator()
	{
		return slist.iterator();
	}
}