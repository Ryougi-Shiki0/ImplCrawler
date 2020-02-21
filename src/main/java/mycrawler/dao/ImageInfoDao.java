package mycrawler.dao;
import mycrawler.pojo.ImageInfo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageInfoDao { //extends JpaRepository<ImageInfo,Long>
    /**
     * 保存图片信息
     * @param imageInfo
     */
    @Insert("insert into imageinfo(id,picName,url,tag,authorId,author) values(#{id},#{picName},#{url},#{tag},#{authorId},#{author});")
    void savePixivImageInfo(ImageInfo imageInfo);

    /*List<ImageInfo> findByTags(String tag);*/
}
