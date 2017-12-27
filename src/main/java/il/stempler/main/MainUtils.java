package il.stempler.main;

/**
 * Created by ofer on 27/12/17.
 */
import lombok.extern.log4j.Log4j;
import org.apache.tools.ant.filters.StringInputStream;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
@Log4j
public class MainUtils {


    private static String noHiddens = null;
    static String wantedJson = null;

//------------------------------------------------------------------------------------------------------------------------------------------

    // Rest template using exchange + setting client header instead of original hesaders + Returning response with servletResponse.getWriter().print(replyString);
    public void replaceHeaders(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String destination, String messageBody){

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        HttpEntity<String> request = new HttpEntity<String>(messageBody);

        String method = servletRequest.getMethod();
        log.debug("Sending data: [" + messageBody + "]");
        try {
            HttpEntity<String> response = template.exchange(destination, HttpMethod.valueOf(method), request, String.class);
            String replyString = response.getBody();

            HttpHeaders headers = response.getHeaders();

            // Switching client headers with LDP response headers
            Set<Entry<String, List<String>>> set = headers.entrySet();
            for (Entry<String, List<String>> entry : set) {

                // System.out.print( "Header:" + entry.getKey() );
                for (String val : entry.getValue()) {
                    // System.out.println(" Val:" + val );
                    servletResponse.setHeader(entry.getKey(), val);
                }
            }

            servletResponse.getWriter().print(replyString);
        } catch (Exception e){
            log.error("Could not send message");
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    // getForEntity
    public boolean getForEntity(String messageBody, String url) {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.getForEntity(url, String.class);
        String body = response.getBody();
        EntityExample entityExample = this.convertJsonStringToObject(body, EntityExample.class);
        if(body == null || entityExample == null ){
            return false;
        }
        return true;
    }
    //------------------------------------------------------------------------------------------------------------------------------------------


    //Builds an entity from RestTemplate Response body
    public  <T> T convertJsonStringToObject(String json, Class<?> clazz) {
        Exception exp = null;
        T targetObject = null;
        if ( json == null || clazz == null){
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES , false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
            targetObject = (T) objectMapper.readValue(new StringInputStream(json,"UTF-8"), clazz);
        } catch (JsonParseException e) {
            exp = e;
        } catch (JsonMappingException e) {
            exp = e;
        } catch (IOException e) {
            exp = e;
        }finally{
            if (exp != null){
                throw new RuntimeException("GeneralUtils.convertJsonStringToObject() - ERROR - Exception:[" + exp.getClass().getSimpleName() + "], msg:[" + exp.getMessage()+ "] while parsing json:[" + json + "]", exp);
            }
        }
        return targetObject;

    }
    //------------------------------------------------------------------------------------------------------------------------------------------

    public String getSpecificJson(String StringXml, String wantedContent) {

        String content = null;
        wantedJson = null;
        try {
            JSONObject DatatoJson = XML.toJSONObject(StringXml);
            // System.out.println(DatatoJson);
            content = this.iterateSpecificJson(DatatoJson, StringXml, wantedContent);
        } catch (Exception e) {
            log.error("Error getting StringXml content");
            e.printStackTrace();
        }
        if (wantedJson == null || wantedJson.equals("")) {
            log.debug("No wanted Content was found for [" + wantedContent + "]");
            return null;
        } else {
            log.debug("Found wanted content for [" + wantedContent + "]");
            return wantedJson;
        }

    }
    public String iterateSpecificJson(JSONObject jo, String original, String wantedContent) throws JSONException {

        Iterator< ? > keys = jo.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (jo.get(key) instanceof JSONObject) {
                iterateSpecificJson(jo.getJSONObject(key), original, wantedContent);

            } else {
                Object ob = jo.get(key);
                String s = ob.toString();

//					System.out.println(jo.get(o.toString()));
//					System.out.println(o.toString());
                if (key.equals(wantedContent)) {

//						System.out.println(s);
                    wantedJson = s;
                    return wantedJson;
                }

            }
        }

        return null;

    }
//		public String iterateSpecificJson(JSONObject jo, String original,
//				String wantedContent) {
//				for (Object o : jo.keySet()) {
//
//				if (jo.get(o.toString()) instanceof JSONObject) {
//					iterateSpecificJson(jo.getJSONObject(o.toString()), original, wantedContent);
//
//				} else {
//					Object ob = jo.get(o.toString());
//					String s = ob.toString();
//
////					System.out.println(jo.get(o.toString()));
////					System.out.println(o.toString());
//					if (o.toString().equals(wantedContent)) {
//
////						System.out.println(s);
//						wantedJson = s;
//						return wantedJson;
//					}
//
//				}
//			}
//
//			return null;
//
//		}

    public static boolean validateNumeric(String value){
        try{
            Long.parseLong(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //----------------------------------------------------------------------------------
    public static String stringGenerator() {
        char[] chars = "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

}
