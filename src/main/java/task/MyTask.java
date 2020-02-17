package task;

import task.pixiv.ImageProcessor;

public class MyTask {
    public static void main(String[] args) {
        ImageProcessor task=new ImageProcessor();
        String[] keyWords={"百合","10000"};
        task.pixivSearchByTags(keyWords);
    }
}
