package service.Impl;

import dao.ImageInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.ImageInfo;

import java.util.List;

//@Service
public class ImageInfoService implements service.ImageInfoService {

    //@Autowired
    private ImageInfoDao imageInfoDao;

    @Override
    //@Transactional
    public void save(ImageInfo imageInfo) {
        //根据url查询数据
        ImageInfo param=new ImageInfo();
        param.setUrl(imageInfo.getUrl());

        //执行查询
        List<ImageInfo> list=this.findImage(param);

        //判断查询数据是否为空
        if(list.isEmpty()){
            //若查询数据为空，则表示不存在，或已更新，则更新数据库
            //this.imageInfoDao.saveAndFlush(imageInfo);
        }
        //
    }

    @Override
    public List<ImageInfo> findImage(ImageInfo imageInfo) {
        //设置查询条件
        Example<ImageInfo> example=Example.of(imageInfo);
        //执行查询
        //return this.imageInfoDao.findAll(example);
        return null;
    }
}
