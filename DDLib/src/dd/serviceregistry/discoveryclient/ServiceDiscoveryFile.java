
package dd.serviceregistry.discoveryclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class ServiceDiscoveryFile 
{
    
    private final static String         SERVICE_DISCOVERY_FILE_NAME = "service_registry.xml";
    
    public static String[] getNodesHostingService(int serviceId)
    {
        Element         rootElement = getRootElement(SERVICE_DISCOVERY_FILE_NAME);
        NodeList        services = rootElement.getChildNodes();
        Element         service;
        NodeList        nodes;
        Element         node;
        String[]           result = null;
        
        
        // should use xpath but skip it for now
        
        for (int i = 0; i < services.getLength(); i++)
        {
            service = (Element)services.item(i);
            nodes = service.getChildNodes();
            
            if (Integer.parseInt(service.getAttribute("id")) == serviceId)
            {
                result = new String[nodes.getLength()];
                
                for (int ii = 0; ii < nodes.getLength(); ii++)
                {
                    node = (Element)nodes.item(ii);
                    result[ii] = node.getAttribute("id");
                }
            }
        }
        
        return result;
    }
    
    
    private static Element getRootElement(String fileName)
    {
        File                        config = getItemInGfDir(fileName);
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
            //log.log("Xml file processing exception: " + ex.getMessage());
        }       

        return element;
    }
        
    public static File getItemInGfDir(String subFolderOrFile)
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
            //log.log("Cant get/find file system item: " + ex.getMessage());            
        }
        
        return item;
    }
        
        
        
        
    
    // placements - service id / node id
    public void storeServicePlacementConfiguration(int[][] placements, String[] ips)
    {
        int             nodeId;
        
        
        try 
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("dsp");
            doc.appendChild(rootElement);

            
            
            for (int serviceId = 0; serviceId < placements.length; serviceId++)
            {

                Element se = doc.createElement("service");
                rootElement.appendChild(se);
                se.setAttribute("id", Integer.toString(serviceId));

                for (int nodeIndex = 0; nodeIndex < placements[serviceId].length; nodeIndex++)
                {
                    Element ne = doc.createElement("node");
                    se.appendChild(ne);
                    nodeId = placements[serviceId][nodeIndex];
                    ne.setAttribute("id", ips[nodeId]);
                }
            }
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            File outputFile = getItemInGfDir(SERVICE_DISCOVERY_FILE_NAME);
            StreamResult result = new StreamResult(outputFile);

            transformer.transform(source, result);

            
        } 
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } 
        catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
