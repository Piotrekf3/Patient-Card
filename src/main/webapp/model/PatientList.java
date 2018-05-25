package model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.google.common.collect.Lists;
import misc.XMLParser;
import org.hl7.fhir.dstu3.model.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class PatientList implements Serializable {
    private FhirContext ctx;
    @Inject
    private Global global;
    @PostConstruct
    private void init() {
        ctx = global.getCtx();
    }

    public FhirContext getContext() {
        return ctx;
    }

    public List<Patient> getPatients(String familyName) {
        IGenericClient client = ctx.newRestfulGenericClient(Global.serverBase);
        // Perform a search
        Bundle results = client.search().forResource(Patient.class)
                .count(500)
                .where(Patient.FAMILY.matches().values(familyName))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        List<Patient> patients = new ArrayList<>();

        for(int i=0; i<results.getEntry().size(); i++) {
            Patient patient = (Patient) results.getEntry().get(i).getResource();
            repair(patient);
            patients.add(patient);
        }

        return patients;
    }

    private void repair(Patient patient) {
        if(!patient.hasName()) {
            HumanName name = new HumanName();
            name.setFamily("Unknown");
            name.setGiven(Lists.newArrayList(new StringType("Unknown")));
            patient.addName(name);
        }
        if(!patient.hasGender()) {
            patient.setGender(Enumerations.AdministrativeGender.OTHER);
        }
        if(!patient.hasBirthDate()) {
            patient.setBirthDate(new Date());
        }
    }

}
