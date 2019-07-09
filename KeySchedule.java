//Hadar Cochavi 204719843


public class KeySchedule {

	private int[] V = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
	private int[] PC1_C = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3,
			60, 52, 44, 36 };

	private int[] PC1_D = { 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5,
			28, 20, 12, 4 };

	private int[] InversePC1 = { 8, 16, 24, 56, 52, 44, 36, 7, 15, 23, 55, 51, 43, 35, 6, 14, 22, 54, 50, 42, 34, 5, 13,
			21, 53, 49, 41, 33, 4, 12, 20, 28, 48, 40, 32, 3, 11, 19, 27, 47, 39, 31, 2, 10, 18, 26, 46, 38, 30, 1, 9,
			17, 25, 45, 37, 29 };

	private int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41,
			52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

	private int[] InversePC2 = { 5, 24, 7, 16, 6, 10, 20, 18, 0, 12, 3, 15, 23, 1, 9, 19, 2, 0, 14, 22, 11, 0, 13, 4, 0,
			17, 21, 8, 47, 31, 27, 48, 35, 41, 0, 46, 28, 0, 39, 32, 25, 44, 0, 37, 34, 43, 29, 36, 38, 45, 33, 26, 42,
			0, 30, 40
	};

	public KeySchedule() {
	}


	public long getPC1Val(long k) {
		int c0 = getC0Val(k);
		int d0 = getD0Val(k);

		return (((long) c0) << 28 | d0);
	}

	private int getC0Val(long k) {
		int C0Val = 0;
		int bitpos = 27;

		for (int i = 0; i < 28; i++) {
			C0Val |= ((k & (1L << 64 - PC1_C[i])) >> 64 - PC1_C[i]) << bitpos;
			bitpos--;
		}

		return C0Val;
	}

	private int getD0Val(long k) {
		int bitpos = 27;
		int D0Val = 0;

		for (int i = 0; i < 28; i++) {
			D0Val |= ((k & (1L << (64 - PC1_D[i]))) >> (64 - PC1_D[i])) << bitpos;
			bitpos--;
		}

		return D0Val;
	}

	
	public long getInversePC1Val(long in) {
		int bitpos = 55;
		long inverseVal = 0L;

		for (int i = 0; i < 56; i++) {
			inverseVal |= ((in & (1L << (56 - InversePC1[i]))) >> (56 - InversePC1[i])) << bitpos;
			bitpos--;
		}

		return inverseVal;
	}


	private long getPC2Val(int C, int D) {
		int bitpos = 47;
		long in = (((long) C) << 28) + (long) D;
		long PC2Val = 0;

		for (int i = 0; i < 48; i++) {
			PC2Val += ((in & (1L << 56 - PC2[i])) >> 56 - PC2[i]) << bitpos;
			bitpos--;
		}

		return PC2Val;
	}

	
	public long[] getDecryptRoundKeys(long k, int rounds) {
		long[] encRoundKeys = getEncryptRoundKeys(k, rounds);
		int index = rounds - 1;
		long[] roundKeys = new long[rounds];

		for (int i = 0; i < rounds; i++) {
			roundKeys[i] = encRoundKeys[index];
			index--;
		}

		return roundKeys;
	}

	public long getInversePC2Val(long in, int guessBits) {
		long InversePC2Val = 0L;
		int bitpos = 55;
		int guessBitPosition = 7;
		for (int i = 0; i < 56; i++) {
			if (InversePC2[i] != 0) {
				InversePC2Val += ((in & (1L << 48 - InversePC2[i])) >> 48 - InversePC2[i]) << bitpos;
			} else {
				if ((guessBits & (1 << guessBitPosition)) == (1 << guessBitPosition)) {
					InversePC2Val += (1L << bitpos);
				}
				guessBitPosition--;
			}
			bitpos--;
		}

		return InversePC2Val;
	}

	public long[] getEncryptRoundKeys(long k, int rounds) {
		long[] roundKeys = new long[rounds];
		int c0 = getC0Val(k);
		int d0 = getD0Val(k);
		int cVal, dVal;
		int cUpBits = 0;
		int dUpBits = 0;

		cVal = c0;
		dVal = d0;

		for (int i = 0; i < rounds; i++) {
			if (V[i] == 1) {
				cUpBits = (cVal & 134217728) >> 27;
				dUpBits = (dVal & 134217728) >> 27;

				cVal &= 134217727;
				dVal &= 134217727;
			} else if (V[i] == 2) {
				cUpBits = (cVal & 201326592) >> 26;
				dUpBits = (dVal & 201326592) >> 26;

				cVal &= 67108863;
				dVal &= 67108863;
			}

			cVal = cVal << V[i];
			dVal = dVal << V[i];

			cVal += cUpBits;
			dVal += dUpBits;

			roundKeys[i] = getPC2Val(cVal, dVal);
		}

		return roundKeys;
	}


}
