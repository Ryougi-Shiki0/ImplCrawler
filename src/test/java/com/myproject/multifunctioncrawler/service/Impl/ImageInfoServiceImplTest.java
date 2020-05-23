package com.myproject.multifunctioncrawler.service.Impl;


import com.google.common.collect.Lists;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.Matchers.*;


public class ImageInfoServiceImplTest {
    @Autowired
    ImageInfoService imageInfoService;

    @Test
    public void testSearchPixivImageByTags(){
        //List<String> list=imageInfoService.searchPixivImageByTags("Fate");
        //Assert.assertThat("数量错误",list.size(),is(4609));
    }
}
