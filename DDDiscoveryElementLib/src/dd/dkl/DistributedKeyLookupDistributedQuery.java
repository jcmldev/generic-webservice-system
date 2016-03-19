
package dd.dkl;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DistributedKeyLookupDistributedQuery 
{    
    private LoggingContext                                      m_log;
    private String[]                                            m_backupNodeIps;
    private String                                              m_dependentIp;
    private ArrayList<DistributedKeyLookupAsynchRequest>        m_requests = new ArrayList<DistributedKeyLookupAsynchRequest>();
    
    
    public DistributedKeyLookupDistributedQuery( 
            LoggingContext log,
            String[] backupNodeIps,
            String dependentIp)
    {
        m_log = log;
        m_backupNodeIps = backupNodeIps;
        m_dependentIp = dependentIp;
    }
    
    public String[] runDistributedQuery()
    {
        sendRequests();
        waitForAllResponses();  
        return getResponseDependencies();
    }
    
    private String[] getResponseDependencies()
    {
        Set<String>             responseDependencies = new TreeSet<String>();
        String[]                response = null;
        
        
        if (getWasAtLeastOneRequestSuccessful())
        {
            for(DistributedKeyLookupAsynchRequest request : m_requests)
            {
                if (request.getWasRequestSuccessful())
                {
                    addDependenciesIntoList(
                            responseDependencies, 
                            request.getResponseDependencies());
                }
            }
            
            response = responseDependencies.toArray(new String[0]);
        }        
        
        m_log.log("DKL: Received dependencies ...");

        for(String dependence : responseDependencies)
        {
            m_log.log("     dependence: " + dependence);
        }
        
        return response;
    }
    
    private boolean getWasAtLeastOneRequestSuccessful()
    {
        for(DistributedKeyLookupAsynchRequest request : m_requests)
        {
            if (request.getWasRequestSuccessful()) return true;
        }
        
        return false;
    }
    
    private void addDependenciesIntoList(Set<String> dependenciesList, List<String> dependenciesToAdd)
    {
        for(String dependency : dependenciesToAdd)
        {
            if (!dependenciesList.contains(dependency))
            {
                dependenciesList.add(dependency);
            }
        }
    }
    
    private void waitForAllResponses()
    {
        // wait untill all responses are received before continue on processing data
        try
        {
            while (!getAreAllRequestsDone()) 
            {
                Thread.sleep(10);
            }
        }
        catch(Exception ex)
        {
            m_log.log(ex);
        }    
    }
    
    private boolean getAreAllRequestsDone()
    {
        for(DistributedKeyLookupAsynchRequest request : m_requests)
        {
            if (!request.getIsDone())
            {
                return false;
            }
        }
        
        return true;
    }
    
    private void sendRequests()
    {
        DistributedKeyLookupAsynchRequest             request;
        
        
        for(String backupNodeIp : m_backupNodeIps)
        {
            request = new DistributedKeyLookupAsynchRequest(
                    m_log,
                    backupNodeIp, 
                    m_dependentIp);

            try
            {
                Thread.sleep(10);
            }
            catch(Exception ex)
            {
                m_log.log(ex);
            }    
            
            m_requests.add(request);
            request.sendRequest();
        }    
    }       
}
