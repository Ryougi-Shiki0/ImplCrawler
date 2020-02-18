package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pojo.ImageInfo;
import java.util.List;

public interface ImageInfoDao { //extends JpaRepository<ImageInfo,Long>
    /**
     * 保存图片信息
     * @param imageInfo
     */
    void saveImageInfo(ImageInfo imageInfo);

    /**
     * 按照标签搜索图片
     * @param tag
     * @return List
     */
    List<ImageInfo> findByTags(String tag);
}
