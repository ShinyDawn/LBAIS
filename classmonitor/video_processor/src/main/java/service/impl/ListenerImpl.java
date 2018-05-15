package service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.model.ClassMessage;
import service.model.RaiseHand;
import service.model.RaiseHandStudent;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;
import service.service.ListenerService;

@Service
public class ListenerImpl implements ListenerService, Observer {
	@Autowired
	BehaviorRepository behaviorRepo;
	@Autowired
	StudentRepository studentRepo;

	private static ClassMessage message = new ClassMessage();
	private static ArrayList<RaiseHandStudent> raiseHandStudent = new ArrayList<RaiseHandStudent>();

	@Override
	public int getMessage() {
		return message.getMessage();
	}

	@Override
	public ArrayList<RaiseHandStudent> getRaiseHandStudent() {
		return raiseHandStudent;
	}

	@Override
	public void update(Observable o, Object arg1) {
		if (o instanceof RaiseHand) {
			RaiseHand raiseHand = (RaiseHand) o;
			ArrayList<RaiseHandStudent> newList = raiseHand.getRaiseHand();

			boolean isAnswer = false;
			if (newList.size() > 0) {
				for (int i = 0; i < newList.size(); i++) {
					if (newList.get(i).getAnswer() == 1) {
						isAnswer = true;
						raiseHandStudent.clear();
						raiseHandStudent.add(newList.get(i));
						break;
					}
				}
			}
			if (!isAnswer) {
				for (RaiseHandStudent r : newList) {
					boolean isRepeat = false;
					for (RaiseHandStudent r1 : raiseHandStudent) {
						if (r.getSid() == r1.getSid() || r.getRaiseHand() == 0 || r.getSname() == null) {
							isRepeat = true;
							break;
						}
					}
					if (!isRepeat)
						raiseHandStudent.add(r);
				}
				Comparator<RaiseHandStudent> c = new Comparator<RaiseHandStudent>() {
					@Override
					public int compare(RaiseHandStudent o1, RaiseHandStudent o2) {
						if (o1.getStandup() == 0 && o2.getStandup() != 0)
							return -1;
						else if (o1.getStandup() != 0 && o2.getStandup() == 0)
							return 1;
						else if (o1.getStandup() == 0 && o2.getStandup() == 0 && o1.getRaiseHand() >= o2.getRaiseHand())
							return -1;
						else if (o1.getStandup() == 0 && o2.getStandup() == 0 && o1.getRaiseHand() < o2.getRaiseHand())
							return 1;
						else if ((double) o1.getStandup() / o1.getRaiseHand() < (double) o2.getStandup()
								/ o2.getRaiseHand())
							return -1;
						else
							return 1;
					}
				};
				raiseHandStudent.sort(c);
				for (int i = 0; i < raiseHandStudent.size(); i++) {
					if (raiseHandStudent.get(i).getRaiseHand() == 0) {
						raiseHandStudent.remove(i);
						i--;
					}
				}
			}
			if (raiseHandStudent.size() > 0)
				message.setMessage(1);
		}
	}
}
