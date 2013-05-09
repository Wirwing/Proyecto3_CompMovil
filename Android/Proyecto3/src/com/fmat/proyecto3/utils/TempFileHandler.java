package com.fmat.proyecto3.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;

public class TempFileHandler {

	private static final String SEPARATOR = "###################################";

	public static void createTempFile(BufferedWriter bw, Exercise exercise, ExerciseAnswer answer) throws IOException{

		String[] statements = StatementSorter.rearrangeStatementsByKeys(
				exercise.getStatements(), answer.getAnswerKeys());
		
		long millis = TimeUnit.SECONDS.toMillis(answer.getDurationInSeconds());
		
		String elapsedTime = String.format(
				"Tiempo: %d minutos, %d segundos",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

		//Datos estudiante
		
		bw.append(SEPARATOR);
		bw.newLine();
		bw.append("Estudiante");
		bw.newLine();
		bw.newLine();
		bw.append("Matricula: " + answer.getStudentId());
		bw.newLine();
		bw.append("Nombre: " + answer.getStudentName());
		bw.newLine();
		bw.append("Licenciatura: " + answer.getCareer());
		
		bw.newLine();
		bw.newLine();
		bw.append(SEPARATOR);
		bw.newLine();

		//Datos Ejercicio
		bw.append("Ejercicio");
		bw.newLine();
		bw.append("ID: " + exercise.getId());
		bw.newLine();
		bw.append("Intrucciones: ");
		bw.newLine();
		bw.append(exercise.getDescription());
		bw.newLine();
		bw.newLine();
		bw.append("Sentencias: ");
		bw.newLine();
		bw.append("------------------------");
		bw.newLine();
		for(String statement : statements){
			bw.append(statement);
			bw.newLine();
			bw.newLine();
		}
		bw.append("------------------------");
		bw.newLine();
		bw.newLine();
		bw.append("Duracion Ejercicio: " + elapsedTime);
		bw.newLine();
		bw.append(SEPARATOR);
		bw.newLine();
		
		bw.flush();
		bw.close();
		
	}
}
