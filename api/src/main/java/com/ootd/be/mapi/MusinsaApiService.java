package com.ootd.be.mapi;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ootd.be.mapi.MusinsaDto.Category;
import com.ootd.be.mapi.MusinsaDto.Goods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusinsaApiService {

    private static final String API_URL = "https://search.musinsa.com/api/search/v2/goods";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(Feature.IGNORE_UNKNOWN, true);

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    public List<Goods> search(String keyword, Category category) {
        return category.categoryCodes.stream().map(code -> {
                           try {
                               UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(API_URL);
                               MusinsaDto.DefaultParameter.toParameters.accept(uriComponentsBuilder);
                               uriComponentsBuilder.queryParam("keyword", keyword);
                               uriComponentsBuilder.queryParam("category1DepthCode", code.code);

                               return request.apply(uriComponentsBuilder);
                           } catch (Exception e) {
                               log.error("{}", e);
                               return null;
                           }
                       }).filter(res -> res != null && res.getData() != null && !CollectionUtils.isEmpty(res.getData().getList()))
                                     .map(res -> res.getData().getList())
                                     .flatMap(Collection::stream)
                                     .collect(Collectors.toList());
    }

    private Function<UriComponentsBuilder, MusinsaApiRes> request = (builder) -> {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(builder.build().toUri()).build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            return OBJECT_MAPPER.readValue(response.body(), MusinsaApiRes.class);
        } catch (Exception e) {
            log.error("{}", e);
            return new MusinsaApiRes();
        }
    };

}
