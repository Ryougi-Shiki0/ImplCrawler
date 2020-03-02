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
    @Insert("insert into imageinfo(tag,author,id,authorId,url,picName) values(#{tag},#{author},#{id},#{authorId},#{url},#{picName});")
    void savePixivImageInfo(ImageInfo imageInfo);

    @Select("select url from imageinfo where tag like #{tag}")
    List<String> searchPixivImageByTag(@Param("tag") String tag);

    @Select("select url from imageinfo where tag like #{tag1} and tag like #{tag2} and tag like #{tag3}")
    List<String> searchPixivImageByTags(String tag1,String tag2,String tag3);


}
