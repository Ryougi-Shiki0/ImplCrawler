package com.myproject.multifunctioncrawler.dao;
import com.myproject.multifunctioncrawler.pojo.ImageInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableCaching
@Mapper
public interface ImageInfoDao { //extends JpaRepository<ImageInfo,Long>
    /**
     * 保存图片信息
     * @param imageInfo
     */
    @Insert("insert into imageinfo(id,picName,url,tag,authorId,author) values(#{id},#{picName},#{url},#{tag},#{authorId},#{author});")
    void savePixivImageInfo(ImageInfo imageInfo);

    @Select("select url from imageinfo where tag like #{tag}")
    List<String> searchPixivImageByTag(String tag);

    @Select("select url from imageinfo where tag like #{tag1}")
    List<String> searchPixivImageByTags(String tag1,String tag2,String tag3);
}
