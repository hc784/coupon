//package coupon.auth.util;
//
//import java.security.MessageDigest;
//
//public class Encrypt {
//    private static final String SHA256 = "SHA-256";
//
//    public static String encrypt(String byteString) {
//        try {
//            MessageDigest md = MessageDigest.getInstance(SHA256);
//            byte[] passBytes = byteString.getBytes();
//            md.reset();
//            byte[] digested = md.digest(passBytes);
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < digested.length; i++)
//                sb.append(Integer.toString((digested[i] & 0xFF) + 256, 16).substring(1));
//            return sb.toString();
//        } catch (Exception e) {
//        }
//        return byteString;
//    }
//}
//
