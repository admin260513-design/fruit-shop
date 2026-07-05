package org.example.frusitshopapp.contorller;

import lombok.RequiredArgsConstructor;
import org.example.frusitshopapp.service.CartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;


//    1. 장바구니 추가

//    2. 장바구니 조회

//    3. 장바구니 변경

//    4. 장바귄 삭제

}
