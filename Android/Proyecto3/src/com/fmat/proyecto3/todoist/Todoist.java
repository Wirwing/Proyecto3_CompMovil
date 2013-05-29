package com.fmat.proyecto3.todoist;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * Acceso a Todoist
 * @author Fabián Castillo
 *
 */
public class Todoist {

	/**Token necesario para la comunicación con Todoist**/
	private String token;

	/**
	 * Constructor 
	 */
	public Todoist() {
		this(null);
	}

	/**
	 * Constructor
	 * @param token token para comunicación con Todoist
	 */
	public Todoist(String token) {
		this.token = token;
	}

	/**
	 * Realiza el inicio de sesión con Todoist
	 * @param email email de usuario
	 * @param password contraseña del usuario
	 * @return Usuario Objeto Usuario con la información del usuario autenticado
	 * @throws TodoistException Si existe algún error en la comunicación o si
	 * la combinación usuario/contraseña es incorrecta
	 */
	public User login(String email, String password) throws TodoistException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		HttpGet request = createRequest(TodoistAPI.METHOD_LOGIN, params);
		User user = execute(request, User.class);
		if (user != null)
			this.token = user.getApiToken();
		return user;
	}

	/**
	 * Obtiene los proyectos del usuario
	 * @return Conjunto de proyectos del usuario
	 */
	public Project[] getProjects() {
		if (this.token != null) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", token);
			HttpGet request = createRequest(TodoistAPI.METHOD_GET_PROJECTS,
					params);
			return execute(request, Project[].class);
		} else {
			return null;
		}
	}

	/**
	 * Crea un nuevo item o tarea en Todoist
	 * @param projectId Id del proyecto del item o tarea
	 * @param content Descripción del item o tarea
	 * @param dateString Fecha del item o tarea. Los formatos permitidos
	 * se describen en: https://todoist.com/Help/timeInsert
	 * @return El Item creado
	 */
	public Item addItem(long projectId, String content, String dateString) {
		if (this.token != null) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", token);
			params.put("project_id", projectId);
			params.put("content", content);
			params.put("date_string", dateString);
			HttpGet request = createRequest(TodoistAPI.METHOD_ADD_ITEM, params);
			return execute(request, Item.class);
		} else {
			return null;
		}
	}
	
	/**
	 * Obtiene el token para comunicación
	 * @return token
	 */
	public String getToken(){
		return token;
	}
	
	/**
	 * Establece el token para comunicación
	 * @param token el token
	 */
	public void setToken(String token){
		this.token = token;
	}

	private HttpGet createRequest(String command, Map<String, Object> parameters) {
		return createRequest(command, parameters, false);
	}

	private HttpGet createRequest(String command,
			Map<String, Object> parameters, boolean secure) {
		List<NameValuePair> qparameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			Object value = entry.getValue();
			String paramValue = value == null ? null : value.toString();
			qparameters.add(new BasicNameValuePair(entry.getKey(), paramValue));
		}

		try {
			String scheme = secure ? "https" : "http";
			int port = secure ? 443 : 80;

			return new HttpGet(URIUtils.createURI(scheme, "todoist.com", port,
					"/API/" + command,
					URLEncodedUtils.format(qparameters, "UTF-8"), null));
		} catch (URISyntaxException e) {
			throw new TodoistException(e);
		}
	}

	private <T> T execute(HttpGet request, final Class<T> resultType) {
		try {
			ResponseHandler<T> handler = new ResponseHandler<T>() {
				public T handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					String answer = EntityUtils.toString(response.getEntity());
					System.out.println("response: " + answer);

					if (response.getStatusLine().getStatusCode() != 200) {
						throw new TodoistException(response.getStatusLine()
								.getReasonPhrase());
					}

					if (Pattern.matches("\".*\"", answer)) {
						throw new TodoistException(answer);
					}

					Gson gson = new Gson();
					return gson.fromJson(answer, resultType);
				}
			};
			HttpClient client = new DefaultHttpClient();
			return client.execute(request, handler);
		} catch (ClientProtocolException e) {
			throw new TodoistException(e);
		} catch (IOException e) {
			throw new TodoistException(e);
		}
	}

}
