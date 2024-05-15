package top.anets.utils;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ftm
 * @date 2024-05-09 14:51
 * 与 RestTemplate 相比，WebClient 有如下优势：
 *
 * 非阻塞，Reactive 的，并支持更高的并发性和更少的硬件资源。
 * 提供利用 Java 8 lambdas 的函数 API。
 * 支持同步和异步方案。
 * 支持从服务器向上或向下流式传输。
 * https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html#webflux-client-builder-reactor-timeout
 */
public class WebClientUtil {

    private   static HttpClient httpClient = HttpClient.create().tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
            .doOnConnected(
                    conn -> conn.addHandlerLast(new ReadTimeoutHandler(10))//读取超时发生在一定时间内没有读取数据
                            .addHandlerLast(new WriteTimeoutHandler(10)))//写入超时发生在写入操作无法在特定时间内完成
    );

    private   static  WebClient webClient  = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();








    public static WebClient getWebClient(){
        return WebClient.builder()
                .build();
    }

    /**
     * get方法
     * @param url
     * @param responseType
     * @return
     * @param
     */
    public static  <T> T get(String url, Class<T> responseType) {
        return webClient.get().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(responseType).
//                timeout(Duration.ofSeconds(5)).
                block();
    }


    public static <T> T get(String url, Map<String, Object> params, Class<T> responseType) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        return get(uriBuilder.toUriString(), responseType);
    }

    /**
     * get多条数据
     * @param url
     * @param responseType
     * @return
     * @param <T>
     */
    public static <T> List<T> list(String url, Class<T> responseType){
        return webClient.get().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToFlux(responseType).collectList().block();
    }

    /**
     * post方法
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @param
     */
    public static <T> T post(String url, Object requestBody, Class<T> responseType) {
        return webClient.post().
                uri(url).
                contentType(MediaType.APPLICATION_JSON).
                bodyValue(requestBody).
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(responseType).
                block();
    }

    /**
     * put方法
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @param
     */
    public static <T> T put(String url, Object requestBody, Class<T> responseType){
        return webClient.put().
                uri(url).
                contentType(MediaType.APPLICATION_JSON).
                bodyValue(requestBody).
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(responseType).
                block();
    }

    /**
     * 删除方法
     * @param url
     * @param responseType
     * @return
     * @param
     */
    public static <T> T delete(String url, Class<T> responseType){
        return webClient.delete().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(responseType).
                block();
    }


}