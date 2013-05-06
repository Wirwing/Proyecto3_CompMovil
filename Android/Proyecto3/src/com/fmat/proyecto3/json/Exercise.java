package com.fmat.proyecto3.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Exercise implements Parcelable {

	private String id;

	@SerializedName("titulo")
	private String title;

	@SerializedName("descripcion")
	private String description;

	@SerializedName("sentencias")
	private String[] statements;

	public Exercise() {

	}

	public Exercise(Parcel source) {

		id = source.readString();
		title = source.readString();
		description = source.readString();
		statements = source.createStringArray();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titulo) {
		this.title = titulo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getStatements() {
		return statements;
	}

	public void setStatements(String[] statements) {
		this.statements = statements;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeStringArray(statements);
	}

	/**
	 * 
	 * This field is needed for Android to be able to create new objects,
	 * individually or as arrays.
	 * 
	 * This also means that you can use use the default constructor to create
	 * the object and use another method to hyrdate it as necessary.
	 * 
	 * I just find it easier to use the constructor. It makes sense for the way
	 * my brain thinks ;-)
	 * 
	 */
	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {

		public Exercise createFromParcel(Parcel in) {
			return new Exercise(in);
		}

		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};

}
