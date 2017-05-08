package com.example.henry.digidrop.services;

import android.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

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

    public static String encryptMsg(String msg, String pubkey) {
        return null;
    }

    public static KeyPair generateKeys() {
        KeyPair keyPair;
        try {
            KeyPairGenerator generator;
            generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(2048, new SecureRandom());

            keyPair = generator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyWrapper convertKeysToString(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        byte[] publicKeyBytes = publicKey.getEncoded();
        String pubKeyStr = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
        byte[] privKeyBytes = privateKey.getEncoded();
        String privKeyStr = new String(Base64.encode(privKeyBytes, Base64.DEFAULT));

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
