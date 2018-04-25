package process.model;

public class ScreenShot {

	private int[] rgbArr;
	private int height;
	private int width;
	private long timestamp;

	public ScreenShot(int[] rgbArr, int height, int width, long timestamp) {
		super();
		this.rgbArr = rgbArr;
		this.height = height;
		this.width = width;
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int[] getRgbArr() {
		return rgbArr;
	}

	public void setRgbArr(int[] rgbArr) {
		this.rgbArr = rgbArr;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
