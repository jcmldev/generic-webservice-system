package dd.monitor;

import dd.dependenceset.DependencySet;
import dd.graph.DependenceGraph;
import dd.monitor.multihopgp.BackupDataCalculator;
import dd.monitor.multihopgp.BackupDataSet;
import dd.output.LoggingContext;
import fl.FlStatusProvider;
import java.util.ArrayList;
import java.util.Collection;

public class ServerMonitor 
{
    public static final String          INTRA_DEPENDENCE_SOURCE_AND_SERVICE_DELIMITER = "_INTRA_";

    private static ServerMonitor        m_instance;
    private ServiceMonitors             m_serviceMonitors = new ServiceMonitors();
    private HostList                    m_dependentHosts = new HostList();
    private HostList                    m_antecedentHosts = new HostList();
    private DklIpDependenceList         m_dklDependencies = new DklIpDependenceList();
    
    
    private ServerMonitor()
    {}
    
    public void addInterDependencyOccurrence(String strSource, String strTarget)
    {
        ServiceMonitor      serviceMonitor;
        
        
        if ((strSource != null) && (strTarget != null))
        {
            serviceMonitor = this.m_serviceMonitors.get(strSource);
            serviceMonitor.addInterDependencyOccurrence(strTarget);
        }
    }

    // intra dependence is dependence of incoming dependence to outgoing dependence
    // thus it is implemented here in same structure as inter dependence
    // the source is the combination of incoming source and the service 
    // the target is the outgoing target
    public void addIntraDependencyOccurrence(String strSource, String middleService, String strTarget)
    {
        strSource = strSource + INTRA_DEPENDENCE_SOURCE_AND_SERVICE_DELIMITER + middleService;
        addInterDependencyOccurrence(strSource, strTarget);
    }

    public void addInterDependencyOccurrence(String strSource, String strTarget, long timestamp)
    {
        ServiceMonitor      serviceMonitor;
        
        
        if ((strSource != null) && (strTarget != null))
        {
            serviceMonitor = this.m_serviceMonitors.get(strSource);
            serviceMonitor.addInterDependencyOccurrence(strTarget, timestamp);
        }
    }
    
    public void addIntraDependencyOccurrence(String strSource, String middleService, String strTarget, long timestamp)
    {
        strSource = strSource + INTRA_DEPENDENCE_SOURCE_AND_SERVICE_DELIMITER + middleService;
        addInterDependencyOccurrence(strSource, strTarget, timestamp);
    }

    public String[] getInterDependenciesForTimeWindow(String serviceId, long fromTimestamp, long toTimestamp)
    {
        if ((serviceId == null) || (serviceId.length() == 0)) return null;

        ArrayList                               strIDs = new ArrayList();
        ServiceMonitor                          serviceMonitor;
        Collection<InterDependency>             interDependencies;
        
        
        serviceMonitor = this.m_serviceMonitors.get(serviceId);

        interDependencies = serviceMonitor.getInterDependencies(fromTimestamp, toTimestamp);

        for (InterDependency interDependency : interDependencies)
        {
          strIDs.add(interDependency.getTarget());
        }

        return (String[])strIDs.toArray(new String[0]);
    }

    // its kinda messy but otherwise it would requrie some significant rework
    public DependencySet getAllInterDependenciesForTimeWindow(LoggingContext log, long fromTimestamp, long toTimestamp)
    {
        ArrayList<String>                       strIDs;
        Collection<InterDependency>             interDependencies;
        DependencySet                           ds = new DependencySet();
        DependencySet                           serviceDs;
        

        for(ServiceMonitor serviceMonitor : m_serviceMonitors.getMonitors())
        {
            strIDs = new ArrayList();
            interDependencies = serviceMonitor.getInterDependencies(fromTimestamp, toTimestamp);

            for (InterDependency interDependency : interDependencies)
            {
              strIDs.add(interDependency.getTarget());
            }
            
            serviceDs = loadLocalDependencySet(
                    log, 
                    serviceMonitor.getServiceId(), 
                    strIDs.toArray(new String[0]), 
                    fromTimestamp, 
                    toTimestamp);
            
            ds.addDependencySet(serviceDs);
        }
        
        return ds;
    }

    public static DependencySet loadLocalDependencySet(
            LoggingContext log,
            String seviceId,
            String[] antecedentServices,
            long fromTimestamp, 
            long toTimestamp)
    {
        DependencySet                       resultSet = new DependencySet();
        FlStatusProvider                    flProvider = new FlStatusProvider(log, seviceId, fromTimestamp, toTimestamp);
        DependenceGraph.FlStatus            flStatusEX;
        DependenceGraph.FlStatus            flStatusTO;
        long                                lastEXTimestamp;
        long                                firstTOTimestamp;
        
        
        flStatusEX = flProvider.getStatusEXOfService();
        flStatusTO = flProvider.getStatusTOOfService();
        lastEXTimestamp = flProvider.getLatestEXElementTimestampOfService();
        firstTOTimestamp = flProvider.getFirstTOElementTimestampOfService();
        
        resultSet.addServiceInformation(
                seviceId, 
                DependenceGraph.ReachabilityStatus.Directly, 
                flStatusEX,
                flStatusTO, 
                lastEXTimestamp, 
                firstTOTimestamp);
        
        for (String antecedentService : antecedentServices)
        {
            flStatusEX = flProvider.getStatusEXOfDependency(antecedentService);
            flStatusTO = flProvider.getStatusTOOfDependency(antecedentService);
            lastEXTimestamp = flProvider.getLatestEXElementTimestampOfDependency(antecedentService);
            firstTOTimestamp = flProvider.getFirstTOElementTimestampOfDependency(antecedentService);
            
            resultSet.addDependencyInformation(
                    seviceId, 
                    antecedentService, 
                    flStatusEX,
                    flStatusTO, 
                    lastEXTimestamp, 
                    firstTOTimestamp);
        }
        
        return resultSet;
    }
    
    public static ServerMonitor getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new ServerMonitor();
        }
        
        return m_instance;
    }
    /*
    public static void reset()
    {
        m_instance = new ServerMonitor();
    }
*/

    public HostList getDepedentHosts()
    {
        return m_dependentHosts;
    }
    
    public HostList getAntecedentHosts()
    {
        return m_antecedentHosts;
    }
    
    public DklIpDependenceList getDklDependencies()
    {
        return m_dklDependencies;
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("Monitor status............................................");
        this.m_serviceMonitors.writeOut(log);
    }

    public String[] getBackupData(long fromTimestamp, long toTimestamp, int timeSlotSize)
    {
        return this.m_serviceMonitors.getBackupData(fromTimestamp, toTimestamp, timeSlotSize);
    }
    
    public void addBackupDataIntoMultihopGpDataSet(
        BackupDataSet dataSet,
        BackupDataCalculator backupCalculator)
    {
        m_serviceMonitors.addBackupDataIntoMultihopGpDataSet(
                dataSet, 
                backupCalculator);
    }
}