package mycrawler.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Login {
    private static HttpGet get;
    private static HttpPost post;
    private static CookieStore cookieStore;
    private static CloseableHttpResponse response;

    /**
     * 登录前的预备方法，用于获取登录时的动态参数：post_key
     *
     * @return
     * */

    private static String preLogin() {
        String post_keyStr = "";
        get = new HttpGet("https://accounts.pixiv.net/login?lang=en&source=pc&view_type=page&ref=wwwtop_accounts_index");
        HttpHost proxy=new HttpHost("127.0.0.1",1080);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(3000)
                .build();
        get.setConfig(requestConfig);
        try (
                //创建client时传入一个cookieStore
                CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build()
        ) {
            response = client.execute(get);
            String responseContent = EntityUtils.toString(response.getEntity());

            //解析返回的网页，获取到post_key
            Document doc = Jsoup.parse(responseContent);
            Element post_key = doc.select("input[name=post_key]").get(0);
            post_keyStr = post_key.attr("value");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return post_keyStr;
    }

    /**
     * 登录
     *
     */
    public static void login() {
        String post_keyStr = preLogin();
        post = new HttpPost("https://accounts.pixiv.net/api/login?lang=en");
        HttpHost proxy=new HttpHost("127.0.0.1",1080);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(3000)
                .build();
        post.setConfig(requestConfig);
        try (
                //创建client时传入一个cookieStore
                CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build()
        ) {

            //准备参数
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("pixiv_id", "17757078"));
            params.add(new BasicNameValuePair("password", "1q2w3e4r"));
            params.add(new BasicNameValuePair("post_key", post_keyStr));
            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            response = client.execute(post);
            String responseContent = EntityUtils.toString(response.getEntity());
            //解析返回的json
            JSONObject responseJson = JSON.parseObject(responseContent);
            JSONObject responseJsonBody = JSON.parseObject(responseJson.get("body").toString());
            if (responseJsonBody.containsKey("success")) {
                System.out.println("登录成功");
            } else {
                throw new Exception("登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
