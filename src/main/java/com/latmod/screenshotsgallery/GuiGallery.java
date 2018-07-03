package com.latmod.screenshotsgallery;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author LatvianModder
 */
public class GuiGallery extends GuiScreen
{
	public static final GuiGallery INSTANCE = new GuiGallery();

	public Screenshot current = null, toOpen = null;

	private GuiGallery()
	{
	}

	public void setOpenScreenshot(@Nullable Screenshot screenshot)
	{
		toOpen = screenshot;
	}

	@Override
	public void initGui()
	{
		buttonList.clear();

		if (current != null)
		{
			buttonList.add(new GuiButton(0, width - 24, 6, 18, 18, "gui.close")); //LANG
		}
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		if (button.id == 0)
		{
			setOpenScreenshot(null);
		}
	}

	@Override
	public void onGuiClosed()
	{
		if (current != null && current.textureID != 0)
		{
			GlStateManager.deleteTexture(current.textureID);
			current.textureID = 0;
		}
	}

	private void rect(double x, double y, double w, double h)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		buffer.pos(x, y + h, 0).endVertex();
		buffer.pos(x + w, y + h, 0).endVertex();
		buffer.pos(x + w, y, 0).endVertex();
		buffer.pos(x, y, 0).endVertex();
		tessellator.draw();
	}

	private void texturedRect(double x, double y, double w, double h, double u0, double v0, double u1, double v1)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + h, 0).tex(u0, v1).endVertex();
		buffer.pos(x + w, y + h, 0).tex(u1, v1).endVertex();
		buffer.pos(x + w, y, 0).tex(u1, v0).endVertex();
		buffer.pos(x, y, 0).tex(u0, v0).endVertex();
		tessellator.draw();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (current != toOpen)
		{
			onGuiClosed();
			current = toOpen;
			initGui();
		}

		drawDefaultBackground();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		if (current != null && current.textureID == 0)
		{
			BufferedImage image = null;
			File local = new File(current.localFile);

			if (local.exists())
			{
				try
				{
					image = ImageIO.read(local);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

			if (image == null && !current.uploadURL.isEmpty())
			{
				try
				{
					image = ImageIO.read(new URL(current.uploadURL));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

			if (image != null)
			{
				int w = image.getWidth();
				int h = image.getHeight();
				ByteBuffer buffer = GLAllocation.createDirectByteBuffer(3 * w * h);

				for (int c : image.getRGB(0, 0, w, h, null, 0, w))
				{
					buffer.put((byte) (c >> 16));
					buffer.put((byte) (c >> 8));
					buffer.put((byte) c);
				}

				buffer.flip();

				current.textureID = GlStateManager.generateTexture();
				GlStateManager.bindTexture(current.textureID);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, w, h, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
			}
		}

		if (current != null && current.textureID != 0)
		{
			double w = width - 10;
			double h = 20;

			double x = 5;
			double y = 5;

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			GlStateManager.color(1F, 1F, 1F, 0.4F);
			rect(x, y, w, h);

			w = width - 10;
			h = height - 35;

			double scale = Math.max(current.width / w, current.height / h);

			if (scale > 1D)
			{
				w = current.width / scale;
				h = current.height / scale;
			}

			x = (width - w) / 2D;
			y = 25 + (height - h - 25) / 2D;

			GlStateManager.color(0F, 0F, 0F, 0.7F);
			rect(x, y, w, h);

			x += 1;
			y += 1;
			w -= 2;
			h -= 2;

			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.enableTexture2D();
			GlStateManager.bindTexture(current.textureID);

			GlStateManager.color(1F, 1F, 1F, 1F);
			texturedRect(x, y, w, h, 0, 0, 1, 1);
		}

		drawCenteredString(fontRenderer, current == null ? I18n.format("sidebar_button.screenshotgallery.gallery") : current.getDisplayName(), width / 2, 11, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == Keyboard.KEY_ESCAPE || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
		{
			if (mc.player != null)
			{
				mc.player.closeScreen();
			}
			else
			{
				mc.displayGuiScreen(null);
			}
		}
	}
}