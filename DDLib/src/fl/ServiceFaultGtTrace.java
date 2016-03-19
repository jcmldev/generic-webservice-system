
package fl;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import dd.Configuration;
import dd.Timestamp;
import dd.output.LoggingContext;
import fl.serverlog.ServerLogWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

public class ServiceFaultGtTrace 
{         

    public enum FaultType
    {
                // in the simple (initial algorithm) the interpretation of faults is following:
        Service, // local service - only when preconfigured fault is signaled 
        Timeout, // same on service and dependence - once there is fault on dependence it is considered on service as well
        Transitive, // given dependence is not source - ist just passing on the fault
        Network // given dependence is source
    };
    
    private static PrintWriter      m_serviceGtOutput;
    
    
    public static FaultType faultTypeFromString(String name)
    {
        return FaultType.valueOf(name);
    }
        

    
    // need to put here timeout as well!!!
    
    public static FaultType ClientProcess_getFaultTypeFromException(Class classOfWSExceptionWrapper, Exception ex)
    {
        if (isExceptionTimeout(ex)) return FaultType.Timeout;
        if (isExceptionTransitive(ex)) return FaultType.Transitive;
        //if (classOfWSExceptionWrapper.isInstance(ex)) return FaultType.Transitive;
        //if (ex instanceof SOAPFaultException) return FaultType.Transitive;
        return FaultType.Network;
    }

    public static FaultType ServerProcess_getFaultTypeFromException(Class classOfWSExceptionWrapper, Exception ex)
    {
        if (isExceptionTimeout(ex)) return FaultType.Timeout;
        if (isExceptionTransitive(ex)) return FaultType.Transitive;
        //if (classOfWSExceptionWrapper.isInstance(ex)) return FaultType.Transitive;
        //if (ex instanceof ServerSOAPFaultException) return FaultType.Transitive;
        
        return FaultType.Network;
    }
    
    private static boolean isExceptionTransitive(Exception ex)
    {
        //com.sun.xml.ws.fault.ServerSOAPFaultException: 
        
        // if exception message contains string Timeout is it considered to be caused by timeout 
        return (ex.getMessage().indexOf("SOAPFault") > 0);
    }
    
    private static boolean isExceptionTimeout(Exception ex)
    {
        // if exception message contains string Timeout is it considered to be caused by timeout 
        if(ex.getMessage().indexOf("imeout") > 0) return true;
        
        if(ex.getMessage().indexOf("Unexpected end of file from server") > 0) return true;
        
        return false;
    }

    public static void recordServiceNetworkException(
            LoggingContext log, 
            int conversationId,
            String source,
            String target,
            Class classOfWSExceptionWrapper,
            Exception ex)
    {
        recordEntry(
                log, 
                Timestamp.now(),
                conversationId, 
                ServerProcess_getFaultTypeFromException(classOfWSExceptionWrapper, ex), 
                source, 
                target, 
                ex.getClass().getName(),
                ex.getMessage(),
                ex.getCause() == null ? "" : ex.getCause().getClass().getName(),
                ex.getCause() == null ? "" : ex.getCause().getMessage());
    }

    public static void recordClientNetworkException(
            LoggingContext log,
            long timestamp,
            int conversationId,
            String source,
            String target,
            FaultType faultType,
            String exceptionType,
            String exceptionMessage,
            String exceptionCauseType,
            String exceptionCauseMessage)
    {
        recordEntry(
                log, 
                timestamp,
                conversationId, 
                faultType, 
                source, 
                target, 
                exceptionType,
                exceptionMessage,
                exceptionCauseType,
                exceptionCauseMessage);
    }

    public static void recordServiceException(
            LoggingContext log, 
            int conversationId,
            String service,
            Exception ex)
    {
        recordEntry(
                log, 
                Timestamp.now(),
                conversationId, 
                FaultType.Service, 
                service, 
                "", 
                ex.getClass().getName(),
                ex.getMessage(),
                ex.getCause() == null ? "" : ex.getCause().getClass().getName(),
                ex.getCause() == null ? "" : ex.getCause().getMessage());
    }
    
    private static void recordEntry(
                    LoggingContext log,
                    long timestamp,
                    int conversationId,
                    FaultType faultType,
                    String source,
                    String target,
                    String exceptionType,
                    String exceptionMessage,
                    String exceptionCauseType,
                    String exceptionCauseMessage)
    {
        
        ServerLogWriter.recordFault(faultType, source, target);
        
        writeLineIntoOutput(log, 
                "" + timestamp + "," +
                conversationId + "," +
                faultType.name() + "," +
                source  + "," +
                target + "," +
                exceptionType + "," +
                exceptionMessage + "," +
                exceptionCauseType + "," +
                exceptionCauseMessage);
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_serviceGtOutput == null)
        {
            try 
            {
                m_serviceGtOutput = new PrintWriter(Configuration.getServiceFaultGtFile(log));
                m_serviceGtOutput.println("timestamp,conversationId,faultType,source,target,exceptionType,exceptionMessage,exceptionCauseType,exceptionCauseMessage");
            } 
            catch (IOException ex) 
            {
                log.log(ex);
            }
        }
    
        m_serviceGtOutput.println(line);
        m_serviceGtOutput.flush();
    }
}