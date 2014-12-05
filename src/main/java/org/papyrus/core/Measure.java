package org.papyrus.core;

public class Measure {
	
	private float value;	
	private Boolean error;	
	//-----------------------------------------Constructor------------------------------------------------------
	public Measure() {
		value=0;
		error=false;
	}
	//-----------------------------------------Getters and setters----------------------------------------------
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}	
}
