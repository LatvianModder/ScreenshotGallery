package com.latmod.screenshotsgallery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		List<IConfigElement> elements = new ArrayList<>();
		elements.add(new ConfigElement(ScreenshotGalleryConfig.propertyFolder));
		elements.add(new ConfigElement(ScreenshotGalleryConfig.propertyOutputURLPrefix));
		elements.add(new ConfigElement(ScreenshotGalleryConfig.propertyGenerateThumbnails));
		return new GuiConfig(p, elements, ScreenshotGallery.MOD_ID, ScreenshotGallery.MOD_ID, false, false, ScreenshotGallery.MOD_NAME)
		{
			@Override
			protected void actionPerformed(GuiButton button)
			{
				if (button.id == 2000)
				{
					entryList.saveConfigElements();
					ScreenshotGalleryConfig.config.save();
					ScreenshotGalleryConfig.reloadProperties();
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