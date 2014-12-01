package org.papyrus.core;

import java.util.ArrayList;

public class ProcessContainer {
	
	private ArrayList<Process> processCont;
	private int errors;
	private float average, sum;	
	
	//-------------------------------Constructor-----------------------------------------------------
	public ProcessContainer(){
		processCont=new ArrayList<Process>();
		errors=0;
		average=0;
		sum=0;
	}
	
	//-------------------------------Getters and setters---------------------------------------------
	public ArrayList<Process> getProcessCont() {
		return processCont;
	}
	public void setProcessCont(ArrayList<Process> processCont) {
		this.processCont = processCont;
	}
	public int getErrors() {
		return errors;
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public float getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	public float getSum() {
		return sum;
	}
	public void setSum(float sum) {
		this.sum = sum;
	}
	
	//------------------------------------------Methods---------------------------------------------
	public void calculateSum(){
		for(Process process : processCont){
			sum+=process.getValue();
		}
	}
	public void calculateAverage(){
		calculateSum();
		average=sum/processCont.size();
	}
	public void addError(){		
		errors+=1;
	}	
}
