
package dd.gpdklmanager.gptarget;

import dd.Configuration;
import dd.ExperimentConfiguration;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class GpTargetProviderFactory 
{
    public static GpTargetProvider getTargetProviderFromConfiguration(
            LoggingContext log,
            BackupOnLocalHost localGpAndDkl,
            int maxMetricInRoutingTable)
    {
        String                      algorithmType = ExperimentConfiguration.getGossipProtocolBackupNodeSelectionAlgorithm(log);
        List<String>                nodesWithGossipProtocol = getNodesWithGossipProtocol(log);
        GpTargetProvider            targetProvider = null;
        
        
        log.log("GP taget provider algorithm: " + algorithmType);
        
        switch(algorithmType)
        {
            case "dependence":
                targetProvider = new GpTargetProviderDependent(
                        log, 
                        nodesWithGossipProtocol, 
                        localGpAndDkl,
                        maxMetricInRoutingTable);
                break;
                
            case "routingtable":
                targetProvider = new GpTargetProviderRoutingTable(
                        log, 
                        nodesWithGossipProtocol, 
                        localGpAndDkl,
                        maxMetricInRoutingTable);
                break;
        }
        
        if (targetProvider == null)
        {
            log.log("!!!!!!!!!!!!! configuration of GP target provider is wrong: " + algorithmType);
        }
        
        return targetProvider;
    }
    
    public static GpTargetProviderDependent getTargetProviderDependent(
            LoggingContext log,
            BackupOnLocalHost localGpAndDkl,
            int maxMetricInRoutingTable)
    {
        return new GpTargetProviderDependent(
                log, 
                getNodesWithGossipProtocol(log), 
                localGpAndDkl,
                maxMetricInRoutingTable);
    }
    
    public static List<String> getNodesWithGossipProtocol(LoggingContext log)
    {
        Element             nodesElement;
        NodeList            nodeElements;
        Element             nodeElement;
        final String        attributeName = "hasGpService";
        boolean             attributeValue;
        ArrayList<String>   nodes = new ArrayList<>();
        
        
        nodesElement = Configuration.getNodesElement(log);
        nodeElements = nodesElement.getElementsByTagName("node");

        for(int i = 0; i<nodeElements.getLength(); i++)
        {
            nodeElement = (Element) nodeElements.item(i);
            
            if (nodeElement.hasAttribute(attributeName))
            {
                attributeValue = Boolean.parseBoolean(nodeElement.getAttribute(attributeName));
                
                if (attributeValue)
                {
                    nodes.add(nodeElement.getAttribute("ip"));
                }
            }
        }
        
        return Arrays.asList(nodes.toArray(new String[0]));
    }
}
