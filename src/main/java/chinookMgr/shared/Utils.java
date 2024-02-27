package chinookMgr.shared;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	public static byte[] toMD5(byte[] input) {
		try {
			return MessageDigest.getInstance("MD5").digest(input);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}