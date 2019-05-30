/**
 * Copyright (C) 2014-2019 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ciem.schematron.utils;

import com.ciem.schematron.file.SchematronFile;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.file.FileSystemRecursiveIterator;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.lang.ClassPathHelper;
import com.helger.commons.string.StringHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.serialize.MicroReader;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * A utility class to list all the available test files.
 *
 * @author Philip Helger
 */
public final class SchematronFilesUtil
{
  private static final ICommonsList<SchematronFile> s_aSCHs = _readDI (new ClassPathResource("sch/dirindex.xml"));
  // private static final ICommonsList<SchematronFile> s_aSVRLs = _readDI (new ClassPathResource("test-svrl/dirindex.xml"));
  private static final ICommonsList<SchematronFile> s_aXMLs = _readDI (new ClassPathResource("xsd/dirindex.xml"));

  @Nonnull
  private static ICommonsList<SchematronFile> _readDI (@Nonnull final IReadableResource aRes)
  {
    if (false)
      ClassPathHelper.getAllClassPathEntries ().forEach (x -> {
        System.out.println (x);
        if (new File (x).isDirectory ())
        {
          final FileSystemRecursiveIterator it = new FileSystemRecursiveIterator(new File (x));
          it.forEach (y -> System.out.println (StringHelper.getRepeated ("  ", it.getLevel ()) + y));
        }
      });
    ValueEnforcer.notNull (aRes, "Resource");
    ValueEnforcer.isTrue (aRes.exists (), () -> "Resource " + aRes + " does not exist!");

    final ICommonsList<SchematronFile> ret = new CommonsArrayList<>();
    final IMicroDocument aDoc = MicroReader.readMicroXML (aRes);
    if (aDoc == null)
      throw new IllegalArgumentException ("Failed to open/parse " + aRes + " as XML");
    String sLastParentDirBaseName = null;
    for (final IMicroElement eItem : aDoc.getDocumentElement ().getAllChildElements ())
      if (eItem.getTagName ().equals ("directory"))
        sLastParentDirBaseName = eItem.getAttributeValue ("basename");
      else
        if (eItem.getTagName ().equals ("file"))
          ret.add (new SchematronFile (sLastParentDirBaseName,
                                           new ClassPathResource(eItem.getAttributeValue ("name")),
                                           eItem.getAttributeValue ("basename")));
        else
          throw new IllegalArgumentException ("Cannot handle " + eItem);
    return ret;
  }

  private SchematronFilesUtil()
  {}

  @Nonnull
  @Nonempty
  public static ICommonsList<IReadableResource> getAllValidSchematronFiles ()
  {
    return s_aSCHs.getAllMapped (aFile -> !aFile.getFileBaseName ().startsWith ("NegativeSample") &&
                                          !aFile.getParentDirBaseName ().equals ("include"),
                                 SchematronFile::getResource);
  }

  @Nonnull
  @Nonempty
  public static ICommonsList<IReadableResource> getAllInvalidSchematronFiles ()
  {
    return s_aSCHs.getAllMapped (aFile -> aFile.getFileBaseName ().startsWith ("NegativeSample") &&
                                          !aFile.getParentDirBaseName ().equals ("include"),
                                 SchematronFile::getResource);
  }

  @Nonnull
  @Nonempty
  public static ICommonsList<IReadableResource> getAllValidXMLFiles ()
  {
    return s_aXMLs.getAllMapped (aFile -> aFile.getFileBaseName ().startsWith ("PositiveSample"),
                                 SchematronFile::getResource);
  }

  @Nonnull
  @Nonempty
  public static ICommonsList<IReadableResource> getAllInvalidXMLFiles ()
  {
    return s_aXMLs.getAllMapped (aFile -> aFile.getFileBaseName ().startsWith ("NegativeSample"),
                                 SchematronFile::getResource);
  }
}
