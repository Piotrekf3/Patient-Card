package view;

import model.PatientList;

import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PatientView implements Serializable {
    private String patient;

    @Inject
    private PatientList patientList;

    @PostConstruct
    private void init() {
        patient = patientList.getPatients();
    }

    public String getPatient() {
        return patient;
    }
}
