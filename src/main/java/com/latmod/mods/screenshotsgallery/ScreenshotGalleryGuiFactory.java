package com.latmod.mods.screenshotsgallery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Collections;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class ScreenshotGalleryGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft mc)
	{
	}

	@Override
	public boolean hasConfigGui()
	{
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen p)
	{
		return new GuiConfig(p, ScreenshotGalleryConfig.getElements(), ScreenshotGallery.MOD_ID, ScreenshotGallery.MOD_ID, false, false, ScreenshotGallery.MOD_NAME)
		{
			@Override
			protected void actionPerformed(GuiButton button)
			{
				if (button.id == 2000)
				{
					entryList.saveConfigElements();
					ScreenshotGalleryConfig.saveConfig();
					mc.displayGuiScreen(parentScreen);
				}
				else
				{
					super.actionPerformed(button);
				}
			}
		};
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return Collections.emptySet();
	}
}