package process.model;

public class Config {

	private String sourceProducer;
	private String debugProcessor;
	private String processor;
	private String dir;
	private String debug;
	private int start;
	private int end;
	private double interval;

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getSourceProducer() {
		return sourceProducer;
	}

	public void setSourceProducer(String sourceProducer) {
		this.sourceProducer = sourceProducer;
	}

	public String getDebugProcessor() {
		return debugProcessor;
	}

	public void setDebugProcessor(String debugProcessor) {
		this.debugProcessor = debugProcessor;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}
}
