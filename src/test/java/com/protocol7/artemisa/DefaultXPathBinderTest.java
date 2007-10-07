package com.protocol7.artemisa;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

public class DefaultXPathBinderTest extends XPathBinderTestTemplate {


    @Override
    protected XPathBinder createNoContextBinder() throws XPathExpressionException {
        BeanBinding bb = new BeanBinding(SimpleBean.class);
        bb.addProperty(new PropertyBinding("foo", xPath.compile("/root/bean/foo/text()")));
        bb.addProperty(new PropertyBinding("bar", xPath.compile("/root/bean/@bar")));
        BindingDefintion bd = new BindingDefintion(bb);
        
        
        return new DefaultXPathBinder(bd);
    }

    protected XPathBinder createWithContextBinder() throws Exception {
        BeanBinding bb = new BeanBinding(SimpleBean.class, xPath.compile("/root/bean"));
        bb.addProperty(new PropertyBinding("foo", xPath.compile("foo/text()")));
        bb.addProperty(new PropertyBinding("bar", xPath.compile("@bar")));
        BindingDefintion bd = new BindingDefintion(bb);
        
        return new DefaultXPathBinder(bd);
    }

    protected XPathBinder createMultipleBeansBinder() throws Exception {
        BeanBinding bb = new BeanBinding(SimpleBean.class, xPath.compile("/root/bean"));
        bb.addProperty(new PropertyBinding("foo", xPath.compile("foo/text()")));
        bb.addProperty(new PropertyBinding("bar", xPath.compile("@bar")));
        BindingDefintion bd = new BindingDefintion(bb);
        
        return new DefaultXPathBinder(bd);
    }

    protected XPathBinder createWithNamespaceBinder() throws Exception {
        BeanBinding bb = new BeanBinding(SimpleBean.class);
        
        Map<String, String> ns = new HashMap<String, String>();
        ns.put("ns", "http://protocol7.com/foo");

        xPath.setNamespaceContext(new SimpleNamespaceContext(ns));
        
        bb.addProperty(new PropertyBinding("foo", xPath.compile("/ns:root/ns:bean/ns:foo/text()")));
        bb.addProperty(new PropertyBinding("bar", xPath.compile("/ns:root/ns:bean/@bar")));
        BindingDefintion bd = new BindingDefintion(bb);
        
        return new DefaultXPathBinder(bd);
    }
    
    protected XPathBinder createComplexBeanBinder() throws Exception {
        BeanBinding bb1 = new BeanBinding(SimpleBean.class, xPath.compile("child"));
        bb1.addProperty(new PropertyBinding("foo", xPath.compile("foo/text()")));
        bb1.addProperty(new PropertyBinding("bar", xPath.compile("@bar")));

        BeanBinding bb2 = new BeanBinding(ComplexBean.class, xPath.compile("/root/bean"));
        bb2.addProperty(new PropertyBinding("foo", xPath.compile("foo/text()")));
        bb2.addProperty(new PropertyBinding("simpleBean", bb1));
        
        BindingDefintion bd = new BindingDefintion(bb2);
        
        return new DefaultXPathBinder(bd);
    }

}
