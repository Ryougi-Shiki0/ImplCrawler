package com.myproject.multifunctioncrawler.service.Impl;

import com.alibaba.fastjson.JSON;
import com.myproject.multifunctioncrawler.dao.ImageInfoDao;
import com.myproject.multifunctioncrawler.pojo.ImageInfo;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import com.myproject.multifunctioncrawler.service.redis.ImageInfoKey;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("imageInfoService")
public class ImageInfoServiceImpl implements ImageInfoService {

    @Autowired
    private ImageInfoDao imageInfoDao;
    @Autowired
    private RedisService redisService;

    @Override
    public void savePixivImagesData(ResultItems resultItems) {
        //获取封装好的数据
        List<ImageInfo> imageInfos = resultItems.get("ImageInfo");
        if (!CollectionUtils.isNotEmpty(imageInfos)) {
            try {
                for (ImageInfo imageInfo : imageInfos) {
                    imageInfoDao.savePixivImageInfo(imageInfo);
                }
            } catch (DuplicateKeyException e) {
                log.error("Duplicated Image. ImageInfo={}",imageInfos);
            }
            log.info("Save Success.");
        }
    }

    @Override
    public List<String> searchPixivImageByTags(String tags) {
        String[] temp = tags.split(" ");
        log.info("Tag: " + tags);
        List<String> list = new ArrayList<>();
        if (temp.length == 0) {
            return list;
        }
        String res0 = redisService.get(ImageInfoKey.getById, tags, String.class);
        if (temp.length == 1) {
            list = imageInfoDao.searchPixivImageByTag("%" + temp[0] + "%");
        } else {
            return list;
        }
        if (res0 == null && list != null) {
            redisService.set(ImageInfoKey.getById, tags, JSON.toJSON(list));
        }
        return list;
    }
}
