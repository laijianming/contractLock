package com.ragdoll.uitls;

import com.ragdoll.aesEncript.AesHelper;
import com.ragdoll.constant.ClientConstant;
import com.ragdoll.rsaEncript.RSASign;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 请求工具类
 */
public class HttpClientUtil {

    public static String doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 加验证头
            setHeaderSid(httpGet);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 将文件下载到本地 返回一个输入流
     *
     * @param fileUrl
     * @return
     */
    public static InputStream downloadFile(String fileUrl) {
        try {
            //1、创建实例
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            //2、创建请求
            HttpGet httpGet = new HttpGet(fileUrl);

            httpGet.setHeader("Range", "73482-");
            // 加验证头
            setHeaderSid(httpGet);
            //3、执行
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            //4、获取实体
            HttpEntity httpEntity = closeableHttpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                return inputStream;
            }
            closeableHttpResponse.close();
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPostUploadFile(String url, InputStream is, String fileName) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", is, ContentType.MULTIPART_FORM_DATA, fileName);
            builder.addTextBody("filename", fileName);
            builder.addTextBody("Content-Disposition", fileName);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 加验证头
            setHeaderSid(httpPost);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 给每个请求加上 X-SID 和 X-Signature 请求头，用于验证
     */
    public static void setHeaderSid(HttpRequestBase httpMethod) throws Exception {
        String randomKey = AesHelper.getRandomKey();
        // 加签
        String X_Signature = RSASign.generateRSASignature(ClientConstant.PRI, randomKey);
        // 赋值请求头
        httpMethod.setHeader("X-SID",randomKey);
        httpMethod.setHeader("X-Signature",X_Signature);
    }

}
