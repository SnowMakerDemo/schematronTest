package com.ciem.schematron;

import com.ciem.schematron.service.IValidateSchematronService;
import com.ciem.schematron.utils.SchematronFilesUtil;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.SchematronException;
import com.helger.schematron.SchematronHelper;
import com.helger.schematron.pure.binding.xpath.PSXPathQueryBinding;
import com.helger.schematron.pure.bound.IPSBoundSchema;
import com.helger.schematron.pure.errorhandler.IPSErrorHandler;
import com.helger.schematron.pure.exchange.PSReader;
import com.helger.schematron.pure.exchange.SchematronReadException;
import com.helger.schematron.pure.model.PSSchema;
import com.helger.schematron.pure.validation.SchematronValidationException;
import com.helger.schematron.xslt.SchematronResourceSCH;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.serialize.read.DOMReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchematronApplicationTests {

    private static final String VALID_SCHEMATRON = "sch/valid01.sch";
    private static final String VALID_XMLINSTANCE = "xml/valid01.xml";
    private static final String PRIFIX_SCHEMATRON = "schematron-";

    @Autowired
    IValidateSchematronService validateSchematronService;

    public static void testSchematronAndXSD(IReadableResource aXmlRes,String schematronT){
        for (final IReadableResource aRes : SchematronFilesUtil.getAllValidSchematronFiles ()) {
            String schematronName= aRes.getPath().split("/")[1];

            if(schematronT.equals(schematronName)){
                System.out.println(aRes.getPath() + "=====" + aXmlRes.getPath());
                final IMicroDocument aDoc = SchematronHelper.getWithResolvedSchematronIncludes(aRes);
                assertNotNull(aDoc);
                // Read to domain object
                final PSReader aReader = new PSReader(aRes);
                final PSSchema aSchema;
                try {
                    aSchema = aReader.readSchemaFromXML(aDoc.getDocumentElement());
                    assertNotNull(aSchema);

                    // Create a compiled schema
                    final String sPhaseID = null;
                    final IPSErrorHandler aErrorHandler = null;
                    final IPSBoundSchema aBoundSchema = PSXPathQueryBinding.getInstance().bind(aSchema, sPhaseID, aErrorHandler);

                    final SchematronOutputType aSVRL = aBoundSchema.validateComplete(DOMReader.readXMLDOM(aXmlRes),
                            aXmlRes.getAsURL().toExternalForm());
                    System.out.println(aSVRL.getActivePatternAndFiredRuleAndFailedAssert());
                    assertNotNull(aSVRL);
                } catch (SchematronReadException e) {
                    e.printStackTrace();
                } catch (SchematronValidationException e) {
                    e.printStackTrace();
                } catch (SchematronException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Test
    public void testSchametronAndInvalidXSD(){
        // Check all documents
        for (final IReadableResource aXmlRes : SchematronFilesUtil.getAllInvalidXMLFiles()) {
            String[] strs = aXmlRes.getPath().split("/")[1].split("-");
            String schematronT = PRIFIX_SCHEMATRON + strs[1] + "-" +  strs[2] + ".sch";
            testSchematronAndXSD(aXmlRes,schematronT);
        }
    }

    @Test
    public void testSchametronAndValidXSD(){
        // Check all documents
        for (final IReadableResource aXmlRes : SchematronFilesUtil.getAllValidXMLFiles()) {
            String[] strs = aXmlRes.getPath().split("/")[1].split("-");
            String schematronT = PRIFIX_SCHEMATRON + strs[1] + "-" +  strs[2] + ".sch";
            testSchematronAndXSD(aXmlRes,schematronT);
        }
    }

    @Test
    public void testSchematronByPart(){
        File aSchematron = new File("D:\\workSpace\\ciem\\schematron\\src\\main\\resources\\sch\\valid01.sch");
        File aXML = new File("D:\\workSpace\\ciem\\schematron\\src\\main\\resources\\xml\\valid01.xml");
        final SchematronOutputType aSO = validateSchematronService.validateXMLViaPureSchematron2(aSchematron, aXML);
        System.out.println(aSO);
    }

    @Test
    public void testCustomFunctions(){
        final ISchematronResource aSV = SchematronResourceSCH.fromFile ("D:\\workSpace\\ciem\\schematron\\src\\main\\resources\\sch\\schematron-4-1.sch");
        final Document aDoc;
        try {
            aDoc = aSV.applySchematronValidation (new FileSystemResource("D:\\workSpace\\ciem\\schematron\\src\\main\\resources\\xsd\\NegativeSample-4-1-1.xsd"));
            System.out.println ("-----------------------------");
            System.out.println (com.helger.xml.serialize.write.XMLWriter.getNodeAsString (aDoc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

