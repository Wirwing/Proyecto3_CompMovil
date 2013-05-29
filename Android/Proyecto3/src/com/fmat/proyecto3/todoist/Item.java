package com.fmat.proyecto3.todoist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable{
	@SerializedName("user_id")
	private Integer userId;
	@SerializedName("priority")
	private Integer priority;
	@SerializedName("content")
	private String content;
	@SerializedName("project_id")
	private Integer projectId;
	@SerializedName("date_string")
	private String dateString;
	
	public Item(){
		
	}
	
	public Item(Parcel source) {
		userId = source.readInt();
		priority = source.readInt();
		content = source.readString();
		projectId = source.readInt();
		dateString = source.readString();	
	}
	
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the projectId
	 */
	public Integer getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}
	/**
	 * @param dateString the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(userId);
		dest.writeInt(priority);
		dest.writeString(content);
		dest.writeInt(projectId);
		dest.writeString(dateString);	
	}

	@Override
	public String toString() {
		return content;
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
