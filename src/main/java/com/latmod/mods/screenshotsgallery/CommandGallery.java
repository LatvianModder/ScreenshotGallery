package com.latmod.mods.screenshotsgallery;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CommandGallery extends CommandBase
{
	@Override
	public String getName()
	{
		return "screenshotgallery";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.screenshotgallery.usage";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		if (args.length > 0)
		{
			Screenshot screenshot = ScreenshotManager.INSTANCE.get(args[0]);

			if (screenshot != null)
			{
				GuiGallery.INSTANCE.setOpenScreenshot(screenshot);
			}
		}

		new Thread(() -> Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(GuiGallery.INSTANCE))).start();
	}
}