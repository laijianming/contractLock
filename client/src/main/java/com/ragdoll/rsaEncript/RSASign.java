package com.ragdoll.rsaEncript;

import org.apache.commons.codec.binary.Hex;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 验签类
 * @author jianming
 * @create 2021-03-09-1:43
 */
public class RSASign {


    /**
     *  生成RSA公、私钥对
     * */
    public static Map<String, String> generateRSAKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e){
            throw e ;
        }
        keyPairGenerator.initialize(512);
        //秘钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        PublicKey publicKey = keyPair.getPublic();
        //私钥
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyStr = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyStr = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());

        Map<String, String> map = new HashMap();
        map.put("PRIVATE_KEY", privateKeyStr);
        map.put("PUBLIC_KEY", publicKeyStr);
        return map;
    }

    /**
     * 进行签名
     * @param  base64RsaPrivateKey 被进行BASE64编码的RAS算法生成的私钥
     * @param  data 要被签名的数据
     * */
    public static String generateRSASignature(String base64RsaPrivateKey,String data) throws Exception {
        byte[] bytes = java.util.Base64.getDecoder().decode(base64RsaPrivateKey) ;
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));
        //生成签名
        byte[] result = signature.sign();
        return  Hex.encodeHexString(result) ;
    }

    /**
     * 对签名进行验证
     * @param  base64RsaPublicKey 被进行BASE64编码的RAS算法生成的公钥
     * @param  hexSign   十六进制编码后的签名
     * @param  signData 被签名的数据
     * */
    public static boolean verify(String base64RsaPublicKey,String hexSign,String signData) throws Exception {
        byte[] bytes = java.util.Base64.getDecoder().decode(base64RsaPublicKey) ;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory  = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(signData.getBytes("UTF-8"));
        return signature.verify(Hex.decodeHex(hexSign.toCharArray())) ;
    }


}
