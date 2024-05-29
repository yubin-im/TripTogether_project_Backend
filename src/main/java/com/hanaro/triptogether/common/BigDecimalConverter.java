package com.hanaro.triptogether.common;

import java.math.BigDecimal;

public class BigDecimalConverter {
    public static BigDecimal convertStringToBigDecimal(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
        String sanitizedStr = numberStr.replace(",", "");
        return new BigDecimal(sanitizedStr);
    }
}
