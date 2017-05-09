package com.example.henry.digidrop.services;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Evan on 5/8/17.
 */

public class CryptoUtils {
/*
    public static void keyPair(Context context) {
        SharedPreferences shared = context.getSharedPreferences("Context", Context.MODE_PRIVATE);
        String pubKeyString = shared.getString("PublicKey", null);
        String privKeyString = shared.getString("PrivateKey", null);
        SharedPreferences.Editor SPE;
        if (pubKeyString == null && privKeyString == null) {
            try {
                KeyPairGenerator generator;
                generator = KeyPairGenerator.getInstance("RSA", "BC");
                generator.initialize(256, new SecureRandom());
                keyPair = generator.generateKeyPair();
                pubKey = keyPair.getPublic();
                privKey = keyPair.getPrivate();

                byte[] publicKeyBytes = pubKey.getEncoded();
                String pubKeyStr = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
                byte[] privKeyBytes = privKey.getEncoded();
                String privKeyStr = new String(Base64.encode(privKeyBytes, Base64.DEFAULT));
                SPE = shared.edit();
                SPE.putString("PublicKey", pubKeyStr);
                SPE.putString("PrivateKey", privKeyStr);
                SPE.commit();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }

    }
    */

    public static PrivateKey getPvtKeyFromStr(String str) {

        try {
            byte[] keyBytes = Base64.decode(str.getBytes("utf-8"), Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPubKeyFromStr(String str) {

        try {
            byte[] keyBytes = Base64.decode(str.getBytes("utf-8"), Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptMsg(String msg, String pvtKeyStr) {
        PrivateKey privateKey = getPvtKeyFromStr(pvtKeyStr);
        byte[] msgBytes = Base64.decode(msg, Base64.DEFAULT);

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(msgBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String encryptMsg(String msg, String pubKeyStr) {
        PublicKey publicKey = getPubKeyFromStr(pubKeyStr);
        byte[] msgBytes = msg.getBytes();

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(msgBytes), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static KeyPair generateKeys() {
        KeyPair keyPair;
        try {
            KeyPairGenerator generator;
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());

            keyPair = generator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyWrapper convertKeysToString(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        byte[] publicKeyBytes = publicKey.getEncoded();
        String pubKeyStr = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT);
        byte[] privKeyBytes = privateKey.getEncoded();
        String privKeyStr = Base64.encodeToString(privKeyBytes, Base64.DEFAULT);

        return new KeyWrapper(pubKeyStr, privKeyStr);
    }

    public static class KeyWrapper {

        private String pub, pvt;

        public KeyWrapper(String pub, String pvt) {
            KeyWrapper.this.pub = pub;
            KeyWrapper.this.pvt = pvt;
        }

        public String getPub() {
            return pub;
        }

        public String getPvt() {
            return pvt;
        }
    }

}
