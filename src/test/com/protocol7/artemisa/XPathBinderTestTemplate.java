package com.protocol7.artemisa;


public abstract class XPathBinderTestTemplate extends TestTemplate {


    
    protected abstract XPathBinder createNoContextBinder() throws Exception;
    
    public void testNoContext() throws Exception {
        XPathBinder binder = createNoContextBinder();
        
        SimpleBean bean = (SimpleBean) binder.bind(buildDocumentFromFile("src/test/test1.xml"));
        
        assertEquals("Text1", bean.getFoo());
        assertEquals("Text2", bean.getBar());
    }

    protected abstract XPathBinder createWithContextBinder() throws Exception;

    public void testWithContext() throws Exception {
        XPathBinder binder = createWithContextBinder();
        
        SimpleBean bean = (SimpleBean) binder.bind(buildDocumentFromFile("src/test/test1.xml"));
        
        assertEquals("Text1", bean.getFoo());
        assertEquals("Text2", bean.getBar());
    }

    protected abstract XPathBinder createMultipleBeansBinder() throws Exception;

    public void testMultipleBeans() throws Exception {
        XPathBinder binder = createMultipleBeansBinder();
        
        Object[] beans = (Object[]) binder.bind(buildDocumentFromFile("src/test/test2.xml"));
        
        SimpleBean bean = (SimpleBean) beans[0];
        assertEquals("Text1", bean.getBar());
        assertEquals("Text2", bean.getFoo());

        bean = (SimpleBean) beans[1];
        assertEquals("Text3", bean.getBar());
        assertEquals("Text4", bean.getFoo());

        bean = (SimpleBean) beans[2];
        assertEquals("Text5", bean.getBar());
        assertEquals("Text6", bean.getFoo());
    }
    
    protected abstract XPathBinder createWithNamespaceBinder() throws Exception;

    public void testWithNamespace() throws Exception {        
        XPathBinder binder = createWithNamespaceBinder();
        
        SimpleBean bean = (SimpleBean) binder.bind(buildDocumentFromFile("src/test/test3.xml"));
        
        assertEquals("Text1", bean.getBar());
        assertEquals("Text2", bean.getFoo());
    }

    protected abstract XPathBinder createComplexBeanBinder() throws Exception;
    
    public void testComplexBean() throws Exception {

        XPathBinder binder = createComplexBeanBinder();
        
        ComplexBean bean = (ComplexBean) binder.bind(buildDocumentFromFile("src/test/test_complex1.xml"));
        
        assertEquals("Text2", bean.getFoo());
        
        SimpleBean childBean = bean.getSimpleBean();
        assertEquals("Text3", childBean.getFoo());
        assertEquals("Text4", childBean.getBar());
    }
}
