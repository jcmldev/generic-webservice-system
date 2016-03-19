
package dd;

import com.sun.xml.ws.client.BindingProviderProperties;
import dd.output.LoggingContext;
import java.net.*;
import java.util.Enumeration;
import javax.xml.ws.BindingProvider;


public class Network 
{
 
    public static void setServiceUrl(
        BindingProvider bindingProvider, 
        String serviceUrl,
        int timeout)
    { 
        int connectTimeout = timeout;
        int responseTimeout = timeout;
        
        
        bindingProvider.getRequestContext().put( 
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
            serviceUrl);

        bindingProvider.getRequestContext().put(
                "com.sun.xml.ws.request.timeout", 
                responseTimeout);
        
        bindingProvider.getRequestContext().put(
                "com.sun.xml.ws.connect.timeout", 
                connectTimeout);
        
        bindingProvider.getRequestContext().put(
                "com.sun.xml.internal.ws.request.timeout", 
                responseTimeout);
        
        bindingProvider.getRequestContext().put(
                "com.sun.xml.internal.ws.connect.timeout", 
                connectTimeout);        
        
        System.setProperty("sun.net.client.defaultReadTimeout", Integer.toString(responseTimeout));
        System.setProperty("sun.net.client.defaultConnectTimeout", Integer.toString(connectTimeout));
        
        /*
MyInterface myInterface = new MyInterfaceService().getMyInterfaceSOAP();
Map<String, Object> requestContext = ((BindingProvider)myInterface).getRequestContext();
requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 3000); // Timeout in millis
requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 1000); // Timeout in millis
myInterface.callMyRemoteMethodWith(myParameter);
*/        
        
    }
        
    public static String getGenericWsUrl(LoggingContext log, String hostIp, String serviceName)
    {
        String url = ExperimentConfiguration.getServiceSystemGenericServiceAddress(log);
        
        
        url = serviceUrlSetIpInUrl(url, hostIp);
        url = serviceUrlSetServiceNameInUrl(url, serviceName);
        
        return url;
    }
        
    public static String serviceUrlSetIpInUrl(String url, String ip)
    {
        return url.replace("{ip}", ip);
    }
    
    public static String serviceUrlSetServiceNameInUrl(String url, String serviceName)
    {
        return url.replace("{service_name}", serviceName);
    }

        
    // Returns first ip found in list of network interfaces
    public static String getLocalHostIp(LoggingContext log)
    {
        Enumeration<NetworkInterface>       nIs = null;
        NetworkInterface                    nI;
        Enumeration<InetAddress>            iAs;
        InetAddress                         iA;
        String                              iP;
        
        
        try
        {
            nIs = NetworkInterface.getNetworkInterfaces();
        }
        catch (SocketException ex)
        {
            log.log(ex);
        }
         
        while(nIs.hasMoreElements())
        {
            nI = (NetworkInterface)nIs.nextElement(); 
            iAs = nI.getInetAddresses();

            while(iAs.hasMoreElements())
            { 
                iA = (InetAddress)iAs.nextElement(); 

                if(!iA.isLoopbackAddress() && (!iA.isLinkLocalAddress()) && (iA instanceof Inet4Address)) 
                {
                    iP = iA.getHostAddress();
                    return iP;
                }
            }
        }
        
        return null;
    }    
    
    public static String getHostIpFromUrl(LoggingContext log, String serviceUrl) 
    {
        try {
            
            URL             url = new URL(serviceUrl);

            return url.getHost();
        } 
        catch (MalformedURLException ex) 
        {
            log.log(ex);
        }
        
        return null;
    }
    
    public static String getServiceNameFromUrl(LoggingContext log, String serviceUrl)
    {
        String          serviceName = serviceUrl;
        
        
        try 
        {
            URL         url = new URL(serviceUrl);
            
            serviceName = url.getPath();
            
        } 
        catch (MalformedURLException ex) 
        {
            log.log(ex);
        }
    
        return serviceName;
    }
 
    public static String getClientApplicationProcessId(LoggingContext log)
    {
    	return getProcessId(log) +
                ":" +
                Network.getLocalHostIp(log);
    }
        
    public static long getProcessId(LoggingContext log)
    {
        String          runtimeId;
        long            processId = 0;
        
        
        try
        {
            runtimeId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
                    
            processId = Long.parseLong(
                            runtimeId.substring(
                                0, 
                                runtimeId.indexOf('@')));
        }
        catch(Exception e)
        {
            log.log("getProcessId");
            log.log(e);
        }
        
        return processId;
    }
}