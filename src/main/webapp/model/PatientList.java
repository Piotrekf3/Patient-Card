package model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import misc.XMLParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class PatientList {

    private FhirContext ctx;
    private static String serverBase = "http://hapi.fhir.org/baseDstu3";

    public PatientList() {
        ctx = FhirContext.forDstu3();
    }

    public FhirContext getContext() {
        return ctx;
    }

    public List<Patient> getPatients() {
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        // Perform a search
        Bundle results = client.search().forResource(Patient.class)
                .where(new StringClientParam("family").matches().value("duck"))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        System.out.println("Found " + results.getEntry().size() + " patients named 'duck'");
        List<Patient> patients = new ArrayList<>();
        patients.add((Patient) results.getEntry().get(0).getResource());
        return patients;
    }

}
