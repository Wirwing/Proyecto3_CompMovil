package com.fmat.proyecto3.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Bean que contiene los datos del resultado del ejercicio. Implementa la
 * interaz {@link Parcelable} para poder ser enviado entre actividades y
 * Servicios.
 * 
 * @author Irving
 * 
 */
public class ExerciseAnswer implements Parcelable {

	/**
	 * Identificador cuando se pasa la respuesta dentro de un Bundle
	 */
	public static final String EXTRA_EXERCISE_ANSWER = "EXTRA_EXERCISE_ANSWER";
	
	@SerializedName("idEjercicio")
	private String id;

	@SerializedName("matricula")
	private String studentId;

	@SerializedName("nombre")
	private String studentName;

	@SerializedName("licenciatura")
	private String career;

	@SerializedName("duracion")
	private int durationInSeconds;

	@SerializedName("respuestas")
	private int[] answerKeys;

	@SerializedName("comentarios")
	private String comments;

	public ExerciseAnswer() {
		// TODO Auto-generated constructor stub
	}

	ExerciseAnswer(Parcel source) {

		id = source.readString();
		studentId = source.readString();
		studentName = source.readString();
		career = source.readString();
		durationInSeconds = source.readInt();
		answerKeys = source.createIntArray();
		comments = source.readString();

	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	public int[] getAnswerKeys() {
		return answerKeys;
	}

	public void setAnswerKeys(int[] answerKeys) {
		this.answerKeys = answerKeys;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(studentId);
		dest.writeString(studentName);
		dest.writeString(career);
		dest.writeInt(durationInSeconds);
		dest.writeIntArray(answerKeys);
		dest.writeString(comments);

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
	public static final Parcelable.Creator<ExerciseAnswer> CREATOR = new Parcelable.Creator<ExerciseAnswer>() {

		public ExerciseAnswer createFromParcel(Parcel in) {
			return new ExerciseAnswer(in);
		}

		public ExerciseAnswer[] newArray(int size) {
			return new ExerciseAnswer[size];
		}
	};

}
