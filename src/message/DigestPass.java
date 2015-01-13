package message;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestPass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String x="abc";
		try {
			MessageDigest m=MessageDigest.getInstance("MD5");
			m.update(x.getBytes("UTF8"));
			byte s[]=m.digest();
			for(byte l:s){
				System.out.print(l);
			}
			System.out.println();
			String result="";
			for(int i=0;i<s.length;i++){
				result+=Integer.toHexString((0x000000ff&s[i]|0xffffff00)).substring(6);
			}
			System.out.println(result);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
