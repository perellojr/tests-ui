package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIBaseUtils {

	private final static String USER_AGENT = "Chrome";

	public String getResponseAPI(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	public String getResponseAPI(String urlAPI, String cpf, String codUsuario) throws IOException {
		String retorno = null;

		try {
			String cookie = ".AGIPLAN-AUTH-PORTAL-ASPNETCORE=" + codUsuario + "|" + cpf;
			URL url = new URL(urlAPI);
			HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
			conexao.setRequestMethod("GET");
			conexao.setRequestProperty("ACCEPT", "application/json");

			if (!codUsuario.equals("") || !cpf.equals("")) {
				conexao.setRequestProperty("Cookie", cookie);
			}

			if (conexao.getResponseCode() == 200) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
				String saida;
				while ((saida = bufferedReader.readLine()) != null) {
					retorno = saida;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return retorno;
	}
}
