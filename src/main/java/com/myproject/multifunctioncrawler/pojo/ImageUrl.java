package com.myproject.multifunctioncrawler.pojo;

import javax.validation.constraints.NotNull;

public class ImageUrl {
    @NotNull
    String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
