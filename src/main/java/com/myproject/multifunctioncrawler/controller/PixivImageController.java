package com.myproject.multifunctioncrawler.controller;


import com.alibaba.fastjson.JSON;
import com.myproject.multifunctioncrawler.pojo.ImageUrl;
import com.myproject.multifunctioncrawler.result.Result;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import com.myproject.multifunctioncrawler.service.redis.ImageInfoKey;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.service.pixiv.ImageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/pixiv")
@SessionAttributes(value={"json"})
public class PixivImageController {
    @Autowired
    private ImageInfoService imageInfoService;

    @Autowired
    private ImageProcessor imageProcessor;

    @Autowired
    private RedisService redisService;

    public ImageUrl getImageUrl(@RequestParam("tags") String tags){
        ImageUrl imageUrl=new ImageUrl();
        imageUrl.setTags(tags);
        return imageUrl;
    }

    @RequestMapping("/getPixivImageUrl")
    @ResponseBody
    public String getPixivImageUrl(@ModelAttribute ImageUrl imageUrl, Model model){
        List<String> res=imageInfoService.searchPixivImageByTags(imageUrl.getTags());
        log.info(""+res.size());
        //model.addAttribute("json", result);
        //return "successResults";
        return JSON.toJSONString(res);
    }

    @RequestMapping("/getCrawlerResults")
    public String getCrawlerResults(@ModelAttribute ImageUrl imageUrl, Model model){
        String[] searchTags=imageUrl.getTags().trim().split(" ");
        if(searchTags.length<1)
            return "failed";
        imageProcessor.pixivSearchByTags(searchTags);
        return "success";
    }

    @RequestMapping("/test")
    @ResponseBody
    public Result<Long> redisGet(){
        Long v1=redisService.get(ImageInfoKey.getById,"key1",Long.class);
        return Result.success(v1);
    }

    @RequestMapping("/test2")
    @ResponseBody
    public Result<String> redisSet(){
        redisService.set(ImageInfoKey.getById,"key2",123456L);
        String str=redisService.get(ImageInfoKey.getById,"key2",String.class);
        return Result.success(str);
    }
}
