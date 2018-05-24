package model;

import ca.uhn.fhir.context.FhirContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Global {
    public static final String serverBase = "http://hapi.fhir.org/baseDstu3";

    public FhirContext getCtx() {
        return ctx;
    }

    public void setCtx(FhirContext ctx) {
        this.ctx = ctx;
    }

    private FhirContext ctx;

    public Global() {
        ctx = FhirContext.forDstu3();
    }
}
