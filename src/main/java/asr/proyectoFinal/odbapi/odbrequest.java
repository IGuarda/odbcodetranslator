package asr.proyectoFinal.odbapi;
import java.io.BufferedReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*curl --request GET --url 'https://api.eu.apiconnect.ibmcloud.com/hella-ventures-car-diagnostic-api/api/v1/dtc
 * ?client_id=398f6f60-8d12-439c-938f-1162405d3d44&client_secret=A8xV8kP0hH7oW8tS2kT4cT5tA2pL2sY4rE5rW6rF0dN6dA1yV4
 * &code_id=P0001&vin=WBAES26C05D&language=EN' --header 'accept: application/json' --header 'content-type: application/json'
 */
public class odbrequest {
	public static String getodbcode(String palabra, String sourceModel, String destModel, boolean conversational) {
     String url = "http://api.ipinfodb.com/v3/ip-city/?key=d64fcfdfacc213c7ddf4ef911dfe97b55e4696be3532bf8302876c09ebd06b&ip=74.125.45.100&format=json";
     URL obj = new URL(url);
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
String traduccionJSON = translationResult.toString();
JsonParser parser = new JsonParser();
JsonObject rootObj =
parser.parse(traduccionJSON).getAsJsonObject();
JsonArray traducciones = rootObj.getAsJsonArray("translations");
String traduccionPrimera = palabra;
if(traducciones.size()>0)
traduccionPrimera =
traducciones.get(0).getAsJsonObject().get("translation").getAsString();
return traduccionPrimera;