package com.phuture.instant.model.rss;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "rss")
public class Rss {
    @JacksonXmlElementWrapper(localName = "channel") public Channel channel;
}
