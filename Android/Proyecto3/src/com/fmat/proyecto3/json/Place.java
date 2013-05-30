package com.fmat.proyecto3.json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean que contiene los datos de la ubicacion de un ejercicio
 * @author Irving
 *
 */
public class Place implements Parcelable	{

	private double latitude;
	private double longitude;
	private double radious;
	
	
	public Place(Parcel source) {

		latitude = source.readDouble();
		longitude = source.readDouble();
		radious = source.readDouble();

	}
	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeDouble(radious);
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
	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {

		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};
	
	
	
}
