package com.ragdoll.aesEncript;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author jianming
 * @create 2021-03-08-20:26
 */
public class AesHelper {

    // 指定加密key
    private static String RANDOM_KEY="erdws32wq43e4r5t6fdszxdcvgbhjijkiu8iu9oplikjuyteq1qazxcbnmkbcdxs";

    // 获取默认加密cipher
    public static Cipher getEncryptCipher() throws Exception{
        return getCipher(Cipher.ENCRYPT_MODE);
    }
    // 获取默认解密cipher
    public static Cipher getDecryptCipher() throws Exception{
        return getCipher(Cipher.DECRYPT_MODE);
    }
    public static Cipher getCipher(int model) throws Exception{
        //1.获取加密生成器
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        //2.根据ecnodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        keygen.init(128, new SecureRandom(RANDOM_KEY.getBytes()));
        //3.产生原始对称密钥
        SecretKey original_key=keygen.generateKey();
        //4.获得原始对称密钥的字节数组
        byte [] raw=original_key.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key=new SecretKeySpec(raw, "AES");
        //6.根据指定算法AES自成密码器
        Cipher cipher=Cipher.getInstance("AES");
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(model, key);
        return cipher;
    }
}