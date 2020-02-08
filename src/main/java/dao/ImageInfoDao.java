package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pojo.ImageInfo;

public interface ImageInfoDao { //extends JpaRepository<ImageInfo,Long>
    /**
     * 保存图片信息
     * @param imageInfo
     */
    void saveImageInfo(ImageInfo imageInfo);
}
