package com.ibm.dd.ctx;

import java.util.Date;

public class Product implements Comparable<Product>{
	private String name;
	private String type;
	private String brand;
	private String partNumber;
	private Date acquisitionDate;
	private boolean eligibleForUpgrade=false;
	private boolean deviceProtection=false;
	
	public Product(){}
	
	public Product(String n, String t,String b, String pn){
		this.brand=b;
		this.name=n;
		this.type=t;
		this.partNumber=pn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setAcquisitionDate(Date acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}

	public boolean isEligibleForUpgrade() {
		return eligibleForUpgrade;
	}

	public void setEligibleForUpgrade(boolean eligibleForUpgrade) {
		this.eligibleForUpgrade = eligibleForUpgrade;
	}
	
	public boolean isDeviceProtection() {
		return deviceProtection;
	}

	public void setDeviceProtection(boolean deviceProtection) {
		this.deviceProtection = deviceProtection;
	}

	@Override
	public int compareTo(Product o) {
		return (this.getPartNumber().compareTo(o.getPartNumber()));
	}
}
