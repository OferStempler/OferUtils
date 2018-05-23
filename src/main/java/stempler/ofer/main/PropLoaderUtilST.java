package stempler.ofer.main;

/**
 * Created by ofer on 23/05/18.
 */
import org.apache.log4j.Logger;
import java.io.*;
import java.util.Properties;

/**
 *
 * DESC : Singleton class for loading properties from classpath
 * EXAMPLE in class: String sanitizeFolder   = PropLoaderUtilST.getInstance().getPropertyValue(Consts.SANITIZE );
 *
 */
public class PropLoaderUtilST {

    private static PropLoaderUtilST INSTANCE = new PropLoaderUtilST();
    private final String CONF_FL_NM = "fileName.properties";
    private Logger logger = Logger.getLogger(this.getClass());
    //private final String CONF_FL_NM = "processManager.properties";


    //-----------------------------------------------------------------------------------------------------------------
    private PropLoaderUtilST(){;}
    //-----------------------------------------------------------------------------------------------------------------
    public static PropLoaderUtilST getInstance(){
        return INSTANCE;
    }
    //-----------------------------------------------------------------------------------------------------------------
    public String getPropertyValue(String propName) {

//		logger.debug("PropLoaderUtilST.propName() - propName:[" + propName + "]");
        Properties prop = new Properties();
        InputStream input = null;
        String val = null;
        String propFilePath = null;
        try {
            String catalina_base  = System.getProperty("catalina.base");

            propFilePath = catalina_base + "/conf/" + CONF_FL_NM;
            File file = new File(propFilePath );

            if (file.exists() ){
                prop.load(new FileReader(file));
            }
            else { //get from classpath

                input = this.getClass().getClassLoader().getResourceAsStream(CONF_FL_NM);

                prop.load(input);
            }
            val = prop.getProperty(propName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
//		logger.debug("PropLoaderUtilST.getPropertyValue() - propName:[" + propName + "], val:[" + val + "], propFilePath:[" + propFilePath + "]");
        return val;
    }
}