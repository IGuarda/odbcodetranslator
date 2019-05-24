package asr.proyectoFinal.odbapi;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL; 
/*curl --request GET --url 'https://api.eu.apiconnect.ibmcloud.com/hella-ventures-car-diagnostic-api/api/v1/dtc
 * ?client_id=398f6f60-8d12-439c-938f-1162405d3d44&client_secret=A8xV8kP0hH7oW8tS2kT4cT5tA2pL2sY4rE5rW6rF0dN6dA1yV4
 * &code_id=P0001&vin=WBAES26C05D&language=EN' --header 'accept: application/json' --header 'content-type: application/json'
 */
public class odbrequest {
	public static String getodbcode(String id, String vin, String lan) throws Exception {
     String url = "https://api.eu.apiconnect.ibmcloud.com/hella-ventures-car-diagnostic-api/api/v1/dtc" + 
     		"?client_id=398f6f60-8d12-439c-938f-1162405d3d44&client_secret=A8xV8kP0hH7oW8tS2kT4cT5tA2pL2sY4rE5rW6rF0dN6dA1yV4" + 
     		"&code_id="+id+"&vin="+vin+"&language="+lan;
     URL obj;

		obj = new URL(url);
	
     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
     // optional default is GET
     con.setRequestMethod("GET");
     //add request header
     con.setRequestProperty("User-Agent", "Mozilla/5.0");
     int responseCode = con.getResponseCode();
     System.out.println("\nSending 'GET' request to URL : " + url);
     System.out.println("Response Code : " + responseCode);
     BufferedReader in = new BufferedReader(
             new InputStreamReader(con.getInputStream()));
     String inputLine;
     StringBuffer response = new StringBuffer();
     while ((inputLine = in.readLine()) != null) {
     	response.append(inputLine);
     }
     in.close();
	
     //print in String
     System.out.println(response.toString());
     //Read JSON response and print
     String JSON =response.toString();
     JsonParser parser = new JsonParser();
     JsonObject rootObj = parser.parse(JSON).getAsJsonObject();
     JsonObject datos = rootObj.getAsJsonObject("dtc_data");
     String salida = id;
     salida = "System: "+datos.getAsJsonObject().get("system").getAsString();
     salida = salida +" Fault: "+datos.getAsJsonObject().get("fault").getAsString();
     return salida;

	
	}
     /*
     System.out.println("statusMessage- "+myResponse.getString("statusMessage"));
     System.out.println("ipAddress- "+myResponse.getString("ipAddress"));
     System.out.println("countryCode- "+myResponse.getString("countryCode"));
     System.out.println("countryName- "+myResponse.getString("countryName"));
     System.out.println("regionName- "+myResponse.getString("regionName"));
     System.out.println("cityName- "+myResponse.getString("cityName"));
     System.out.println("zipCode- "+myResponse.getString("zipCode"));
     System.out.println("latitude- "+myResponse.getString("latitude"));
     System.out.println("longitude- "+myResponse.getString("longitude"));
     System.out.println("timeZone- "+myResponse.getString("timeZone"));
*/
}