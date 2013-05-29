package com.fmat.proyecto3.todoist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Representa a un usuario en Todoist
 * @author Fabián
 *
 */
public class User implements Parcelable{
	@SerializedName("email")
	private String email;
	@SerializedName("full_name")
	private String fullName;
	@SerializedName("id")
	private Integer id;
	@SerializedName("api_token")
	private String apiToken;
	
	public User(){
		
	}
	
	public User(Parcel src){
		email = src.readString();
		fullName = src.readString();
		id = src.readInt();
		apiToken = src.readString();
	}

	/**
	 * @return el id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id el id a establecer
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return el token del api de todoist para este usuario
	 */
	public String getApiToken() {
		return apiToken;
	}
	/**
	 * @param apiToken el token del api de todoist para este usuario
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	/**
	 * @return el email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email el email a establecer
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return el nombre completo
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName el nombre completo a establecer
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(email);
		dest.writeString(fullName);
		dest.writeInt(id);
		dest.writeString(apiToken);
	}

	@Override
	public String toString() {
		return fullName;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
