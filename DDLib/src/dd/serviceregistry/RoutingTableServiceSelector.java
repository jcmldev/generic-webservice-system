
package dd.serviceregistry;

import dd.RoutingEntry;
import dd.RoutingTable;
import dd.Network;
import dd.output.LoggingContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;


public class RoutingTableServiceSelector implements ServiceSelector
{
    private static final int                NODE_NOT_FOUND_METRIC = -1;
    
    private LoggingContext                  m_log;
    
    
    public RoutingTableServiceSelector(LoggingContext log)
    {
        m_log = log;
    }
    
    @Override
    public String selectService(String[] hosts) 
    {
        HashMap<InetAddress,RoutingEntry>       routes;
        InetAddress                             hostIp;
        int                                     metric;
        int                                     lowestMetric = Integer.MAX_VALUE;
        String                                  lowestMetricService = null;
    
        
        routes = getRoutes();
        
        if (routes == null)
        {
            return hosts[0];
        }
        
        for(String host : hosts)
        {
            hostIp = null;
            
            if (host.equals(Network.getLocalHostIp(m_log)))
            {
                return host;
            }
            
            try 
            {
                hostIp = InetAddress.getByName(host);
            } 
            catch (UnknownHostException ex) 
            {
                m_log.log(ex);
            }
            
            if (hostIp != null)
            {
                metric = getMetricOfIp(routes, hostIp);
                
                if (metric == NODE_NOT_FOUND_METRIC) continue;
                
                if (metric < lowestMetric)
                {
                    lowestMetric = metric;
                    lowestMetricService = host;
                }
            }
        }
        
        return lowestMetricService;
    }
    
    private HashMap<InetAddress, RoutingEntry> getRoutes()
    {
        HashMap<InetAddress,RoutingEntry>       routes = null;
        
        
        try
        {
            routes = RoutingTable.get();
        }
        catch(Exception ex)
        {
            m_log.log("Exception while loading route table...");
            //m_log.log(ex);
        }
        
        return routes;
    }
    
    private int getMetricOfIp(HashMap<InetAddress,RoutingEntry> routes, InetAddress serviceIp)
    {
        int                 metric = NODE_NOT_FOUND_METRIC;
        RoutingEntry        entry = routes.get(serviceIp);
        
        
        if (entry != null && entry.isUp)
        {
            metric = entry.metric;
        }

        return metric;
    }
}