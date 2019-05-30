package com.ciem.schematron.service;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import javax.annotation.Nonnull;
import java.io.File;

public interface IValidateSchematronService {

    SchematronOutputType validateXMLViaPureSchematron2(@Nonnull final File aSchematronFile,
                                                              @Nonnull final File aXMLFile);
}
