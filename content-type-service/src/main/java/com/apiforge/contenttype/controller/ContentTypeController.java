package com.apiforge.contenttype.controller;

import com.apiforge.contenttype.dto.ContentTypeDto;
import com.apiforge.contenttype.service.ContentTypeService;
import com.apiforge.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content-types")
public class ContentTypeController {

    @Autowired
    private ContentTypeService contentTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContentTypeDto>> createContentType(@RequestBody ContentTypeDto dto) {
        ContentTypeDto created = contentTypeService.createContentType(dto);
        return ResponseEntity.ok(ApiResponse.success("Content type created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContentTypeDto>>> getAllContentTypes() {
        List<ContentTypeDto> contentTypes = contentTypeService.getAllContentTypes();
        return ResponseEntity.ok(ApiResponse.success(contentTypes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentTypeDto>> getContentTypeById(@PathVariable Long id) {
        ContentTypeDto contentType = contentTypeService.getContentTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(contentType));
    }

    @GetMapping("/api-id/{apiId}")
    public ResponseEntity<ApiResponse<ContentTypeDto>> getContentTypeByApiId(@PathVariable String apiId) {
        ContentTypeDto contentType = contentTypeService.getContentTypeByApiId(apiId);
        return ResponseEntity.ok(ApiResponse.success(contentType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentTypeDto>> updateContentType(
            @PathVariable Long id,
            @RequestBody ContentTypeDto dto) {
        ContentTypeDto updated = contentTypeService.updateContentType(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Content type updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContentType(@PathVariable Long id) {
        contentTypeService.deleteContentType(id);
        return ResponseEntity.ok(ApiResponse.success("Content type deleted successfully", null));
    }
}
