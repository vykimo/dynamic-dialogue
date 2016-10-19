package com.ibm.dd.ctx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Fact represents derived true statement, with a level of confidence
 * @author Jerome Boyer
 *
 */
@XmlType(name="Fact")
@XmlAccessorType(XmlAccessType.FIELD)
public class Fact {

	private String name;
	private String value;
	private double confidence;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public Fact(){}
	public Fact(String n,double c){
		this.name=n;
		this.confidence=c;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
