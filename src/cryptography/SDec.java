package cryptography;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SDec {
	public static void decode() {
		try {
			// 获取密文
			FileInputStream fs = new FileInputStream("Senc.data");
			int num = fs.available();
			byte[] ctext = new byte[num];
			fs.read(ctext);
			// 获取秘钥
			FileInputStream f2 = new FileInputStream("key1.dat");
			int keyNum = f2.available();
			byte[] keykb = new byte[keyNum];
			f2.read(keykb);
			SecretKeySpec k = new SecretKeySpec(keykb, "DESede");
			// 解密
			Cipher cp = Cipher.getInstance("DESede");
			cp.init(Cipher.DECRYPT_MODE, k);
			byte[] preText = cp.doFinal(ctext);
			String p = new String(preText, "UTF8");
			System.out.println(p);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SDec.decode();
	}

}
