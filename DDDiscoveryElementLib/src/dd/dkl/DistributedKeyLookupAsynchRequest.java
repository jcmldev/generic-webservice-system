
package dd.dkl;

import dd.output.LoggingContext;
import java.util.List;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

public class DistributedKeyLookupAsynchRequest 
{
    private LoggingContext          m_log;
    private String                  m_backupNodeIp;
    private String                  m_dependentIp;
    private boolean                 m_isDone = false;
    private boolean                 m_wasRequestSuccessful = false;
    private List<String>            m_responseDependencies;
    
    
    public DistributedKeyLookupAsynchRequest(
            LoggingContext log,
            String backupNodeIp,
            String dependentIp)
    {
        m_log = log;
        m_backupNodeIp = backupNodeIp;
        m_dependentIp = dependentIp;
    }
    
    public boolean getIsDone()
    {
        return m_isDone;
    }
    
    public boolean getWasRequestSuccessful()
    {
        return m_wasRequestSuccessful;
    }
    
    public List<String> getResponseDependencies()
    {
        return m_responseDependencies;
    }
    
    public void sendRequest()
    {
        DistributedKeyLookupServiceWrapper               dklWrapper;
        
        
        dklWrapper = new DistributedKeyLookupServiceWrapper(m_log, m_backupNodeIp);
        
        m_log.log(
                "DKL: Asynch request sent to node: " +
                m_backupNodeIp + 
                " for antecedent dependencies of node ip: " + 
                m_dependentIp);
        
        try 
        {    
            AsyncHandler<dd.bck.DKLGetAntecedentServicesOfDependentIpResponse> asyncHandler = 
                    new AsyncHandler<dd.bck.DKLGetAntecedentServicesOfDependentIpResponse>() 
            {

                @Override
                public void handleResponse(Response<dd.bck.DKLGetAntecedentServicesOfDependentIpResponse> response) 
                {
                    try 
                    {
                        m_responseDependencies = response.get().getReturn();
                        m_wasRequestSuccessful = true;

                        m_log.log(
                            "DKL: Received response from node: " +
                            m_backupNodeIp + 
                            " for antecedent dependencies of node ip: " + 
                            m_dependentIp);
                        
                        for(String dependence : m_responseDependencies)
                        {
                            m_log.log("     dependence: " + dependence);
                        }
                    } 
                    catch (Exception ex) 
                    {
                        m_log.log(
                                "DKL: Request for antecedent dependencies of ip: " + 
                                m_dependentIp + 
                                " sent to backup node: " + m_backupNodeIp +
                                " failed with exception ...");
                        m_log.log(ex);
                    }
                    
                    m_isDone = true;
                }
            };
            
            java.util.concurrent.Future<? extends Object> result = dklWrapper.getPort().dklGetAntecedentServicesOfDependentIpAsync(
                    m_dependentIp, 
                    asyncHandler);
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "DKL: Method of request for dependencies of: " + 
                    m_dependentIp + 
                    " sent to backup node: " + m_backupNodeIp +
                    " failed with exception ...");
        }
    }    
}
