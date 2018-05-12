package process.service;

public interface DispatchService {

	public void init(int classroom);
	
	public void dispatch(SourceService source,PoseService pose,AnalyseService classBehavior,AnalyseService selfStudy);
}
