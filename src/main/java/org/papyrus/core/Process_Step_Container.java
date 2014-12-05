package org.papyrus.core;

import java.util.ArrayList;

public class Process_Step_Container {
	
	private ArrayList<Process_Step> process_steps;
	private int maxAmountMeasures;
	
	//-----------------------------------------Constructor------------------------------------------------------
	public Process_Step_Container(){
		process_steps = new ArrayList<Process_Step>();
		maxAmountMeasures=0;
	}
	//-----------------------------------------Getters and setters----------------------------------------------
	public ArrayList<Process_Step> getProcess_steps() {
		return process_steps;
	}
	public int getMaximumMeasures() {
		return maxAmountMeasures;
	}
	public void setMaximumMeasures(int maximumMeasures) {
		this.maxAmountMeasures = maximumMeasures;
	}
	//-----------------------------------------Methods-----------------------------------------------------------	
}
