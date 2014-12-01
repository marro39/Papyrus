package org.papyrus.core;

public class Process {
	private String processStep, meausure;
	private float value;
	private boolean error;
	
	//-------------------------------------Constructor---------------------------------------------------
	public Process(){};
	
	//-------------------------------------Getterns and setters------------------------------------------	
	public String getProcessStep() {
		return processStep;
	}
	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}
	public String getMeausure() {
		return meausure;
	}
	public void setMeausure(String meausure) {
		this.meausure = meausure;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
}
