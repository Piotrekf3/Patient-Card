package model;

import org.hl7.fhir.instance.model.Patient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class PatientList {
    public String getPatients() {
        return "dupa";
    }

}
