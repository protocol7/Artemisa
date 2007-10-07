package com.protocol7.artemisa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

final class DocumentBackedNamespaceContext implements
        NamespaceContext {
    
    private Document document;
    
    public DocumentBackedNamespaceContext(Document document) {
        this.document = document;
    }

    public String getNamespaceURI(String prefix) {
        return document.lookupNamespaceURI(prefix);
    }

    public String getPrefix(String namespaceURI) {
        return document.lookupPrefix(namespaceURI);
    }

    public Iterator<String> getPrefixes(String namespaceURI) {
        
        String prefix = getPrefix(namespaceURI);
        
        List<String> prefixes = new ArrayList<String>(1);
        if(prefix != null) {
            prefixes.add(prefix);
        }
        
        return prefixes.iterator();
    }
}