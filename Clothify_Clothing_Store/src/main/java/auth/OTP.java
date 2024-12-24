package auth;

import java.util.Random;

public class OTP {
    public static String generateOTP() {
        String numbers = "0123456789";
        Random rand = new Random();
        char[] otp = new char[4];
        for (int i = 0; i < 4; i++) {
            otp[i] = numbers.charAt(rand.nextInt(numbers.length()));
        }
        return new String(otp);
    }
}
