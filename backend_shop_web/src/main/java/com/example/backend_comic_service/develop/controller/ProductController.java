package com.example.backend_comic_service.develop.controller;

import com.example.backend_comic_service.develop.model.base_response.BaseListResponseModel;
import com.example.backend_comic_service.develop.model.base_response.BaseResponseModel;
import com.example.backend_comic_service.develop.model.model.ProductModel;
import com.example.backend_comic_service.develop.service.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @PostMapping("/add-or-change")
    public BaseResponseModel<ProductModel> addOrChange(@RequestPart("productModel") String productModel, @RequestPart("files") List<MultipartFile> files) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return productService.addOrChangeProduct(mapper.readValue(productModel, ProductModel.class), files);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @GetMapping("/delete")
    public BaseResponseModel<Integer> delete(@RequestParam(value = "id", required = false) Integer id,
                                             @RequestParam(value = "status", required = false) Integer status) {
        return productService.deleteProduct(id, status);
    }

    @GetMapping("/detail")
    public BaseResponseModel<ProductModel> detail(@RequestParam(value = "id", required = false) Integer id) {
        return productService.getProductById(id);
    }

    @GetMapping("/get-list-product")
    public BaseListResponseModel<List<ProductModel>> getListProduct(@RequestParam(value = "keySearch", required = false) String keySearch,
                                                                    @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                                    @RequestParam(value = "typeId", required = false) Integer typeId,
                                                                    @RequestParam(value = "status", required = false) Integer status,
                                                                    @RequestParam(value = "pageIndex") Integer pageIndex,
                                                                    @RequestParam(value = "pageSize") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return productService.getListProduct(categoryId, typeId, keySearch, status, pageable);
    }

    @GetMapping("/generate-code")
    public BaseResponseModel<String> generateCode() {
        return productService.generateCode();
    }

}
