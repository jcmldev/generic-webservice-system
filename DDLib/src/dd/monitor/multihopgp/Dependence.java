
package dd.monitor.multihopgp;

import dd.output.LoggingContext;


public class Dependence 
{
    BackupDataSetRecord.RecordType      m_type;
    private String                      m_dependentServiceId;
    private String                      m_antecedentServiceId;
    private BackupContainer             m_occurranceContainer;
    
    
    public Dependence(
            LoggingContext log,
            BackupDataSetRecord.RecordType type,
            String dependentServiceId,
            String antecedentServiceId)
    {
        m_type = type;
        m_antecedentServiceId = antecedentServiceId;
        m_dependentServiceId = dependentServiceId;
        m_occurranceContainer = new BackupContainer();
    }
    
    public String getId()
    {
        return m_type.name() + m_dependentServiceId + m_antecedentServiceId;
    }
    
    public BackupDataSetRecord.RecordType getType()
    {
        return m_type;
    }
    
    public String getDependentServiceId()
    {
        return m_dependentServiceId;
    }
    
    public String getAntecedentServiceId()
    {
        return m_antecedentServiceId;
    }
    
    public BackupContainer getBackupContainer() 
    { 
        return m_occurranceContainer; 
    }    
}
