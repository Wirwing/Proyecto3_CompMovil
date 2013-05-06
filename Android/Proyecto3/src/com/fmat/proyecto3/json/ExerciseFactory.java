package com.fmat.proyecto3.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ExerciseFactory {

	private static final Gson gson = new Gson();
	
	public static StringEntity marshallAnswer(ExerciseAnswer answer) throws UnsupportedEncodingException, JsonSyntaxException{
		
		String json = gson.toJson(answer).toString();
		
		return new StringEntity(json);
		
	}
	
	public static Exercise unmarshallExercise(HttpEntity responseEntity)
			throws IllegalStateException, IOException {

		InputStreamReader contentReader = new InputStreamReader(
				responseEntity.getContent());

		Exercise exercise = gson.fromJson(contentReader, Exercise.class);

		return exercise;

	}
	
}
