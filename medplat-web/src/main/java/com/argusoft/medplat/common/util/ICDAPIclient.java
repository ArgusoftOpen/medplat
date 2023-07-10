package com.argusoft.medplat.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An util class for International Classification of Diseases
 * @author harshit
 * @since 31/08/2020 4:30
 */
public class ICDAPIclient {

    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;
    private final String baseUrl;
    private final String scope;
    private final String grantType;
    private String token;

        
            
    public ICDAPIclient(String tokenEndpoint,String clientId,String clientSecret,String baseUrl) {
        this.tokenEndpoint = tokenEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.baseUrl = baseUrl;
        this.scope = "icdapi_access";
        this.grantType = "client_credentials";
    }
    
    
    public void setToken(String token){
        this.token = token;
    }

	/**
	 * Returns the OAUTH2 token
	 * @return An OAUTH2 token
	 * @throws IOException Signals that an I/O exception has occurred
	 */
	public String getToken() throws IOException  {

		Logger.getLogger(ICDAPIclient.class.getName()).log(Level.SEVERE, "Getting token");
		URL url = new URL(tokenEndpoint);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		// set parameters to post
		String urlParameters =
        		"client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
        		"&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
			"&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
			"&grant_type=" + URLEncoder.encode(grantType, StandardCharsets.UTF_8);
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// response
		int responseCode = con.getResponseCode();
		Logger.getLogger(ICDAPIclient.class.getName()).log(Level.SEVERE, "Token Response Code : {}",responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// parse JSON response
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObj = mapper.readTree(response.toString());
		return jsonObj.get("access_token").textValue();
	}


	/**
	 * Access ICD API
	 * @param uri An uri value
	 * @return A response string
	 * @throws IOException Signals that an I/O exception has occurred
	 */
	public String getURI(String uri) throws IOException {

		URL url = new URL(baseUrl + uri);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// HTTP header fields to set
		con.setRequestProperty("Authorization", "Bearer "+ token);
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Accept-Language", "en");
		con.setRequestProperty("API-Version", "v2");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}