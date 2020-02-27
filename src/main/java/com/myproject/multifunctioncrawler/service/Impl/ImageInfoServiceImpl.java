package com.myproject.multifunctioncrawler.service.Impl;

import com.myproject.multifunctioncrawler.dao.ImageInfoDao;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myproject.multifunctioncrawler.pojo.ImageInfo;
import us.codecraft.webmagic.ResultItems;
import java.util.List;

@Service("imageInfoService")
public class ImageInfoServiceImpl implements ImageInfoService {

    @Autowired
    private ImageInfoDao imageInfoDao;

    @Override
    public void savePixivImagesData(ResultItems resultItems) {
        //获取封装好的数据
        List<ImageInfo> imageInfos=resultItems.get("ImageInfo");
        if(!imageInfos.isEmpty()){
            for (ImageInfo imageInfo : imageInfos) {
                imageInfoDao.savePixivImageInfo(imageInfo);
            }
        }
    }

    @Override
    public List<String> searchPixivImageByTags(String tags) {
        String[] temp= tags.split(" ");
        System.out.println("调用searchPixivImageByTags: "+tags);
        if(temp.length==0)  return null;
        if(temp.length==1){
            return imageInfoDao.searchPixivImageByTag("%"+temp[0]+"%");
        }
        else {
            String[] searchTags=new String[3];
            System.arraycopy(temp, 0, searchTags, 0, temp.length);
            return imageInfoDao.searchPixivImageByTags("%"+searchTags[0]+"%","%"+searchTags[1]+"%","%"+searchTags[2]+"%");
        }
    }
}
