package stempler.ofer.main;

/**
 * Created by ofer on 22/05/18.
 */
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Log4j
public class Test_XMLToJson {

    private static JSONObject original;
    //---------------------------------------------------------------------------------------------------------------------------------
    synchronized private JSONObject ConvertXMLToJson(JSONObject jsonNew) {
        Map<String, String> map = new HashMap<String, String>();
        lookForArrays(jsonNew, map, jsonNew);
        if(original != null){
            JSONObject finalJson = original;
            original = null;
            return finalJson;
        } else {
            return jsonNew;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    public void lookForArrays(JSONObject jo, Map<String, String> map, JSONObject originalJson){
        for (Object o : jo.keySet()) {
            map.put(o.toString(),  (jo.get(o.toString()).toString()));
//			System.out.println(o.toString());
//			System.out.println(jo.get(o.toString()));
            if(jo.has("isArray")){
                log.debug("Found 'isArray' attribute");
                convertJsonObjectToJsonArray(jo, o, map, originalJson);
                log.debug("Updated JSON: " + originalJson.toString());
                break;
            }
            if (jo.has(o.toString()) && jo.get(o.toString()) instanceof JSONObject) {
                lookForArrays(jo.getJSONObject(o.toString()), map, originalJson);
            } else if (jo.has(o.toString()) && jo.get(o.toString()) instanceof JSONArray){
                lookForArrays(jo.getJSONArray(o.toString()), map, originalJson);
            }
        }
    }

    private void lookForArrays(JSONArray jsonArray, Map<String, String> map, JSONObject originalJson) {
        for (int i = 0; i < jsonArray.length(); i++) {

            if(jsonArray.get(i) instanceof JSONObject){
                lookForArrays((JSONObject) jsonArray.get(i), map, originalJson);
            } else if (jsonArray.get(i) instanceof JSONArray){
                lookForArrays((JSONArray) jsonArray.get(i), map, originalJson);
            }
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------
    private JSONObject convertJsonObjectToJsonArray(JSONObject jo, Object o, Map<String, String> jsonMap, JSONObject originalJson) {
        String result  	   = null;
        JSONArray arr  	   = new JSONArray();
        Object content	   = null;
        JSONObject newJson = null;
        String jsonString  = null;
        result = jsonMap.entrySet().stream()
                .filter(map -> map.getValue().equals(jo.toString()))
                .map(map -> map.getKey())
                .collect(Collectors.joining());
        log.debug("Replacing array for tag: ["+result+"]");
        if(original == null){
            original = originalJson;
        }
//		isValid attribute will also result with a 'content' tag when the there is only one object ->
//		<ownershipType isArray="1">aaa</ownershipType> will be {"ownershipType":{"isArray":1,"content":"aaa"}

        //For single object array, replace with array[]
        if (jo.has("content") && jo.has("isArray")){

            content = getContent(jo);
            arr.put(content);
            jsonString = original.toString().replace(jo.toString(), arr.toString());


            //For multi object array that was marked with isArray, remove 'isArray' tag
        } else if (jo.has("isArray")) {
            log.debug("Removing isArray tag from a valid array");
            String temp = jo.toString();
            jo.remove("isArray");
            jsonString = original.toString().replace(temp, jo.toString());
        }
        newJson = new JSONObject(jsonString);
        original = newJson;
        return newJson;
    }
    //---------------------------------------------------------------------------------------------------------------------------------

    private Object getContent(JSONObject jo) {
        Object content = null;

        try{
            content = jo.getString("content");
            return content;
        } catch (Exception e){

        }
        try{
            content = jo.getJSONObject("content").toString();
            return content;
        } catch (Exception e){

        }
        try{
            content = (jo.getInt("content"));
            return content;
        } catch (Exception e){

        }
        try{
            content = jo.getBoolean("content");
            return content;
        } catch (Exception e){

        }
        log.error("Could not find content object type. Returning null");
        return content;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        LoadFile loadFile = new LoadFile();
        Test_XMLToJson testMain = new Test_XMLToJson();
        loadFile.getAllBytesXml2();
        loadFile.getAllBytes();
        String xml = loadFile.getDataXml2();
        String xml2 =loadFile.getData();
        //		System.out.println(xml);

//		Document doc = null;
//		try {
//			doc = Utils.buildXML(xml2);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        JSONObject json = new JSONObject();
        //		JSONObject newJson = testMain.convertXMLToJson(doc.getDocumentElement(), json);
        //		System.out.println(newJson);

        JSONObject jsonNew = XML.toJSONObject(xml2);
        //		System.out.println(jsonNew.toString());

        JSONObject test1 = testMain.ConvertXMLToJson(jsonNew);
        System.out.println(test1);

    }



}
