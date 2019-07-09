//Hadar Cochavi 204719843


public class Permutation {

	private int[] InitialPermutation = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38,
			30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
			61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

	private int[] InverseInitPerm = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46,
			14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59,
			27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

	private int[] Expansion = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16,
			17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

	private int[] InverseExpansion = { 2, 3, 4, 5, 6, 9, 10, 11, 12, 15, 16, 17, 18, 21, 22, 23, 24, 27, 28, 29, 30, 33,
			34, 35, 36, 39, 40, 41, 42, 45, 46, 47 };


	public long expand(long in) {
		long eVal = 0;
		int bitpos = 47;

		for (int i = 0; i < 48; i++) {
			if ((in & (1L << 32 - Expansion[i])) == (1L << 32 - Expansion[i])) {
				eVal |= 1L << bitpos;
			}
			bitpos--;
		}

		return eVal;
	}


	public long InverseE(long in) {
		long eVal = 0;
		int bitpos = 31;

		for (int i = 0; i < 32; i++) {
			if ((in & (1L << 48 - InverseExpansion[i])) == (1L << 48 - InverseExpansion[i])) {
				eVal |= 1L << bitpos;
			}
			bitpos--;
		}

		return eVal;
	}

	private int[] Perm = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3,
			9, 19, 13, 30, 6, 22, 11, 4, 25 };

	private int[] InversePermutation = { 9, 17, 23, 31, 13, 28, 2, 18, 24, 16, 30, 6, 26, 20, 10, 1, 8, 14, 25, 3, 4,
			29, 11, 19, 32, 12, 22, 7, 5, 27, 15, 21 };

	/** Creates a new instance of ExpandPermutation */
	public Permutation() {
		
	}


	public long IP(long plaintext) {
		long ipVal = 0;
		int bitpos = 63;

		for (int i = 0; i < 64; i++) {
			if ((plaintext & (1L << 64 - InitialPermutation[i])) == (1L << 64 - InitialPermutation[i])) {
				ipVal |= 1L << bitpos;
			}

			bitpos--;
		}

		return ipVal;
	}

	public int InverseP(int in) {
		int pVal = 0;
		int bitpos = 31;

		for (int i = 0; i < 32; i++) {
			if ((in & (1L << 32 - InversePermutation[i])) == (1L << 32 - InversePermutation[i])) {
				pVal |= 1L << bitpos;
			}
			bitpos--;
		}

		return pVal;
	}
	
	
	public long InverseIP(long cipher) {
		long ipVal = 0;
		int bitpos = 63;

		for (int i = 0; i < 64; i++) {
			if ((cipher & (1L << 64 - InverseInitPerm[i])) == (1L << 64 - InverseInitPerm[i])) {
				ipVal |= 1L << bitpos;
			}
			bitpos--;
		}

		return ipVal;
	}



	public int perm(int in) {
		int pVal = 0;
		int bitpos = 31;

		for (int i = 0; i < 32; i++) {
			if ((in & (1L << 32 - Perm[i])) == (1L << 32 - Perm[i])) {
				pVal |= 1L << bitpos;
			}
			bitpos--;
		}

		return pVal;
	}


}