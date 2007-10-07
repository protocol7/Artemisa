package com.protocol7.artemisa;

public class BindingDefintion {

    private BeanBinding bean;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BindingDefintion))
            return false;
        BindingDefintion bd = (BindingDefintion) o;
        return bean.equals(bd.bean);
    }

    /**
     * @param bean
     */
    public BindingDefintion(BeanBinding bean) {
        this.bean = bean;
    }

    public BeanBinding getBean() {
        return bean;
    }

}
