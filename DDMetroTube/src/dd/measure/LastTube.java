
package dd.measure;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;

public class LastTube extends AbstractFilterTubeImpl {
    
    private LastTube(LastTube original, TubeCloner cloner) {
        super(original, cloner);
    }

    @Override
    public LastTube copy(TubeCloner cloner) {
        return new LastTube(this, cloner);
    }

    LastTube(Tube tube) {
        super(tube);
    }

    @Override
    public NextAction processRequest(Packet request) {

        ProcessingMonitor.End();
        
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
