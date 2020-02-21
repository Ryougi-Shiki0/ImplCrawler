package mycrawler.task;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import mycrawler.service.pixiv.ImageProcessor;

public class MyTask {

    public static void main(String[] args) {
        ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ImageProcessor task= ac.getBean("imageProcessor",ImageProcessor.class);
        String[] keyWords={"fate","30000"};
        task.pixivSearchByTags(keyWords,task);
    }
}
