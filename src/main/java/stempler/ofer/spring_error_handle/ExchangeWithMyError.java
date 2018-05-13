package stempler.ofer.spring_error_handle;

/**
 * Created by ofer on 27/12/17.
 */
import lombok.extern.log4j.Log4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
@Log4j

public class ExchangeWithMyError {


    public MyResponseEntity springExchange(MyResponseEntity entity){


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity(entity, headers);
        RestTemplate rt = new RestTemplate();
        rt.setErrorHandler(new MySpringBootErrorHandler());

        String url = "someURL";
//	        String url = "http://localhost:8085/destination";

        log.debug(" post to:  [" + url + "] request body: [" + entity + "]");
        ResponseEntity<String> exchangeResponse = rt.exchange(url, HttpMethod.POST, request, String.class);
        String responseBody = exchangeResponse.getBody();

        try {
            ObjectMapper mapper = new ObjectMapper();
            MyResponseEntity response = mapper.readValue(responseBody, MyResponseEntity.class);
            log.debug(" response:  " + response);

            if(MySpringBootErrorHandler.isError(exchangeResponse.getStatusCode())){
                log.error("Client or Server Exception");
                MyResponseEntity httpError = new MyResponseEntity();
                httpError.setResponseCode(response.getResponseCode());
                httpError.setResponseDescription(response.getResponseDescription());
                return httpError;
            } else {

                return response;

            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("PRPTransfer generel exception: ", e);
            MyResponseEntity response = new MyResponseEntity();
            response.setResponseDescription("Exception:  "+e.getMessage());
            response.setResponseCode("500");
            return response;
        }

    }


}