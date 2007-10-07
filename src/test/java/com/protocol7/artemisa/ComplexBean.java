package com.protocol7.artemisa;

@XpathBean(xpath="/root")
public class ComplexBean {

    private String foo;
    private SimpleBean simpleBean;
    public String getFoo() {
        return foo;
    }
    
    @XpathProperty(xpath="foo/text()")
    public void setFoo(String foo) {
        this.foo = foo;
    }
    public SimpleBean getSimpleBean() {
        return simpleBean;
    }
    
    @XpathProperty
    public void setSimpleBean(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }
    
    
    
}
