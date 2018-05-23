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
    private String familyName;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Inject
    private PatientList patientList;

    @PostConstruct
    public void init() {
        patients = patientList.getPatients(familyName);
        System.out.println("tutaj init");
        System.out.println(familyName);
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
