package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create() // создаем клиента
                .setUserAgent("Kuvin77")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/" +
                "netology-code/jd-homeworks/master/http/task1/cats"); // создаем и куда направляем
        CloseableHttpResponse response = httpClient.execute(request); // отправка запроса и возврат в response
        System.out.println(response.getStatusLine().getStatusCode()); // вывод результата
//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);// вывод полученных заголовков
        String json = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);// в текст файл

        final ObjectMapper objectMapper = new ObjectMapper();
        Cats[] cats = objectMapper.readValue(json, Cats[].class);//2 вариант TODO из файла JSON все получается
        List<Cats> catsList = new ArrayList<>(Arrays.asList(cats));//2 вариант - продолжение

        System.out.println("За этих проголосовали / в порядке уменьшения голосов:");
        catsList.stream()
                .filter(d -> d.getUpvotes() != null && d.getUpvotes() > 0)
                .sorted(Comparator.comparing(Cats::getUpvotes).reversed())
                .forEach(System.out::println);
        System.out.println("-------------------------------------------------");

        System.out.println("За этих не голосовали:");
        catsList.stream()
                .filter(d -> d.getUpvotes() == null)
                .forEach(System.out::println);
    }
}
