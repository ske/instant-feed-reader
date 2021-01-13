package com.phuture.instant.model.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "image")
public class Image {
    @JacksonXmlProperty public String title;
    @JacksonXmlProperty public String url;
    @JacksonXmlProperty public Integer width;
    @JacksonXmlProperty public Integer height;
}
