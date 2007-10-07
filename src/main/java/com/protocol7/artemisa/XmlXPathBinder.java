package com.protocol7.artemisa;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlXPathBinder extends DefaultXPathBinder {

    private static final String NAMESPACE = "http://protocol7.com/ns/xpath-binding";
    
    private static List<Element> getShallowElementByTagName(Element parent, String ns, String localName) {
        List<Element> matchingElements = new ArrayList<Element>();
        NodeList children = parent.getChildNodes();
        
        for (int i = 0; i < children.getLength(); i++) {
            if(children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                if(child.getNamespaceURI() != null &&
                        child.getNamespaceURI().equals(ns)) {
                    if(child.getLocalName().equals(localName)) {
                        matchingElements.add(child);
                    }
                }
            }
        }
        
        return matchingElements;
    }
    
    private static PropertyBinding buildPropertyBindingFromXml(Element propElm, XPath xPath) throws XPathExpressionException {
        String name = propElm.getAttribute("name");
        String xpathValue = propElm.getAttribute("xpath");
        
        PropertyBinding pb;
        if(xpathValue != null && xpathValue.trim().length() > 0) {
            pb = new PropertyBinding(name, xPath.compile(xpathValue));
        } else {
            List<Element> beans = getShallowElementByTagName(propElm, NAMESPACE, "bean");
            
            if(beans.size() == 1) {
                pb = new PropertyBinding(name, buildBeanBindingFromXml(beans.get(0), xPath));
            } else if(beans.size() > 1) {
                throw new RuntimeException("Multiple bean elements");
            } else {
                throw new RuntimeException("Missing bean element");
            }
        }
        
        
        return pb;
    }
    
    private static BeanBinding buildBeanBindingFromXml(Element beanElm, XPath xPath) throws XPathExpressionException {
        String className = beanElm.getAttribute("class");
        String xpathValue = beanElm.getAttribute("xpath");
        
        
        try {
            BeanBinding bb;
            if(xpathValue != null && xpathValue.trim().length() > 0) {
                bb = new BeanBinding(Class.forName(className), xPath.compile(xpathValue));
            } else {
                bb = new BeanBinding(Class.forName(className));
            }
            
            List<Element> propElms = getShallowElementByTagName(beanElm, NAMESPACE, "property");
            
            for (Element propElem : propElms) {
                bb.addProperty(buildPropertyBindingFromXml(propElem, xPath));
            }
            
            return bb;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }
    
    public static BindingDefintion buildDefinitionFromXml(Document document) throws XPathExpressionException {
        Element binding = document.getDocumentElement();
        
        NamespaceContext ns = new DocumentBackedNamespaceContext(document);
        
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(ns);
        
        List<Element> rootBeans = getShallowElementByTagName(binding, NAMESPACE, "bean");
        
        if(rootBeans.size() == 1) {
            Element rootBean = (Element) rootBeans.get(0);
            
            return new BindingDefintion(buildBeanBindingFromXml(rootBean, xPath));
        } else {
            throw new RuntimeException("There must be exactly one root bean");
        }
    }
    
    public XmlXPathBinder(Document document) throws XPathExpressionException {
        super(buildDefinitionFromXml(document));
    }
}
