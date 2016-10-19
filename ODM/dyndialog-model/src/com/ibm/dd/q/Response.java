package com.ibm.dd.q;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
	private String id;
	private String questionLabel;
	private String value;
	
	
	public Response(){}
	
	public int getResponseNumericValue(){
		if (value == null) return 0;
		if (value.isEmpty()) return 0;
		return Integer.parseInt(value);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionLabel() {
		return questionLabel;
	}

	public void setQuestionLabel(String questionId) {
		this.questionLabel = questionId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	// ---------------------------------------------------------------------------------
	// ------------- Getters --- setters 
	// ---------------------------------------------------------------------------------

}
