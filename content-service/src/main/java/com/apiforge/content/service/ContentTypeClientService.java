package com.apiforge.content.service;

import com.apiforge.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class ContentTypeClientService {

    @Value("${content-type-service.url}")
    private String contentTypeServiceUrl;

    private final WebClient webClient;

    public ContentTypeClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getContentTypeByApiId(String apiId) {
        // This is a simplified call - ideally we should map strongly typed DTOs
        // But for flexibility we are using Map (representing JSON) since this service
        // doesn't have direct access to ContentTypeDto classes unless we move them to common.
        // For now, let's assume we get the raw JSON map.
        
        // We'll trust the response is ApiResponse wrapper
        // The return type of the API is ApiResponse<ContentTypeDto>
        
        ApiResponse response = webClient.get()
                .uri(contentTypeServiceUrl + "/api/content-types/api-id/" + apiId)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();

        if (response != null && response.isSuccess()) {
             // We need to extract data, which is a LinkedHashMap typically when using Jackson
             return (Map<String, Object>) response.getData();
        }
        
        return null;
    }
}
