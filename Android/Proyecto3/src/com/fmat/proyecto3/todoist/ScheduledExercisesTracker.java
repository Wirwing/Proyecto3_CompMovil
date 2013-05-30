package com.fmat.proyecto3.todoist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Lleva el registro de los Ejercicios que el usuario ha calendarizado y los
 * persiste en la aplicación
 * 
 * @author Fabián
 */
public class ScheduledExercisesTracker {

	private DbHelper dbHelper;

	/**
	 * Constructor
	 */
	public ScheduledExercisesTracker(Context context) {
		dbHelper = new DbHelper(context);
	}

	/**
	 * Averigua si determinado ejercicio ha sido calendarizado
	 * 
	 * @param exerciseId
	 *            Identificador del ejercicio
	 * @return true si el ejercicio ha sido calendarizado, false en caso
	 *         contrario
	 */
	public boolean isScheduled(String exerciseId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(DbHelper.TABLE_EXERCISE,
				new String[] { DbHelper.COLUMN_EXERCISE_ID },
				DbHelper.COLUMN_EXERCISE_ID + " = ? ",
				new String[] { exerciseId }, null, null, null);
		boolean found = cursor.moveToFirst();
		cursor.close();
		db.close();
		return found;
	}

	/**
	 * Registra un ejercicio como calendarizado
	 * @param exerciseId
	 *            Identificador del ejercicio
	 */
	public void markAsScheduled(String exerciseId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_EXERCISE_ID, exerciseId);
		db.insert(DbHelper.TABLE_EXERCISE, null, values);
		db.close();
	}

	/**
	 * PRECAUCIÓN
	 * Limpia el registro de ejercicios calendarizados.
	 */
	public void clearScheduleRegistry() {
		dbHelper.restart();
	}

	private class DbHelper extends SQLiteOpenHelper {

		private final static String DATABASE_NAME = "ScheduledExercises.db";
		private final static int DATABASE_VERSION = 1;
		private final static String TABLE_EXERCISE = "exercise";
		private final static String COLUMN_EXERCISE_ID = "id";
		private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_EXERCISE + " (" + COLUMN_EXERCISE_ID
				+ " TEXT PRIMARY KEY NOT NULL);";
		
		private final static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_EXERCISE + ";";

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void restart(){
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(SQL_DELETE_TABLE);
			db.execSQL(SQL_CREATE_TABLE);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_TABLE);
			onCreate(db);
		}

	}

}
