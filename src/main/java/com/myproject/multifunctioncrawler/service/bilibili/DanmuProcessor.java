package com.myproject.multifunctioncrawler.service.bilibili;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Arthas
 */
@Component
public class DanmuProcessor implements PageProcessor {

    @Autowired
    DanmuProcessor danmuProcessor;
    @Autowired
    DanmuPipeline danmuPipeline;
    private Site site = Site.me().setUserAgent("Mozilla/5.0 (Windows NT 10.0; …e/59.0.3071.109 Safari/537.36")
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setSleepTime(1800)
            .setCycleRetryTimes(3)
            .setUseGzip(true)
            .addCookie("Cookie","stardustvideo=1; CURRENT_FNVAL=16; fts=1553425283; im_notify_type_19184916=0; rpdid=|(um|l||JJ)R0J'ullY|kl)Y|; LIVE_BUVID=073b8acbfb5133451f3e3a459e7c2f4b; LIVE_BUVID__ckMd5=155b978310ba9741; _ga=GA1.2.474823898.1562576415; im_notify_type_40418278=0; im_seqno_40418278=14; im_local_unread_40418278=0; _uuid=F4F77D42-935A-229C-F6B3-AB20093CC23566437infoc; im_seqno_19184916=61150; im_local_unread_19184916=0; route=; laboratory=1-1; INTVER=1; stardustpgcv=0606; buvid3=6D162C4E-763D-4A56-BE96-991DE04D9D5B155828infoc; CURRENT_QUALITY=0; PVID=1; DedeUserID=19184916; DedeUserID__ckMd5=cbef98811ec1a631; SESSDATA=a288dc4d%2C1600012831%2Cd60a5*31; bili_jct=1467e65d88607b156342fa7d388362e3; sid=6ifyqv23; bp_t_offset_19184916=371830046261326607")
            .addHeader("Host","api.bilibili.com")
            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
            .addHeader("Accept-Encoding","gzip, deflate, br");

    //https://api.bilibili.com/x/player/pagelist?bvid=BV1Eg4y1a75S&jsonp=jsonp
    @Override
    public void process(Page page) {
        List<Selectable> list=page.getHtml().xpath("//i/d").nodes();
        List<String> res=new LinkedList<>();
        for (Selectable s : list) {
            String temp=s.toString();
            if (temp.contains("太菜了")) {
                res.add(temp);
            }
        }
        page.putField("result",res);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public String[] getUrl(String[] urls){
        String[] res=new String[urls.length];
        int i=0;
        for (String s : urls) {
            res[i]="https://api.bilibili.com/x/v1/dm/list.so?oid="+s;
        }
        return res;
    }

    //@Scheduled(initialDelay = 0,fixedDelay = 1000*60)
    public void start(String[] urls){
        Spider.create(danmuProcessor)
                .addUrl(danmuProcessor.getUrl(urls))
                .addPipeline(danmuPipeline)
                .thread(5)
                .run();
    }
}
