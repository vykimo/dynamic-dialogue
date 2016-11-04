package com.ibm.dd.q;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ibm.dd.ctx.CustomerContext;
import com.ibm.dd.ctx.Fact;

@XmlRootElement(name="Assessment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Assessment {
	public static final String NEW="New";
	private String uid;
	private CustomerQuery customerQuery;

	private String status;
	private Date creationDate;
	private Set<Response> responses;
	private CustomerContext customerContext; 
	private Set<Recommendation> recommendations;
	private String customerId;
	private Set<Question> questions = new HashSet<Question>();
	private Question nextQuestion;
	private Response lastResponse;
	private String topLevelMessage;


	private HashMap<String,Fact> derivedFacts =new HashMap<String,Fact>() ;
	
	
	public Assessment(){}

	public Response getResponseToQuestionId(String qid){
		for (Response r : getResponses()) {
			if (r.getQuestionLabel().equals(qid)) return r;
		}
		return null;
	}
	
	public void stopQuestion(String msg){
		Question tk=new Question();
		tk.setLabel(msg);
		setNextQuestion(tk);
	}
	
	public void setResponseToQuestionId(String label,String v){
		Response r = getResponseToQuestionId(label);
		if (r == null) {
			r= new Response();
			r.setQuestionLabel(label);
			getResponses().add(r);
		}
		r.setValue(v);
		setLastResponse(r);
	}
	
	public void addRecommendation(String msg,double w,double c){
		getRecommendations().add(Recommendation.buildRecommendation(msg, w,c));
	}
	
	public void addTask(String msg,double w){
		getRecommendations().add(Recommendation.buildTask(msg, w));
	}
	
	public void addTask(String msg){
		getRecommendations().add(Recommendation.buildTask(msg, 1));
	}
	
	public void referenceApp(String name, String url) {
		getRecommendations().add(Recommendation.buildAppReference(name, url));
	}
	
	public void defineSearch(String search) {
		getRecommendations().add(Recommendation.buildSearch(search));
	}
	
	public void removeRecommendation(String label) {
		for (Recommendation r : this.getRecommendations()) {
			if (r.getMessage().contains(label)) {
				this.getRecommendations().remove(r);
				break;
			}
		}
	}
	
	public boolean isRecommendationPresent(String msg){
		for (Recommendation r: this.getRecommendations()){
			if (r.getMessage().contains(msg)) {
			    return true;
			}
		}	
		return false;
	}
	
	public void addSetNextQuestion(String label,String type,String options) {
		Question q=addQuestion(label,type,options);
		setNextQuestion(q);
	}
	
	public Question addQuestion(String label,String type,String options){
		Question q = new Question();
		q.setLabel(label);
		q.setType(type);
		q.parseOptionsAsCSV(options);
		addQuestion(q);
		return q;
	}
	
	public void addSimpleSetNextQuestion(String label,String type) {
		Question q=addSimpleQuestion(label,type);
		setNextQuestion(q);
	}
	
	public Question  addSimpleQuestion(String label,String type){
		Question q = new Question();
		q.setLabel(label);
		q.setType(type);
		addQuestion(q);
		return q;
	}
	
	
	
	public void addQuestion(Question q) {
		 if (!getQuestions().contains(q))
			 getQuestions().add(q);
	}
	
	public void answerNextQuestion(String rep) {
		Response r = new Response();
		r.setValue(rep);
		r.setQuestionLabel(getNextQuestion().getLabel());
		getResponses().add(r);
		setLastResponse(r);
	}
	
	public void answerNextQuestion(Response r) {
		getResponses().add(r);
		setLastResponse(r);
	}
	
	public void addFact(String n,String v, double c){
		Fact f = new Fact(n,c);
		f.setValue(v);
		addFact(f);
	}
	
	public void removeFact(Fact f){
		getDerivedFacts().remove(f.getName());
		f=null;
	}
	
	public void addFact(Fact f){
		getDerivedFacts().put(f.getName(),f);
	}
	
	// ---------------------------------------------------------------------------------
	// ------------- Getters --- setters 
	// ---------------------------------------------------------------------------------	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<Response> getResponses() {
		if(responses == null)  responses = new HashSet<Response>();
		return responses;
	}

	public void setResponses(Set<Response> responses) {
		this.responses = responses;
	}

	public CustomerContext getCustomerContext() {
		return customerContext;
	}

	public void setCustomerContext(CustomerContext customerContext) {
		this.customerContext = customerContext;
	}

	public Set<Recommendation> getRecommendations() {
		if (recommendations == null) recommendations= new HashSet<Recommendation>();
		return recommendations;
	}

	public void setRecommendations(Set<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}


	public CustomerQuery getCustomerQuery() {
		return customerQuery;
	}

	public void setCustomerQuery(CustomerQuery customerQuery) {
		this.customerQuery = customerQuery;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Question getNextQuestion() {
		return nextQuestion;
	}

	public void setNextQuestion(Question nextQuestion) {
		this.nextQuestion = nextQuestion;
	}

	public Response getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(Response lastResponse) {
		this.lastResponse = lastResponse;
	}
	

	public HashMap<String,Fact>  getDerivedFacts() {
		return derivedFacts;
	}

	public void setDerivedFacts(HashMap<String,Fact>  derivedFacts) {
		this.derivedFacts = derivedFacts;
	}
	
	public String getTopLevelMessage() {
		return topLevelMessage;
	}

	public void setTopLevelMessage(String topLevelMessage) {
		this.topLevelMessage = topLevelMessage;
	}
}
