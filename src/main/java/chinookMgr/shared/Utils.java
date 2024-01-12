package chinookMgr.shared;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	public static byte[] toMD5(String input) {
		try {
			return MessageDigest.getInstance("MD5").digest(input.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}