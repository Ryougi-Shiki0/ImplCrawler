package com.myproject.multifunctioncrawler.service;

import us.codecraft.webmagic.ResultItems;

import java.util.List;


public interface ImageInfoService {

    void savePixivImagesData(ResultItems resultItems);

    List<String> searchPixivImageByTags(String tags);
}
