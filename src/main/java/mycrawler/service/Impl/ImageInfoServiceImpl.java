package mycrawler.service.Impl;

import mycrawler.dao.ImageInfoDao;
import mycrawler.service.ImageInfoService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import mycrawler.pojo.ImageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;

import java.io.InputStream;
import java.util.List;

@Service("imageInfoService")
public class ImageInfoServiceImpl implements mycrawler.service.ImageInfoService {

    @Autowired
    private ImageInfoDao imageInfoDao;

    @Override
    public void savePixivImagesData(ResultItems resultItems) {
        //获取封装好的数据
        List<ImageInfo> imageInfos=resultItems.get("ImageInfo");
        if(!imageInfos.isEmpty()){
            for (ImageInfo imageInfo : imageInfos) {
                imageInfoDao.savePixivImageInfo(imageInfo);
            }
        }
    }
}
