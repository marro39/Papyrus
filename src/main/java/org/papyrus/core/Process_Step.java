package org.papyrus.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Process_Step{
	private ArrayList<Measure_Container> measures;
	private String processStep;
	
	//-----------------------------------------Constructor------------------------------------------------------
	public Process_Step(){		
		measures = new ArrayList<Measure_Container>();
	}
	//-----------------------------------------Getters and setters----------------------------------------------
	public ArrayList<Measure_Container> getMeasures() {
		return measures;
	}

	public void setMeasures(ArrayList<Measure_Container> measures) {
		this.measures = measures;
	}

	public String getProcessStep() {
		return processStep;
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}
	//-----------------------------------------Methods----------------------------------------------
	public void sortMeasures(){
		Collections.sort(measures, new Measure_Container());
	}
}
