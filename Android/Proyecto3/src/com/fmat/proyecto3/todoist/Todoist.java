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

public class Todoist {

	private String token;

	public Todoist() {
		this(null);
	}

	public Todoist(String token) {
		this.token = token;
	}

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
	
	public String getToken(){
		return token;
	}
	
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
