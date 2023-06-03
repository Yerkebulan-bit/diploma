package kz.iitu.diploma_resource_server.util;

import java.util.Random;

public class SecurityUtil {

    public static final String NUMBERS = "0123456789";
    private static final Random RND = new Random();

    private static final long MINUTE_IN_MILLISECONDS = 60_000L;

    public static String generateOTP(int length) {
        var numbers = NUMBERS;
        char[] otp = new char[length];

        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(RND.nextInt(numbers.length()));
        }

        return new String(otp);
    }

    public static long getOtpExpirationTime(int minutesTillExpiration) {
        return System.currentTimeMillis() + (MINUTE_IN_MILLISECONDS * minutesTillExpiration);
    }

}
