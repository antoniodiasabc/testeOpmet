package br.com.atech.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

@Component
public class ClientWebservice2 {

	private static final String KEY_LOGIN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b25pbjMiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJhdWRpdC5jIn0seyJhdXRob3JpdHkiOiJhdWRpdC5kIn0seyJhdXRob3JpdHkiOiJhdWRpdC5yIn0seyJhdXRob3JpdHkiOiJhdWRpdC51In0seyJhdXRob3JpdHkiOiJpd3h4bS1zZXJ2aWNlLmNyZWF0ZSJ9LHsiYXV0aG9yaXR5IjoiaXd4eG0tc2VydmljZS5yZWFkIn1dLCJwcm9maWxlUm9sZSI6IlNZU1RFTSIsImlhdCI6MTYwMjA5MzUyNSwiZXhwIjoxNjAyOTU3NTI1fQ.cqo2kj51x6P2r3qpo8H6HMc_BEkWMxWjKGg5C84DNgw";
	// String server = "http://172.30.5.22:8082";
	String server = "http://10.103.3.6:8443";

	public void testTac2IWXXM21(String msgIn) {
		try {
			HttpUriRequest request = new HttpGet(
					"https://opmet-qa2.atech.com.br:31453/insertiwxxmcontent?tac=" + msgIn);
			request.addHeader("Authorization", KEY_LOGIN);

			HttpResponse response = HttpClientBuilder.create().build().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				readData(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readData(HttpEntity entity) throws IOException {
		InputStream content = null;
		String result = "";
		try {
			content = entity.getContent();
			result = new BufferedReader(new InputStreamReader(content)).lines().parallel()
					.collect(Collectors.joining("\n"));
		} finally {
			content.close();
		}
		return result;
	}

	public void sendQAIWXXMInsert(String msg) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://opmet-qa2.atech.com.br:31453/insertiwxxmcontent");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("tac", msg));
		try {
			httppost.addHeader("Authorization", KEY_LOGIN);
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			// Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("status resposta: " + response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				readData(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String sendQAIWXXMSearch(String localidade, String msg, String dataIni, String dataEnd) {
		String result = "";
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://opmet-qa2.atech.com.br:31453/searchiwxxm");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(4);
		params.add(new BasicNameValuePair("local", localidade));
		params.add(new BasicNameValuePair("msg", msg));
		params.add(new BasicNameValuePair("data_ini", dataIni));
		params.add(new BasicNameValuePair("data_fim", dataEnd));
		try {
			httppost.addHeader("Authorization", KEY_LOGIN);
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			// Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("status resposta: " + response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result = readData(entity);
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void sendQAIWXXMInsertFile(String fileName) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://opmet-qa2.atech.com.br:31453/insertiwxxm");

		try {
			HttpEntity entityNew = httppost.getEntity();
			httppost.addHeader("Authorization", KEY_LOGIN);
			httppost.setEntity(entityNew);

			// Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("status resposta: " + response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				readData(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendQAIWXXMInsertFile2(String fileName) {
		File fileToServer = new File(fileName);
		sendFileIWXXM(fileToServer);
		System.out.println("enviou o anexo");
	}

	public void sendFileIWXXM(File fileToUpload) {
		final String API_URI = "https://opmet-qa2.atech.com.br:31453/insertiwxxm";

		final ClientConfig config = new DefaultClientConfig();
		final Client client = Client.create(config);
		final WebResource resource = client.resource(API_URI);

		// the file to upload, represented as FileDataBodyPart
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("xml", fileToUpload);
		fileDataBodyPart
				.setContentDisposition(FormDataContentDisposition.name("xml").fileName(fileToUpload.getName()).build());

		@SuppressWarnings("resource")
		final MultiPart multiPart = new FormDataMultiPart()
				.field("xml", fileToUpload.getName(), MediaType.APPLICATION_XML_TYPE).bodyPart(fileDataBodyPart);
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		// POST request final
		ClientResponse response = resource.type("multipart/form-data").header("Authorization", KEY_LOGIN)
				.post(ClientResponse.class, multiPart);
		final String result = getStringFromInputStream(response.getEntityInputStream());
		System.out.println("INFO >>> Response from API was: " + result);
		client.destroy();
	}

	// convert InputStream to String
	private String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
