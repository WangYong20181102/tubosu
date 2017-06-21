package com.tbs.tobosutype.model;

import java.io.Serializable;
public class City implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String nm;
	private String py;
	private String firstPY;
	
	public String getFirstPY() {
		return firstPY;
	}

	public City() {
		super();
	}

	public City(String id, String nm, String py,String firstPY) {
		super();
		this.id = id;
		this.nm = nm;
		this.py = py;
		this.firstPY=firstPY;
	}

	public String getId() {
		return id;
	}

	public String getNm() {
		return nm;
	}

	public String getPy() {
		return py;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", nm=" + nm + ", py=" + py + "]";
	}
	
}
