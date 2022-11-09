package fr.univ.angers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import fr.univ.angers.service.InternalCRMServiceImpl;
import ws_crm.*;

@Endpoint
public class InternalClientEndpoint {
    private static final String NAMESPACE_URI = "http://ws-crm";
    private InternalCRMServiceImpl internalCRMServiceImpl;

    @Autowired
    public InternalClientEndpoint(InternalCRMServiceImpl internalCRMServiceImpl) {
        this.internalCRMServiceImpl = internalCRMServiceImpl;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findLeadsRequest")
    @ResponsePayload
    public FindLeadsResponse findLeads(@RequestPayload FindLeadsRequest request) {
        FindLeadsResponse response = new FindLeadsResponse();
        response.getInternalClient().addAll(this.internalCRMServiceImpl.findLeads(request.getLowAnnualRevenue(),
                request.getHighAnnualRevenue(), request.getState()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findLeadsByDateRequest")
    @ResponsePayload
    public FindLeadsResponse findLeadsByDate(@RequestPayload FindLeadsByDateRequest request) {
        FindLeadsResponse response = new FindLeadsResponse();
        response.getInternalClient().addAll(this.internalCRMServiceImpl.findLeadsByDate(request.getStartDate(), request.getEndDate()));
        return response;
    }
}
