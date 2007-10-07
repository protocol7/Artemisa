package com.protocol7.artemisa.examples.atom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.protocol7.artemisa.AnnotationsXPathBinder;
import com.protocol7.artemisa.TestTemplate;
import com.protocol7.artemisa.XPathBinder;
import com.protocol7.artemisa.XmlXPathBinder;

public class DiveIntoAtomTest extends TestTemplate {

    public void testXmlBinding() throws Exception {
        XPathBinder binder = new XmlXPathBinder(buildDocumentFromFile("src/test/resources/atom-binding.xml"));
        
        AtomFeed feed = (AtomFeed) binder.bind(buildDocumentFromFile("src/test/resources/diveintomark.xml"));
        
        assertEquals("dive into mark", feed.getTitle());
        assertEquals("2007-07-01T02:34:32Z", feed.getUpdated()); // TODO replace with date
        assertEquals("Mark Pilgrim", feed.getAuthor());
        
        List<AtomEntry> entries = feed.getEntries();
        
        AtomEntry entry = entries.get(0);
        assertEquals("iRony", entry.getTitle());

        entry = entries.get(1);
        assertEquals("iGroove for iPhone?", entry.getTitle());
        
        entry = entries.get(2);
        assertEquals("The persistence of memory", entry.getTitle());

        AtomEntry[] entriesArray = feed.getEntriesArray();
        
        assertEquals("iRony",                       entriesArray[0].getTitle());
        assertEquals("iGroove for iPhone?",         entriesArray[1].getTitle());
        assertEquals("The persistence of memory",   entriesArray[2].getTitle());
    }

    public void testAnnotationsBinding() throws Exception {
        Map<String, String> ns = new HashMap<String, String>();
        ns.put("atom", "http://www.w3.org/2005/Atom");
        XPathBinder binder = new AnnotationsXPathBinder(AtomFeed.class, ns);
        
        AtomFeed feed = (AtomFeed) binder.bind(buildDocumentFromFile("src/test/resources/diveintomark.xml"));
        
        assertEquals("dive into mark", feed.getTitle());
        assertEquals("2007-07-01T02:34:32Z", feed.getUpdated()); // TODO replace with date
        assertEquals("Mark Pilgrim", feed.getAuthor());
        
        List<AtomEntry> entries = feed.getEntries();

        AtomEntry entry = entries.get(0);
        assertEquals("iRony", entry.getTitle());
        
        entry = entries.get(1);
        assertEquals("iGroove for iPhone?", entry.getTitle());
        
        entry = entries.get(2);
        assertEquals("The persistence of memory", entry.getTitle());

        AtomEntry[] entriesArray = feed.getEntriesArray();
        
        assertEquals("iRony",                       entriesArray[0].getTitle());
        assertEquals("iGroove for iPhone?",         entriesArray[1].getTitle());
        assertEquals("The persistence of memory",   entriesArray[2].getTitle());

    }
}
