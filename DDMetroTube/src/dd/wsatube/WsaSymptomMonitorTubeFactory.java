
package dd.wsatube;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubeFactory;
import dd.wsatube.WsaMonitorTube.ProcessingFlowDirection;
import javax.xml.ws.WebServiceException;


public class WsaSymptomMonitorTubeFactory implements TubeFactory 
{

    @Override
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException 
    {
        return new WsaSymptomMonitorTube(
                context.getTubelineHead(), 
                ProcessingFlowDirection.Outbound, 
                context);
    }

    @Override
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException 
    {
        return new WsaSymptomMonitorTube(
                context.getTubelineHead(), 
                ProcessingFlowDirection.Inbound, 
                context);
    }
}
