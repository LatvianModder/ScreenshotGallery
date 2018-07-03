package com.latmod.screenshotsgallery;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public final class Screenshot implements Comparable<Screenshot>, INBTSerializable<NBTTagCompound>
{
	public final ScreenshotManager manager;
	public final String id;
	public int year, month, day, hour, minute, second, millis;
	public String displayName = "";
	public String localFile = "";
	public String uploadURL = "";
	public int width, height, size;
	public List<String> tags = Collections.emptyList();
	private String formattedFileName = "";

	public int textureID = 0, thumbnailTextureID = 0;

	public Screenshot(ScreenshotManager m, String _id)
	{
		manager = m;
		id = _id;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		if (!displayName.isEmpty())
		{
			nbt.setString("Name", displayName);
		}

		nbt.setByteArray("Time", new byte[] {(byte) month, (byte) day, (byte) hour, (byte) minute, (byte) second});

		if (millis > 0)
		{
			nbt.setShort("Millis", (short) millis);
		}

		if (!localFile.isEmpty())
		{
			nbt.setString("Local", localFile.replace(File.separatorChar, '/'));
		}

		if (!uploadURL.isEmpty())
		{
			nbt.setString("Upload", uploadURL);
		}

		nbt.setShort("Width", (short) width);
		nbt.setShort("Height", (short) height);
		nbt.setInteger("Size", size);

		if (!tags.isEmpty())
		{
			NBTTagList tagsList = new NBTTagList();

			for (String tag : tags)
			{
				tagsList.appendTag(new NBTTagString(tag));
			}

			nbt.setTag("Tags", tagsList);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		displayName = nbt.getString("Name");
		byte[] timeArray = nbt.getByteArray("Time");
		month = timeArray[0];
		day = timeArray[1];
		hour = timeArray[2];
		minute = timeArray[3];
		second = timeArray[4];
		millis = nbt.getInteger("Millis");
		localFile = nbt.getString("Local").replace('/', File.separatorChar);
		uploadURL = nbt.getString("Upload");
		width = nbt.getShort("Width");
		height = nbt.getShort("Height");
		size = nbt.getInteger("Size");

		tags = new ArrayList<>();

		NBTTagList tagList = nbt.getTagList("Tags", Constants.NBT.TAG_STRING);

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			tags.add(tagList.getStringTagAt(i));
		}

		if (tags.isEmpty())
		{
			tags = Collections.emptyList();
		}
	}

	public String toString()
	{
		return id;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Screenshot && id.equals(o.toString());
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public int compareTo(Screenshot o)
	{
		int i = Integer.compare(year, o.year);

		if (i == 0)
		{
			i = Integer.compare(month, o.month);
		}

		if (i == 0)
		{
			i = Integer.compare(day, o.day);
		}

		if (i == 0)
		{
			i = Integer.compare(hour, o.hour);
		}

		if (i == 0)
		{
			i = Integer.compare(minute, o.minute);
		}

		if (i == 0)
		{
			i = Integer.compare(second, o.second);
		}

		if (i == 0)
		{
			i = Integer.compare(millis, o.millis);
		}

		return i;
	}

	public String getFormattedFileName()
	{
		if (formattedFileName.isEmpty())
		{
			formattedFileName = ScreenshotGalleryConfig.formatting.getString()
					.replace("{id}", id)
					.replace("{year}", Integer.toString(year))
					.replace("{month}", String.format("%02d", month))
					.replace("{day}", String.format("%02d", day))
					.replace("{hour}", String.format("%02d", hour))
					.replace("{minute}", String.format("%02d", minute))
					.replace("{second}", String.format("%02d", second))
					.replace("{millis}", String.format("%03d", millis))
					+ ".png";
		}

		return formattedFileName;
	}

	public String getDisplayName()
	{
		return displayName.isEmpty() ? getFormattedFileName() : displayName;
	}
}