package com.fmat.proyecto3.service;

public class HttpMethod {
	
	public static final int GET = 0x1;
	public static final int POST = 0x2;
	public static final int PUT = 0x3;
	public static final int DELETE = 0x4;

	public static String verbToString(int verb) {
		switch (verb) {
		case GET:
			return "GET";

		case POST:
			return "POST";

		case PUT:
			return "PUT";

		case DELETE:
			return "DELETE";
		}

		return "";
	}
	
}
