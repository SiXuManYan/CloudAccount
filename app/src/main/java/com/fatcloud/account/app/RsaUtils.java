package com.fatcloud.account.app;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Rsa 工具类
 */
public class RsaUtils {
    private static final String KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAgQ+ysYnxSA6PIGFS7LS7eORHYEqipfYOuejUxNKKWkUw2cyX++Z9dFK58/erWkYz6Z1JkH4X8apVi09JtSyTtwIDAQABAkAcH5iX2XBLfGix7KNOU1/ayxvGntzsfz7cQiFDNoHRg5z3np5tSxoFX9VCySQBs5fNZ4jU4XFvny04Ng/uVh3RAiEAwBiPqodCsJ/ZIACbQvtI98TCK657E1Q/KTpIA0jGGBkCIQCr/u2xtcqDs56SNqXM1Kl+c7S7rWBaQ4bxdXLmVVjETwIhAJ4i2go5JWZ/gN++gBJJCQ2nNW1+SqVj4kcPSn8hpqnpAiBxDIqnN9n4XuNnL0wjKdSOLPcqNHcUXTYhFxWCl65UuQIgCK01C/kUDn7h6xaQc3WnUlKd8sIhVo+imuPcEvjsmX4=";

    /**
     * 解码PrivateKey
     *
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
//            byte[] byteKey = Base64Decoder.decodeToBytes(key);
            byte[] byteKey = Base64.decode(key, Base64.DEFAULT);
            PKCS8EncodedKeySpec x509EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(x509EncodedKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 产生MD5WithRSA签名信息
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String signMd5WithRsa(String plainText) {
        try {
            return sign(plainText.getBytes(), "SHA1WithRSA", getPrivateKey(KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 产生RSA签名信息（指定签名方法）
     *
     * @param plainText
     * @param signatureMethod
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(byte[] plainText, String signatureMethod, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance(signatureMethod);
        sig.initSign(privateKey);
        sig.update(plainText);
        return new String(Base64.encode(sig.sign(), Base64.DEFAULT));
    }

}