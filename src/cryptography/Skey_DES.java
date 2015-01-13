package cryptography;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 
 * function:…˙≥…√ÿ‘ø
 */
public class Skey_DES {
	public static void generate() {
		SecretKey k;
		FileOutputStream fs = null;
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DESede");
			kg.init(168);
			k = kg.generateKey();
			byte[] kb = k.getEncoded();
			fs = new FileOutputStream("key1.dat");
			fs.write(kb);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		Skey_DES.generate();

	}

}
