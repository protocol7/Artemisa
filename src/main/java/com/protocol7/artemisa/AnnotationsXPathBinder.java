package com.protocol7.artemisa;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.protocol7.artemisa.XpathProperty.BeanClassDefault;

public class AnnotationsXPathBinder extends DefaultXPathBinder {

    public AnnotationsXPathBinder(Class<?> clazz, Map<String, String> ns) throws XPathExpressionException {
        super(buildDefinitionFromClass(clazz, ns));
    }

    private static String getPropertyName(Method method) {
        String name = method.getName().substring(3);
        
        char[] nameArray = name.toCharArray();
        
        nameArray[0] = Character.toLowerCase(nameArray[0]);
        
        return new String(nameArray);
    }
    
    private static BindingDefintion buildDefinitionFromClass(Class<?> clazz, Map<String, String> ns) throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new SimpleNamespaceContext(ns));
        
        return new BindingDefintion(createBeanBindingFromClass(clazz, xpath));


    }

    private static boolean isProperty(Method method) {
        return method.getName().startsWith("set") &&
            method.getParameterTypes().length == 1;
    }
    
    private static BeanBinding createBeanBindingFromClass(Class<?> clazz,
            XPath xpath) throws XPathExpressionException {
        XpathBean annot = clazz.getAnnotation(XpathBean.class);
        if(annot != null) {
            BeanBinding bb;
            if(annot.xpath() != null && annot.xpath().trim().length() > 0) {
                bb = new BeanBinding(clazz, xpath.compile(annot.xpath()));
            } else {
                bb = new BeanBinding(clazz);
            }
            
            Method[] methods = clazz.getMethods();
            
            for(Method method : methods) {
                if(isProperty(method)) {
                    XpathProperty propAnnot = method.getAnnotation(XpathProperty.class);

                    if(propAnnot != null) {
                        String name = getPropertyName(method);

                        PropertyBinding pb;
                        if(propAnnot.xpath() != null && propAnnot.xpath().trim().length() > 0) {
                            pb = new PropertyBinding(name, xpath.compile(propAnnot.xpath()));
                        } else {
                            Class<?> propClass = method.getParameterTypes()[0];
                            
                            if(propClass == List.class) {
                                // let's look at the annotation
                                XpathProperty xpathProperty = method.getAnnotation(XpathProperty.class); 
                                if(xpathProperty != null) {
                                    Class<?> beanClass = xpathProperty.beanClass();
                                    if(beanClass != null && beanClass != BeanClassDefault.class) {
                                        propClass = beanClass;
                                    }
                                }
                            } else if(propClass.isArray()) {
                                propClass = propClass.getComponentType();
                            }
                            
                            pb = new PropertyBinding(name, createBeanBindingFromClass(propClass, xpath));
                        }
                        
                        bb.addProperty(pb);
                    }
                }
            }
            
            return bb;
        } else {
            throw new RuntimeException("Annotations not found on class " + clazz);
        }
    }

    
}
