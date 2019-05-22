package asr.proyectoFinal.services;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;

public class Traductor
{
	public static String translate(String palabra, String sourceModel, String destModel, boolean conversational)
	{
		String model;
		if(sourceModel.equals("en") || destModel.equals("en"))
		{
			model=sourceModel+"-"+destModel;
			if(conversational) 
				model+="-conversational";
		}
		else
			return translate(translate(palabra,sourceModel,"en",conversational),"en",destModel,conversational); //translate to english, then to dest

		LanguageTranslator languageTranslator = new LanguageTranslator();
		languageTranslator.setUsernameAndPassword("apikey","j5mqVbQERSzuQuk-btE-CrncvPAShUFsDkDyNQauseaM");

		languageTranslator.setEndPoint("https://gateway.watsonplatform.net/language-translator/api");
		 
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
}