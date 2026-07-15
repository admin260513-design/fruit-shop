package org.example.jwt_test.service;

import lombok.RequiredArgsConstructor;
import org.example.jwt_test.entity.Product;
import org.example.jwt_test.entity.ProductStatus;
import org.example.jwt_test.entity.User;
import org.example.jwt_test.repository.ProductRepository;
import org.example.jwt_test.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 등록, 조회, 수정, 저장(완료), 삭제

    public Product creatProduct(String username, String proudctName, int stock, int price, String dece, String img_url){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = Product.builder()
                .user(user)
                .productName(proudctName)
                .stock(stock)
                .price(price)
                .img_url(img_url)
                .dece(dece)
                .build();
        return productRepository.save(product);
    }

    public List<Product> findAllProduct(){
        return productRepository.findAll();
    }

    // 상품 ID로 조회
    public Product getProduct(Long id){
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("등록 상품을 찾을 수 없습니다."));
    }

    // 상품명으로 조회
    public Product getProductName(String productName){
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new IllegalArgumentException("등록 상품을 찾을 수 없습니다."));
    }

    // 상품 판매 종료
    public Product enSale(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        product.setProductStatus(ProductStatus.ended);

        return productRepository.save(product);

    }

    // 상품 삭제
    public void deleteById(Long id){
        if(!productRepository.existsById(id)){
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }
        productRepository.deleteById(id);
    }

}

