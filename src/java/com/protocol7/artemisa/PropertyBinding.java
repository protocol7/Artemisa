package com.protocol7.artemisa;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Node;

import com.protocol7.artemisa.casters.DefaultCaster;

public class PropertyBinding {

    private String name;
    private XPathExpression xpath;
    private BeanBinding beanBinding;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PropertyBinding))
            return false;
        PropertyBinding pb = (PropertyBinding) o;
        
        return name.equals(pb.name) &&
            EqualsHelper.equalsOrBothNull(xpath, pb.xpath) &&
            EqualsHelper.equalsOrBothNull(beanBinding, pb.beanBinding);
    }
    
    public void bind(Object bean, Node node) throws Exception {
        Object value;
        if(xpath != null) {
            value = (String)xpath.evaluate(node, XPathConstants.STRING);
        } else {
            // bean
            value = beanBinding.bind(node, ClassUtils.getPropertyType(bean, name));
            
        }

        ClassUtils.setProperty(bean, name, value, new DefaultCaster());
    }
    
    
    /**
     * @param name
     * @param beanBinding
     */
    public PropertyBinding(String name, BeanBinding beanBinding) {
        super();
        this.name = name;
        this.beanBinding = beanBinding;
    }


    /**
     * @param name
     * @param xpath
     */
    public PropertyBinding(String name, XPathExpression xpath) {
        super();
        this.name = name;
        this.xpath = xpath;
    }


    public String getName() {
        return name;
    }

    public BeanBinding getBean() {
        return beanBinding;
    }

}
