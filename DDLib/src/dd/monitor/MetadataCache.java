
package dd.monitor;

import dd.output.LoggingContext;
import java.util.*;

public class MetadataCache 
{
    private static final String                   METADATA_ELEMENT_DELIMITER = "-";
    
    private Map<String, Set<String> >             m_metadata = new TreeMap<>();
    
    
    public MetadataCache()
    {}

    public void addMetadata(String[] metadataRecords)
    {
        String[]        elements;
        
        
        for(String record : metadataRecords)
        {
            elements = record.split(METADATA_ELEMENT_DELIMITER);
            addTargetOfNode(elements[0], elements[1]);
        }
    }
    
    public String[] getMetadata()
    {
        ArrayList<String>           records = new ArrayList<>();
    
        
        for(String nodeId : m_metadata.keySet())
        {
            for(String targetId : m_metadata.get(nodeId))
            {
                records.add(nodeId + METADATA_ELEMENT_DELIMITER + targetId);
            }
        }
        
        return records.toArray(new String[0]);
    }
    
    public String[] getBackupTargetsOfNode(String nodeId)
    {
        if (m_metadata.containsKey(nodeId))
        {
            return m_metadata.get(nodeId).toArray(new String[0]);
        }
        
        return null;
    }
    
    public void addTargetOfNode(String nodeId, String targetId)
    {
        Set<String>         targets;
        
        
        if (!m_metadata.containsKey(nodeId))
        {
            m_metadata.put(nodeId, new TreeSet<String>());
        }
        
        targets = m_metadata.get(nodeId);
        targets.add(targetId);
    }   
    
    public void writeOut(LoggingContext log)
    {
        log.log("MetadataCache records:");
        
        for (String nodeId : m_metadata.keySet())
        {
            for (String targetId : m_metadata.get(nodeId).toArray(new String[0]))
            {
                log.log("       nodeId: " + nodeId + " targetId: " + targetId);
            }
        }
    }
}