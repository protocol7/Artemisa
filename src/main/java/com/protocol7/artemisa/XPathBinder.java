package com.protocol7.artemisa;

import org.w3c.dom.Node;

public interface XPathBinder {

    Object bind(Node node) throws Exception;

}