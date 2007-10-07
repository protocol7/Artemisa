package com.protocol7.artemisa;



import java.util.List;

import org.w3c.dom.Node;


public class BeanBindingTest extends TestTemplate {

    private static Node singleChildNode = buildNodeFromString("<root><child /></root>");
    private static Node multiChildNode = buildNodeFromString("<root><child /><child /><child /></root>");
    private static Node zeroChildNode = buildNodeFromString("<root></root>");
    
    private BeanBinding bb;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        bb = new BeanBinding(SimpleBean.class, xPath.compile("/root/child"));
    }

    public void testNullClassHintAndSingleHit() throws Exception {
        SimpleBean simpleBean = (SimpleBean) bb.bind(singleChildNode, null);
        
        assertNotNull(simpleBean);
    }

    public void testNullClassHintAndMultipleHits() throws Exception {
        SimpleBean[] simpleBeans = (SimpleBean[]) bb.bind(multiChildNode, null);
        
        assertEquals(3, simpleBeans.length);
        assertNotNull(simpleBeans[0]);
        assertNotNull(simpleBeans[1]);
        assertNotNull(simpleBeans[2]);
    }
    
    public void testNullClassHintAndZeroHits() throws Exception {
        SimpleBean simpleBean = (SimpleBean) bb.bind(zeroChildNode, null);
        
        assertNull(simpleBean);
    }
    
    public void testBeanClassHintAndSingleHit() throws Exception {
        SimpleBean simpleBean = (SimpleBean) bb.bind(singleChildNode, SimpleBean.class);
        
        assertNotNull(simpleBean);
    }
    
    public void testArrayClassHintAndSingleHit() throws Exception {
        SimpleBean[] simpleBeans = (SimpleBean[]) bb.bind(singleChildNode, new SimpleBean[0].getClass());
        
        assertEquals(1, simpleBeans.length);
        assertNotNull(simpleBeans[0]);
    }
    
    public void testArrayClassHintAndZeroHit() throws Exception {
        SimpleBean[] simpleBeans = (SimpleBean[]) bb.bind(zeroChildNode, new SimpleBean[0].getClass());
        
        assertEquals(0, simpleBeans.length);
    }
    
    @SuppressWarnings("unchecked")
    public void testListClassHintAndZeroHit() throws Exception {
        List<SimpleBean> simpleBeans = (List<SimpleBean>) bb.bind(zeroChildNode, List.class);
        
        assertEquals(0, simpleBeans.size());
    }

    @SuppressWarnings("unchecked")
    public void testListClassHintAndSingleHit() throws Exception {
        List<SimpleBean> simpleBeans = (List<SimpleBean>) bb.bind(singleChildNode, List.class);
        
        assertEquals(1, simpleBeans.size());
        assertNotNull(simpleBeans.get(0));
    }
    
    public void testBeanClassHintAndMultipleHits() throws Exception {
        SimpleBean simpleBean = (SimpleBean) bb.bind(multiChildNode, SimpleBean.class);
        
        assertNotNull(simpleBean);
    }

    public void testBeanClassHintAndZeroHits() throws Exception {
        SimpleBean simpleBean = (SimpleBean) bb.bind(zeroChildNode, SimpleBean.class);
        
        assertNull(simpleBean);
    }
    
    @SuppressWarnings("unchecked")
    public void testListClassHintAndMultipleHits() throws Exception {
        List<SimpleBean> simpleBeans = (List<SimpleBean>) bb.bind(multiChildNode, List.class);
        
        assertEquals(3, simpleBeans.size());
        assertNotNull(simpleBeans.get(0));
        assertNotNull(simpleBeans.get(1));
        assertNotNull(simpleBeans.get(2));
    }
    
    public void testArrayClassHintAndMultipleHits() throws Exception {
        SimpleBean[] simpleBeans = (SimpleBean[]) bb.bind(multiChildNode, new SimpleBean[0].getClass());
        
        assertEquals(3, simpleBeans.length);
        assertNotNull(simpleBeans[0]);
        assertNotNull(simpleBeans[1]);
        assertNotNull(simpleBeans[2]);
    }
}
