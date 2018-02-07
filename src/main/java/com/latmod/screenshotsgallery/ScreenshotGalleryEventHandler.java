package com.latmod.screenshotsgallery;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
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
		File folder = ScreenshotGalleryConfig.general.folder.isEmpty() ? new File(Minecraft.getMinecraft().mcDataDir, "screenshots") : new File(ScreenshotGalleryConfig.general.folder);

		if (!folder.exists())
		{
			folder.mkdirs();
		}

		if (folder.exists() && folder.isDirectory())
		{
			File file = new File(folder, event.getScreenshotFile().getName());
			event.setScreenshotFile(file);

			ITextComponent output = new TextComponentString(file.getName());

			if (ScreenshotGalleryConfig.general.output_url_prefix.isEmpty())
			{
				output.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath()));
			}
			else
			{
				output.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ScreenshotGalleryConfig.general.output_url_prefix + "/" + file.getName()));
			}

			output.getStyle().setUnderlined(true);
			event.setResultMessage(new TextComponentTranslation("screenshot.success", output));
		}
	}
}