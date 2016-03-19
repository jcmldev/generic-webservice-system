
package dd.measure;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;

public class FirstTube extends AbstractFilterTubeImpl {
    
    private FirstTube(FirstTube original, TubeCloner cloner) {
        super(original, cloner);
    }

    @Override
    public FirstTube copy(TubeCloner cloner) {
        return new FirstTube(this, cloner);
    }

    FirstTube(Tube tube) {
        super(tube);
    }

    @Override
    public NextAction processRequest(Packet request) {

        ProcessingMonitor.Start();
        
        return super.processRequest(request);
    }

    @Override
    public NextAction processResponse(Packet response) {
        return super.processResponse(response);
    }

    @Override
    public NextAction processException(Throwable throwable) {
        return super.processException(throwable);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
    }
}
