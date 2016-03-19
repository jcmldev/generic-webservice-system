package dd;

import dd.output.LoggingContext;


public class RuntimeHelper {

    public static void waitDelay(LoggingContext log, int delay)
    {
        Integer             lock = new Integer(0);
        
        
        try 
        {
            synchronized(lock)
            {
                lock.wait(delay);
            }
        } 
        catch (InterruptedException ex) 
        {
            log.log(ex);
        }    
    }    
}
