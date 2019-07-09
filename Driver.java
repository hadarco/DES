//Hadar Cochavi 204719843

import java.util.Scanner;
import java.math.BigInteger;

public class Driver {
	
	public static void main(String[] args) {

		Des des = new Des();

		long Ctext = -1;
		long plainText = Long.decode("0x2CA8AE52A1610000");
		long key = Long.decode("0x123456789456");
		
		Ctext = des.encryption(plainText, key, 3);
		System.out.print("Encrypted Text:" + " " + Long.toHexString(Ctext));

		long Dtext = -1;
		Dtext = des.decryption(Ctext, key , 3);
		System.out.print("\nDecrypted Text:" + " " + Long.toHexString(Dtext));

	}
}
