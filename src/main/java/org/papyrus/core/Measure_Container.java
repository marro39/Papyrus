package org.papyrus.core;

import java.util.ArrayList;
import java.util.Comparator;

public class Measure_Container implements Comparator<Measure_Container>{
	private ArrayList<Measure> measureContainer;
	private String measureTitel;
	private float average,totalAmount;
	private int errors;	
	
	//-----------------------------------------Constructor------------------------------------------------------
	public Measure_Container() {
		measureContainer = new ArrayList<Measure>();
		average=0;
		totalAmount=0;
		errors=0;
	}
	//-----------------------------------------Getters and setters----------------------------------------------

	public ArrayList<Measure> getMeasureContainer() {
		return measureContainer;
	}

	public String getMeasureTitel() {
		return measureTitel;
	}

	public void setMeasureTitel(String measureTitel) {
		this.measureTitel = measureTitel;
	}		
	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	//-----------------------------------------Methods-----------------------------------------------------------
	public void calcTotalAmount(){
		for(Measure measure : measureContainer){
			//System.out.println("Value: " + measure.getValue());			
			totalAmount+=measure.getValue();
			//System.out.println("Measurecontainer size is: " + measureContainer.size());
		}
	}
	public void calcAverage(){		
		average=(totalAmount/measureContainer.size());
	}
	public void calcErrors(){
		for(Measure measure : measureContainer){
			if(measure.getError()==true){
				errors++;
			}
		}
	}
	public int compare(Measure_Container o1, Measure_Container o2) {
		return o2.errors - o1.errors; 
	}
	
}
