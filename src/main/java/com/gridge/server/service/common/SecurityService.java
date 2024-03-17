package com.gridge.server.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final PasswordEncoder passwordEncoder;
    private final AesBytesEncryptor aesBytesEncryptor;

    public String twoWayEncrypt(String message) {
        byte[] encrypt = aesBytesEncryptor.encrypt(message.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    public String twoWayDecrypt(String digest) {
        byte[] decryptBytes = stringToByteArray(digest);
        byte[] decrypt = aesBytesEncryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String oneWayEncrypt(String message) {
        return passwordEncoder.encode(message);
    }

    public boolean isOneWayEncryptionMatch(String message, String digest) {
        return passwordEncoder.matches(message, digest);
    }

    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte :bytes){
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }
}
