package com.myproject.multifunctioncrawler.dao;

import com.myproject.multifunctioncrawler.entity.ImageInfo;

public interface ImageInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImageInfo record);

    int insertSelective(ImageInfo record);

    ImageInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImageInfo record);

    int updateByPrimaryKey(ImageInfo record);
}