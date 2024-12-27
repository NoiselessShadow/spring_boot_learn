package com.itheima;

import cn.hutool.core.util.ObjectUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPGET {


    public static void main(String[] args) throws Exception {
        String  url = "https://www.5ccc.org/xiaoshuo/6567263/114428445.html";
        List<String> contentList = new ArrayList<>();

        int count = 0;
        contentList = extracted(contentList, url);
        if(ObjectUtil.isNotEmpty(contentList)){
            for (int i = 0; i < 100; i++) {
//                System.out.println(contentList.get(i));
            }
        }

        // 文件路径
        String filePath = "C:\\Users\\Administrator\\Desktop\\testLoad.txt";

        // 调用方法将 List 写入文件
        writeListToFile(contentList, filePath);

    }

    private static List<String> extracted(List<String> contentList, String url) throws Exception {
        String  headUrl = "https://www.5ccc.org";
        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, ((x509Certificates, s) -> true)).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse execute = httpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            String string = EntityUtils.toString(entity);
//            System.out.println(string);


            // 使用Jsoup解析HTML
            Document document = Jsoup.parse(string);

            // 查找指定标签的内容，例如获取所有的<p>标签

            Elements titles = document.select("h3");
            for (Element paragraph : titles) {
                String title = paragraph.text();
                contentList.add(title);
            }

            Elements paragraphs = document.select("p");
            for (Element paragraph : paragraphs) {
                String content = paragraph.text();
                contentList.add(content);
            }
            String href = "";
            Elements paragraphs2 = document.select("a");
            for (Element paragraph : paragraphs2) {
                String text = paragraph.text();
                if("下一章".equals(text)){
                    href = paragraph.attr("href"); // 获取<a>标签的href属性的值
                    System.out.println(href); // 打印href属性的值
                    href = headUrl + href;
                    break;
                }
            }

            if(!href.contains("114429431")){
                extracted(contentList, href);
            } else {
                return contentList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return contentList;
    }



    public static void writeListToFile(List<String> list, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();  // 添加换行符
            }
            System.out.println("列表已成功写入文件: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
