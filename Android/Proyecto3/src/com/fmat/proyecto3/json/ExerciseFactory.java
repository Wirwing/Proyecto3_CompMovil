package com.fmat.proyecto3.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * Clase estatica que aplana e infla los ejercicios recibidos y enviados por JSON. 
 * 
 * @author Irving
 *
 */
public class ExerciseFactory {

	private static final Gson gson = new Gson();
	
	private ExerciseFactory(){
		
	}
	
	/**
	 * 
	 * Pasa el objeto respuesta a una representacion en cadena JSON.
	 * 
	 * @param answer Respuesta a ejercicio
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JsonSyntaxException
	 */
	public static StringEntity marshallAnswer(ExerciseAnswer answer) throws UnsupportedEncodingException, JsonSyntaxException{
		
		String json = gson.toJson(answer).toString();
		
		return new StringEntity(json);
		
	}
	
	/**
	 * 
	 * Crea un objeto ejercicio desde la cadena de texto JSON.
	 * 
	 * @param responseEntity JSON recibido
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static Exercise unmarshallExercise(HttpEntity responseEntity)
			throws IllegalStateException, IOException {

		InputStreamReader contentReader = new InputStreamReader(
				responseEntity.getContent());

		Exercise exercise = gson.fromJson(contentReader, Exercise.class);

		return exercise;

	}
	
	/**
	 * 
	 * Crea un objeto ejercicio desde la cadena de texto JSON.
	 * 
	 * @param responseEntity JSON recibido
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static Exercise[] unmarshallExercises(HttpEntity responseEntity)
			throws IllegalStateException, IOException {

		InputStreamReader contentReader = new InputStreamReader(
				responseEntity.getContent());

		Exercise[] exercises = gson.fromJson(contentReader, Exercise[].class);

		return exercises;

	}
	
}
