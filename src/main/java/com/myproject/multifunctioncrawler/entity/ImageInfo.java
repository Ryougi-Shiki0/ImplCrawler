package com.myproject.multifunctioncrawler.entity;

public class ImageInfo {
    private Integer id;

    private String tag;

    private String author;

    private Integer authorid;

    private String url;

    private String picname;

    public ImageInfo(Integer id, String tag, String author, Integer authorid, String url, String picname) {
        this.id = id;
        this.tag = tag;
        this.author = author;
        this.authorid = authorid;
        this.url = url;
        this.picname = picname;
    }

    public ImageInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname == null ? null : picname.trim();
    }
}