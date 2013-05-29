package com.fmat.proyecto3.todoist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Representa un proyecto de Todoist
 * 
 * @author Fabián Castillo
 * 
 */
public class Project implements Parcelable {
	@SerializedName("id")
	private Integer id;
	@SerializedName("name")
	private String name;
	@SerializedName("user_id")
	private Integer userId;

	public Project() {

	}

	public Project(Parcel source) {
		id = source.readInt();
		name = source.readString();
		userId = source.readInt();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(userId);
	}

	@Override
	public String toString() {
		return name;
	}

	public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {

		public Project createFromParcel(Parcel in) {
			return new Project(in);
		}

		public Project[] newArray(int size) {
			return new Project[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
