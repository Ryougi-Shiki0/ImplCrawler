package mycrawler.controller;

import lombok.extern.slf4j.Slf4j;
import mycrawler.dao.ImageInfoDao;
import mycrawler.pojo.ImageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import us.codecraft.webmagic.ResultItems;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/pixivImage")
public class PixivImageController {

}
