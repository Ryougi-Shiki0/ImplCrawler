package service.pixiv;

import dao.ImageInfoDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pojo.ImageInfo;
import service.Downloader;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.InputStream;
import java.util.List;

public class PixivDownloader implements Downloader, Pipeline {
    private InputStream in;
    private SqlSession sqlSession;
    private ImageInfoDao imageInfoDao;

    public void init()throws Exception{
        //1.读取配置文件，生成字节输入流
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.获取SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //3.获取SqlSession对象
        sqlSession = factory.openSession();
        //4.获取dao的代理对象
        imageInfoDao = sqlSession.getMapper(ImageInfoDao.class);
    }

    public void destroy()throws Exception{
        //提交事务
        sqlSession.commit();
        //6.释放资源
        sqlSession.close();
        in.close();
    }

    @Override
    public void saveData(ResultItems resultItems) {
        try{
            this.init();
            //获取封装好的数据
            List<ImageInfo> imageInfos=resultItems.get("ImageInfo");
            if(!imageInfos.isEmpty()){
                for (ImageInfo imageInfo : imageInfos) {
                    imageInfoDao.saveImageInfo(imageInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                this.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try{
            this.init();
            //获取封装好的数据
            List<ImageInfo> imageInfos=resultItems.get("ImageInfo");
            if(!imageInfos.isEmpty()){
                for (ImageInfo imageInfo : imageInfos) {
                    imageInfoDao.saveImageInfo(imageInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                this.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
