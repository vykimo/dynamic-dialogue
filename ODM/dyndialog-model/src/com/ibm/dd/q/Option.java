package com.ibm.dd.q;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Option")
@XmlAccessorType(XmlAccessType.FIELD)
public class Option {
	private String name;
	private boolean selected=false;
	public Option(){}
	
	public Option(String pname) {
		this.name=pname;
		this.selected=false;
	}

	public String toString(){
		return getName()+" "+isSelected();
	}
	
	// ---------------------------------------------------------------------------------
	// ------------- Getters --- setters 
	// ---------------------------------------------------------------------------------
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
