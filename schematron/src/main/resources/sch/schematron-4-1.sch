<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xs="http://www.w3.org/2001/XMLSchema"
            xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2">
    <sch:title> NIEM example</sch:title>

    <xsl:include href="ndr-functions.xsl"/>

    <sch:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
    <sch:ns prefix="xsl" uri="http://www.w3.org/1999/XSL/Transform"/>
    <sch:ns prefix="nf" uri="http://reference.niem.gov/niem/specification/naming-and-design-rules/4.0/#NDRFunctions"/>
    <sch:ns prefix="ct" uri="http://release.niem.gov/niem/conformanceTargets/3.0/"/>
    <sch:ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <sch:ns prefix="appinfo" uri="http://release.niem.gov/niem/appinfo/4.0/"/>
    <sch:ns prefix="structures" uri="http://release.niem.gov/niem/structures/4.0/"/>

    <sch:pattern id="rule_7-4"><sch:title>Document element is xs:schema</sch:title>
        <sch:rule context="*[. is nf:get-document-element(.)]">
            <sch:assert test="self::xs:schema">Rule 7-4: The [document element] of the [XML document] MUST have the name xs:schema.</sch:assert>
        </sch:rule>
    </sch:pattern>
</sch:schema>