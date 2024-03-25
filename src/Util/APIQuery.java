package Util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

// Basic class for API calls

public class APIQuery {
	private String urlStr;
	private List<Pair<String, String>> params;
	private String method = "GET";
	private String function;
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public static int OK = 200;
	
	public static interface APICallback {
		public void run(int responseCode, BufferedReader jsonResultReader, String errorMessage);
	}
	
	public APIQuery(String url) {
		this.urlStr = url;
		this.params = new ArrayList<Pair<String, String>>();
	}
	
	public APIQuery(String url, String method) {
		this(url);
		this.method = method;
	}
	
	public APIQuery addParam(String name, String value) {
		params.add(new Pair<String, String>(name, value));
		return this;
	}
	
	public APIQuery key(String key) {
		this.addParam("key", key);
		return this;
	}
	
	public APIQuery function(String name) {
		this.function = name;
		return this;
	}
	
	public void exec(APICallback callback) throws Exception {
		//Add function to url
		urlStr += "/" + function;
		
		//Add params to url
		
		boolean first = true;
		for (var param : params) {
			if(first) {
				urlStr += "?";
				first = false;
			} else {
				urlStr += "&";
			}
			
			param.setVal1(param.getVal1().replace(" ", "+"));
			param.setVal2(param.getVal2().replace(" ", "+"));

			urlStr += param.getVal1() + "=" + param.getVal2();
		}
		
		final var url = new URL(urlStr);
		
		// Execute async
		executor.submit(() -> {								
			
			try {
				// Connect to API
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(method);
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				
				// Check for error
				var responseCode = con.getResponseCode();
				if(responseCode != OK) {
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			        StringBuilder errorMessage = new StringBuilder();
			        String line;
			        while ((line = errorReader.readLine()) != null) {
			            errorMessage.append(line);
			        }
			        errorReader.close();
			        
			        callback.run(responseCode, null, errorMessage.toString());
			        return;
				}
				
		        
	        callback.run(responseCode, new BufferedReader(new InputStreamReader(con.getInputStream())), null);
			} catch (Exception e) {
				callback.run(500, null, e.getMessage());
			}
		});
	}
} 
