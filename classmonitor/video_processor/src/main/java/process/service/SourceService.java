package process.service;

public interface SourceService {

	public void init(String dir,String target);
	
	public String getNextSource();
}
