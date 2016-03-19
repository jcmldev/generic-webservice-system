
package dddatamerger;

import dd.Configuration;
import java.io.File;
import java.io.PrintWriter;

public class DDDataMerger {

    /*
     * - Searches folder in which it resides
     * - for each category of files given by prefix
     * - combines content of the files into single file such that:
     * -- it uses only single header line
     * -- add new first column with ip address of node extracted from the name of the file
     */
    
    public static void main(String[] args) 
    {
        String              directoryPath = getDirectoryPath();

        
        directoryPath = "c:\\gf\\output\\";
        
        System.out.println("DD File harverster ...");
        
        
        System.out.println("...");
        System.out.println("Processing: Ground truth");
        processGt(directoryPath);

        System.out.println("...");
        System.out.println("Processing: Service faults - ground truth");
        processFSGt(directoryPath);
        
        System.out.println("...");
        System.out.println("Processing: Dependence graphs");
        processDg(directoryPath);
        
        System.out.println("...");
        System.out.println("Processing: Client conversations");
        processConversations(directoryPath);
        
        System.out.println("...");
        System.out.println("Processing: Gossip protocol traces");
        processGp(directoryPath);
        
        System.out.println("Done");
    }

    private static void processFSGt(String directoryPath)
    {
        FileBatch           fileBatch = new FileBatch(
                Configuration.SERVICE_FAULT_GT_FILE_PREFIX, 
                Configuration.ATTRIBUTE_SEPARATOR,
                Configuration.FILE_POSTFIX,
                false,
                "ip",
                "serviceid",
                "\\scenario_f_sgt.csv");
                
        fileBatch.processFiles(directoryPath);
    }

    private static void processGt(String directoryPath)
    {
        FileBatch           fileBatch = new FileBatch(
                Configuration.SERVICE_GT_FILE_PREFIX, 
                Configuration.ATTRIBUTE_SEPARATOR,
                Configuration.FILE_POSTFIX,
                false,
                "ip",
                "serviceid",
                "\\scenario_gt.csv");
                
        fileBatch.processFiles(directoryPath);
    }

    private static void processGp(String directoryPath)
    {
        FileBatch           fileBatch = new FileBatch(
                Configuration.GP_FILE_PREFIX, 
                null,
                Configuration.FILE_POSTFIX,
                false,
                "ip",
                null,
                "\\scenario_gp.csv");
                
        fileBatch.processFiles(directoryPath);
    }
    private static void processDg(String directoryPath)
    {
        FileBatch           fileBatch = new FileBatch(
                Configuration.DG_FILE_PREFIX, 
                null,
                Configuration.FILE_POSTFIX,
                false,
                "ip",
                null,
                "\\scenario_dgs.csv");
                
        fileBatch.processFiles(directoryPath);
    }

    private static void processConversations(String directoryPath)
    {
        FileBatch           fileBatch = new FileBatch(
                Configuration.CONVERSATION_FILE_PREFIX, 
                null,
                Configuration.FILE_POSTFIX,
                false,
                "ip",
                null,
                "\\scenario_conversations.csv");
                
        fileBatch.processFiles(directoryPath);
    }

    private static String getDirectoryPath()
    {
        File                jarFile;
        
        
        jarFile = new File(DDDataMerger.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        
        return jarFile.getParent() + "\\" + Configuration.OUTPUT_DIRECTORY;
    }    
}
