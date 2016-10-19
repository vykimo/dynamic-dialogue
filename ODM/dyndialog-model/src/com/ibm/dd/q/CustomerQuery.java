package com.ibm.dd.q;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent the query the customer is entering at first interaction with our
 * system
 * @author jeromeboyer
 *
 */

@XmlRootElement(name="CustomerQuery")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerQuery {
	private String firstQueryContent;
	// from the query a Natural Classication
	private Set<Category> categories = new HashSet<Category>();
	private String userId;
	private String acceptedCategory;
	
	public CustomerQuery(){}

	public CustomerQuery(String q) {
		this.firstQueryContent=q;
	}

	public String getFirstQueryContent() {
		return firstQueryContent;
	}

	public void setFirstQueryContent(String firstQueryContent) {
		this.firstQueryContent = firstQueryContent;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	public void addCategory(Category c){
		getCategories().add(c);
	}
	
	public void addCategory(String name){
		Category c= new Category(name);
		getCategories().add(c);
	}

	public void addCategory(String name, double d) {
		Category c= new Category(name,d);
		getCategories().add(c);
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAcceptedCategory() {
		return acceptedCategory;
	}

	public void setAcceptedCategory(String acceptedCategory) {
		this.acceptedCategory = acceptedCategory;
	}
}
