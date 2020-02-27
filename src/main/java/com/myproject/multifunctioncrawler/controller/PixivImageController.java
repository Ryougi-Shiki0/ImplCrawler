package com.myproject.multifunctioncrawler.controller;


import com.alibaba.fastjson.JSON;
import com.myproject.multifunctioncrawler.pojo.ImageUrl;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes(value={"json"})
public class PixivImageController {
    @Autowired
    private ImageInfoService imageInfoService;

    @RequestMapping("/start")
    public String start(){
        return "index.html";
    }

    public ImageUrl getImageUrl(@RequestParam("tags") String tags){
        ImageUrl imageUrl=new ImageUrl();
        imageUrl.setTags(tags);
        return imageUrl;
    }

    @RequestMapping("/getPixivImageUrl")
    //@ResponseBody
    public String getPixivImageUrl(@ModelAttribute ImageUrl imageUrl, Model model){
        List<String> res=imageInfoService.searchPixivImageByTags(imageUrl.getTags());
        String result=JSON.toJSONString(res);
        model.addAttribute("json", JSON.toJSONString(res));
        return "success.html";
    }
}
