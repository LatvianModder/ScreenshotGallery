package com.latmod.screenshotsgallery;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author LatvianModder
 */
public class ThreadUploadFTP extends Thread
{
	private final Screenshot screenshot;
	private final BufferedImage image;
	private final String password;

	public ThreadUploadFTP(Screenshot s, BufferedImage img, String pass)
	{
		screenshot = s;
		image = img;
		password = pass;
	}

	@Override
	public void run()
	{
		try
		{
			String host = ScreenshotGalleryConfig.FTP.host.getString();
			String user = ScreenshotGalleryConfig.FTP.user.getString();
			String path = ScreenshotGalleryConfig.FTP.path.getString() + "/" + screenshot.getFormattedFileName();
			URL url = new URL(String.format("ftp://%s:%s@%s/%s;type=i", user, password, host, path));
			URLConnection conn = url.openConnection();
			OutputStream outputStream = conn.getOutputStream();
			ImageIO.write(image, "PNG", outputStream);
			outputStream.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
	}
}