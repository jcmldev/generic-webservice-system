package dddatamerger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileBatch 
{
    private String              m_prefix;
    private String              m_attributeSeparator;
    private String              m_postfix;
    private boolean             m_hasSeparator;
    private String              m_firstAttributeName;
    private String              m_secondAttributeName;
    private String              m_outputFileName;
    
    private PrintWriter         m_fileOutput;
    
    
    public FileBatch(
            String prefix,
            String attributeSeparator,
            String postfix,
            boolean hasSeparator,
            String firstAttributeName,
            String secondAttributeName,
            String outputFileName)
    {
        m_prefix = prefix;
        m_attributeSeparator = attributeSeparator;
        m_postfix = postfix;
        m_hasSeparator = hasSeparator;
        m_firstAttributeName = firstAttributeName;
        m_secondAttributeName = secondAttributeName;
        m_outputFileName = outputFileName;
    }
    
    public void processFiles(String directoryPath)
    {
        File                directory = new File(directoryPath);
        String              fileName;
        String              ip;
        String              secondAttributeValue = null;
        FileNameParser      fileNameParser;
        
        
        for(File file : directory.listFiles())
        {
            fileName = file.getName();
            
            fileNameParser = new FileNameParser(
                    fileName, 
                    m_prefix, 
                    m_attributeSeparator, 
                    m_postfix, 
                    m_hasSeparator);
            
            if (fileNameParser.fileNameMatchesPattern())
            {
                if (m_hasSeparator)
                {
                    ip = fileNameParser.getFirstAttribute();                
                    secondAttributeValue = fileNameParser.getSecondAttribute();
                }
                else
                {
                    ip = fileNameParser.getSingleAttribute();
                }
                
                ip = ip.replaceAll("_", ".");
         
                System.out.println("Processing file: " + file.getPath());
                
                processFile(file, m_firstAttributeName, ip, m_secondAttributeName, secondAttributeValue);
            }
        }
    }
    
    private void processFile(
            File file, 
            String firstAttributeName, 
            String firstAttributeValue,
            String secondAttributeName, 
            String secondAttributeValue)
    {
        Scanner             scanner;
        String              linePrefix;
        String              line;
        String              headerPrefix;
        String              header;
        
        
        headerPrefix = firstAttributeName + ",";
        linePrefix = firstAttributeValue + ",";
        
        if (secondAttributeValue != null)
        {
            headerPrefix += secondAttributeName + ",";
            linePrefix += secondAttributeValue + ",";
        }
        
        try
        {
            scanner = new Scanner(file);
            
            header = headerPrefix + scanner.nextLine();
            
            while(true)
            {
                line = linePrefix + scanner.nextLine();
                writeLineIntoOutput(file, header, line);
            }
        }
        catch(NoSuchElementException ex)
        {}
        catch(Exception ex)
        {
            Logger.getLogger(DDDataMerger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeLineIntoOutput(File file, String header, String line)
    {
        if (m_fileOutput == null)
        {
            try 
            {
                m_fileOutput = new PrintWriter(file.getParent() + m_outputFileName);
                System.out.println("Output file: " + file.getParent() + m_outputFileName);
                m_fileOutput.println(header);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(DDDataMerger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        //line = removeErrorContent(line);
        
        m_fileOutput.println(line);
        m_fileOutput.flush();
    }
    /*
    private String removeErrorContent(String line)
    {
        // deal with the following content: [row,col {unknown-source}]: [1,0]
        //line = "test[row,col {unknown-source}]: [1,0], test";
        
        int errIndex = line.indexOf("[row,");

        
        if (errIndex > -1)
        {
            line = line.substring(0, errIndex + 4) + line.substring(errIndex + 5, line.length());
            errIndex = line.indexOf(",", errIndex);
            line = line.substring(0, errIndex) + line.substring(errIndex + 1, line.length());            
        }
        
        return line;
    }
    */
}