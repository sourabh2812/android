package com.idealyticapps.stoprape.bean;

public class UserMarker {

	private String name;

	private String phoneNbr;

	private Double mLatitude;

	private Double mLongitude;

	private int dangerFlag;

	private Boolean isCurrentUser;

	public UserMarker(String name, String phoneNbr, Double latitude, Double longitude, int dangerFlag,
			Boolean isCurrentUser) {
		this.name = name;
		this.phoneNbr = phoneNbr;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.dangerFlag = dangerFlag;
		this.isCurrentUser = isCurrentUser;
	}

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
	 * @return the phoneNbr
	 */
	public String getPhoneNbr() {
		return phoneNbr;
	}

	/**
	 * @param phoneNbr
	 *            the phoneNbr to set
	 */
	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	/**
	 * @return the mLatitude
	 */
	public Double getmLatitude() {
		return mLatitude;
	}

	/**
	 * @param mLatitude
	 *            the mLatitude to set
	 */
	public void setmLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}

	/**
	 * @return the mLongitude
	 */
	public Double getmLongitude() {
		return mLongitude;
	}

	/**
	 * @param mLongitude
	 *            the mLongitude to set
	 */
	public void setmLongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
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

	/**
	 * @return the isCurrentUser
	 */
	public Boolean getIsCurrentUser() {
		return isCurrentUser;
	}

	/**
	 * @param isCurrentUser
	 *            the isCurrentUser to set
	 */
	public void setIsCurrentUser(Boolean isCurrentUser) {
		this.isCurrentUser = isCurrentUser;
	}

}