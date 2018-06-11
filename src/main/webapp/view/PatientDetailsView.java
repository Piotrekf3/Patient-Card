package view;

import model.PatientDetails;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class PatientDetailsView implements Serializable {

    private TimelineModel model;
    private List<Observation> observations;
    private List<MedicationStatement> medications;

    private boolean selectable = true;
    private boolean zoomable = true;
    private boolean moveable = true;
    private boolean stackEvents = true;
    private String eventStyle = "box";
    private boolean axisOnTop;
    private boolean showCurrentTime = true;
    private boolean showNavigation = false;

    @Inject
    private PatientDetails patientDetails;

    @PostConstruct
    private void init() {
        model = new TimelineModel();
        Calendar cal = Calendar.getInstance();
        observations = patientDetails.getPatientObservations();
        for (Observation observation : observations) {
            try {
                if (observation.hasEffectiveDateTimeType()) {
                    System.out.println("Observation");
                    DateTimeType date = observation.getEffectiveDateTimeType();
                    cal.set(date.getYear(), date.getMonth(), date.getDay(), 0, 0, 0);
                    model.add(new TimelineEvent(new Wrapper(observation.getCode().getText(), observation), cal.getTime()));
                }
            } catch (FHIRException e) {
                e.printStackTrace();
            }
        }
        medications = patientDetails.getPatientMedications();
        for (MedicationStatement medication : medications) {
            try {
                System.out.println("Medication");
                DateTimeType date = DateTimeType.today();
                if (medication.hasDateAsserted())
                    date = medication.getEffectiveDateTimeType();
                cal.set(date.getYear(), date.getMonth(), date.getDay(), date.getTzHour(), date.getTzMin(), 0);
                model.add(new TimelineEvent(new Wrapper("Medication", medication), cal.getTime()));
            } catch (FHIRException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSelect(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();
        String date = timelineEvent.getStartDate().toString();
        Wrapper data = (Wrapper)timelineEvent.getData();

        String text = "";
        if(data.getResource().getResourceType().equals(ResourceType.Observation)) {
            Observation observation = (Observation) data.getResource();
            XhtmlNode details = observation.getText().getDiv();
            text = details.getValue().replaceAll("\\<[^>]*>","");
        }
        else if(data.getResource().getResourceType().equals(ResourceType.MedicationStatement)) {
            try {
                MedicationStatement statement = (MedicationStatement) data.getResource();
                text = statement.getMedicationCodeableConcept().getText();
            }
            catch (FHIRException ex) {
                ex.printStackTrace();
            }
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, date, text);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public TimelineModel getModel() {
        return model;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isZoomable() {
        return zoomable;
    }

    public void setZoomable(boolean zoomable) {
        this.zoomable = zoomable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isStackEvents() {
        return stackEvents;
    }

    public void setStackEvents(boolean stackEvents) {
        this.stackEvents = stackEvents;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public boolean isAxisOnTop() {
        return axisOnTop;
    }

    public void setAxisOnTop(boolean axisOnTop) {
        this.axisOnTop = axisOnTop;
    }

    public boolean isShowCurrentTime() {
        return showCurrentTime;
    }

    public void setShowCurrentTime(boolean showCurrentTime) {
        this.showCurrentTime = showCurrentTime;
    }

    public boolean isShowNavigation() {
        return showNavigation;
    }

    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation = showNavigation;
    }

    public class Wrapper implements Serializable{
        private String text;
        private Resource resource;

        public Wrapper(String text, Resource resource) {
            this.text = text;
            this.resource = resource;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
