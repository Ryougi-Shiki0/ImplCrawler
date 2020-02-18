package service;

import pojo.ImageInfo;

import java.util.List;

public interface ImageInfoService {
    /**
     * 根据Tag查找图片
     * @return imageInfo
     */
    List<ImageInfo> findImage(String tags);
}
