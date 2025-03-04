package com.net1707.backend.config;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class VNPayUtils {
    public static String generateSignature(Map<String, String> params, String secretKey) {
        SortedMap<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.remove("vnp_SecureHash");

        String data = sortedParams.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));

        return hmacSHA512(secretKey, data);
    }

    public static String buildQueryString(Map<String, String> params, boolean encode) {
        return params.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> (encode ? URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII) : entry.getKey()) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }


    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HMAC-SHA512", e);
        }
    }

    public static boolean isValidSignature(Map<String, String> params, String receivedHash, String secretKey) {
        return generateSignature(params, secretKey).equalsIgnoreCase(receivedHash);
    }

}
