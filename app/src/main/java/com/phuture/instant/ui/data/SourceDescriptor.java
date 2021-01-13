package com.phuture.instant.ui.data;

import com.phuture.instant.model.Source;

public class SourceDescriptor {

    public String sourceId;
    public String title;

    private SourceDescriptor() {}

    static SourceDescriptor createSingle(Source source){
        SourceDescriptor i = new SourceDescriptor();
        i.sourceId = source.id;
        i.title = source.name;
        return i;
    }

    static SourceDescriptor createAll() {
        SourceDescriptor i = new SourceDescriptor();
        i.sourceId = null;
        i.title = "All";
        return i;
    }

}
