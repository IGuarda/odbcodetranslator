package asr.proyectoFinal.services;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.util.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions.Builder;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
public class Traductor{
public static String translate(String palabra){
	LanguageTranslator service = new LanguageTranslator();

	//service.setUsernameAndPassword("apikey","j5mqVbQERSzuQuk-btE-CrncvPAShUFsDkDyNQauseaM");
	service.setApiKey("j5mqVbQERSzuQuk-btE-CrncvPAShUFsDkDyNQauseaM");
	service.setEndPoint("https://gateway-lon.watsonplatform.net/language-translator/api");
		 
	TranslateOptions translateOptions = new TranslateOptions.Builder()
	 .addText(palabra)
	 .modelId("en-es")
	 .build();
	TranslationResult translationResult = service.translate(translateOptions).execute();
	System.out.println(translationResult);
	String traduccionJSON = translationResult.toString();
	JsonParser parser = new JsonParser();
	JsonObject rootObj = parser.parse(traduccionJSON).getAsJsonObject();
	String wordCount = rootObj.get("word_count").getAsString();
	JsonArray traducciones = rootObj.getAsJsonArray("translations");
	String traduccionPrimera = palabra;
	if(traducciones.size()>0)
		traduccionPrimera =	traducciones.get(0).getAsJsonObject().get("translation").getAsString();
	return traduccionPrimera;
	}
}
		
		
		
		/*
		TranslateOptions translateOptions = new
		TranslateOptions.Builder()
		 .addText(palabra)
		 .modelId(model)
		 .build();

		TranslationResult translationResult = languageTranslator.translate(translateOptions).execute();
		
		System.out.println(translationResult);

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
	}
}*/