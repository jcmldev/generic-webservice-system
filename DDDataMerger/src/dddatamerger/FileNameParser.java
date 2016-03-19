
package dddatamerger;

public class FileNameParser 
{
    private String              m_fileName;
    private String              m_prefix;
    private String              m_attributeSeparator;
    private String              m_postfix;
    private boolean             m_hasSeparator;
    
    
    public FileNameParser(
            String fileName,
            String prefix,
            String attributeSeparator,
            String postfix,
            boolean hasSeparator)
    {
        m_fileName = fileName;
        m_prefix = prefix;
        m_attributeSeparator = attributeSeparator;
        m_postfix = postfix;
        m_hasSeparator = hasSeparator;
    }
    
    public boolean fileNameMatchesPattern()
    {
        boolean         match;
        
        if (m_hasSeparator)
        {
            match = (hasPrefix() && hasAttributeSepparator() && hasPostfix());
        }
        else
        {
            match = (hasPrefix() && hasPostfix());
        }
        
        return match;
    }
    
    public boolean hasPrefix()
    {
        return m_fileName.startsWith(m_prefix);
    }
    
    public boolean hasPostfix()
    {
        return m_fileName.endsWith(m_postfix);
    }
    
    public boolean hasAttributeSepparator()
    {
        return m_fileName.contains(m_attributeSeparator);
    }
    
    public String getSingleAttribute()
    {
        return m_fileName.substring(
                m_prefix.length(), 
                m_fileName.length() - m_postfix.length());
    }
    
    public String getFirstAttribute()
    {
        return m_fileName.substring(
                m_prefix.length(), 
                m_fileName.indexOf(m_attributeSeparator));
    }

    public String getSecondAttribute()
    {
        return m_fileName.substring(
                m_fileName.indexOf(m_attributeSeparator) + 1, 
                m_fileName.length() - m_postfix.length());
    }    
}