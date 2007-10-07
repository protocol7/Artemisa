package com.protocol7.artemisa;



@XpathBean(xpath="child") public class SimpleBean {

    private String foo;
    private String bar;
    public String getFoo() {
        return foo;
    }
    
    @XpathProperty(xpath="foo/text()")
    public void setFoo(String foo) {
        this.foo = foo;
    }
    public String getBar() {
        return bar;
    }
    @XpathProperty(xpath="@bar")
    public void setBar(String bar) {
        this.bar = bar;
    }
    
}
