package com.xh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author H.Yang
 * @date 2023/7/25
 */
@RestController
public class JWTController {

    /**
     * 需要有token
     *
     * @return
     */
    @GetMapping("/authTest")
    public String authTest() {

        return "auth test.";
    }

    /**
     * 没有token也能正常返回
     *
     * @return
     */
    @GetMapping("/noAuthTest")
    public String noAuthTest() {

        return "no auth test.";
    }


}
