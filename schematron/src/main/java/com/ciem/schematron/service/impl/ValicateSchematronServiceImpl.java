package com.ciem.schematron.service.impl;

import com.ciem.schematron.service.IValidateSchematronService;
import com.ciem.schematron.utils.SchematronValidateUtil;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.File;

@Service
public class ValicateSchematronServiceImpl implements IValidateSchematronService {

    public SchematronOutputType validateXMLViaPureSchematron2(@Nonnull final File aSchematronFile,
                                                              @Nonnull final File aXMLFile) {

        try {
            return SchematronValidateUtil.validateXMLViaPureSchematron2(aSchematronFile,aXMLFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
