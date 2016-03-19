
package dd;

import dd.output.LoggingContext;
import java.io.File;
import java.util.Date;


public class ClientStartSignal 
{
    public static final String          SIGNAL_FILE_NAME = "gf_client_start.txt";
    
    public static boolean getShouldClientStart(LoggingContext log)
    {
        File        singnalFile;
        
        
        singnalFile = Configuration.getItemInGfDir(log, SIGNAL_FILE_NAME);
        
        return singnalFile.exists();
    }
    
    public static void waitForSignalToStart(LoggingContext log)
    {
        log.log("Waiting for signal file ...");
        
        while (true)
        {
            if (ClientStartSignal.getShouldClientStart(log))
            {
                log.log("Signal file found");
                break;
            }
            else
            {
                log.log("Check for signal file at: " + (new Date()).toString());
                //checkign every second
                RuntimeHelper.waitDelay(log, 1000);
            }
        }
    }
}
