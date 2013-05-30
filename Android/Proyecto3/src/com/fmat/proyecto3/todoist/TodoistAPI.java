package com.fmat.proyecto3.todoist;

/**
 * Información sobre el API de Todoist
 * 
 * @author Fabián
 * 
 */
public final class TodoistAPI {
	/** Servidor de todoist **/
	public final static String HOST = "api.todoist.com/API";
	/** Método para login **/
	public final static String METHOD_LOGIN = "login";
	/** Método para obtener los proyectos del usuario **/
	public final static String METHOD_GET_PROJECTS = "getProjects";
	/** Método agregar item **/
	public final static String METHOD_ADD_ITEM = "addItem";
	/** Valor nulo **/
	public final static int NOT_SET = -1;
}
