package com.apiforge.content.service;

import com.apiforge.content.repository.DynamicContentRepository;
import com.apiforge.common.exception.CustomExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ContentService {

    @Autowired
    private DynamicContentRepository dynamicContentRepository;

    @Autowired
    private ContentTypeClientService contentTypeClientService;

    public Map<String, Object> createContent(String apiId, Map<String, Object> data) {
        // Validate content type exists
        Map<String, Object> contentType = contentTypeClientService.getContentTypeByApiId(apiId);
        if (contentType == null) {
             throw new CustomExceptions.ResourceNotFoundException("Content type not found: " + apiId);
        }

        // Validate fields against schema (simplified - assume valid for now)
        // Ideally we iterate over contentType['fields'] and check data types and required fields

        String tableName = "ct_" + apiId;
        return dynamicContentRepository.create(tableName, data);
    }

    public List<Map<String, Object>> getAllContent(String apiId) {
        String tableName = "ct_" + apiId;
        // Verify table/content type existence implicitly via catch or explicit check
        // Ideally explicit check via client service
        return dynamicContentRepository.findAll(tableName);
    }

    public Map<String, Object> getContentById(String apiId, Long id) {
        String tableName = "ct_" + apiId;
        Map<String, Object> content = dynamicContentRepository.findById(tableName, id);
        if (content == null) {
            throw new CustomExceptions.ResourceNotFoundException("Content not found");
        }
        return content;
    }

    @Transactional
    public Map<String, Object> updateContent(String apiId, Long id, Map<String, Object> data) {
        // Check existence
        getContentById(apiId, id);
        
        String tableName = "ct_" + apiId;
        return dynamicContentRepository.update(tableName, id, data);
    }

    @Transactional
    public void deleteContent(String apiId, Long id) {
        // Check existence
        getContentById(apiId, id);

        String tableName = "ct_" + apiId;
        dynamicContentRepository.delete(tableName, id);
    }
}
