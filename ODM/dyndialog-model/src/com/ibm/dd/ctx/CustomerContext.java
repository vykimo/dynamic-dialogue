package com.ibm.dd.ctx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CustomerContext")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerContext {
	private String customerID;
	private int vipPoints;
	private HashMap<String,Fact> derivedFacts =new HashMap<String,Fact>() ;
	private Set<Product> ownedProducts = new HashSet<Product>();
	private Product impactedProduct;

	
	public CustomerContext(){}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public HashMap<String,Fact>  getDerivedFacts() {
		return derivedFacts;
	}

	public void setDerivedFacts(HashMap<String,Fact>  derivedFacts) {
		this.derivedFacts = derivedFacts;
	}

	public Set<Product> getOwnedProducts() {
		return ownedProducts;
	}

	public void setOwnedProducts(Set<Product> ownedProducts) {
		this.ownedProducts = ownedProducts;
	}

	public int getVipPoints() {
		return vipPoints;
	}

	public void setVipPoints(int vipPoints) {
		this.vipPoints = vipPoints;
	}

	public Product getImpactedProduct() {
		return impactedProduct;
	}

	public void setImpactedProduct(Product impactedProduct) {
		this.impactedProduct = impactedProduct;
	}
}
