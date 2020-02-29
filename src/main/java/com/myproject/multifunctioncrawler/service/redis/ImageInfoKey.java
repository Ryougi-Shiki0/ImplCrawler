package com.myproject.multifunctioncrawler.service.redis;


import com.myproject.multifunctioncrawler.pojo.ImageInfo;

public class ImageInfoKey extends BasePrefix {
    public static ImageInfoKey getById=new ImageInfoKey("id");
    public static ImageInfoKey getByName=new ImageInfoKey("name");

    public ImageInfoKey(String prefix){
        super(0,prefix);
    }
}
