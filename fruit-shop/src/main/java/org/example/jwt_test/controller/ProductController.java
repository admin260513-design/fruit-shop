package org.example.jwt_test.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jwt_test.dto.ProductDto;
import org.example.jwt_test.entity.Product;
import org.example.jwt_test.security.CustomUserDetails;
import org.example.jwt_test.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // 상품 등록 - 로그인한 사람만 가능
    @PostMapping
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @Valid @RequestBody ProductDto dto){
        Product product = productService.creatProduct(
                userDetails.getUsername(),
                dto.getProductName(), dto.getStock(), dto.getPrice(), dto.getDece(), dto.getImg_url()
        );
        return ResponseEntity.ok(product);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.findAllProduct());
    }

    // 상품 하나 조회 (id로)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    // 상품명으로 조회 (productName)
    @GetMapping("/{productname}/")
    public ResponseEntity<?> getProduct(@PathVariable String productname){
        return ResponseEntity.ok(productService.getProductName(productname));
    }

    // 상품 판매 종료
    @PatchMapping("/{id}/end-sale")
    public ResponseEntity<?> endSale(@PathVariable Long id){
        return ResponseEntity.ok(productService.enSale(id));
    }

    // 상품 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}