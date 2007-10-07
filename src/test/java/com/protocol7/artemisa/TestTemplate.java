package com.protocol7.artemisa;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public abstract class TestTemplate extends TestCase {
    private static XPathFactory xPathFactory = XPathFactory.newInstance();
    protected XPath xPath = xPathFactory.newXPath();
    private static DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    static {
        domFactory.setNamespaceAware( true);
    }
    
    @Override
    protected void setUp() throws Exception {
        xPath.reset();
    }

    protected static Document buildDocumentFromFile(String xmlPath) {
        File xmlInput = new File(xmlPath);
        
        DocumentBuilder builder;
        try {
            builder = domFactory.newDocumentBuilder();
            return builder.parse(xmlInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Node buildNodeFromString(String s) {
        InputSource source = new InputSource(new StringReader(s));
        DocumentBuilder builder;
        try {
            builder = domFactory.newDocumentBuilder();
            return builder.parse(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
