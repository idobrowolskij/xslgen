package com.id.xslgen.service;

import com.id.xslgen.dto.XmlValidationErrorDto;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class XmlValidationService {
    public Optional<XmlValidationErrorDto> validateXml(String xml) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();

            parser.parse(new InputSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))), new DefaultHandler());
            return Optional.empty();
        } catch (SAXParseException e) {
            return Optional.of(new XmlValidationErrorDto(Math.max(1, e.getLineNumber() - 1), e.getColumnNumber(), e.getMessage()));
        } catch (Exception e) {
            return Optional.of(new XmlValidationErrorDto(-1, -1, "Error: " + e.getMessage()));
        }
    }
}
