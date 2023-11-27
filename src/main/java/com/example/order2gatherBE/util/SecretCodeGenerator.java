package com.example.order2gatherBE.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecretCodeGenerator {

    public static String generateSecretCode(int hostID) {
        try {
            // Get current time in milliseconds
            long currentTimeMillis = System.currentTimeMillis();

            // Concatenate current time and hostID as a string
            String inputString =
                String.valueOf(currentTimeMillis) + String.valueOf(hostID);

            // Get the SHA-256 hash of the input string
            byte[] hashedBytes = getSHA256Hash(inputString);

            // Extract the first 4 bytes from the hash
            byte[] truncatedHash = new byte[4];
            System.arraycopy(
                hashedBytes,
                0,
                truncatedHash,
                0,
                truncatedHash.length
            );

            // Convert the truncated hash to a 6-digit string
            int secretCode = Math.abs(
                ByteBuffer.wrap(truncatedHash).getInt() % 1000000
            );

            // Format the secret code as a 6-digit string (padded with zeros if necessary)
            return String.format("%06d", secretCode);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getSHA256Hash(String input)
        throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes());
    }
}
