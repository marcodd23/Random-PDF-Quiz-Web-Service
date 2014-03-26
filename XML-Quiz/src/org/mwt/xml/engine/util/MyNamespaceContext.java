/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.XMLConstants;

/**
 *
 * @author marco
 */
public class MyNamespaceContext implements javax.xml.namespace.NamespaceContext{
    
    private final Map<String,String> bindings;

    //costruttore
    public MyNamespaceContext() {
        bindings = new HashMap<String, String>();
    }
    
    
    public void addBinding(String prefix, String uri){
        
        bindings.put(prefix, uri);
    }

    @Override
    public String getNamespaceURI(String prefix) {
        
     if(prefix.equals(XMLConstants.XML_NS_PREFIX)){
         return XMLConstants.XML_NS_URI;
     }else if(prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)){
         return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;  
     }
     
     else if (bindings.containsKey(prefix)){
         return bindings.get(prefix);
     }else {
         
         return XMLConstants.NULL_NS_URI;
     }
     
     
    }

    @Override
    public String getPrefix(String namespaceURI) {
         return null;        
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
          return null;        
    }
    
}
