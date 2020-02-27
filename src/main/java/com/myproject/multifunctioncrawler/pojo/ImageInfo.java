package com.myproject.multifunctioncrawler.pojo;


public class ImageInfo {

    private long id;
    private String picName;
    private String url;
    private String tag;
    private long authorId;
    private String author;

    public ImageInfo() {
    }

    public ImageInfo(ImageInfo imageInfo){
        this.id=imageInfo.id;
        this.picName=imageInfo.picName;
        this.url=imageInfo.url;
        this.tag=imageInfo.tag;
        this.authorId=imageInfo.authorId;
        this.author=imageInfo.author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
