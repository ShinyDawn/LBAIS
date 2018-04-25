package process.impl;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

//生成视频文件的首帧为图片
//windows下的版本
public class App {
	// public static final String FFMPEG_PATH = "E:/ffmpeg/ffmpeg.exe";
	public static boolean processImg(String veido_path) {
		try {
			FFmpegFrameGrabber g = new FFmpegFrameGrabber(veido_path + ".AVI");
			g.start();
			for (int i = 0; i < 10; i++) {
				Frame f = g.grabImage();
				Java2DFrameConverter converter = new Java2DFrameConverter();
				String imageMat = "png";
				String FileName = veido_path + "_" + System.currentTimeMillis() + "." + imageMat;
				BufferedImage bi = converter.getBufferedImage(f);
				File output = new File(FileName);

				ImageIO.write(bi, imageMat, output);

			}
			g.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		processImg("D:/workspace/video/openpose/IMG_1928_result");
	}
}
