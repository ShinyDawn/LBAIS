package process.service;

public interface InitService {
	
	public void init();
	
	public void initSourceProducer();
	
	public void initProcessor();
	
	public void initDebugProcessor();
}
