package com.protocol7.artemisa.examples.atom;

import com.protocol7.artemisa.XpathBean;
import com.protocol7.artemisa.XpathProperty;

@XpathBean(xpath="atom:entry")
public class AtomEntry {
    private String title;
    private String published;
    private String updated;
    private String content;
    public String getTitle() {
        return title;
    }
    
    @XpathProperty(xpath="atom:title/text()")
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPublished() {
        return published;
    }
    @XpathProperty(xpath="atom:published/text()")
    public void setPublished(String published) {
        this.published = published;
    }
    public String getUpdated() {
        return updated;
    }
    @XpathProperty(xpath="atom:updated/text()")
    public void setUpdated(String updated) {
        this.updated = updated;
    }
    public String getContent() {
        return content;
    }
    @XpathProperty(xpath="atom:content/text()")
    public void setContent(String content) {
        this.content = content;
    }
}
