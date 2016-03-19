
package fl;

import dd.ExperimentConfiguration;
import dd.graph.DependenceGraph;
import dd.output.LoggingContext;
import fl.serverlog.LogEntry;
import fl.serverlog.ServerLogReader;
import java.util.Date;


public class FlStatusProvider 
{
    private ServerLogReader     m_serverLogReader;
    private LoggingContext      m_log;
    private String              m_serviceId;
    private Date                m_fromTimestampEX;
    private Date                m_toTimestampEX;
    private Date                m_fromTimestampTO;
    private Date                m_toTimestampTO;
    private LogEntry[]          m_logEntriesEX = null;
    private LogEntry[]          m_logEntriesTO = null;
    
    
    public FlStatusProvider(
            LoggingContext log,
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        m_log = log;
        m_serviceId = serviceId;
        m_fromTimestampEX = new Date(fromTimestamp);
        m_toTimestampEX = new Date(toTimestamp);
        m_fromTimestampTO = m_toTimestampEX;
        m_toTimestampTO = new Date(toTimestamp + ExperimentConfiguration.getFlTimeoutSymptomsTimeWindowSize(m_log));
    }
    
    private LogEntry[] getLogEntriesEX()
    {
        if (!ExperimentConfiguration.getFlServerLogScanningOn(m_log))
        {
            return new LogEntry[0];
        }
        
        if (m_logEntriesEX == null)
        {
            m_logEntriesEX = getServerLogReader().getEntriesForServiceInTimeWindow(
                    m_serviceId, 
                    m_fromTimestampEX, 
                    m_toTimestampEX);            
        }
        
        return m_logEntriesEX;
    }
    
    private LogEntry[] getLogEntriesTO()
    {
        if (!ExperimentConfiguration.getFlServerLogScanningOn(m_log))
        {
            return new LogEntry[0];
        }
        
        if (m_logEntriesTO == null)
        {
            m_logEntriesTO = getServerLogReader().getEntriesForServiceInTimeWindow(
                    m_serviceId, 
                    m_fromTimestampTO, 
                    m_toTimestampTO);            
        }
        
        return m_logEntriesTO;
    }
    
    private ServerLogReader getServerLogReader()
    {
        if (m_serverLogReader == null)
        {
            m_serverLogReader = new ServerLogReader(
                    m_log, 
                    fl.Configuration.getServerLogPath());
        }
        
        return m_serverLogReader;
    }
    
    public DependenceGraph.FlStatus getStatusEXOfService()
    {
        DependenceGraph.FlStatus        flStatus = DependenceGraph.FlStatus.NoFault;
        boolean                         elementFault = false;
        boolean                         transitiveFault = false;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            switch (entry.getFaultType())
            {
                case Network:
                case Service:
                    elementFault = true;
                    break;
                    
                case Transitive:
                    transitiveFault = true;
                    break;
            }
        }

        if (elementFault && !transitiveFault) flStatus = DependenceGraph.FlStatus.ElementFault;
        if (!elementFault && transitiveFault) flStatus = DependenceGraph.FlStatus.TransitiveFault;
        if (elementFault && transitiveFault) flStatus = DependenceGraph.FlStatus.ElementAndTransitiveFault;

        return flStatus;
    }
    
    public long getLatestEXElementTimestampOfService()
    {
        long                            timestamp;
        long                            latestTimestamp = 0;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            switch (entry.getFaultType())
            {
                case Network:
                case Service:
                    timestamp = entry.getTimestamp().getTime();
                    latestTimestamp = (timestamp > latestTimestamp) ? timestamp : latestTimestamp;
                    break;
            }
        }

        return latestTimestamp;    
    }

    public DependenceGraph.FlStatus getStatusTOOfService()
    {
        for(LogEntry entry : getLogEntriesTO())
        {
            if (entry.getFaultType() == ServiceFaultGtTrace.FaultType.Timeout)
            {
                return DependenceGraph.FlStatus.ElementFault;
            }            
        }

        return DependenceGraph.FlStatus.NoFault;
    }
    
    public long getFirstTOElementTimestampOfService()
    {
        long                            timestamp;
        long                            firstTimestamp = 0;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            if (entry.getFaultType() == ServiceFaultGtTrace.FaultType.Timeout)
            {
                timestamp = entry.getTimestamp().getTime();
                if (firstTimestamp == 0)
                {
                    firstTimestamp = timestamp;
                }
                else
                {
                    firstTimestamp = (timestamp < firstTimestamp) ? timestamp : firstTimestamp;
                }
            }
        }

        return firstTimestamp;    
    }
    
    public DependenceGraph.FlStatus getStatusEXOfDependency(String antecedentServiceId)
    {
        DependenceGraph.FlStatus        flStatus = DependenceGraph.FlStatus.NoFault;
        boolean                         elementFault = false;
        boolean                         transitiveFault = false;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            if (entry.getTarget().equals(antecedentServiceId))
            {
                switch (entry.getFaultType())
                {
                    case Network:
                    case Service:
                        elementFault = true;
                        break;

                    case Transitive:
                        transitiveFault = true;
                        break;
                }
            }
        }

        if (elementFault && !transitiveFault) flStatus = DependenceGraph.FlStatus.ElementFault;
        if (!elementFault && transitiveFault) flStatus = DependenceGraph.FlStatus.TransitiveFault;
        if (elementFault && transitiveFault) flStatus = DependenceGraph.FlStatus.ElementAndTransitiveFault;

        return flStatus;
    }
    
    public long getLatestEXElementTimestampOfDependency(String antecedentServiceId)
    {
        long                            timestamp;
        long                            latestTimestamp = 0;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            if (entry.getTarget().equals(antecedentServiceId))
            {
                switch (entry.getFaultType())
                {
                    case Network:
                    case Service:
                        timestamp = entry.getTimestamp().getTime();
                        latestTimestamp = (timestamp > latestTimestamp) ? timestamp : latestTimestamp;
                        break;
                }
            }
        }
        
        return latestTimestamp;    
    }

    public DependenceGraph.FlStatus getStatusTOOfDependency(String antecedentServiceId)
    {
        for(LogEntry entry : getLogEntriesTO())
        {
            if (entry.getTarget().equals(antecedentServiceId))
            {
                if (entry.getFaultType() == ServiceFaultGtTrace.FaultType.Timeout)
                {
                    return DependenceGraph.FlStatus.ElementFault;
                }
            }
        }

        return DependenceGraph.FlStatus.NoFault;
    }
    
    public long getFirstTOElementTimestampOfDependency(String antecedentServiceId)
    {
        long                            timestamp;
        long                            firstTimestamp = 0;
                
        
        for(LogEntry entry : getLogEntriesEX())
        {
            if (entry.getTarget().equals(antecedentServiceId))
            {
                if (entry.getFaultType() == ServiceFaultGtTrace.FaultType.Timeout)
                {
                    timestamp = entry.getTimestamp().getTime();
                    if (firstTimestamp == 0)
                    {
                        firstTimestamp = timestamp;
                    }
                    else
                    {
                        firstTimestamp = (timestamp < firstTimestamp) ? timestamp : firstTimestamp;
                    }
                }
            }
        }
        
        return firstTimestamp;    
    }
}
