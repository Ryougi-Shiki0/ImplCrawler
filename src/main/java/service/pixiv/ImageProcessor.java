package service.pixiv;

import org.springframework.stereotype.Component;
import pojo.ImageInfo;
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
    private Site site=Site.me()
            .setCharset("utf8")
            .setTimeOut(10000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3)
            //添加请求头，有些网站会根据请求头判断该请求是由浏览器发起还是由爬虫发起的
            .addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .addHeader("Accept-Encoding", "gzip, deflate, br").addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Connection", "keep-alive").addHeader("Referer", "https://accounts.pixiv.net/login?return_to=https%3A%2F%2Fwww.pixiv.net%2F&lang=zh&source=pc&view_type=page");
            //添加Cookie

    List<Selectable> illustIdList;
    List<Selectable> userIdList;
    List<Selectable> tagList;
    List<Selectable> titleList;
    List<Selectable> userNameList;
    int curPage=1;

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
            if(url.contains("&s_mode=s_tag")){
                for(;curPage<=imageAmount;curPage++){
                    StringBuilder temp=new StringBuilder(url);
                    temp.insert(url.length()-13,"&p="+curPage);
                    requests.add(temp.toString());
                }
            }
            else {
                for(;curPage<=imageAmount;curPage++){
                    requests.add(url + "&p="+curPage);
                }
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
        illustIdList= json.jsonPath("$..illustId").nodes();
        userIdList= json.jsonPath("$..userId").nodes();
        tagList= json.jsonPath("$..tags").nodes();
        titleList= json.jsonPath("$..illustTitle").nodes();
        userNameList= json.jsonPath("$..userName").nodes();
        for(int i=0;i<illustIdList.size();i++){
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
        String url="";
        if(keyWords.length==1)
            url="https://www.pixiv.net/ajax/search/artworks/"+keyWords[0];
        else {
            StringBuilder temp=new StringBuilder();
            for(int i=0;i<keyWords.length;i++){
                if(i!=keyWords.length-1)
                    temp.append(keyWords[i]).append("%20");
                else
                    temp.append(keyWords[i]);
            }
            url="https://www.pixiv.net/ajax/search/artworks/"+temp+"?word="+temp+"&s_mode=s_tag";
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


    public void pixivSearchByTags(String[] keyWords) {
        String url=getPixivUrl(keyWords);
        //设置代理
        HttpClientDownloader downloader=new HttpClientDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));

        Spider.create(new ImageProcessor())
                .addUrl(url)//"https://www.pixiv.net/ajax/search/artworks/%E7%99%BE%E5%90%88?word=%E7%99%BE%E5%90%88&p=2"
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .setDownloader(downloader)
                .addPipeline(new PixivDownloader())
                .thread(1)
                .run();
    }
}
