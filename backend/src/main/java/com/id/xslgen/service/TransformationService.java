package com.id.xslgen.service;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TransformationService {
    public void generatePdf(Path xsl, Path xml, Path outPdf) {
        try {
            Files.createDirectories(outPdf.getParent());

            FopFactory fopFactory = FopFactory.newInstance(new java.io.File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            try (OutputStream out = Files.newOutputStream(outPdf)) {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsl.toFile()));

                Source src = new StreamSource(xml.toFile());
                Result res = new SAXResult(fop.getDefaultHandler());

                transformer.transform(src, res);
            }
        } catch (Exception e) {
            throw new RuntimeException("PDF transform failed", e);
        }
    }
}

