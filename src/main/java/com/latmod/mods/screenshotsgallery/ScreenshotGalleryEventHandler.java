package com.latmod.mods.screenshotsgallery;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ScreenshotGallery.MOD_ID, value = Side.CLIENT)
public class ScreenshotGalleryEventHandler
{
	@SubscribeEvent
	public static void onScreenshot(ScreenshotEvent event)
	{
		if (ScreenshotGalleryConfig.folderFile.exists() && ScreenshotGalleryConfig.folderFile.isDirectory())
		{
			long size = event.getScreenshotFile().length();

			if (size > Integer.MAX_VALUE)
			{
				return;
			}

			Screenshot screenshot = ScreenshotManager.INSTANCE.newScreenshot();
			String fileName = event.getScreenshotFile().getName();
			screenshot.year = Integer.parseInt(fileName.substring(0, 4));
			screenshot.month = Integer.parseInt(fileName.substring(5, 7));
			screenshot.day = Integer.parseInt(fileName.substring(8, 10));
			screenshot.hour = Integer.parseInt(fileName.substring(11, 13));
			screenshot.minute = Integer.parseInt(fileName.substring(14, 16));
			screenshot.second = Integer.parseInt(fileName.substring(17, 19));
			screenshot.millis = (int) (System.currentTimeMillis() % 1000L);
			screenshot.size = (int) size;
			screenshot.width = event.getImage().getWidth();
			screenshot.height = event.getImage().getHeight();

			File file = new File(ScreenshotGalleryConfig.folderFile, screenshot.getFormattedFileName());
			event.setScreenshotFile(file);
			screenshot.localFile = file.getAbsolutePath();

			ThreadedFileIOBase.getThreadedIOInstance().queueIO(ScreenshotManager.INSTANCE::saveIndex);

			ITextComponent output = new TextComponentString(screenshot.getDisplayName());
			output.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/screenshotgallery " + screenshot.id));
			output.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Open #" + screenshot.id + " in gallery")));
			output.getStyle().setUnderlined(true);
			event.setResultMessage(new TextComponentTranslation("screenshot.success", output));
			//event.setCanceled(true);
		}
	}
}