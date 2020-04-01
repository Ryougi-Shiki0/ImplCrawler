package com.myproject.multifunctioncrawler.service.bilibili;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DanmuPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        Path danmuFilePath=Paths.get("Danmu.txt");
        try {
            if (!Files.exists(danmuFilePath)) {
                Files.createFile(danmuFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(danmuFilePath)) {
            List<String> list = resultItems.get("result");
            for (String s : list) {
                writer.write(s);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
