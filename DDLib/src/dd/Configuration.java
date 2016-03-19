
package dd;

import dd.output.LoggingContext;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Configuration 
{
    public static final String                 DD_CONFIGURATION_FILE_NAME = "ddconfig.xml";
    public static final String                 CLIENT_OUTPUT_FILE_PREFIX = "gc";
    public static final String                 FILE_POSTFIX = ".csv";
    public static final String                 SERVICE_GT_FILE_PREFIX = "sgt";
    public static final String                 WSA_MESSAGE_GT_FILE_PREFIX = "wsa_msg";
    public static final String                 WSA_SYMPTOM_GT_FILE_PREFIX = "wsa_symptom";
    public static final String                 SERVICE_FAULT_GT_FILE_PREFIX = "f_sgt";
    public static final String                 DG_FILE_PREFIX = "dg";
    public static final String                 GP_FILE_PREFIX = "gp";
    public static final String                 CONVERSATION_FILE_PREFIX = "conv";
    public static final String                 ATTRIBUTE_SEPARATOR = "-";
    public static final String                 OUTPUT_DIRECTORY = "output";
    
    
    public static File getItemInGfDir(LoggingContext log, String subFolderOrFile)
    {
        File                        root;
        File                        gf;
        File                        item = null;
        
        try
        {
            root = File.listRoots()[0];
            gf = new File(root, "gf");
            item = new File(gf, subFolderOrFile);
        }
        catch(Exception ex)
        {
            log.log("Cant get/find file system item: " + ex.getMessage());
        }
        
        return item;
    }
    
    public static File getFileInOutputDir(LoggingContext log, String fileName)
    {
        File                        gf = getItemInGfDir(log, OUTPUT_DIRECTORY);
        File                        file = null;
        
        try
        {
            file = new File(gf, fileName);
        }
        catch(Exception ex)
        {
            if (log != null)
            {
                log.log("Cant find file: " + ex.getMessage());
            }
            else
            {
                System.out.println("Excepiton while opening in output directory: " + ex.getMessage());
            }
        }
        
        return file;
    }

    public static File getCLientOutputFile(LoggingContext log)
    {
        String              fileName;
        
        fileName = CLIENT_OUTPUT_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_")
                + FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getServiceGtFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = SERVICE_GT_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getWsaMessageGtFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = WSA_MESSAGE_GT_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getWsaSymptomGtFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = WSA_SYMPTOM_GT_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getServiceFaultGtFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = SERVICE_FAULT_GT_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getDgFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = DG_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +                
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getGpFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = GP_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +                
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getConversationFile(LoggingContext log)
    {
        String              fileName;
        
        
        fileName = CONVERSATION_FILE_PREFIX + 
                Network.getLocalHostIp(log).replaceAll("\\.", "_") +                
                FILE_POSTFIX;
        
        return getFileInOutputDir(log, fileName);
    }
    
    public static File getServiceLogFile(String serviceName)
    {
        String              fileName;
        LoggingContext      log = null; // log cant be parameter of making log - thus if this is needed it will just fail 
        
        
        fileName = Network.getLocalHostIp(log).replaceAll("\\.", "_") +                
                "-" +
                serviceName + 
                ".log";
    
        return getFileInOutputDir(log, fileName);
    }
    
    private static Element getConfigurationRootElement(LoggingContext log, String fileName)
    {
        File                        config = getItemInGfDir(log, fileName);
        DocumentBuilderFactory      dbFactory;
        DocumentBuilder             dBuilder;
        Document                    doc;
        Element                     element = null;

        
        try
        {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(config);

            doc.getDocumentElement().normalize();
            
            element = doc.getDocumentElement();
        } 
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            log.log("Xml file processing exception: " + ex.getMessage());
        }       

        return element;
    }
    
    private static Element getFirstSubElement(
            Element element,
            String subElementTagName)
    {
        Element         subElement;
        
        
        subElement = (Element) element.getElementsByTagName(subElementTagName).item(0);
    
        return subElement;
    }
        
    private static Element getSubElement(
            LoggingContext log,
            Element element,
            String subElementTagName, 
            String attributeName, 
            String attributeValue)
    {
        NodeList                subElements = element.getElementsByTagName(subElementTagName);
        Element                 subElement;
        
        
        for(int i = 0; i<subElements.getLength(); i++)
        {
            subElement = (Element) subElements.item(i);
            
            if (subElement.getAttribute(attributeName).equals(attributeValue))
            {
                return subElement;
            }
        }
        
        log.log("Configuratin element not found - tag: " + subElementTagName + " - " + attributeName + "=" + attributeValue);
        
        return null;
    }
    
    private static Element getDDRoot(LoggingContext log)
    {
        Element             rootElement;
        
        
        rootElement = getConfigurationRootElement(log, DD_CONFIGURATION_FILE_NAME);
        
        return rootElement;
    }
    
    private static Element getClientsElement(LoggingContext log)
    {
        return getFirstSubElement(getDDRoot(log), "clients");
    }

    public static Element getExperimentElement(LoggingContext log)
    {
        return getFirstSubElement(getDDRoot(log), "experiment");
    }

    public static Element getNodesElement(LoggingContext log)
    {
        return getFirstSubElement(getDDRoot(log), "nodes");
    }
        
    public static Element getServiceRegistryConfiguration(LoggingContext log)
    {
        return getNodesElement(log);
    }
    
    public static Element getNodeElement(LoggingContext log, String nodeIp)
    {
        return getSubElement(
                log, 
                getNodesElement(log), 
                "node", 
                "ip", 
                nodeIp);
    }
    
    public static Element getGenericClientConfiguration(LoggingContext log)
    {
        String              hostIp = Network.getLocalHostIp(log);
        Element             clientsElement;
        Element             clientElement;
        
        
        clientsElement = getClientsElement(log);

        clientElement = getSubElement(
                log, 
                clientsElement, 
                "client", 
                "ip", 
                hostIp);
        
        if (clientElement == null)
        {
            log.log("Specific configuration of client Ip not found - using default configuration");
            clientElement = getSubElement(
                    log, 
                    clientsElement, 
                    "client", 
                    "default", 
                    "true");
            
            // set new conversation id seed, based on Ip
            if (clientElement != null)
            {
                int conversationIdSeed = Integer.parseInt(clientElement.getAttribute("conversationIdSeed"));
                int lastPartOfIp = Integer.parseInt(hostIp.substring(hostIp.lastIndexOf(".") + 1));

                
                conversationIdSeed = conversationIdSeed * lastPartOfIp;

                clientElement.setAttribute("conversationIdSeed", "" + conversationIdSeed);
            }
        }        

        return clientElement;
    }

    public static Element getServiceConfigurationOnLocalNode(LoggingContext log, String serviceName)
    {
        String              hostIp = Network.getLocalHostIp(log);
        Element             nodesElement;
        Element             nodeElement;
        Element             serviceElement;
        
        
        nodesElement = getNodesElement(log);

        nodeElement = getSubElement(
                log, 
                nodesElement, 
                "node", 
                "ip", 
                hostIp);
        
        serviceElement = getSubElement(
                log, 
                nodeElement, 
                "service", 
                "name", 
                serviceName);

        return serviceElement;
    }
}
