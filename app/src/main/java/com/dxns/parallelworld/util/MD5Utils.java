package com.dxns.parallelworld.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * MD5加盐算法
 * 服务端实现相同的算法
 */
public class MD5Utils {



        protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        protected static MessageDigest messagedigest = null;

        static {
            try {
                messagedigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsaex) {
                System.err.println(MD5Utils.class.getName()
                        + "初始化失败，MessageDigest不支持MD5Util。");
                nsaex.printStackTrace();
            }
        }





    /**
     * 生成含有随机盐的密码
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = getMD5String(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return getMD5String(password + salt).equals(new String(cs1));
    }


        /**
         * 功能：得到文件的md5值。
         *
         
         * @date 2014年06月24日
         * @param file
         *            文件。
         * @return String
         * @throws IOException
         *             读取文件IO异常时。
         */
        public static String getFileMD5String(File file) throws IOException {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        }

        /**
         * 功能：得到一个字符串的MD5值。
         *
         
         * @date 2014年06月24日
         * @param str
         *            字符串
         * @return String
         */
        public static String getMD5String(String str) {
            return getMD5String(str.getBytes());
        }

        private static String getMD5String(byte[] bytes) {
            messagedigest.update(bytes);
            return bufferToHex(messagedigest.digest());
        }

        private static String bufferToHex(byte bytes[]) {
            return bufferToHex(bytes, 0, bytes.length);
        }

        private static String bufferToHex(byte bytes[], int m, int n) {
            StringBuffer stringbuffer = new StringBuffer(2 * n);
            int k = m + n;
            for (int l = m; l < k; l++) {
                appendHexPair(bytes[l], stringbuffer);
            }
            return stringbuffer.toString();
        }

        private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
            char c0 = hexDigits[(bt & 0xf0) >> 4];
            char c1 = hexDigits[bt & 0xf];
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
}
