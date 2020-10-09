package br.com.atech.controler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClientWebservice1 {

	Logger logger = LoggerFactory.getLogger(ClientWebservice1.class);


	// String server = "http://172.30.5.22:8082";
	String server = "http://10.103.3.6:8443";

	@Value("${server.ssl.key-store}")
	private Resource trustStore;
	@Value("${server.ssl.key-store-password}")
	private String trustStorePassword;

	public void testTac2IWXXM21() {
		try {
			String msg = java.net.URLEncoder.encode("METAR UUWW 090830Z 23005MPS CAVOK 14/03 Q1021 R24/000070 NOSIG",
					"UTF-8");

			HttpUriRequest request = new HttpGet("http://localhost:8083/iwxxmConverter/api/convert?message=" + msg);

			HttpResponse response = HttpClientBuilder.create().build().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				readData(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// // @Todo testar com o gravar msg no proc
	// public void testProcValidate(String msg) {
	// try {
	//
	// String url = "http://172.30.5.22:8082/rest/proc/validate";
	// HttpUriRequest request = new HttpPost(url);
	// request.setHeader("Authorization",
	// "Bearer
	// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvYnNlcnZhZG9yIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiMSJ9LHsiYXV0aG9yaXR5IjoiMyJ9LHsiYXV0aG9yaXR5IjoiNSJ9LHsiYXV0aG9yaXR5IjoiNiJ9XX0.FaJZaaRlNeQ6pJpf1LEQFYpqPYkKUNCE_snRWZ5x-vw");
	//
	// HttpResponse response = HttpClientBuilder.create().build().execute(request);
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity entity = response.getEntity();
	// readData(entity);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void testProcSave(String msg) {
	// try {
	//
	// String url = "http://172.30.5.22:8082/rest/proc/process";
	// HttpUriRequest request = new HttpPost(url);
	// request.setHeader("Authorization",
	// "Bearer
	// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvYnNlcnZhZG9yIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiMSJ9LHsiYXV0aG9yaXR5IjoiMyJ9LHsiYXV0aG9yaXR5IjoiNSJ9LHsiYXV0aG9yaXR5IjoiNiJ9XX0.FaJZaaRlNeQ6pJpf1LEQFYpqPYkKUNCE_snRWZ5x-vw");
	//
	// HttpResponse response = HttpClientBuilder.create().build().execute(request);
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity entity = response.getEntity();
	// readData(entity);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void testValidateHttp(String msgIn) {
		try {

			HttpUriRequest request = new HttpPost();
			request.setHeader("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

			String url = server + "/rest/proc/validate";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(msgIn);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			if (responseCode == HttpStatus.SC_OK) {
				System.out.println("Enviada msg: " + msgIn);
			}
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());

			if (response.length() < 5) {
				System.out.println("Salvando msg ");
				testProcessHttp(msgIn);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testProcessHttp(String msgIn) {
		try {

			HttpUriRequest request = new HttpPost();
			request.setHeader("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

			String url = server + "/rest/proc/process";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			ProcessorMessageResource dataMsg = new ProcessorMessageResource();
			dataMsg.setBody(msgIn);
			dataMsg.setPriority("GG");
			dataMsg.setSender("AMHS");
			dataMsg.setTimestamp(LocalDateTime.now().toString());

			ObjectMapper mapper = new ObjectMapper();
			try {
				String json = mapper.writeValueAsString(dataMsg);
				// add request header
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization",
						"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());

				wr.writeBytes(json);
				wr.flush();
				wr.close();

				int responseCode = con.getResponseCode();

				if (responseCode == HttpStatus.SC_OK) {
					System.out.println("Enviada msg: " + msgIn);
				}
				System.out.println("Response Code : " + responseCode);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readData(HttpEntity entity) throws IOException {
		InputStream content = null;
		InputStreamReader reader = null;
		try {
			content = entity.getContent();
			reader = new InputStreamReader(content);
			char[] linha = new char[2048];
			while (reader.ready()) {
				reader.read(linha);
				System.out.println(linha);
			}
		} finally {
			content.close();
			reader.close();
		}
	}

	public void testeHttpsv1(String msgIn) {
		String serverQA = "https://opmet-dev.atech.com.br:8445";
		//String serverQA = "https://opmet-qa1.atech.com.br:8445";
		//String serverQA = "http://10.103.3.17:30445";
		//String url = serverSSl + "/rest/proc/process";
		String url = serverQA + "/rest/proc/process";

		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			ClientHttpResponse execute = restTemplate().getRequestFactory().createRequest(obj.toURI(), HttpMethod.POST)
					.execute();
		//	System.out.println(execute.getStatusCode());

			ProcessorMessageResource dataMsg = new ProcessorMessageResource();
			dataMsg.setBody(msgIn);
			dataMsg.setPriority("GG");
			dataMsg.setSender("AMHS");
			dataMsg.setTimestamp(LocalDateTime.now().toString());

			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(dataMsg);
			// add request header
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(json);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			if (responseCode == 204) {
				System.out.println("Enviada msg: " + msgIn);
			}
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			logger.error(response.toString());
			//System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void enviaQA2(String msgIn) {
		//String serverQA = "https://opmet-dev.atech.com.br:8445";
		//String serverQA = "https://opmet-qa1.atech.com.br:8445";
		//String serverQA = "http://10.103.3.17:30445";
		//String url = serverSSl + "/rest/proc/process";

		String url = "https://opmet-qa2.atech.com.br:31453/insertiwxxmcontent";
		String msg = "?tac=" + msgIn;
		//String url = serverQA + "/rest/proc/process";

		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			ClientHttpResponse execute = restTemplate().getRequestFactory().createRequest(obj.toURI(), HttpMethod.POST)
					.execute();
		//	System.out.println(execute.getStatusCode());

			/* ProcessorMessageResource dataMsg = new ProcessorMessageResource();
			dataMsg.setBody(msgIn);
			dataMsg.setPriority("GG");
			dataMsg.setSender("AMHS");
			dataMsg.setTimestamp(LocalDateTime.now().toString()); */

			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(msg);
			// add request header
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b25pbjMiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJhdWRpdC5jIn0seyJhdXRob3JpdHkiOiJhdWRpdC5kIn0seyJhdXRob3JpdHkiOiJhdWRpdC5yIn0seyJhdXRob3JpdHkiOiJhdWRpdC51In0seyJhdXRob3JpdHkiOiJpd3h4bS1zZXJ2aWNlLmNyZWF0ZSJ9LHsiYXV0aG9yaXR5IjoiaXd4eG0tc2VydmljZS5yZWFkIn1dLCJwcm9maWxlUm9sZSI6IlNZU1RFTSIsImlhdCI6MTYwMDc4MTE4NCwiZXhwIjoxNjAxNjQ1MTg0fQ.5BxWov7FQDmIiGBBWs3L79nBZWseF3vHXkgmC_JgBng");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(json);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			if (responseCode == 204) {
				System.out.println("Enviada msg: " + msgIn);
			}
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			logger.error(response.toString());
			//System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void testeWebserviceRedemet(String msgIn) {
		String serverSSl = "https://opmet-dev.atech.com.br:8445";
	//	String serverQA = "https://opmet-qa1.atech.com.br:8452";
	//	String serverQA  = "http://10.103.3.17:30445";
		//String serverQA  = "http://10.60.0.63:30448";
		//String serverQA  = "http://10.60.0.63:30452";
		//http://10.103.3.47:30448/consulta_redemet?local=SBBR&msg=Metar&data_ini=2019112600&data_fim=2019112623%22

		String url = serverSSl + "/rest/proc/process";
	//	String url = serverQA + "/rest/proc/process";

		URL obj;
		try {
			String uri =	"/consulta_redemet?local=SBBR,SBMT&msg=Metar&data_ini=2019120200&data_fim=2019120300"; //  -H \"accept: */*\" -H \"Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM\"";
		   // String encodedurl = URLEncoder.encode(uri,"UTF-8");
		//	String url =  serverQA + uri;

			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//ClientHttpResponse execute = restTemplate().getRequestFactory().createRequest(obj.toURI(), HttpMethod.GET)
			//		.execute();
			//System.out.println(execute.getStatusCode());

//			HttpClient client = HttpClientBuilder.create().build();
//	        HttpGet httpGet = new HttpGet(url);
//	        HttpResponse resposta = client.execute(httpGet);
//	        HttpEntity entity = resposta.getEntity();
//	        System.out.println(entity.getContent());

//			ProcessorMessageResource dataMsg = new ProcessorMessageResource();
//			dataMsg.setBody(msgIn);
//			dataMsg.setPriority("GG");
//			dataMsg.setSender("AMHS");
//			dataMsg.setTimestamp(LocalDateTime.now().toString());

			ObjectMapper mapper = new ObjectMapper();

		//	String json = mapper.writeValueAsString(dataMsg);
			// add request header
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes("");
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			if (responseCode == 204) {
				System.out.println("Enviada msg: " + msgIn);
			}
			System.out.println("Response Code : " + responseCode + " " + msgIn);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			logger.error(response.toString());
			//System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public void testeWebserviceRedemet2(String msgIn) {
		//String serverSSl = "https://opmet-dev.atech.com.br:8445";
	//	String serverQA = "https://opmet-qa1.atech.com.br:8452";
		String serverQA  = "http://10.103.3.17:30448";
		//String serverQA  = "http://10.60.0.63:30448";
		//String serverQA  = "http://10.60.0.63:30452";
		//http://10.103.3.47:30448/consulta_redemet?local=SBBR&msg=Metar&data_ini=2019112600&data_fim=2019112623%22

		//String url = serverSSl + "/rest/proc/process";
		//String url = serverQA + "/rest/proc/process";

		URL obj;
		try {
			String uri =	"/consulta_redemet?local=SBBR,SBMT&msg=Metar&data_ini=2019120200&data_fim=2019120300";
		   // String encodedurl = URLEncoder.encode(uri,"UTF-8");
			String url =  serverQA + uri;

			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//ClientHttpResponse execute = restTemplate().getRequestFactory().createRequest(obj.toURI(), HttpMethod.GET)
			////		.execute();
			//System.out.println(execute.getStatusCode());

			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");
			con.connect();
			//System.out.println(con.getResponseCode());
			//System.out.println(con.getContent());

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			con.disconnect();

			logger.info(response.toString());

//			HttpClient client = HttpClientBuilder.create().build();
//	        HttpGet httpGet = new HttpGet(url);
//	        httpGet.addHeader("Authorization",
//					"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJhdXRoIjpbXX0.MuKsMplmjP85_AUJKNx2KasexOUoTho5BaBYs8tSctM");
//	        HttpResponse resposta = client.execute(httpGet);
//	        HttpEntity entity = resposta.getEntity();
	      //  System.out.println(entity.getContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RestTemplate restTemplate() throws Exception {
		final RestTemplate restTemplate = new RestTemplate();
		// restTemplate.getInterceptors().add(new RestTemplateHeaderInterceptor());
		// restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		restTemplate.setRequestFactory(httpReqFactory());
		return restTemplate;
	}

	private HttpComponentsClientHttpRequestFactory httpReqFactory() throws Exception {
		final SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
		final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		final HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return factory;
	}

	//
	// public void testValidateMetarXML() {
	// try {
	// BufferedReader reader = new BufferedReader(new FileReader(
	// new
	// File("C:\\home\\opmet\\iwxxmout\\A_LABR01URSS060530_C_URSS_20180906101347.xml")));
	// String xml = "";
	// while (reader.ready()) {
	// xml += reader.readLine();
	// }
	// reader.close();
	//
	// String msg = java.net.URLEncoder.encode(xml, "UTF-8");
	//
	// HttpUriRequest request = new
	// HttpGet("http://localhost:8083/iwxxmConverter/api/convert2TAC?message=" +
	// msg);
	//
	// HttpResponse response = HttpClientBuilder.create().build().execute(request);
	// assertTrue(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
	// HttpEntity entity = response.getEntity();
	// readData(entity);
	// } catch (Exception e) {
	// e.printStackTrace();
	// assertFalse(true);
	// }
	// }

}
