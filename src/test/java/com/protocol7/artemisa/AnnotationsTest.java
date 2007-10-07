package com.protocol7.artemisa;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

public class AnnotationsTest extends TestCase {

    private XPathFactory xPathFactory = XPathFactory.newInstance();
    protected XPath xPath = xPathFactory.newXPath();
    
    @Override
    protected void setUp() throws Exception {
        xPath.reset();
    }

    protected Document buildDocument(String xmlPath) throws Exception {
        File xmlInput = new File(xmlPath);
        
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware( true);
        
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        return builder.parse(xmlInput);
    }
    
    public void testSimpleBean() throws Exception {
        
        AnnotationsXPathBinder binder = new AnnotationsXPathBinder(SimpleBean.class, null);
        
        SimpleBean bean = (SimpleBean) binder.bind(buildDocument("src/test/resources/annot-test1.xml"));
        
        assertEquals("Text1", bean.getFoo());
        assertEquals("Text2", bean.getBar());
    }

    public void testComplexBean() throws Exception {
        
        AnnotationsXPathBinder binder = new AnnotationsXPathBinder(ComplexBean.class, null);
        
        ComplexBean bean = (ComplexBean) binder.bind(buildDocument("src/test/resources/annot-complex.xml"));
        
        assertEquals("Text3", bean.getFoo());
        
        SimpleBean simpleBean = bean.getSimpleBean();
        assertEquals("Text1", simpleBean.getFoo());
        assertEquals("Text2", simpleBean.getBar());
        
        
    }
}
