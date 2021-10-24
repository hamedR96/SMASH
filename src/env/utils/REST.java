package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class REST {

	private String rootURL;

	public REST(String rootURL) {
		this.rootURL = rootURL;
		System.out.println("Creating REST connection: " + rootURL);
	}
	
	static public enum RestAcceptFormat {
		TEXT("text/plain"),
		XML("text/xml"),
		JSON("application/json");

		private final String text;

		RestAcceptFormat(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	public String send(String restRequest, RestAcceptFormat accept) {
		System.out.println("Send REST command: " + rootURL + restRequest);
		
		try {
			URL url = new URL(rootURL + restRequest.replace("#", "%23").replace(" ", "%20"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("accept", accept.toString());
			connection.connect();
			System.out.println(connection.getURL());
			switch (connection.getResponseCode()) {
			case HttpURLConnection.HTTP_OK:
			case HttpURLConnection.HTTP_NO_CONTENT:
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				return response.toString();
			}
		} catch (Exception e) {
			System.out.println("Error creating REST connection: " + e.toString());
		}
		return null;
	}
}
