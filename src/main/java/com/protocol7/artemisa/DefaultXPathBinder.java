package com.protocol7.artemisa;

import org.w3c.dom.Node;

public class DefaultXPathBinder implements XPathBinder {

    private BindingDefintion defintion;
    
    public DefaultXPathBinder(BindingDefintion defintion) {
        this.defintion = defintion;
    }


    /* (non-Javadoc)
     * @see com.protocol7.xpathbinding.XPathBinder2#bind(org.w3c.dom.Document)
     */
    public Object bind(Node node) throws Exception {
        return defintion.getBean().bind(node);
    }
    
}
