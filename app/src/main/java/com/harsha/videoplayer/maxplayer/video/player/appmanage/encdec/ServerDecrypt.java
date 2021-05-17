package com.harsha.videoplayer.maxplayer.video.player.appmanage.encdec;

import android.util.Base64;

import com.bumptech.glide.load.Key;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ServerDecrypt {
    private static final String ALGORITHM = "RSA";
    private static final String RSA_PUBLICE = "";

    private static PublicKey getPublicKeyFromX509(String str, String str2) throws Exception {
        return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(Base64.decode(str2, 0)));
    }

    public static String decryptByPublic(String str) {
        byte[] bArr;
        try {
            PublicKey publicKeyFromX509 = getPublicKeyFromX509(ALGORITHM, new String(Base64.decode(VideoPlayerManager.getInstance().getToken(), 0)));
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(2, publicKeyFromX509);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(str, 0));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[128];
            while (true) {
                int read = byteArrayInputStream.read(bArr2);
                if (read == -1) {
                    return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
                }
                if (128 == read) {
                    bArr = bArr2;
                } else {
                    bArr = new byte[read];
                    for (int i = 0; i < read; i++) {
                        bArr[i] = bArr2[i];
                    }
                }
                byteArrayOutputStream.write(instance.doFinal(bArr));
            }
        } catch (Exception unused) {
            return null;
        }
    }

    public static String encryptByPublic(String str) {
        try {
            PublicKey publicKeyFromX509 = getPublicKeyFromX509(ALGORITHM, new String(Base64.decode(VideoPlayerManager.getInstance().getToken(), 0)));
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(1, publicKeyFromX509);
            return new String(Base64.encode(instance.doFinal(str.getBytes(Key.STRING_CHARSET_NAME)), 0)).replace("\n", "");
        } catch (Exception unused) {
            return null;
        }
    }

    private static Cipher cipher(int i, String str) throws Exception {
        if (str.length() == 32) {
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(i, new SecretKeySpec(str.getBytes(), "AES"), new IvParameterSpec(str.substring(0, 16).getBytes()));
            return instance;
        }
        throw new RuntimeException("SecretKey length is not 32 chars");
    }

    public static String encrypt(String str, String str2) {
        try {
            return new String(Base64.encode(cipher(1, str2).doFinal(str.getBytes(Key.STRING_CHARSET_NAME)), 0));
        } catch (Exception unused) {
            return null;
        }
    }

    public static String decrypt(String str, String str2) {
        try {
            return new String(cipher(2, str2).doFinal(Base64.decode(str.getBytes(), 0)), Key.STRING_CHARSET_NAME);
        } catch (Exception unused) {
            return null;
        }
    }
}
