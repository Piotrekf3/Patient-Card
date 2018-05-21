package misc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.Patient;

public class XMLParser {
    private FhirContext ctx;
    private IParser parser;

    public XMLParser() {
        ctx = FhirContext.forDstu2();
        parser = ctx.newXmlParser();
    }

    public Patient parsePatient(String msgString) {
        return parser.parseResource(Patient.class, msgString);
    }
}
