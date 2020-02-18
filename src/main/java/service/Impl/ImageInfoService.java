package service.Impl;

import dao.ImageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.ImageInfo;

import java.util.List;

@Service
public class ImageInfoService implements service.ImageInfoService {

    //@Autowired
    private ImageInfoDao imageInfoDao;

    /**
     * 按照标签搜索
     * @param tags
     * @return List
     */
    @Override
    public List<ImageInfo> findImage(String tags) {
        //执行查询
        return this.imageInfoDao.findByTags(tags);
    }
}
