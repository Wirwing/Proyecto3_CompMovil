package com.fmat.proyecto3.todoist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fmat.proyecto3.R;

/**
 * Información sobre la configuración de todoist en este dispositivo
 * @author Fabián Castillo
 *
 */
public class TodoistConfig {
	public final static int NOT_SET = -1;
	/**
	 * Obtiene el token de todoist almacenado
	 * 
	 * @param context
	 * @return el token de todoist o null si no se encuentra
	 */
	public static String findApiToken(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String token = prefs.getString(
				context.getString(R.string.pref_todoist_token), null);
		return token;
	}

	/**
	 * Obtiene el proyecto de todoist almacenado
	 * 
	 * @param context
	 * @return el project de todoist o NOT_SET si no se encuentra
	 */
	public static int findProjectId(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		int projectId = prefs.getInt(
				context.getString(R.string.pref_todoist_project_id),
				TodoistAPI.NOT_SET);
		return projectId;
	}
	
	/**
	 * Averigua si todoist ha sido completamente configurado.
	 * Es decir, si se ha establecido un inicio de sesión y si se ha elegido un proyecto.
	 * @return true si todoist ha sido configurado, false en caso contrario 
	 */
	public static boolean isTodoistConfigured(Context context){
		return findApiToken(context) != null && findProjectId(context) != NOT_SET;
	}

}
