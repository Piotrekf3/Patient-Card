package model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import org.hl7.fhir.dstu3.model.*;

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

    public List<Observation> getPatientObservations() {
        IGenericClient client = ctx.newRestfulGenericClient(Global.serverBase);

        System.out.println(patientUrl);

        IdDt id = new IdDt(patientUrl);
        Bundle observations = client.search().forResource(Observation.class)
                .where(new StringClientParam("patient._id").matches().value(id.getIdPart()))
                .count(500)
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();

        System.out.println("size=" + observations.getEntry().size());
        List<Observation> resources = new ArrayList<>();
        List<Bundle.BundleEntryComponent> entries = observations.getEntry();
        for(int i=0; i<entries.size(); i++) {
            resources.add((Observation)entries.get(i).getResource());
        }
        return resources;
    }

    public List<MedicationStatement> getPatientMedications() {
        IGenericClient client = ctx.newRestfulGenericClient(Global.serverBase);

        System.out.println(patientUrl);

        IdDt id = new IdDt(patientUrl);
        Bundle medications = client.search().forResource(MedicationStatement.class)
                .where(new StringClientParam("patient._id").matches().value(id.getIdPart()))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();

        System.out.println("size=" + medications.getEntry().size());
        List<MedicationStatement> resources = new ArrayList<>();
        List<Bundle.BundleEntryComponent> entries = medications.getEntry();
        for(int i=0; i<entries.size(); i++) {
            resources.add((MedicationStatement) entries.get(i).getResource());
        }
        return resources;
    }

    public String getPatientName() {
        IGenericClient client = ctx.newRestfulGenericClient(Global.serverBase);


        Bundle patients = client.search().byUrl(patientUrl + "/$everything")
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        Patient patient = (Patient) patients.getEntry().get(0).getResource();
        if(patient.hasName())
            return patient.getName().get(0).getGiven().get(0) + " " + patient.getName().get(0).getFamily();
        else
            return "";
    }

    public String getPatientUrl() {
        return patientUrl;
    }

    public void setPatientUrl(String patientUrl) {
        this.patientUrl = patientUrl;
    }
}
