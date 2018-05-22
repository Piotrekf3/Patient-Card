package misc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Patient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

public class XMLParser {
    private IParser parser;

    public XMLParser(FhirContext ctx) {
        ctx = FhirContext.forDstu2();
        parser = ctx.newXmlParser();
    }

    public Patient parsePatient(String msgString) {
        return parser.parseResource(Patient.class, msgString);
    }
}
