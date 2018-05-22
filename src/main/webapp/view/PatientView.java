package view;

import model.PatientList;
import org.hl7.fhir.dstu3.model.Patient;

import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PatientView implements Serializable {
    private List<Patient> patients;

    @Inject
    private PatientList patientList;

    @PostConstruct
    private void init() {
        patients = patientList.getPatients();
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
