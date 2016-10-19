package com.ibm.dd.q;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Recommendation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Recommendation {
    protected String message;
    protected double weight;
    protected String type;
    protected double confidence;
    protected Date creationDate;
    protected String url;
    


	protected boolean accepted=true;
    
    public Recommendation(String msg,String t,double w,double c){
    	this.weight=w;
    	this.type=t;
    	this.confidence=c;
    	setMessage(msg);
    	this.creationDate=new Date();
    }
    
    public Recommendation(String msg,String t,String u,double w,double c){
    	this.weight=w;
    	this.type=t;
    	this.confidence=c;
    	this.url=u;
    	setMessage(msg);
    	this.creationDate=new Date();
    }
    
    public Recommendation(){}
    
    public String toString(){
    	StringBuffer sb = new StringBuffer();
    	if (getType().contains("Task"))
    		sb.append("Task: "+getMessage());
    	else 
    		sb.append("Recommendation: "+getMessage());
    	return sb.toString();
    }
    
    public static Recommendation buildTask(String msg,double w){
    	return new Recommendation(msg, "Task", w,1);
    }
    
	public static Recommendation buildRecommendation(String msg, double w,
			double c) {
		return new Recommendation(msg, "Recommendation", w,c);
	}

	public static Recommendation buildAppReference(String msg, String url) {
		return new Recommendation(msg, "App", url,1,1);
	}
	
	public static Recommendation buildSearch(String msg) {
		return new Recommendation(msg, "Search", 1,1);
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		if(getType() == null) this.message = msg;
		if (getType().contains("Task")) this.message="Add task: "+msg+" in the plan";
		if (getType().contains("App")) this.message="Add app: "+msg+" at "+getUrl() +" in the plan";
		if (getType().contains("Phase")) this.message="Add this phase: "+msg+" in the plan";
		if (getType().contains("Search")) this.message="Add this phase: "+msg+" in the plan";
		if (getType().contains("Recommendation")) this.message = msg;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	@Override
	public int hashCode() {
		return message.hashCode();
	};
	
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof Recommendation) {
			Recommendation that = (Recommendation)o;
			return this.getMessage().equals(that.getMessage());
		}
		
		return false;
	};
    
    public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
