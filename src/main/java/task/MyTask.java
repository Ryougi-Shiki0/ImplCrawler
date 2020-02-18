package task;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.pixiv.ImageProcessor;

public class MyTask {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("Application.xml");
        ImageProcessor task=applicationContext.getBean("imageProcessor",ImageProcessor.class);
        String[] keyWords={"fate","10000"};
        task.pixivSearchByTags(keyWords);
    }
}
