package stempler.ofer.main;

/**
 * Created by ofer on 22/05/18.
 */
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Log4j
public class LoadFile {

    private String dataXml;
    private String dataJson;
    private String dataXml2;
    private String mockDocNResponse;

    //-------------------------------------------------------------------------------------------------------
    @PostConstruct
    public String getDockNRes(){

        log.debug("Reading mockDocNResponse response...");

        try{

            ClassLoader calssLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = calssLoader.getResourceAsStream("MockGetDocNResponse.txt");

            BufferedReader b = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = b.readLine()) != null) {
                builder.append(line);
            }
            mockDocNResponse = builder.toString();



        }catch (Exception e){
            log.error("Could not read file" + e );
            return null;
        }
        log.debug("Successfully loaded file content to memory");
        return mockDocNResponse;
    }
    @PostConstruct
    //-------------------------------------------------------------------------------------------------------
    public String getAllBytesXml2(){

        log.debug("Reading mock request File....");

        try{

            ClassLoader calssLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = calssLoader.getResourceAsStream("DataXml2.txt");

            BufferedReader b = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = b.readLine()) != null) {
                builder.append(line);
            }
            dataXml2 = builder.toString();



        }catch (Exception e){
            log.error("Could not read file" + e );
            return null;
        }
        log.debug("Successfully loaded file content to memory");
        return dataXml;
    }
    //-------------------------------------------------------------------------------------------------------
    @PostConstruct
    public String getAllBytes(){

        log.debug("Reading mock request File....");

        try{

            ClassLoader calssLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = calssLoader.getResourceAsStream("DataXml.txt");

            BufferedReader b = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = b.readLine()) != null) {
                builder.append(line);
            }
            dataXml = builder.toString();



        }catch (Exception e){
            log.error("Could not read file" + e );
            return null;
        }
        log.debug("Successfully loaded file content to memory");
        return dataXml;
    }
    //-------------------------------------------------------------------------------------------------------
    @PostConstruct
    public String getAllBytesJson(){

        log.debug("Reading mock request File....");

        try{

            ClassLoader calssLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = calssLoader.getResourceAsStream("DataJson.txt");

            BufferedReader b = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = b.readLine()) != null) {
                builder.append(line);
            }
            dataJson = builder.toString();



        }catch (Exception e){
            log.error("Could not read file" + e );
            return null;
        }
        log.debug("Successfully loaded file content to memory");
        return dataJson;
    }
    //-------------------------------------------------------------------------------------------------------

    public String getData() {
        return dataXml;
    }

    public void setData(String data) {
        this.dataXml = data;
    }
    public String getDataJson() {
        return dataJson;
    }
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
    public String getDataXml2() {
        return dataXml2;
    }
    public void setDataXml2(String dataXml2) {
        this.dataXml2 = dataXml2;
    }
    public String getMockDocNResponse() {
        return mockDocNResponse;
    }
    public void setMockDocNResponse(String mockDocNResponse) {
        this.mockDocNResponse = mockDocNResponse;
    }
}