package service.model;

import java.util.ArrayList;
import java.util.Observable;

public class RaiseHand extends Observable {

	private int standup;
	private ArrayList<RaiseHandStudent> raiseHand;

	public int getStandup() {
		return standup;
	}

	public void setStandup(int standup) {
		this.standup = standup;
		setChanged();
	}

	public ArrayList<RaiseHandStudent> getRaiseHand() {
		return raiseHand;
	}

	public void setRaiseHand(ArrayList<RaiseHandStudent> raiseHand) {
		this.raiseHand = raiseHand;
	}
}
