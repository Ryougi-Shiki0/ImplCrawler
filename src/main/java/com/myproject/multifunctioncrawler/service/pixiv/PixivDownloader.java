package com.myproject.multifunctioncrawler.service.pixiv;

import com.myproject.multifunctioncrawler.service.ImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class PixivDownloader implements Pipeline {
    @Autowired
    ImageInfoService imageInfoService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void process(ResultItems resultItems, Task task) {
        imageInfoService.savePixivImagesData(resultItems);
    }
}
