package com.myproject.multifunctioncrawler.service.Impl;


import com.myproject.multifunctioncrawler.AbstractTransactionalTest;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.ResourceUtils;

import java.util.List;


public class ImageInfoServiceImplTest extends AbstractTransactionalTest {
    @Autowired
    ImageInfoService imageInfoService;

    @Test
    @Sql(value = ResourceUtils.CLASSPATH_URL_PREFIX + "image/pixivimages/image_tags.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testSearchPixivImageByTags() {
        List<String> list = imageInfoService.searchPixivImageByTags("Fate");

        Assert.assertThat("数量错误", list.size(), Is.is(4609));
    }

    @Test
    @Sql(ResourceUtils.CLASSPATH_URL_PREFIX + "image/pixivimages/image_tags.sql")
    public void testSearchPixivImageByTags2() {
        List<String> list = imageInfoService.searchPixivImageByTags("Fate");

        Assert.assertThat("数量错误", list.size(), Is.is(4609));
    }
}
