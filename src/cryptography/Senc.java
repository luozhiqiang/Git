package cryptography;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 * 
 * function:使用秘钥进行加密
  */
public class Senc {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		String s = "HelloWorld";
		FileInputStream fs = new FileInputStream("../Security/key1.dat");
		ObjectInputStream b = new ObjectInputStream(fs);
		Key k = (Key) b.readObject();
		Cipher cp = Cipher.getInstance("DESede");
		cp.init(Cipher.ENCRYPT_MODE, k);
		byte[] ptext = s.getBytes("UTF8");
		for (byte p : ptext) {
			System.out.print(p);
		}
		System.out.println();
		byte[] ctext = cp.doFinal(ptext);
		for (byte p : ctext) {
			System.out.print(p);
		}
		FileOutputStream f2=new FileOutputStream("Senc.data");
		f2.write(ctext);
		b.close();
		f2.close();
	}

}
