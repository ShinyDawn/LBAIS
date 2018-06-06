package service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class Background {

	public static void main(String[] args) {
		try {
			File file = new File("D:/workspace/video/openpose/IMG_1928_result.jpg");
			BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
			long start = System.currentTimeMillis();
			int[] rgbArr = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
			rgbArr = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), rgbArr, 0,
					bufferedImage.getWidth());

			for (int i = 0; i < rgbArr.length; i++) {
				Color c = new Color(rgbArr[i]);
				int r = c.getRed();
				r += c.getGreen();
				r += c.getBlue();
				r /= 3;
				c = new Color(r, r, r);
				rgbArr[i] = c.getRGB();
			}

			bufferedImage.setRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), rgbArr, 0,
					bufferedImage.getWidth());
			long end = System.currentTimeMillis();

			System.out.println(end - start);

			ImageIO.write(bufferedImage, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
