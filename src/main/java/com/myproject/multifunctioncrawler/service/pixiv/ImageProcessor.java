package com.myproject.multifunctioncrawler.service.pixiv;

import com.myproject.multifunctioncrawler.pojo.ImageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageProcessor implements PageProcessor {
    private String headerUrl;
    private Site site=Site.me()
            .setCharset("utf8")
            .setTimeOut(10000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3)
            //添加请求头，有些网站会根据请求头判断该请求是由浏览器发起还是由爬虫发起的
            .addHeader("authority","www.pixiv.net")
            .addHeader("method","GET")
            .addHeader("scheme","https")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .addHeader("Accept-Encoding", "gzip, deflate, br").addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            //.addHeader("Connection", "keep-alive")
            //.addHeader("Referer", headerUrl)
            .addHeader("cache-control","max-age=0")
            .addHeader("cookie","first_visit_datetime_pc=2019-03-19+02%3A10%3A50; p_ab_d_id=520418057; a_type=0; b_type=1; module_orders_mypage=%5B%7B%22name%22%3A%22sketch_live%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22tag_follow%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22recommended_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22fanbox%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22user_events%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22sensei_courses%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; yuid_b=JWNDViU; p_ab_id=0; p_ab_id_2=8; __utmc=235335808; __utmz=235335808.1578032298.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.97466048.1578032298; login_ever=yes; __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=17757078=1^9=p_ab_id=0=1^10=p_ab_id_2=8=1^11=lang=zh=1; adr_id=juU0b0sM75JynuurYwTuN6KdlOgLvqwERCEQpxpzg9IeIWS4; OX_plg=pm; __utma=235335808.97466048.1578032298.1578122448.1580045234.3; __gads=ID=41b8ceb486bf754c:T=1580045265:S=ALNI_MY3mRRCDS-uh1eY3VttiVJtCtgHqA; ki_r=; categorized_tags=BU9SQkS-zU~FdOTvv24Ep~HLWLeyYOUF~IVwLyT8B6k~JXj60bPp4r~OEXgaiEbRa~RcahSSzeRf~b8b4-hqot7~m3EJRa33xU~pvU1D1orJa~qWnxmES0AA~tMaqyr0fI5~y8GNntYHsi~yPNaP3JSNF; device_token=548d30e7e7a492ca0fb56aa3b8413031; _td=48127f07-1b9b-4548-b20d-a9eac961f095; ki_t=1580749841370%3B1582306025687%3B1582306025687%3B3%3B8; ki_s=204128%3A0.0.0.0.0; tag_view_ranking=RTJMXD26Ak~zIv0cf5VVk~kGYw4gQ11Z~ETjPkL0e6r~Ce-EdaHA-3~BU9SQkS-zU~jH0uD88V6F~m3EJRa33xU~nQRrj5c6w_~uusOs0ipBx~XpYOJt3r5W~0G1fdsiW-i~jhuUT0OJva~Lt-oEicbBr~wssjDd32Z6~y8GNntYHsi~lH5YZxnbfC~_hSAdpN9rx~yPNaP3JSNF~HLWLeyYOUF~VN7cgWyMmg~aOGQhsapNP~1F9SMtTyiX~nKIMqYEhZ7~JXj60bPp4r~HBuswNf19E~RybylJRnhJ~2pZ4K1syEF~j-DzSVPeak~144zWY--zX~LJo91uBPz4~MSNRmMUDgC~NpsIVvS-GF~FFBbSsTrOz~xhKTZw-7Qp~cm40yP10lH~Vy8C1qCw9h~utVwkll27R~kqu7T68WD3~4ZEPYJhfGu~tMaqyr0fI5~liM64qjhwQ~6lAZFEHdIG~dtERGcxCaJ~NXxDJr1D_u~_pwIgrV8TB~K6JjooB-Ba~i83OPEGrYw~xZ6jtQjaj9~IyRhOdNpxG~hRUnVPuHhQ~T_Vcohvd0e~6nVB4KEOkd~LpjxMAWKke~pa4LoD4xuT~-sp-9oh8uv~3Fj87tYV23~jPsOGzt9Dh~CrFcrMFJzz~xx65A0TTF9~ZZltVrbyeV~9KtA1zktoR~BEa426Zwwo~l5WYRzHH5-~RcahSSzeRf~X_1kwTzaXt~qWnxmES0AA~tVKhOXonun~Oa9b6mEc1T~t2ErccCFR9~Pp-MGp6661~tIqkVZurKP~wb0taLpkzz~tAu08Nnfj1~HY55MqmzzQ~K8esoIs2eW~ZtdTKsZ90-~kwQ7-a01CG~qIdR-zIXpb~EQHKvBDRBz~2fTx4fs8a8~UtYtSHHiBE~Qzw2jAbG1b~8GzYAAN3iS~YdbineTqw6~w9iEsfNzZi~AXMsnOI1nG~F5CBR92p_Q~tgP8r-gOe_~0xsDLqCEW6~ZctZZFuFLC~hZE63jY8x5~RAjfbf58gz~eQO7Nnq-Qm~bkSTvfrPKL~7WfWkHyQ76~gAPGpMHZy-~VcfOJjTggL~UR2Fq0zKkQ~g_bA2jnS8D; is_sensei_service_user=1; login_bc=1; _gid=GA1.2.194219766.1582916993; privacy_policy_agreement=1; PHPSESSID=17757078_hcQHewaUVuI6gBuLl88wTigJzX7OsCTk; c_type=25")
            .addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");

            //添加Cookie

    private int curPage=1;

    @Override
    public void process(Page page) {
        //获取总页数
        Json json=page.getJson();
        int imageAmount=Integer.parseInt(json.jsonPath("$..total").toString());
        if(imageAmount%60!=0)
            imageAmount=imageAmount/60+1;
        String url=page.getUrl().toString();
        if(curPage!=imageAmount){
            //将全部请求页加入请求队列
            List<String > requests=new ArrayList<>();
            for(;curPage<=imageAmount;curPage++){
                StringBuilder temp=new StringBuilder(url);
                temp.insert(url.length()-22,"p="+curPage);
                requests.add(temp.toString());
            }
            page.addTargetRequests(requests);
        }
        this.saveImageInfo(page);
    }

    //存储数据
    private void saveImageInfo(Page page) {
        List<ImageInfo> imageInfos=new ArrayList<>();
        ImageInfo temp=new ImageInfo();

        Json json=page.getJson();
        List<Selectable> illustIdList = json.jsonPath("$..illustId").nodes();
        List<Selectable> userIdList = json.jsonPath("$..userId").nodes();
        List<Selectable> tagList = json.jsonPath("$..tags").nodes();
        List<Selectable> titleList = json.jsonPath("$..illustTitle").nodes();
        List<Selectable> userNameList = json.jsonPath("$..userName").nodes();
        for(int i = 0; i< illustIdList.size(); i++){
            temp.setUrl("www.pixiv.net/artworks/"+Long.parseLong(illustIdList.get(i).toString()));
            temp.setId(Long.parseLong(illustIdList.get(i).toString()));
            temp.setAuthor(userNameList.get(i).toString());
            temp.setAuthorId(Long.parseLong(userIdList.get(i).toString()));
            temp.setTag(tagList.get(i).toString());
            temp.setPicName(titleList.get(i).toString());

            imageInfos.add(new ImageInfo(temp));
        }
        page.putField("ImageInfo",imageInfos);
    }

    @Override
    public Site getSite() {
        return site;
    }

    private String getPixivUrl(String[] keyWords){
        String url;
        if(keyWords.length==1)
            url="https://www.pixiv.net/ajax/search/artworks/"+keyWords[0]+"?&s_mode=s_tag&type=all";
        else {
            StringBuilder temp=new StringBuilder();
            for(int i=0;i<keyWords.length;i++){
                if(i!=keyWords.length-1)
                    temp.append(keyWords[i]).append("%20");
                else
                    temp.append(keyWords[i]);
            }
            url="https://www.pixiv.net/ajax/search/artworks/"+temp+"?word="+temp+"&s_mode=s_tag&type=all";
        }
        return url;
    }

    //@Autowired
    //private SpringDataPipeline springDataPipeline;
    //initialDelay当任务启动，等多久执行方法
    //fixedDelay每隔多久执行方法
    //@Scheduled(initialDelay = 1000,fixedDelay = 100000)
    /*public void process(){
        HttpClientDownloader downloader=new HttpClientDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));
        Spider.create(new ImageProcessor())
                .addUrl("https://www.pixiv.net/ajax/search/artworks/%E7%99%BE%E5%90%88?word=%E7%99%BE%E5%90%88&p=2")//"http://www.pixiv.net/tags/"
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .setDownloader(downloader)
                .thread(3)
                .addPipeline(springDataPipeline)
                .run();
    }*/
    @Autowired
    private PixivDownloader pixivDownloader;

    //@Scheduled(initialDelay = 1000,fixedDelay = 100000)
    public void pixivSearchByTags(String[] keyWords) {
        String url=getPixivUrl(keyWords);
        headerUrl=url;
        //设置代理
        HttpClientDownloader downloader=new HttpClientDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));
        Spider.create(new ImageProcessor())
                .addUrl(url)//"https://www.pixiv.net/ajax/search/artworks/%E7%99%BE%E5%90%88?word=%E7%99%BE%E5%90%88&p=2"
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .setDownloader(downloader)
                .addPipeline(pixivDownloader)
                .thread(1)
                .run();
    }
}
