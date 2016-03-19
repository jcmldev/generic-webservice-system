
package dd.measure;

public class ProcessingMonitor {

    private static long                             s_startTime;
    private static long                             s_aggregatedDuration;        
    private static int                              s_counter;
    
        
    public static void Start()
    {
        s_startTime = System.nanoTime();
    }
    
    public static void End()
    {
        long duration = System.nanoTime() - s_startTime;
        
        
        s_aggregatedDuration += duration;
        s_counter++;
        /*
        System.out.println(
                "request index: " + s_counter + 
                " - duration: " + duration + 
                " - aggregated: " + s_aggregatedDuration + 
                " - average: " + s_aggregatedDuration / s_counter);
    */
     
    }
    /*
    public static long GetAggregatedDuration()
    {
        return s_aggregatedDuration;
    }
    */
}
