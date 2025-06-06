package com.itheima;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

public class HttpRequestExample {
    public static void main(String[] args) {
        String urlString = "https://ahri8.top/dnew.php?t=1733011200";

        try {

            // 设置 SSLContext，允许跳过证书验证
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCertificates, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            System.setProperty("http.proxyHost", "20.10.45.72");
            System.setProperty("http.proxyPort", "7890");
            // 创建URL对象
            URL url = new URL(urlString);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(50000); // 设置连接超时为5秒
            connection.setReadTimeout(50000);    // 设置读取超时为5秒
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");


             //-DsocksProxyHost=127.0.0.1 -DsocksProxyPort=7890   关键是 添加虚拟机！！！
            // 获取响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);


            // 如果响应成功（HTTP 200）
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 获取输入流并读取返回的数据
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // 逐行读取响应内容
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 输出响应内容
                System.out.println("Response Content: ");
                System.out.println(response.toString());
            } else {
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
