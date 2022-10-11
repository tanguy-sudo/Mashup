package internalCRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ws_crm.*;

@Endpoint
public class InternalClientEndpoint {
    private static final String NAMESPACE_URI = "http://ws-crm";
    private InternalClientRepository internalClientRepository;

    @Autowired
    public InternalClientEndpoint(InternalClientRepository internalClientRepository) {
        this.internalClientRepository = internalClientRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findLeadsRequest")
    @ResponsePayload
    public FindLeadsResponse findLeads(@RequestPayload FindLeadsRequest request) {
        FindLeadsResponse response = new FindLeadsResponse();
        response.getInternalClient().addAll(this.internalClientRepository.findLeads(request.getLowAnnualRevenue(),
                request.getHighAnnualRevenue(), request.getState()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findLeadsByDateRequest")
    @ResponsePayload
    public FindLeadsByDateResponse findLeadsByDate(@RequestPayload FindLeadsByDateRequest request) {
        FindLeadsByDateResponse response = new FindLeadsByDateResponse();
        response.getInternalClient().addAll(this.internalClientRepository.findLeadsByDate(request.getStartDate(), request.getEndDate()));
        return response;
    }
}
