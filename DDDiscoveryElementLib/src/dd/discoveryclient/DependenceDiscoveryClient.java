
package dd.discoveryclient;

import dd.ExperimentConfiguration;
import dd.discoveryElement.DiscoveryElement;
import dd.graph.DependenceGraph;
import dd.graph.DependenceGraphMerger;
import dd.output.LoggingContext;


public class DependenceDiscoveryClient
{
    private int                                 m_conversationId; 
    private String                              m_clientId;
    private DependenceGraph.FlStatus            m_clientFlStatusEX;
    private DependenceGraph.FlStatus            m_clientFlStatusTO;
    private long                                m_fromTimestamp;
    private long                                m_toTimestamp;
    private String[]                            m_rootDependencies;
    private DependenceGraph.FlStatus[]          m_rootDependenciesFlStatusEX;
    private DependenceGraph.FlStatus[]          m_rootDependenciesFlStatusTO;
    private int                                 m_attemptIndex = 0;
    private LoggingContext                      m_log;
    
    
    public DependenceDiscoveryClient(
            int conversationId,
            String clientId,
            DependenceGraph.FlStatus clientFlStatusEX,
            DependenceGraph.FlStatus clientFlStatusTO,
            long fromTimestamp,
            long toTimestamp,
            String[] rootDependencies,
            DependenceGraph.FlStatus[] rootDependenciesFlStatusEX,
            DependenceGraph.FlStatus[] rootDependenciesFlStatusTO,
            LoggingContext log)
    {
        m_conversationId = conversationId;
        m_clientId = clientId;
        m_clientFlStatusEX = clientFlStatusEX;
        m_clientFlStatusTO = clientFlStatusTO;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = toTimestamp;
        m_rootDependencies = rootDependencies;
        m_rootDependenciesFlStatusEX = rootDependenciesFlStatusEX;
        m_rootDependenciesFlStatusTO = rootDependenciesFlStatusTO;
        m_log = log;
    }

    public DependenceGraph runDiscovery() 
    {
        DependenceGraph                 unionDG = null;
        int                             numberOfAttempts = ExperimentConfiguration.getDependenceDiscoveryNumberOfAttempts(m_log);
        DependenceGraph                 lastAttemptDG;
        int                             delayBetweenDiscoveryAttempts;
        int                             twAdditionalTimeAfter = ExperimentConfiguration.getDependenceDiscoveryTimeWindowAdditionalTimeAfter(m_log);
        int                             twAdditionalTimeBefore = ExperimentConfiguration.getDependenceDiscoveryTimeWindowAdditionalTimeBefore(m_log);
       
        
        delayBetweenDiscoveryAttempts = ExperimentConfiguration.getDependenceDiscoveryDelayBetweenAttempts(m_log);
        
        m_log.log("Build DG - time window: " + 
            (m_toTimestamp - m_fromTimestamp) + 
            "ms...................");

        if ((twAdditionalTimeBefore != 0) || (twAdditionalTimeAfter != 0))
        {
            m_toTimestamp += twAdditionalTimeAfter;
            m_fromTimestamp -= twAdditionalTimeBefore;
            
            m_log.log("Addition time in time window - before: " + 
                twAdditionalTimeBefore + 
                " after: " + 
                twAdditionalTimeAfter + 
                " -- actual time window: " + 
                (m_toTimestamp - m_fromTimestamp) + 
                "ms");
        }
        
        while(m_attemptIndex < numberOfAttempts)
        {
            lastAttemptDG = runDiscoveryAttempt();

            m_log.log("Discovery attempt: " + m_attemptIndex);
            lastAttemptDG.writeOut(m_log);

            if (m_attemptIndex == 1)
            {
                unionDG = lastAttemptDG;
            }
            else
            {
                unionDG = DependenceGraphMerger.createDGUnion(unionDG, lastAttemptDG);
                m_log.log("Union DG");
                unionDG.writeOut(m_log); 
            }

            if (!unionDG.getContainsUnreachableNodes()) {
                break;
            }
            
            if (m_attemptIndex < numberOfAttempts)
            {
                executeDelay(delayBetweenDiscoveryAttempts);
            }
        }

        return unionDG;
    }
    
    private DependenceGraph runDiscoveryAttempt()
    {
        DependenceGraph                 dg;
        
        
        m_attemptIndex++;

        m_log.log("Starting discovery - attempt: " + m_attemptIndex);

        if (ExperimentConfiguration.getDependenceDiscoveryUseDependenceDiscoveryProtocol(m_log))
        {
            m_log.log("Using - Dependence discovery protocol");
            dg = runDependenceDiscoveryProtocol();
        }
        else
        {
            m_log.log("Using - Centralized discovery");
            dg = runCentralizedDiscovery();
        }
        
        loadRootAndLevel1Symptoms(dg);
        dg.getRoot().setReachabilityStatus(DependenceGraph.ReachabilityStatus.Directly);
                
        return dg;
    }
    
    private void loadRootAndLevel1Symptoms(DependenceGraph dg)
    {
        dg.getRoot().setFlStatusEX(m_clientFlStatusEX);
        dg.getRoot().setFlStatusTO(m_clientFlStatusTO);
        
        // check the order is right (now there should be only one outoging dependence anyway)
        for (int i = 0; i < dg.getRoot().getOutEdges().size(); i++)
        {
            dg.getRoot().getOutEdges().get(i).setFlStatusEX(m_rootDependenciesFlStatusEX[i]);
            dg.getRoot().getOutEdges().get(i).setFlStatusTO(m_rootDependenciesFlStatusTO[i]);
        }
    }

    private DependenceGraph runDependenceDiscoveryProtocol()
    {
        DependenceDataProviderDistributed       dependenceDataProvider = new DependenceDataProviderDistributed(m_log);
        DiscoveryElement                        discoveryElement = new DiscoveryElement(m_log, dependenceDataProvider);
        DependenceGraph                         dg;

 
        dependenceDataProvider.runDistributedQuery(
                m_conversationId,
                m_rootDependencies, 
                m_fromTimestamp, 
                m_toTimestamp);
        
        dg = discoveryElement.discoverInterDependenciesForService(
                m_conversationId,
                m_clientId, 
                m_rootDependencies, 
                m_fromTimestamp, 
                m_toTimestamp);
    
        return dg;
    }
        
    private DependenceGraph runCentralizedDiscovery()
    {
        DependenceDataProviderCentralized       dependenceDataProvider = new DependenceDataProviderCentralized(m_log);
        DiscoveryElement                        discoveryElement = new DiscoveryElement(m_log, dependenceDataProvider);
        DependenceGraph                         dg;

        
        dg = discoveryElement.discoverInterDependenciesForService(
                m_conversationId,
                m_clientId, 
                m_rootDependencies, 
                m_fromTimestamp, 
                m_toTimestamp);
    
        return dg;
    } 
    
    private synchronized void executeDelay(int delay)
    {        
        m_log.log("Delay between discovery attempts: " + delay + "ms");
        
        if (delay == 0) {
            return;
        }
        
        try 
        {
            this.wait(delay);
        } 
        catch (java.lang.Exception ex) 
        {
            m_log.log(ex);
        }
    }
}