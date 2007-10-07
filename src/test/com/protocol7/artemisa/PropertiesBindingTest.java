package com.protocol7.artemisa;

import java.util.Date;

import org.w3c.dom.Node;

public class PropertiesBindingTest extends TestTemplate {

    public void testSimpleBinding() throws Exception {
        PropertyBinding pb = new PropertyBinding("foo", xPath.compile("/root/text()"));
        
        Node node = buildNodeFromString("<root>bar</root>");
        
        SimpleBean bean = new SimpleBean();
        
        pb.bind(bean, node);
        
        assertEquals("bar", bean.getFoo());
    }
    
    private static class TestBean {
        private int theInt;
        private Date theDate;

        public Date getTheDate() {
            return theDate;
        }

        public void setTheDate(Date theDate) {
            this.theDate = theDate;
        }

        public int getTheInt() {
            return theInt;
        }

        public void setTheInt(int theInt) {
            this.theInt = theInt;
        }
    }
    
    public void testIntBinding() throws Exception {
        PropertyBinding pb = new PropertyBinding("theInt", xPath.compile("/root/text()"));
        
        Node node = buildNodeFromString("<root>123</root>");
        
        TestBean bean = new TestBean();
        
        pb.bind(bean, node);
        
        assertEquals(123, bean.getTheInt());
    }

    public void testDateBinding() throws Exception {
        PropertyBinding pb = new PropertyBinding("theDate", xPath.compile("/root/text()"));
        
        Node node = buildNodeFromString("<root>2007-07-04T11:12:13</root>");
        
        TestBean bean = new TestBean();
        
        pb.bind(bean, node);
        
        assertEquals(new Date(107, 6, 4, 11, 12, 13), bean.getTheDate());
    }
}
