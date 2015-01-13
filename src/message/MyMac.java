package message;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MyMac {

	public static void main(String[] args) {
		// 获取秘钥
		byte[] kb={11,-105,-119,50,4,-105,16,38,-14,-111,21,-95,70-15,76,74};
		SecretKeySpec key=new SecretKeySpec(kb,"HMACSHAI");
		try {
			//获取Mac对象
			Mac m=Mac.getInstance("HmacMD5");
			m.init(key);
			String s="How are you";
			m.update(s.getBytes("UTF8"));
			byte[] result=m.doFinal();
			System.out.println(result);
			String string="";
			for(int i=0;i<result.length;i++){
				string+=Integer.toHexString((0x000000ff&result[i]|0xffffff00)).substring(6);
			}
			System.out.println(string);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
