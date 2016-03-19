
package dd;

import dd.output.LoggingContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class Wsdl 
{
    public static URL getWsdlFilePathURLInGfDir(LoggingContext log, String serviceName)
    {
        String              path = "wsdl/" + serviceName + ".wsdl";
        File                wsdlFile = Configuration.getItemInGfDir(log, path);
        URL                 url = null;
        
        try {
            url = new URL("file:///" + wsdlFile.getPath());
        } 
        catch (MalformedURLException ex) {
            log.log(ex);
        }
        
        return url;
    }
}
