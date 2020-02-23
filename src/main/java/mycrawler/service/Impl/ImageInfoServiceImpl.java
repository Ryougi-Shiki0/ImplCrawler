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
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.ResultItems;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
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

    @Override
    public List<String> searchPixivImageByTags(String tags) {
        String[] temp= tags.split(" ");
        System.out.println("调用searchPixivImageByTags: "+tags);
        if(temp.length==0)  return null;
        if(temp.length==1){
            return imageInfoDao.searchPixivImageByTag("%"+temp[0]+"%");
        }
        else {
            String[] searchTags=new String[3];
            System.arraycopy(temp, 0, searchTags, 0, temp.length);
            return imageInfoDao.searchPixivImageByTags("%"+searchTags[0]+"%","%"+searchTags[1]+"%","%"+searchTags[2]+"%");
        }
    }
}
