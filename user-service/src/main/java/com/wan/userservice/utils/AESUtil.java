package com.wan.userservice.utils;

import cn.hutool.core.util.StrUtil;
import com.wan.commonservice.exception.ArgumentNullException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class AESUtil {

    // 定义加密算法名称
    private static final String ALGORITHM = "AES";
    // 定义加密变换模式，包括算法、操作模式、填充方式
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    // 定义保存密钥的文件名称
    private static final String KEY_FILE = "secretKey.key";
    // 定义保存初始化向量的文件名称
    private static final String IV_FILE = "iv.key";
    private static final Map<String, byte[]> keyMap = new HashMap<>();

    static {
        try {
            loadKeyAndIV();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用AES密钥对内容进行加密
     *
     * @param content 待加密的内容
     * @return 加密后的字符串
     * @throws NoSuchPaddingException 如果密码器无法使用指定的填充方式
     * @throws IllegalBlockSizeException 如果块密码所需的块大小不正确
     * @throws NoSuchAlgorithmException 如果无法使用指定的算法
     * @throws InvalidKeySpecException 如果密钥规范无效
     * @throws BadPaddingException 如果填充方式不正确
     * @throws InvalidKeyException 如果密钥无效
     * @throws InvalidAlgorithmParameterException 如果算法参数无效
     */
    public static String encryption(String content) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        // 检查内容是否为空，如果为空则抛出异常
        if (StrUtil.isBlank(content)) {
            throw new ArgumentNullException("需要的加密内容不能为空");
        }
        // 使用AES密钥对内容进行加密
        return Encryption(content, keyMap.get("secretKey"), keyMap.get("iv"));
    }

    /**
     * 使用AES密钥对内容进行加密
     *
     * @param content 需要加密的内容
     * @param secretKey AES密钥
     * @param iv 初始化向量
     * @return 加密后的内容，经过Base64编码
     * @throws NoSuchAlgorithmException 如果无法找到指定的算法
     * @throws InvalidKeySpecException 如果密钥的规格无效
     * @throws NoSuchPaddingException 如果加密器无法使用指定的填充方式
     * @throws InvalidKeyException 如果密钥无效
     * @throws BadPaddingException 如果填充方式错误
     * @throws IllegalBlockSizeException 如果数据块的大小不合法
     * @throws InvalidAlgorithmParameterException 如果算法参数无效
     */
    private static String Encryption(String content, byte[] secretKey, byte[] iv) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        // 创建SecretKeySpec对象，使用AES算法
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, ALGORITHM);
        // 创建IvParameterSpec对象，使用初始化向量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        // 创建Cipher对象，使用AES/CBC/PKCS5Padding算法
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        // 初始化Cipher对象为加密模式，并传入密钥和初始化向量
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        // 使用Cipher对象对内容进行加密，并将加密后的字节数组转换为Base64编码的字符串
        // 返回加密后的字符串
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密内容
     *
     * @param content 待解密的内容，不能为空
     * @return 解密后的字符串
     * @throws Exception 如果解密过程中发生错误或内容为空，则抛出异常
     */
    public static String decryption(String content) throws Exception {
        // 检查输入内容是否为空，如果为空则抛出异常
        if (StrUtil.isBlank(content)) {
            throw new ArgumentNullException("需要的解密内容不能为空");
        }
        // 使用AES密钥对内容进行解密并返回结果
        return Decryption(content, keyMap.get("secretKey"), keyMap.get("iv"));
    }

    /**
     * 使用AES算法进行解密
     *
     * @param content 需要解密的内容，经过Base64编码
     * @param secretKey AES密钥
     * @param iv 初始化向量
     * @return 解密后的字符串
     * @throws Exception 如果解密过程中发生错误，则抛出异常
     */
    private static String Decryption(String content, byte[] secretKey, byte[] iv) throws Exception {
        // 创建SecretKeySpec对象，使用AES算法
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, ALGORITHM);
        // 创建IvParameterSpec对象，使用初始化向量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        // 创建Cipher对象，使用AES/CBC/PKCS5Padding算法
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        // 初始化Cipher对象为解密模式，并传入密钥和初始化向量
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 将Base64编码的内容转换为字节数组
        byte[] contentBytes = Base64.decodeBase64(content);

        // 使用Cipher对象对内容进行解密，并返回解密后的字符串
        return new String(cipher.doFinal(contentBytes), StandardCharsets.UTF_8);
    }

    /**
     * 加载AES密钥和初始化向量
     * 此方法首先检查指定文件路径上的密钥和IV文件是否存在，如果不存在，则生成新的密钥和IV并保存
     * 如果密钥文件存在，则读取密钥和IV并将其加载到内存中的keyMap中
     *
     * @throws Exception 如果文件读取或密钥生成过程中发生错误，则抛出异常
     */
    private static void loadKeyAndIV() throws Exception {
        // 检查密钥和IV文件是否存在
        File keyFile = new File(KEY_FILE);
        File ivFile = new File(IV_FILE);

        // 如果密钥和IV文件不存在，则生成并保存新的密钥和IV
        if (!keyFile.exists() || !ivFile.exists()) {
            genKeyAndIV();
            saveKeyAndIV();
        } else {
            // 如果密钥和IV文件存在，则读取并加载密钥和IV
            try (BufferedInputStream keyInputStream = new BufferedInputStream(Files.newInputStream(keyFile.toPath()));
                 BufferedInputStream ivInputStream = new BufferedInputStream(Files.newInputStream(ivFile.toPath()))) {
                // 读取密钥和IV并存入keyMap中
                byte[] secretKey = new byte[32]; // AES密钥长度为16字节（256位）
                byte[] iv = new byte[16]; // IV长度为16字节
                keyInputStream.read(secretKey);
                ivInputStream.read(iv);
                keyMap.put("secretKey", secretKey);
                keyMap.put("iv", iv);
            }
        }
    }

    private static void saveKeyAndIV() throws IOException {
        try (BufferedOutputStream keyOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(KEY_FILE)));
             BufferedOutputStream ivOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(IV_FILE)))) {
            keyOutputStream.write(keyMap.get("secretKey"));
            ivOutputStream.write(keyMap.get("iv"));
        }
    }

    /**
     * 生成AES密钥和初始化向量
     *
     * 此方法用于生成AES算法的密钥和初始化向量，生成之后，将它们保存到文件中
     *
     * @throws NoSuchAlgorithmException 如果无法找到指定的算法"AES"，则抛出此异常
     */
    private static void genKeyAndIV() throws NoSuchAlgorithmException {
        // 实例化密钥生成器，指定算法为AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        // 初始化密钥生成器，指定密钥长度为256位
        keyGenerator.init(256);
        // 生成密钥
        SecretKey secretKey = keyGenerator.generateKey();

        // 生成初始化向量（IV）
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // 将密钥和IV存储在keyMap中，以便后续使用
        keyMap.put("secretKey", secretKey.getEncoded());
        keyMap.put("iv", iv);
    }
}
