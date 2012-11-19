package com.login;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.http.connection.WebConnection;

/**
 * @author X.L.Zhang
 * обнГ1:25:04 2012-11-19
 */
public class RenrenLogin {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		WebConnection.renrenLogin();

	}

}
