package service;

import pojo.ImageInfo;

import java.util.List;

public interface ImageInfoService {
    /**
     * 保存图片信息
     * @param imageInfo
     */
    public void save(ImageInfo imageInfo);

    /**
     * 根据Tag查找图片
     * @return imageInfo
     */
    public List<ImageInfo> findImage(ImageInfo imageInfo);
}
