package dd.genericclient2;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;
import java.util.ArrayList;

public class LocalDependenceDiscoveryData
{   
    public static String[] getHostProcessInterDependenciesForTimeWindow(
            LoggingContext log,
            //String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        String              serviceId = Network.getClientApplicationProcessId(log);
        String[]            dependencies;
        
        
        dependencies = ServerMonitor.getInstance().getInterDependenciesForTimeWindow(
                serviceId, 
                fromTimestamp, 
                toTimestamp);
        
        // debug
        if (ExperimentConfiguration.getUseSingleMachineDebugCode(log))
        {
            log.log("Using hard-coded list of outgoing dependencies - debug on client!!!");
            dependencies = new String[]{
            //"http://" + Network.getLocalHostIp(log) + ":8080/WS2/GWS", 
            "http://" + Network.getLocalHostIp(log) + ":8080/WS1/GWS"
            };
        }
        
        
        dependencies = removeUnrelatedServicesFromDependencies(dependencies);
        
        return dependencies;
    }
    
    private static String[] removeUnrelatedServicesFromDependencies(String[] dependencies)
    {
        ArrayList<String>       list = new ArrayList<>();
        
        
        for (String dependency : dependencies)
        {
            if (!dependency.contains("DDWSProvider") && !dependency.contains("Backup"))
            {
                list.add(dependency);
            }
        }
        
        return list.toArray(new String[0]);
    }
}