package web;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.*;

public class LoadWeb {
	public String loadString() {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line = "";
		{

			try {
				url = new URL("http://localhost:8080/RESTfulCRUD/rest/employees");
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));

				line = br.readLine();

			} catch (MalformedURLException mue) {
				mue.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return line;
	}

	public void saveFile() {
		try (FileWriter writer = new FileWriter("out.txt", false)) {
			String text = loadString();
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public List parseJSON() {
		String result = "";
		List<Object> srcList = new ArrayList<Object>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("out.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();

			JSONObject jobj = new JSONObject(result);
			JSONArray jarr = new JSONArray(jobj.getJSONArray("employee").toString());
			for (int i = 0; i < jarr.length(); i++) {
				System.out.println("Employee #" + i + ": " + jarr.getJSONObject(i));
			}
			srcList = jarr.toList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return srcList;
	}

	public void sendPost(String empName, String empNo, String position) throws Exception {
		JSONObject json = new JSONObject();
		json.put("empName", empName);
		json.put("empNo", empNo);
		json.put("position", position);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost("http://localhost:8080/RESTfulCRUD/rest/employees");
			StringEntity params = new StringEntity(json.toString());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			httpClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
	}

	public void delete(String delNo) {
		StringBuilder builder = new StringBuilder();
		builder.append("http://localhost:8080/RESTfulCRUD/rest/employees/");
		builder.append(delNo);
		try {
			URL url = new URL(builder.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			System.out.println(connection.getResponseCode());
			connection.setRequestMethod("DELETE");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}