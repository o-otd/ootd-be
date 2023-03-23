package com.ootd.be.mapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ootd.be.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MusinsaApiService {

    private static final String API_URL = "https://search.musinsa.com/api/search/v2/goods?siteKindId=musinsa&sex=A&viewType=small&size=60&includeSoldOut=true&page=1&sort=POPULAR&originalYn=N&includeUnisex=true&keyword=";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(Feature.IGNORE_UNKNOWN, true);

    public MusinsaApiRes search(String keyword) {

        HttpClient client = HttpClient.newBuilder().build();
        URI uri = getUri(keyword);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            MusinsaApiRes res = OBJECT_MAPPER.readValue(response.body(), MusinsaApiRes.class);
            res.getData().getList().forEach(item -> {
                log.info("{}", item);
            });

            return res;
        } catch (Exception e) {
            throw new ValidationException("검색 실패 : ", e);
        }
    }

    private URI getUri(String keyword) {
        try {
            URI uri = new URI(API_URL + URLEncoder.encode(keyword, StandardCharsets.UTF_8));
            return uri;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
