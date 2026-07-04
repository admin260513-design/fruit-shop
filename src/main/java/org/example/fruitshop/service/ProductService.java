package org.example.fruitshop.service;

import lombok.RequiredArgsConstructor;
import org.example.fruitshop.entity.Product;
import org.example.fruitshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 등록, 조회, 수정, 저장(완료), 삭제

    // 1. 과일 등록
    // 판매자, 과일명, 수량, 가격으로 등록
    // 상품 등록일
    public Product createdProduct(String username, String productName, int stock, int price, String imgUrl){
        Product product = Product.builder()
                .userName(username)
                .productName(productName)
                .stock(1)
                .price(1)
                .createdAt(LocalDateTime.now())
                .imgUrl(imgUrl)
                .build();

        return productRepository.save(product);
    }

//    2.상품 전체 조회
//    - Repository에서 전체 상품 가져오기
//    전체조회 = List<Product> 반환 / 아무것도 안 받기 / findAll()
    public List<Product> findAllProduct(){
        return productRepository.findAll();
    }

    //3. 상품 단건 조회
    //- id로 상품 찾기
    //- 없으면 예외 처리
    //단건조회 = 상품 반환 / id 받기 / findById()
    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
    }

    //4. 상품 수정
    //- id로 기존 상품 찾기
    //- title, content 수정
    //- 없으면 예외 처리
    // 수정 = 상품 반환 / findById 후 값 변경
    public Product updatepProduct(Long id, String username, String productname, int stock, int price, String imgurl){
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        product.setProductName(productname);
        product.setUserName(username);
        product.setStock(stock);
        product.setPrice(price);
        product.setImgUrl(imgurl);

        return productRepository.save(product);
    }

    //6. 상품 삭제
    //- id가 존재하는지 확인
    //- 있으면 삭제
    //- 없으면 예외 처리
    // 삭제 = void 반환 / id 받기 / deleteById()
    public void deleteById(Long id){
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }
        productRepository.deleteById(id);
    }

}
