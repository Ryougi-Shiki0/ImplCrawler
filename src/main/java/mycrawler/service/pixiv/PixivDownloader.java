package mycrawler.service.pixiv;

import mycrawler.dao.ImageInfoDao;
import mycrawler.pojo.ImageInfo;
import mycrawler.service.ImageInfoService;
import mycrawler.service.Impl.ImageInfoServiceImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.io.InputStream;
import java.util.List;


public class PixivDownloader implements Pipeline {

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void process(ResultItems resultItems, Task task) {
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
        ImageInfoService imageInfoService = (ImageInfoService) ac.getBean("imageInfoService");
        imageInfoService.savePixivImagesData(resultItems);
    }
}
