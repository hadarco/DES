//Hadar Cochavi 204719843

public class Des {
	private SBox sbox;
	private KeySchedule key_s;
	private int sboxOut;
	private int firstRound;
	private int thirdRound;
	private int fourthRound;
	private Permutation expandPermutation;

	public Des() {
		sbox = new SBox();
		expandPermutation = new Permutation();
		key_s = new KeySchedule();
	}


	public long decryption(long cipher, long key, int rounds) {
		int leftValue, rightValue, prevLeftValue, prevRightValue;
		long expandedVal, sboxInput, plaintext;
		int sboxOutput;

		// Get the number of rounds keys needed for this operation
		long[] roundKeys = key_s.getDecryptRoundKeys(key, rounds);

		// Perform the initial permutation
		long ipCipher = expandPermutation.IP(cipher);
		int L0 = (int) ((ipCipher & 0xffffffff00000000L) >> 32);
		int R0 = (int) (ipCipher & 0xffffffff);

		leftValue = L0;
		rightValue = R0;

		for (int i = 0; i < rounds; i++)
		{
			prevLeftValue = leftValue;
			prevRightValue = rightValue;

			leftValue = prevRightValue;

			// Perform the expansion
			expandedVal = expandPermutation.expand(prevRightValue);

			// Xor the expanded value with the round key
			sboxInput = expandedVal ^ roundKeys[i];

			sboxOutput = 0;

			// Get the output for the SBoxes for the input for this round
			for (int j = 0; j < 8; j++) {
				sboxOutput |= (sbox.getSboxValue((int) ((sboxInput & (0x3fL << (j * 6))) >> (j * 6)), 8 - j)) << (j
						* 4);
			}

			int fVal = expandPermutation.perm(sboxOutput);

			if (i != rounds - 1) {
				rightValue = prevLeftValue ^ fVal;
			} else {
				leftValue = prevLeftValue ^ fVal;
			}
		}

		plaintext = (((long) leftValue) << 32) + (rightValue & 0x00000000ffffffffL);

		// Perform the inverse initial permutation
		long ipPlaintext = expandPermutation.InverseIP(plaintext);

		return ipPlaintext;
	}

	
	public long encryption(long plaintext, long key, int rounds) {
		int sboxOutput;
		int leftValue, rightValue, prevLeftValue, prevRightValue;
		long expandedVal, sboxInput, ciphertext;

		// Get the number of rounds keys needed for this operation
		long[] roundKeys = key_s.getEncryptRoundKeys(key, rounds);

		// Perform the initial permutation
		long ipPlaintext = expandPermutation.IP(plaintext);
		int L0 = (int) ((ipPlaintext & 0xffffffff00000000L) >> 32);
		int R0 = (int) (ipPlaintext & 0xffffffff);

		leftValue = L0;
		rightValue = R0;

		for (int i = 0; i < rounds; i++) {
			prevLeftValue = leftValue;
			prevRightValue = rightValue;

			leftValue = prevRightValue;

			// Perform the expansion
			expandedVal = expandPermutation.expand(prevRightValue);

			// Xor the expanded value with the round key
			sboxInput = expandedVal ^ roundKeys[i];

			// Get the output for the SBoxes for the input for this round
			sboxOutput = 0;
			int sval;
			for (int j = 0; j < 8; j++) {
				sval = (int) ((sboxInput & (0x3fL << (j * 6))) >> (j * 6));
				sboxOutput |= (sbox.getSboxValue((int) ((sboxInput & (0x3fL << (j * 6))) >> (j * 6)), 8 - j)) << (j
						* 4);
			}

			int fVal = expandPermutation.perm(sboxOutput);

			// For the cryptanalysis computations.
			if (i == 0) {
				firstRound = fVal;
			} else if (i == 2) {
				thirdRound = fVal;
			} else if (i == 3) {
				fourthRound = fVal;
			} else if (i == rounds - 1) {
				sboxOut = sboxOutput;
			}

			if (i != rounds - 1) {
				rightValue = prevLeftValue ^ fVal;
			} else {
				leftValue = prevLeftValue ^ fVal;
			}
		}

		ciphertext = (((long) leftValue) << 32) + (rightValue & 0x00000000ffffffffL);

		// Perform the inverse initial permutation
		long ipCipher = expandPermutation.InverseIP(ciphertext);

		return ipCipher;
	}



	public int getThirdRound() {
		return thirdRound;
	}

	
	public int getFourthRound() {
		return fourthRound;
	}

	public int getFirstRound() {
		return firstRound;
	}


}