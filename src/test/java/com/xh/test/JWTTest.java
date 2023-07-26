package com.xh.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author H.Yang
 * @date 2023/7/25
 */
@Slf4j
@RunWith(SpringRunner.class)
public class JWTTest {

    private static final String SIGN = "1234567890";

    /**
     * 默认 HS265(HmacSHA256)算法
     */
    @Test
    public void demo() {
        String token = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "andrew")
                .setKey(SIGN.getBytes())
                .sign();

        System.out.println(token);

        // 默认验证HS265的算法
        boolean verify = JWT.of(token).setKey(SIGN.getBytes()).verify();
        System.out.println(verify);

        // 验证签名，主要包括：Token是否正确、生效时间不能晚于当前时间、失效时间不能早于当前时间、签发时间不能晚于当前时间
        boolean validate = JWT.of(token).setKey(SIGN.getBytes()).validate(0);
        System.out.println(validate);

        // 打印数据
        //header
        JSONObject headers = JWT.of(token).getHeaders();
        Console.log(headers);
        //payload
        JSONObject payloads = JWT.of(token).getPayloads();
        Console.log(payloads);
    }

    /**
     * 默认 HS265(HmacSHA256)算法
     */
    @Test
    public void demo2() {
        String token = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "andrew")
                .setKey(SIGN.getBytes())
                .sign();

        System.out.println(token);

        JWT jwt = JWTUtil.parseToken(token);

        // 验证算法是否正确
        boolean verifyKey = jwt.setKey(SIGN.getBytes()).verify();
        System.out.println(verifyKey);

        // 验证签名，主要包括：Token是否正确、生效时间不能晚于当前时间、失效时间不能早于当前时间、签发时间不能晚于当前时间
        boolean verifyTime = jwt.validate(0);
        System.out.println(verifyTime);

        // 打印数据
        //header
        JSONObject headers = jwt.getHeaders();
        Console.log(headers);
        //payload
        JSONObject payloads = jwt.getPayloads();
        Console.log(payloads);
    }

    @Test
    public void demo3() {
        JWT jwt = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "andrew")
                .setKey(SIGN.getBytes());

        String token = jwt.sign();
        System.out.println(token);

        jwt = JWTUtil.parseToken(token);

        boolean verifyKey = jwt.setKey(SIGN.getBytes()).verify();
        System.out.println(verifyKey);

        boolean verifyTime = jwt.validate(0);
        System.out.println(verifyTime);
    }

    @Test
    public void demo4() {
        Map<String, Object> map = new HashMap<>();
        map.put("sub", "1234567890");
        map.put("name", "andrew");

        JWT jwt = JWT.create();

        // 设置携带数据
        map.forEach(jwt::setPayload);

        // 设置密钥
        jwt.setKey(SIGN.getBytes());

        String token = jwt.sign();
        System.out.println(token);

        jwt = JWTUtil.parseToken(token);

        boolean verifyKey = jwt.setKey(SIGN.getBytes()).verify();
        System.out.println(verifyKey);

        boolean verifyTime = jwt.validate(0);
        System.out.println(verifyTime);
    }

    @Test
    public void demo5() {
        Map<String, Object> map = new HashMap<>();
        map.put("sub", "1234567890");
        map.put("name", "andrew");

        String token = JWTUtil.createToken(map, SIGN.getBytes());
        System.out.println(token);

        JWT jwt = JWTUtil.parseToken(token);

        boolean verifyKey = jwt.setKey(SIGN.getBytes()).verify();
        System.out.println(verifyKey);

        boolean verifyTime = jwt.validate(0);
        System.out.println(verifyTime);
    }

    @Test
    public void demo6() throws InterruptedException {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.SECOND, 3);

        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", "1234567890");
        payload.put("name", "andrew");
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);

        String token = JWTUtil.createToken(payload, SIGN.getBytes());
        System.out.println(token);

        Thread.sleep(5 * 1000);

        JWT jwt = JWTUtil.parseToken(token);

        boolean verifyKey = jwt.setKey(SIGN.getBytes()).verify();
        System.out.println(verifyKey);

        // 验证签名，主要包括：Token是否正确、生效时间不能晚于当前时间、失效时间不能早于当前时间、签发时间不能晚于当前时间
        boolean verifyTime = jwt.validate(0);
        System.out.println(verifyTime);
    }

    @Test
    public void demo7() throws InterruptedException {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.SECOND, 3);

        JWT jwt = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "andrew")
                .setKey(SIGN.getBytes())
                .setExpiresAt(newTime) //过期时间
                .setIssuedAt(now);//签发时间

        String token = jwt.sign();
        System.out.println(token);

        Thread.sleep(5 * 1000);

        boolean verifyKey = JWTUtil.verify(token, SIGN.getBytes());
        System.out.println(verifyKey);

        // 验证签名，主要包括：Token是否正确、生效时间不能晚于当前时间、失效时间不能早于当前时间、签发时间不能晚于当前时间
        boolean verifyTime = JWT.of(token).validate(0);
        System.out.println(verifyTime);
    }

}
