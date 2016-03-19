
package dd.dkl;

import dd.DistributedKeyLookup;
import dd.Network;
import dd.output.LoggingContext;

public class DistributedKeyLookupClient 
{
    private LoggingContext                  m_log;
    private String                          m_serviceId;
    
    /*
     * serviceId - url of unavailable service
     */
    public DistributedKeyLookupClient (LoggingContext log, String serviceId)
    {
        m_log = log;
        m_serviceId = serviceId;
    }
    
    /*
     * Response with no dependencies may mean that the dependencies did not occure 
     * but also that the dependence data are not available (were not synchronized) also the server may have only part of the required data.
     * Therefore, request for dependencies should be send to all backup nodes and the result should be unique sum of the responses
     */
    
    /*
     * The dependent Ip is ip of node the unavailable node 
     * - thus the node hosting the service from constructor parameter
     */
    public String[] getAntecedentServicesOfDependentIp()
    {
        String[]                                        backupHosts;
        DistributedKeyLookupDistributedQuery            dklDistributedQuery;
        String[]                                        dependencies = null;
        

        backupHosts = getBackupHosts();

        m_log.log(
                "DKL - request for antecedent dependencies of ip: " + 
                getServiceHostIp() +
                " to " + backupHosts.length + " nodes" );
        
        if (backupHosts.length > 0)
        {
            dklDistributedQuery = new DistributedKeyLookupDistributedQuery(
                    m_log, 
                    backupHosts, 
                    m_serviceId);

            dependencies = dklDistributedQuery.runDistributedQuery();
        }
        
        return dependencies;
    }  

    private String getServiceHostIp()
    {
        return Network.getHostIpFromUrl(m_log, m_serviceId);
    }
    
    private String[] getBackupHosts()
    {
        return DistributedKeyLookup.getNodesToStoreIpDependencies(m_log, getServiceHostIp());
    }
}
