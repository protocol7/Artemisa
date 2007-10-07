package com.protocol7.artemisa;


public class XmlXPathBinderTest extends XPathBinderTestTemplate {

    @Override
    protected XPathBinder createNoContextBinder() throws Exception {
        return new XmlXPathBinder(buildDocumentFromFile("src/test/resources/no-context.xml"));
    }
    
    @Override
    protected XPathBinder createWithContextBinder() throws Exception {
        return new XmlXPathBinder(buildDocumentFromFile("src/test/resources/with-context.xml"));
    }

    @Override
    protected XPathBinder createMultipleBeansBinder() throws Exception {
        return new XmlXPathBinder(buildDocumentFromFile("src/test/resources/with-context.xml"));
    }
    
    @Override
    protected XPathBinder createWithNamespaceBinder() throws Exception {
        return new XmlXPathBinder(buildDocumentFromFile("src/test/resources/with-namespace.xml"));
    }

    @Override
    protected XPathBinder createComplexBeanBinder() throws Exception {
        return new XmlXPathBinder(buildDocumentFromFile("src/test/resources/complex-bean.xml"));
    }
}
