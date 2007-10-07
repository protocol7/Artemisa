package com.protocol7.artemisa;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanBinding {

    private Class<?> clazz;
    private XPathExpression xpath;
    private List<PropertyBinding> properties = new ArrayList<PropertyBinding>();
    
    public BeanBinding(Class<?> clazz, XPathExpression xpath) {
        super();
        this.clazz = clazz;
        this.xpath = xpath;
    }


    public BeanBinding(Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BeanBinding))
            return false;
        BeanBinding bb = (BeanBinding) o;
        
        return clazz.equals(bb.clazz) &&
            EqualsHelper.equalsOrBothNull(xpath, bb.xpath) &&
            properties.equals(bb.properties);
    }
    
    public Object bind(Node node) throws Exception {
        return bind(node, null);
    }
    
    public Object bind(Node node, Class<?> clazzHint) throws Exception {
        if(xpath != null) {
            NodeList nodeList = (NodeList) xpath.evaluate(node, XPathConstants.NODESET);

            Object[] beans = (Object[]) Array.newInstance(clazz, nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                beans[i] = bindBean(nodeList.item(i));
            }
            
            if(clazzHint == null) {
                if(beans.length == 0) {
                    return null;
                } else if(beans.length == 1) {
                    return beans[0];
                } else {
                    return beans;
                } 
            } else {
                if(clazzHint.isArray()) {
                    return beans;
                } else if(clazzHint == List.class) {
                    return Arrays.asList(beans);
                } else {
                    // bean
                    if(beans.length == 0) {
                        return null;
                    } else {
                        return beans[0];
                    }                    
                }
            }

        } else {
            return bindBean(node);
        }
    }


    private Object bindBean(Node node)
            throws Exception {
        Object bean = getBeanClass().newInstance();
        
        for(PropertyBinding pb : getProperties()) {
            pb.bind(bean, node);
        }
        
        return bean;
    }

    public List<PropertyBinding> getProperties() {
        return properties;
    }

    public void addProperty(PropertyBinding pb) {
        this.properties.add(pb);
    }
    public Class<?> getBeanClass() {
        return clazz;
    }

}
