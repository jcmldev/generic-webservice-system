
package dd.ws;

import dd.Network;
import dd.dependenceset.DependencySet;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;

public class RequestForLocalDependencies 
{    
    private LoggingContext          m_log;
    
    
    public RequestForLocalDependencies(LoggingContext log)
    {
        m_log = log;
    }

    public String[] getInterDependenciesForTimeWindow(
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        String[]        antecedentDependencies;
        String          serviceName;
        DependencySet   resultSet;
        
        
        m_log.log(
                "RequestForLocalDependencies.getInterDependenciesForTimeWindow - serviceId: " + 
                serviceId + 
                " from: " + 
                fromTimestamp + 
                " to: " + 
                toTimestamp);  
        
        serviceName = Network.getServiceNameFromUrl(m_log, serviceId);
        
        m_log.log(
                "Service name: " + 
                serviceName + 
                " ..............................");
        
        antecedentDependencies = ServerMonitor.getInstance().getInterDependenciesForTimeWindow(
                serviceName, 
                fromTimestamp, 
                toTimestamp);
        
        resultSet = ServerMonitor.loadLocalDependencySet(
                m_log, 
                serviceId, 
                antecedentDependencies, 
                fromTimestamp, 
                toTimestamp);
        
        m_log.log(
                "Response of service: " + 
                serviceId + 
                "...............................................");
        resultSet.writeOut(m_log);
        
        return resultSet.toStringArray();
    }
}
