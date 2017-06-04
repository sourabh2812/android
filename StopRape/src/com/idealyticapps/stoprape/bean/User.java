package com.idealyticapps.stoprape.bean;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3200348961839049924L;
	/**
	 * 
	 */
	private String name;

	private String phoneNo;

	private String emailId;

	private Double longitude;

	private Double latitude;

	private Double distance;

	private String password;

	private String lastUpdated;

	private Boolean isCurrentUser;

	private int dangerFlag;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo
	 *            the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the lastUpdated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the isCurrentUser
	 */
	public Boolean isCurrentUser() {
		return isCurrentUser;
	}

	/**
	 * @param isCurrentUser
	 *            the isCurrentUser to set
	 */
	public void setIsCurrentUser(Boolean isCurrentUser) {
		this.isCurrentUser = isCurrentUser;
	}

	/**
	 * @return the dangerFlag
	 */
	public int getDangerFlag() {
		return dangerFlag;
	}

	/**
	 * @param dangerFlag
	 *            the dangerFlag to set
	 */
	public void setDangerFlag(int dangerFlag) {
		this.dangerFlag = dangerFlag;
	}

}