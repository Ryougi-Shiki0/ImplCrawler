package mycrawler.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mycrawler.dao.ImageInfoDao;
import mycrawler.pojo.ImageInfo;
import mycrawler.pojo.ImageUrl;
import mycrawler.service.ImageInfoService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import java.util.List;


@Controller
@RequestMapping("/pixivImage")
@SessionAttributes(value={"json"})
public class PixivImageController {

    @RequestMapping("/getPixivImageUrl")
    public String getPixivImageUrl(@ModelAttribute ImageUrl imageUrl, Model model){
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
        ImageInfoService imageInfoService = (ImageInfoService) ac.getBean("imageInfoService");
        List<String> res=imageInfoService.searchPixivImageByTags(imageUrl.getTags());
        model.addAttribute("json",JSON.toJSONString(res));
        return "success";
    }
}
