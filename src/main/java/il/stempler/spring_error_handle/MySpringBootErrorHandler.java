package il.stempler.spring_error_handle;

/**
 * Created by ofer on 27/12/17.
 */
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
@Log4j
public class MySpringBootErrorHandler implements ResponseErrorHandler  {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        log.error("MyErrorHanler: ResponseStatusCode: [" +  response.getStatusCode() +"] ResponseStatusText: [" + response.getStatusText() +" ]");

    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return isError(response.getStatusCode());
    }


    public static boolean isError (HttpStatus status){
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
    }

}
