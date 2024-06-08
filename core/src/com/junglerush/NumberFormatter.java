package com.junglerush;

import com.badlogic.gdx.math.MathUtils;
import java.math.BigInteger;

public class NumberFormatter {
    public static String formatBigInteger(BigInteger number) {
        String[] suffix = new String[]{
                "", "K", "M", "B", "T", // "", Thousand, Million, Billion, Trillion
                "q", "Q", "s", "S", "o", // Quadrillion, Quintillion, Sextillion, Septillion, Octillion
                "N", "D", "U", "d", "Td", "Qd", "Qnd", "Sd", "Spd", "Od", "Nd", "V", // Nonillion, Decillion, Undecillion, Duodecillion, Tredecillion, Quattuordecillion, Quindecillion, Sexdecillion, Septendecillion, Octodecillion, Novemdecillion, Vigintillion
                "Uv", "Dv", "Tv", "Qtv", "Qnv", "Sv", "Spv", // Unvigintillion, Duovigintillion, Trevigintillion, Quattuorvigintillion, Quinvigintillion, Sexvigintillion, Septenvigintillion
                "Ov", "Nv", "Tg" // Octovigintillion, Novemvigintillion, Trigintillion
        };

        if (number.equals(BigInteger.ZERO)) {
            return "0";
        }

        int magnitude = (number.toString().length() - 1) / 3;
        int index = MathUtils.clamp(magnitude, 0, suffix.length - 1);
        BigInteger divisor = BigInteger.TEN.pow(index * 3);
        BigInteger shortNum = number.divide(divisor);

        return shortNum.toString() + suffix[index];
    }
}
