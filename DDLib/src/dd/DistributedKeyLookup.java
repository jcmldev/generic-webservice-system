
package dd;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DistributedKeyLookup 
{  
    public static String[] getNodesToStoreIpDependencies(LoggingContext log, String ip)
    {
        String[]            ips = getNodesWithDklService(log);
        int                 numberOfBakupNodes = ExperimentConfiguration.getDistributedKeyLookupNumberOfBackupNodes(log);
        

        return getNodesToStoreIpDependencies(log, ips, ip, numberOfBakupNodes);
    }
        
    public static String[] getNodesToStoreIpDependencies(
            LoggingContext log, 
            String[] ips, 
            String ip, 
            int numberOfBakupNodes)
    {
        int             ipHash = ip.hashCode();
        Random          rand = new Random(ipHash);
        int             indexOfDklNode = rand.nextInt(ips.length);
        List<String>    backupIps = new Vector<>();

        

        // get the first backup ip based on hash
        if (ips[indexOfDklNode].equals(ip))
        {
            if (ips.length == 1) return new String[]{};
                
            if (indexOfDklNode == ips.length - 1)
            {
                indexOfDklNode = 0;
            }            
            else 
            {
                indexOfDklNode++;
            }
        }
        
        backupIps.add(ips[indexOfDklNode]);
        
        // get the rest of ips by following the ip sequence from configuration
        
        while (backupIps.size() < numberOfBakupNodes)
        {
            indexOfDklNode++;

            if (ips[indexOfDklNode].equals(ip) || backupIps.contains(ips[indexOfDklNode]))
            {
                if (indexOfDklNode == ips.length - 1)
                {
                    indexOfDklNode = 0;
                }            
                else 
                {
                    indexOfDklNode++;
                }
            }
        
            backupIps.add(ips[indexOfDklNode]);
            
            if (backupIps.size() == ips.length)
            {
                break;
            }
        }
        
        return backupIps.toArray(new String[0]);
    }

    public static String[] getNodesWithDklService(LoggingContext log)
    {
        String              attributeName = "hasDklService";
        ArrayList           nodes = new ArrayList();
        Element             nodesElement = Configuration.getNodesElement(log);
        NodeList            nodeElements = nodesElement.getElementsByTagName("node");
        boolean             attributeValue;
        Element             nodeElement;


        for (int i = 0; i < nodeElements.getLength(); i++)
        {
            nodeElement = (Element)nodeElements.item(i);

            if (nodeElement.hasAttribute(attributeName))
            {
                attributeValue = Boolean.parseBoolean(nodeElement.getAttribute(attributeName));

                if (attributeValue)
                {
                    nodes.add(nodeElement.getAttribute("ip"));
                }
            }
        }

        return (String[])nodes.toArray(new String[0]);
    }
}
