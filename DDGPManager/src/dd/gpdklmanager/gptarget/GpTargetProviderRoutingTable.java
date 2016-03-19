
package dd.gpdklmanager.gptarget;

import dd.Network;
import dd.RoutingEntry;
import dd.RoutingTable;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.output.LoggingContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class GpTargetProviderRoutingTable extends GpTargetProvider
{
    
    // orders hosts according to theyr closeness to the localhost
    
    public GpTargetProviderRoutingTable(
            LoggingContext log, 
            List<String> nodesWithGossipProtocol, 
            BackupOnLocalHost localGpAndDkl,
            int maxMetricInRoutingTable)
    {
        super(log, nodesWithGossipProtocol, localGpAndDkl, maxMetricInRoutingTable);
    }
        
    @Override
    public String[] getTargets() 
    {
        String[]                    reachableNodes = getRoutingTableTargets(0);
        ArrayList<String>           targets = new ArrayList<>();
        
        /*
                System.out.println("routing table");
        for(int i = 0; i<reachableNodes.length; i++)
        {
            System.out.println(reachableNodes[i]);
        }

        
                System.out.println("gp");
        for(int i = 0; i<nodesWithGossipProtocol.size(); i++)
        {
            System.out.println(nodesWithGossipProtocol.get(i));
        }*/

        
        for (String node : reachableNodes)
        {
            if (m_nodesWithGossipProtocol.contains(node))
            {
                targets.add(node);
            }
        }
        
        if (targets.isEmpty())
        {
            m_log.log("There are 0 reachable nodes with gossip service - configuration: " + 
                    m_nodesWithGossipProtocol.size() + " - routing table: " + 
                    reachableNodes.length);
        }
        
        return targets.toArray(new String[0]);        
    }
    
    public String[] getTargetsAtDistance(int hopDistance)
    {
        return getRoutingTableTargets(hopDistance);
    }

    private String[] getRoutingTableTargets(int withThisMetricOnly) 
    {
        HashMap<InetAddress,RoutingEntry>           routes;
        InetAddress[]                               ips;
        InetAddress                                 ip;
        String                                      target;
        int                                         metric;
        SortedMap<Integer, ArrayList<String> >      targets = new TreeMap<>();
        ArrayList<String>                           result = new ArrayList<>();
        Integer[]                                   metrics;
                
                
        try 
        {
            routes = RoutingTable.get();
        } 
        catch (Exception ex) 
        {
            m_log.log("Exception while loading route table: ");
            //m_log.log(ex);
            
            routes = new HashMap<>();
            
            try {
                m_log.log("!!! for testing puposes using localhost address");
                routes.put(
                        InetAddress.getByName(
                        Network.getLocalHostIp(m_log)), 
                        new RoutingEntry(null, 1, Boolean.TRUE));
                //return null;
            } catch (UnknownHostException ex1) {
                m_log.log(ex1);
            }
        }
        
        ips = routes.keySet().toArray(new InetAddress[0]);
        
        for(int i = 0; i < ips.length; i++)
        {
            ip = ips[i];
            target = ip.getHostAddress();
            metric = routes.get(ip).metric;
            
            if (!targets.containsKey(metric)) 
            {
                targets.put(metric, new ArrayList<String>());
            }
            
            targets.get(metric).add(target);
        }
        
        metrics = targets.keySet().toArray(new Integer[0]);
        
        //limit to this metric only
        if (withThisMetricOnly != 0)
        {
            result.addAll(targets.get(withThisMetricOnly));
        }
        else
        {
            m_log.log("routing table state ...");
            for(Integer i : metrics)
            {
                if (i > m_maxMetricInRoutingTable) break;
                result.addAll(targets.get(i));
                m_log.log("hop count " + i, targets.get(i).size());
            }
        }
        
        return result.toArray(new String[0]);
    }
}