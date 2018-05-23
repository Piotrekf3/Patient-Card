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

    public List<Patient> getPatients(String familyName) {
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        // Perform a search
        Bundle results = client.search().forResource(Patient.class)
                .count(500)
                .where(Patient.FAMILY.matches().values(familyName))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        List<Patient> patients = new ArrayList<>();

        for(int i=0; i<results.getEntry().size(); i++) {
            Patient patient = (Patient) results.getEntry().get(i).getResource();
            if(validate(patient))
                patients.add(patient);
        }

        return patients;
    }

    private boolean validate(Patient patient) {
        return patient.hasName() && patient.hasGender() && patient.hasBirthDate();
    }

}
