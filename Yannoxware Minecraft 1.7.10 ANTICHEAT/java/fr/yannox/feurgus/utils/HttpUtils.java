package fr.yannox.feurgus.utils;

import java.net.InetAddress;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive exception aggregation
     */
    public static String get(InetAddress localAddress) {
        try {
            RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).setLocalAddress(localAddress).build();
            HttpGet httpGet = new HttpGet("https://ifconfig.me/ip");
            httpGet.setConfig(config);
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().disableAutomaticRetries().build();){
                String string;
                CloseableHttpResponse response = httpClient.execute((HttpUriRequest)httpGet);
                if (response.getStatusLine().getStatusCode() >= 400) {
                    String string2 = null;
                    return string2;
                }
                try {
                    String body;
                    string = body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
                }
                catch (Throwable throwable) {
                    response.close();
                    throw throwable;
                }
                response.close();
                return string;
            }
        }
        catch (Exception error) {
            return null;
        }
    }
}
