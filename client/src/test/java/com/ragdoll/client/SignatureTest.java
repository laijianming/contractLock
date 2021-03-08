package com.ragdoll.client;

import com.ragdoll.constant.ClientConstant;
import com.ragdoll.rsaEncript.RSAEncrypt;
import com.ragdoll.rsaEncript.RSASign;
import org.junit.jupiter.api.Test;

/**
 * @author jianming
 * @create 2021-03-09-1:28
 */
public class SignatureTest {

    /**
     * jianming
     */
    @Test
    public void test() throws Exception {

        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeYKvm3YxzFOlrzGEytmgCKcXNhVX+mbp687D01rAiO" +
                "G3bjozGGsRqS7mpRnhj6KdyWj79iVz0BNkfW7hyOmTc12XaxM779TQVNiVlWn7u8hv6mCRb+MMOzNfxRhAqfAHVPqi+a7n1Kl4E1Q0i1dm" +
                "WsD4miKQmmFsBCebXSMqrEQIDAQAB";

        // 测试私钥加签，公钥验证
        String str = "abcd";
        // 私钥加签
        String encrypt = RSASign.generateRSASignature(ClientConstant.PRI, str);
        System.out.println("私钥加签后的值 = " + encrypt);
        // 公钥验签
        boolean decrypt = RSASign.verify(pub, encrypt, str);
        System.out.println("公钥验签 = " + decrypt);

    }



}
