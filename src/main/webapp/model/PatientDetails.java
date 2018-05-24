package model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class PatientDetails implements Serializable {
    private FhirContext ctx;
    private String patientUrl;

    @Inject
    private Global global;
    @PostConstruct
    private void init() {
        ctx = global.getCtx();
    }

    public List<Resource> getPatientDetails() {
        IGenericClient client = ctx.newRestfulGenericClient(Global.serverBase);
        Bundle results = client.search()
                .byUrl(patientUrl + "/$everything")
                .count(100)
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        List<Resource> resources = new ArrayList<>();
        List<Bundle.BundleEntryComponent> entries = results.getEntry();
        for(int i=0; i<results.getEntry().size(); i++) {
            resources.add(entries.get(i).getResource());
        }
        return resources;
    }

    public String getPatientUrl() {
        return patientUrl;
    }

    public void setPatientUrl(String patientUrl) {
        this.patientUrl = patientUrl;
    }
}
