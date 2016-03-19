
package dd.measure;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubeFactory;
import javax.xml.ws.WebServiceException;


public class FirstTubeFactory implements TubeFactory {

    @Override
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException {

        return new FirstTube(context.getTubelineHead());
    }

    @Override
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException {
        return null;
    }     
}
