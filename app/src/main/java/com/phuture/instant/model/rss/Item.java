package com.phuture.instant.model.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "item")
public class Item {
    @JacksonXmlProperty public String title;
    @JacksonXmlProperty public String link;
    @JacksonXmlProperty public String pubDate;
    @JacksonXmlProperty public String author;
    @JacksonXmlProperty public String description;

}
