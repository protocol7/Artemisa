package com.protocol7.artemisa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.NamespaceContext;

final class SimpleNamespaceContext implements
        NamespaceContext {
    
    private Map<String, String> ns;
    
    public SimpleNamespaceContext(Map<String, String> ns) {
        this.ns = ns;
    }

    public String getNamespaceURI(String prefix) {
        if(ns != null) {
            return ns.get(prefix);
        } else {
            return null;
        }
    }

    public String getPrefix(String namespaceURI) {
        if(ns == null) {
            return null;
        }
        
        for(Entry<String, String> entry: ns.entrySet()) {
            if(namespaceURI.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Iterator<String> getPrefixes(String namespaceURI) {
        
        List<String> prefixes = new ArrayList<String>();
        
        if(ns != null) {
            for(Entry<String, String> entry: ns.entrySet()) {
                if(namespaceURI.equals(entry.getValue())) {
                    prefixes.add(entry.getKey());
                }
            }
        }
        
        return prefixes.iterator();
    }
}