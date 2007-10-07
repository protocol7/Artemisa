package com.protocol7.artemisa.examples.atom;

import java.util.List;

import com.protocol7.artemisa.XpathBean;
import com.protocol7.artemisa.XpathProperty;

@XpathBean(xpath="/atom:feed")
public class AtomFeed {

    private String title;
    private String author;
    private String updated;
    private List<AtomEntry> entries;
    private AtomEntry[] entriesArray;
    
    public String getTitle() {
        return title;
    }
    
    @XpathProperty(xpath="atom:title/text()")
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    @XpathProperty(xpath="atom:author/atom:name/text()")
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getUpdated() {
        return updated;
    }
    
    @XpathProperty(xpath="atom:updated/text()")
    public void setUpdated(String updated) {
        this.updated = updated;
    }
    public List<AtomEntry> getEntries() {
        return entries;
    }

    @XpathProperty(beanClass=AtomEntry.class)
    public void setEntries(List<AtomEntry> entries) {
        this.entries = entries;
        
    }

    public AtomEntry[] getEntriesArray() {
        return entriesArray;
    }

    @XpathProperty
    public void setEntriesArray(AtomEntry[] entriesArray) {
        this.entriesArray = entriesArray;
        
    }

}
