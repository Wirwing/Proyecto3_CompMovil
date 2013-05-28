package com.fmat.proyecto3.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Bean que contiene los datos del ejercicio recibido por el WS. Implementa la
 * interaz {@link Parcelable} para poder ser enviado entre actividades y
 * Servicios.
 * 
 * @author Irving
 * 
 */
public class Exercise implements Parcelable {

	/**
	 * Identificador cuando se pasa el ejercicio dentro de un Bundle
	 */
	public static final String EXTRA_EXERCISE = "EXTRA_EXERCISE";
	public static final String EXTRA_EXERCISES = "EXTRA_EXERCISES";

	private String id;

	@SerializedName("titulo")
	private String title;

	@SerializedName("fecha")
	private long date;

	@SerializedName("lugar")
	private double[] place;

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
		date = source.readLong();
		place = source.createDoubleArray();
		
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

	public long getDate() {
		return date;
	}

	public void setDate(long fecha) {
		this.date = fecha;
	}

	public double[] getPlace() {
		return place;
	}

	public void setPlace(double[] place) {
		this.place = place;
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
		dest.writeLong(date);
		dest.writeDoubleArray(place);
	}

	@Override
	public String toString() {
		return title;
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
