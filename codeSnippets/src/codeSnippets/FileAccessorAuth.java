package codeSnippets;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Base64;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
//import org.apache.commons.io.IOUtils;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;

public class FileAccessorAuth {



	public static void main(String[] args) {

		FileSystem fileSystem = FileSystems.getDefault();
		Path currentPath = fileSystem.getPath(".");
		String fileOutputPath = "D:\\Java\\testData\\tmpFileOutputPath\\testOutput.txt";
		URL url = null;
		String filePath = "D:\\Java\\testData\\userAuthTest\\testAuth.txt";
		String login = "";
		String password = "";

		/**
		 * 
		 * hr koepf: Doxis wollten wir ja über den Verzeichnispfad mit Dokumenten
		 * versorgen. Der Zugriff über den Verzeichnispfad
		 * 
		 * (\\bosch.com\dfsrb\DfsDE\DIV\C\CE\CET\CET2\normdocs\boschdok\n0179\n0179896_en.pdf)
		 * funktioniert mit dem dafür eingerichteten
		 * 
		 * Benutzer DE\ESH9SI. Falls Sie hierzu das Passwort benötigen, kann ich es
		 * Ihnen gerne nochmal zukommen lassen. Ich habe unter
		 * 
		 * d:\ einen Link „Normendokumente“ hinterlegt, der genau mit diesem Nutzer
		 * arbeitet.
		 * 
		 */

		createAndActivateTrustManager();

		try {
			url = Paths.get(filePath).toUri().toURL();// ergibt: file:/D:/Java/testData/userAuthTest/testAuth.txt
		} catch (Exception e) {
		}

		String encoded = getAuthentication(login, password); // uname:pwd
		URLConnection connection;
		try {
			connection = url.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			String contentType = connection.getContentType();// text/plain Dateiendung bei der tmpFile Erstellung
																// anzugeben ist zum
																// Glück unnötig
			InputStream inputStream;
			try {
				inputStream = connection.getInputStream();
				byte[] bytes = { 50, 50, 50, 33, 31, 79 };// IOUtils.toByteArray(inputStream);
				File fileOutput = new File(Paths.get(filePath).toUri());
				OutputStream out = new FileOutputStream(fileOutputPath);
				out.write(bytes);
				out.close();
				inputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			FileSystem fileSystem1 = FileSystems.getDefault();
			// Path currentPath = fileSystem.getPath(".");
			// String fileOutputPath = "TODO";
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Authentication Base64 encoding username + ":" + password
	 *
	 * 
	 * 
	 * Da es nur für ein Testsystem ist, wird alles geloggt
	 * 
	 */

	public static String getAuthentication(String username, String password) {
		byte[] encodedBytes = Base64.getEncoder().encode((username + ":" + password).getBytes());
		String str = new String(encodedBytes, StandardCharsets.UTF_8);
		byte[] decodedB = Base64.getDecoder().decode(encodedBytes);
		String str2 = new String(decodedB, StandardCharsets.UTF_8);
		return encodedBytes.toString();
	}

	static void createAndActivateTrustManager() {
		// wg https
		// Create a new trust manager that trust all certificates
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}
}
