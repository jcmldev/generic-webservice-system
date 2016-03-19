package dd.tube;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubeFactory;
import javax.xml.ws.WebServiceException;


public class MonitorTubeFactory implements TubeFactory {

    @Override
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException {
        
        //System.out.println("create client tube");
        
        //return new MonitorTube(context.getTubelineHead(), MonitorTube.RequestMessageDirection.Outgoing);

            return new LoggingTube(context.getTubelineHead(), context);
    }

    @Override
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException {
        
        //System.out.println("create server tube");
        
        //return new MonitorTube(context.getTubelineHead(), MonitorTube.RequestMessageDirection.Incoming);
        
        return new LoggingTube(context.getTubelineHead(), context);
    }    
}
