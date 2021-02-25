package com.boot.yuncourier.util;

public interface RSAEncryptUtil {
    String encrypt(String str, String publicKey) throws Exception;
    String decrypt(String str, String privateKey) throws Exception;
}
