package com.itheima;

import cn.hutool.core.util.ObjectUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPGET {


    public static void main(String[] args) throws Exception {
//        -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=10808
        String  url = "https://www.00shu.la/127/127222/53104940.html";
//        String  url = "https://look.twword.com/0725520422/8096_30.html";
        List<String> contentList = new ArrayList<>();

        int count = 0;
        int failTime = 0;

        contentList = extracted(contentList, url, count, failTime);
        if(ObjectUtil.isNotEmpty(contentList)){
            for (int i = 0; i < 100; i++) {
//                System.out.println(contentList.get(i));
            }
        }

        // 文件路径
        String filePath = "C:\\Users\\Administrator\\Desktop\\testLoad9.mrp";

        // 调用方法将 List 写入文件
        writeListToFile(contentList, filePath);

    }

    private static List<String> extracted(List<String> contentList, String url, int count,  int  failTime) throws Exception {
        String  headUrl = "https://www.00shu.la";
        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, ((x509Certificates, s) -> true)).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(100000)  // 连接超时设置为5秒
                    .setSocketTimeout(100000)   // 套接字超时设置为5秒
                    .build();
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setDefaultRequestConfig(requestConfig).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader("User-Agent", "PostmanRuntime/7.37.3");
//            httpGet.setHeader("Transfer-Encoding", "chunked");
//            httpGet.setHeader("Content-Encoding", "br");
//            httpGet.setHeader("Content-Type", "text/html; charset=gb2312");

            CloseableHttpResponse execute = httpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
//            String string = EntityUtils.toString(entity, "gbk");
            String string = EntityUtils.toString(entity, "utf-8");
//            System.out.println(string);


            // 使用Jsoup解析HTML
            Document document = Jsoup.parse(string);

            // 查找指定标签的内容，例如获取所有的<p>标签

            Elements titles = document.select("title");
            for (Element paragraph : titles) {
                String title = paragraph.text();
                title = title.replaceAll("第一卷：默认 ", "");
                title = title.replaceAll("_我法师加奶妈，用七把剑不过份吧-00小说网", "");
                System.out.println(title);

                contentList.add(title);
            }
//
//            Elements paragraphs = document.select("p");
//            for (Element paragraph : paragraphs) {
//                String content = paragraph.text();
//                contentList.add(content);
//            }
//            contentList.add("");





//            Element contentDiv = document.select("div#chaptercontent").first();
            Element contentDiv = document.select("div#content").first();
//            Element contentDiv = document.select("article").first();

            if (contentDiv != null) {
                // 删除包含 "黄易天地" 的 p 标签
                contentDiv.select("p:contains(黄易天地)").remove();
                contentDiv.select("b:contains(最新网址：www.00shu.la)").remove();

                // 删除 class="contnew" 的 p 标签
                contentDiv.select("p.contnew").remove();

                // 删除包含 "章节错误" 的 div
                contentDiv.select("div").remove();


                // 1. 获取原始 HTML 内容
                String rawHtml = contentDiv.html();

                // 2. 处理连续两个 <br> 标签为段落分隔符
                String splitMarker = "##SPLIT##";
                String processedHtml = rawHtml
                        // 匹配连续的 <br> 或 <br/>（允许中间有空格）
                        .replaceAll("(<br\\s*/?>\n?\\s*){2,}", splitMarker)
                        // 处理单个 <br> 为换行符
                        .replaceAll("<br\\s*/?>", "\n");

                // 3. 处理 &nbsp; 和空白字符
                String cleanedText = processedHtml
                        .replaceAll("&nbsp;", " ")  // 替换 &nbsp; 为空格
                        .replaceAll("\\s+", " ")    // 合并连续空格
                        .trim();
                // 4. 按分隔符分割段落
                List<String> paragraphs = Arrays.stream(cleanedText.split(splitMarker))
                        .map(String::trim)          // 清理段落首尾空格
                        .filter(s -> !s.isEmpty())  // 过滤空段落
                        .collect(Collectors.toList());


                contentList.addAll(paragraphs);
                contentList.add("");
//
//                // 5. 输出结果
////                System.out.println("提取段落数: " + paragraphs.size());
////                for (String para : paragraphs) {
////                    System.out.println("【段落】\n" + para + "\n");
////                }
            }

            if(url.contains("59572630")){
//            if(url.contains("53262258")){
                return contentList;
            }
            String href = "";
            Elements paragraphs2 = document.select("a");
            for (Element paragraph : paragraphs2) {
                String text = paragraph.text();
                if("下一章".equals(text)){
                    href = paragraph.attr("href"); // 获取<a>标签的href属性的值
//                    "//www.yssaaj.com/yq/160699/28581735.html";
//                    href = href.replaceAll("//www.yssaaj.com","");
                    System.out.println(href); // 打印href属性的值
                    href = headUrl + href;
//                    href = "https:" + href;
                    break;
                } else if ("下一页".equals(text)){
                    href = paragraph.attr("href"); // 获取<a>标签的href属性的值
                    System.out.println(href); // 打印href属性的值
                    href = headUrl + href;
//                    href = "https:" + href;
                    break;
                }
            }
            count++;
            extracted(contentList, href, count, failTime);
        } catch (Exception e) {

            e.printStackTrace();
            if(count>100){
                return contentList;
            } else {
                failTime++;
                System.out.println(url);
                extracted(contentList, url, count, failTime);

            }

//            throw new Exception(e);

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
