package main.managers.http.client;

import main.managers.exeption.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTasksClient {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8056;
    private final String url;
    private String token;

    public KVTasksClient() {

        url = "http://localhost:" + PORT;
        token = registerAPIToken(url);

    }

    private String registerAPIToken(String url) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {

            token = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        } catch (InterruptedException | IOException e) {

            throw new ManagerSaveException("Регистрация API токена закончилась неудачно. Причина:", e);

        }

        return token;

    }

    public void put(String key, String json) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET))
                .build();

        try {

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {

            throw new ManagerSaveException("Сохранение закончилось неудачно. Причина:", e);

        }

    }

    public String load(String key) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response ;

        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {

            throw new ManagerSaveException("Загрузка закончилась неудачно. Причина:", e);

        }

        return response.body();

    }
}
