package com.ibm.dd.q;

public class Category {

	private String name;
	private double confidence;
	
	public Category(){}
	
	public Category(String n) {
		this.name=n;
		this.confidence=1.0;
	}
	public Category(String n, double c){
		this.name=n;
		this.confidence=c;
	}
	public String getName() {
		return name;
	}
	public double getConfidence() {
		return confidence;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	
}
