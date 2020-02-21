package mycrawler.service;


import mycrawler.pojo.ImageInfo;
import us.codecraft.webmagic.ResultItems;


public interface ImageInfoService {

    void savePixivImagesData(ResultItems resultItems);
}
