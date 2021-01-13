package com.phuture.instant.model.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "channel")
public class Channel {
    @JacksonXmlProperty public String title;
    @JacksonXmlProperty public String link;
    @JacksonXmlProperty public String description;
    @JacksonXmlProperty public String lastBuildDate;
    @JacksonXmlElementWrapper(localName = "image") public Image image;
    @JacksonXmlElementWrapper(localName = "item", useWrapping = false) public List<Item> item;
}

