package task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.ImageInfo;
import service.ImageInfoService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private ImageInfoService imageInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的数据
        List<ImageInfo> imageInfos=resultItems.get("ImageInfo");
        if(!imageInfos.isEmpty()){
            for(int i=0;i<imageInfos.size();i++){
                this.imageInfoService.save(imageInfos.get(i));
            }
        }
    }
}
