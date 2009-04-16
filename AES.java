import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AES 
{
	/**
	 * Constructor for AES.  Constructs a 4x4 byte matrix for each 128-bit string.
	 * @param currentLine a 128-bit string
	 */
	public AES (String currentLine) 
	{
		int cursor = currentLine.length();
		int[][] currentState = new int[4][4];
		for(int row = 0; row < currentState.length; row++)
		{
			for(int col = 0; col < currentState[row].length; col++)
			{
				String pair = currentLine.substring(cursor-2, cursor);
				int pairHex = Integer.valueOf(pair, 16);
				currentState[row][col] = pairHex;
				cursor = cursor-2;
			}
		}
		printTranspose(currentState, true, true);
	}

	public static String[][] hex = {
			{ "63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
			{ "ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0"},
			{ "b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15"},
			{ "04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75"},
			{ "09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84"},
			{ "53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf"},
			{ "d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8"},
			{ "51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2"},
			{ "cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73"},
			{ "60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db"},
			{ "e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79"},
			{ "e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08"},
			{ "ba", "78", "25", "2e", "1c", "a6", "b4", "c7", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a"},
			{ "70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e"},
			{ "e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df"},
			{ "8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16"}};
	
	   //| 0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
	   //---|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|
	public static String[][] invHex = {
		{"52", "09", "6a", "d5", "30", "36", "a5", "38", "bf", "40", "a3", "9e", "81", "f3", "d7", "fb"},
		{"7c", "e3", "39", "82", "9b", "2f", "ff", "87", "34", "8e", "43", "44", "c4", "de", "e9", "cb"},
		{"54", "7b", "94", "32", "a6", "c2", "23", "3d", "ee", "4c", "95", "0b", "42", "fa", "c3", "4e"},
		{"08", "2e", "a1", "66", "28", "d9", "24", "b2", "76", "5b", "a2", "49", "6d", "8b", "d1", "25"},
		{"72", "f8", "f6", "64", "86", "68", "98", "16", "d4", "a4", "5c", "cc", "5d", "65", "b6", "92"},
		{"6c", "70", "48", "50", "fd", "ed", "b9", "da", "5e", "15", "46", "57", "a7", "8d", "9d", "84"},
		{"90", "d8", "ab", "00", "8c", "bc", "d3", "0a", "f7", "e4", "58", "05", "b8", "b3", "45", "06"},
		{"d0", "2c", "1e", "8f", "ca", "3f", "0f", "02", "c1", "af", "bd", "03", "01", "13", "8a", "6b"},
		{"3a", "91", "11", "41", "4f", "67", "dc", "ea", "97", "f2", "cf", "ce", "f0", "b4", "e6", "73"},
		{"96", "ac", "74", "22", "e7", "ad", "35", "85", "e2", "f9", "37", "e8", "1c", "75", "df", "6e"},
		{"47", "f1", "1a", "71", "1d", "29", "c5", "89", "6f", "b7", "62", "0e", "aa", "18", "be", "1b"},
		{"fc", "56", "3e", "4b", "c6", "d2", "79", "20", "9a", "db", "c0", "fe", "78", "cd", "5a", "f4"},
		{"1f", "dd", "a8", "33", "88", "07", "c7", "31", "b1", "12", "10", "59", "27", "80", "ec", "5f"},
		{"60", "51", "7f", "a9", "19", "b5", "4a", "0d", "2d", "e5", "7a", "9f", "93", "c9", "9c", "ef"},
		{"a0", "e0", "3b", "4d", "ae", "2a", "f5", "b0", "c8", "eb", "bb", "3c", "83", "53", "99", "61"},
		{"17", "2b", "04", "7e", "ba", "77", "d6", "26", "e1", "69", "14", "63", "55", "21", "0c", "7d"}};
	
	public static int[][] invSbox = {
		{82,	9,		106,	213,	48,		54,		165,	56,		191,	64,		163,	158,	129,	243,	215,	251},
		{124,	227,	57,		130,	155,	47,		255,	135,	52,		142,	67,		68,		196,	222,	233,	203},	
		{84,	123,	148,	50,		166,	194,	35,		61,		238,	76,		149,	11,		66,		250,	195,	78},	
		{8,		46,		161,	102,	40,		217,	36,		178,	118,	91,		162,	73,		109,	139,	209,	37},	
		{114,	248,	246,	100,	134,	104,	152,	22,		212,	164,	92,		204,	93,		101,	182,	146},	
		{108,	112,	72,		80,		253,	237,	185,	218,	94,		21,		70,		87,		167,	141,	157,	132},	
		{144,	216,	171,	0,		140,	188,	211,	10,		247,	228,	88,		5,		184,	179,	69,		6},	
		{208,	44,		30,		143,	202,	63,		15,		2,		193,	175,	189,	3,		1,		19,		138,	107},	
		{58,	145,	17,	65,	79,	103,	220,	234,	151,	242,	207,	206,	240,	180,	230,	115},	
		{150,	172,	116,	34,	231,	173,	53,	133,	226,	249,	55,	232,	28,	117,	223,	110},	
		{71,	241,	26,	113,	29,	41,	197,	137,	111,	183,	98,	14,	170,	24,	190,	27},	
		{252,	86,	62,	75,	198,	210,	121,	32,	154,	219,	192,	254,	120,	205,	90,	244},	
		{31,	221,	168,	51,	136,	7,	199,	49,	177,	18,	16,	89,	39,	128,	236,	95},	
		{96,	81,	127,	169,	25,	181,	74,	13,	45,	229,	122,	159,	147,	201,	156,	239},	
		{160,	224,	59,	77,	174,	42,	245,	176,	200,	235,	187,	60,	131,	83,	153,	97},	
		{23,	43,	4,	126,	186,	119,	214,	38,	225,	105,	20,	99,	85,	33,	12,	125}};
		
	public static int[][] multCon = {
		{2, 3, 1, 1},
		{1, 2, 3, 1},
		{1, 1, 2, 3},
		{3, 1, 1, 2}};
	
	public static int[][] invMultCon = {
		{14, 11, 13, 9},
		{9, 14, 11, 13},
		{13, 9, 14, 11},
		{11, 13, 9, 14}};
	
	public static int[][] sbox = {
		{99, 	124, 	119, 	123, 	242, 	107, 	111, 	197, 	48, 	1,	 	103, 	43, 	254, 	215, 	171, 	118},
		{202, 	130, 	201, 	125, 	250, 	89, 	71, 	240, 	173, 	212, 	162, 	175, 	156, 	164, 	114, 	192},
		{183, 	253, 	147, 	38, 	54, 	63, 	247, 	204, 	52, 	165, 	229, 	241, 	113, 	216, 	49, 	21},
		{4, 	199, 	35, 	195, 	24, 	150, 	5, 		154, 	7, 		18, 	128, 	226, 	235, 	39, 	178, 	117},
		{9, 	131, 	44, 	26, 	27, 	110, 	90, 	160, 	82, 	59, 	214, 	179, 	41, 	227, 	47, 	132},
		{83, 	209, 	0, 		237, 	32, 	252, 	177, 	91, 	106, 	203, 	190, 	57, 	74, 	76, 	88, 	207},
		{208, 	239, 	170, 	251, 	67, 	77, 	51, 	133, 	69, 	249, 	2, 		127, 	80, 	60, 	159, 	168},
		{81, 	163, 	64, 	143, 	146, 	157, 	56, 	245, 	188, 	182, 	218, 	33, 	16, 	255, 	243, 	210},
		{205, 	12, 	19, 	236, 	95, 	151, 	68, 	23, 	196, 	167, 	126, 	61, 	100, 	93, 	25, 	115},
		{96, 	129, 	79, 	220, 	34, 	42, 	144, 	136, 	70, 	238, 	184, 	20, 	222, 	94, 	11, 	219},
		{224, 	50, 	58, 	10, 	73, 	6, 		36, 	92, 	194, 	211, 	172, 	98, 	145, 	149, 	228, 	121},
		{231, 	200, 	55, 	109, 	141, 	213, 	78, 	169, 	108, 	86, 	244, 	234, 	101, 	122, 	174, 	8},
		{186, 	120, 	37, 	46, 	28, 	166, 	180, 	199, 	232, 	221, 	116, 	31, 	75, 	189, 	139, 	138},
		{112, 	62, 	181, 	102, 	72, 	3, 		246, 	14, 	97, 	53, 	87, 	185, 	134, 	193, 	29, 	158},
		{225, 	248, 	152, 	17, 	105, 	217, 	142, 	148, 	155, 	30, 	135, 	233, 	206, 	85, 	40, 	223},
		{140, 	161, 	137, 	13, 	191, 	230, 	66, 	104, 	65, 	153, 	45, 	15, 	176, 	84, 	187, 	22}};
	
	//for all i rows, the ith row represents the rcon(i+1), with the least significant byte on the left
	public static int[][] rcon = {
		{1, 0, 0, 0},
		{2, 0, 0, 0},
		{4, 0, 0, 0},
		{8, 0, 0, 0},
		{16, 0, 0, 0},
		{32, 0, 0, 0},
		{64, 0, 0, 0},
		{128, 0, 0, 0},
		{27, 0, 0, 0},
		{54, 0, 0, 0}};

	public static int[][] currentSbox = sbox; 
	public static void main(String[] args)
	{
		if(args.length != 2)
		{
			System.out.println("usage: java AES [encrypt/decrypt] [input file]");
			System.exit(0);
		}
		
		try 
		{
			Scanner input = new Scanner(new File(args[1]));
			while (input.hasNextLine()) {
				String line = input.nextLine();
                System.out.println(line);
                AES block = new AES(line);
                
				if(args[0].equals("-e"))
				{
					//encrypt
				}
				else if(args[0].equals("-d"))
				{
					//decrypt
				}
				else
				{
					System.out.println("use -e/-d for encrypt/decrypt.");
					System.exit(0);
				}
            }
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		//TODO - get key
		int[][] key= {
			{0, 0, 0, 0}, 
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}};
					
		//TODO - get input
		int[][] state= {
				{1, 0, 0, 0}, 
				{0, 0, 0, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}};				
		
		key = keyExpansion(key);		
		System.out.println("The expanded key is:");
		printTranspose(key, true, false);
		System.out.println("\n\nEncrypt Phase:\n");
		int round = 0; //starting at zero for initial addRoundKey
		state = addRoundKey(state, key, round);
		
		
		round = 1;
		while(round <= 10)
		{
			state = subBytes(state, round);
			state = shiftRows(state, round);
			if(round != 10)
				state = mixColumns(state, round);
			state = addRoundKey(state, key, round);
			round++;
		}
		
		System.out.println("The ciphertext:");
		printTranspose(state, true, true);
		
		//TODO - break into method
		//TODO - refactor add invSubBytes
		//decryption
		round--; //back to 10
		currentSbox = invSbox;
		System.out.println("\n\nDecrypt Phase:\n");
		while(round >= 1)
		{
			state = addRoundKey(state, key, round);
			if(round != 10)
				state = invMixColumns(state, round);
			state = subBytes(state, round);
			state = invShiftRows(state, round);

			round--;
		}
		state = addRoundKey(state, key, round);
		System.out.println("The decryption of the ciphertext:");
		printTranspose(state, true, true);
	}	 
	
	/**
	 * mixColumns operation for encryption phase.
	 * 
	 * @param state the current 4x4 block to do mixColumns on
	 * @param round the current round
	 * @return result the resulting 4x4 block after mixColumns
	 */
	private static int[][] mixColumns(int[][] state, int round)
	{
		int[][] result = new int[state.length][state[0].length];
		
		for(int row=0; row<state.length; row++)
			result[row] = multRow(state[row]);
		
		System.out.println("After mixColumns("+round+"):");
		printTranspose(result, false, false);		
		return result;
	}
	
	/**
	 * invMixColumns operation for decryption phase.
	 * 
	 * @param state the current 4x4 block to do invMixColumns on
	 * @param round the current round
	 * @return result the resulting 4x4 block after invMixColumns
	 */
	private static int[][] invMixColumns(int[][] state, int round)
	{
		int[][] result = new int[state.length][state[0].length];
		
		for(int row=0; row<state.length; row++)
			result[row] = invMultRow(state[row]);
		
		System.out.println("After invMixColumns("+round+"):");
		printTranspose(result, false, false);		
		return result;
	}
	
	/**
	 * Helper method to mixColumns that matrix "multiplies" row by column
	 * 
	 * @param column the column to multiply
	 * @return result the result of matrix multiplying row by column
	 */
	private static int[] multRow(int[] column)
	{
		int[] result = new int[column.length];
		
		for(int row=0; row<multCon.length; row++)
			for(int col=0; col<multCon[row].length; col++)
				result[row] ^= galoisMult(multCon[row][col],column[col]);
		
		return result;
	}
	
	/**
	 * Helper method to invMixColumns that matrix "multiplies" row by column
	 * 
	 * @param column the column to multiply
	 * @return result the result of matrix multiplying row by column
	 */
	private static int[] invMultRow(int[] column)
	{
		int[] result = new int[column.length];
		
		for(int row=0; row<invMultCon.length; row++)
			for(int col=0; col<invMultCon[row].length; col++)
				result[row] ^= galoisMult(invMultCon[row][col],column[col]);
		
		return result;
	}
	
	/**
	 * Helper method to multRow that implements Galois Multiplication in place of standard matrix
	 * multiplication.
	 * 
	 * @param a element
	 * @param b
	 * @return
	 */
	private static int galoisMult(int a, int b)
	{
		int result = 0;
		for (int i=0; i<8; i++)
		{
			if((b&1) == 1) //low bit of b set
				result = result^a;
			boolean aHighSet = (a & 128) == 128; //8th bit of a set
			a <<= 1;
			assert (a&1) == 0;
			
			a &= 255; //getting rid of 9th bit
			assert ((a&256) != 256);
			
			if (aHighSet)
				a ^= (0x1b);
			
			b >>= 1;
			assert (b&128) != 128; //8 bit is zero
		}
		return result;
	}
	
	
	private static int[][] subBytes(int[][] state, int round)
	{
		int[][] result = new int[state.length][state[0].length];
		
		for (int row=0; row<state.length; row++)
			result[row] = subWord(state[row]);
		
		System.out.println("After subBytes("+round+"):");
		printTranspose(result, false, false);		
		return result;
	}
	
	
	private static int[][] addRoundKey(int[][] state, int[][] key, int round)
	{
		int[][] result = Arrays.copyOf(state, state.length);
		for(int row = 0; row<4; row++)
			result[row] = vectorXor(state[row], key[round*4 + row]);
		
		
		System.out.println("After addRoundKey("+round+"):");
		printTranspose(result, false, false);
		return result;
	}

	private static void printTranspose(int[][] matrix, boolean manyLine, boolean spaceColumns) 
	{
		if(manyLine)
		{
			for(int col=0; col<matrix[0].length; col++)
			{
				for(int row=0; row<matrix.length; row++)
				{
					String hexString = Integer.toHexString(matrix[row][col]);
					
					if (hexString.length() == 1) //case of first zero
						hexString = "0" + hexString;
					if(spaceColumns)
						hexString += " ";	
					if(row%4 == 0)
						System.out.print(" ");
					
					System.out.print(hexString.toUpperCase());
				}
				System.out.println();
			}
		}
		else
		{
			System.out.print(" ");
			for(int row = 0; row<matrix.length; row++)
				for(int col=0; col<matrix[row].length; col++)
				{
					String hexString = Integer.toHexString(matrix[row][col]);
					if (hexString.length() == 1) //case of first zero
						hexString = "0" + hexString;
					
					System.out.print(hexString.toUpperCase());
				}
		}
		System.out.println();
	}
   
   public static int[] leftRotate(int[] origWord, int rotationCount)
   {
	   int[] word = Arrays.copyOf(origWord, origWord.length);
	   
	   while(rotationCount != 0)
	   {
		   int temp = word[0];
		   
		   for(int i = 0; i < word.length-1; i++) 
			   word[i] = word[i+1];
		   
		   word[word.length-1] = temp;
		   rotationCount--;
	   }
	   return word;
   }
   
   public static int[] rightRotate(int[] origWord, int rotationCount)
   {
	   int[] word = Arrays.copyOf(origWord, origWord.length);
	   
	   while(rotationCount != 0)
	   {
		   int temp = word[word.length-1];
		   
		   for(int i=word.length-1; i>0; i--)
			   word[i] = word[i-1];
		   
		   //for(int i = 0; i < word.length-1; i++) 
		//	   word[i+1] = word[i];
		   
		   word[0] = temp;
		   rotationCount--;
	   }
	   return word;
   }
   
   public static int[][] shiftRows(int[][] state, int round)
   {
	   int[][] result = transpose(state);
	   
	   
	   for(int row = 1; row < state.length; row++) 
		   result[row] = leftRotate(result[row], row);
	   
	   result = transpose(result);
	   
	   System.out.println("After shiftRows("+round+"):");
	   printTranspose(result, false, false);
	   
	   return result;
   }
   
   public static int[][] invShiftRows(int[][] state, int round)
   {
	   int[][] result = transpose(state);
	   for(int row = 1; row < state.length; row++) 
		   result[row] = rightRotate(result[row], row);
	   
	   result = transpose(result);
	   
	   System.out.println("After invShiftRows("+round+"):");
	   printTranspose(result, false, false);
	   
	   return result;
   }
   
   public static int[][] transpose(int[][] matrix)
   {
	   int[][] result = new int[matrix[0].length][matrix.length];
	   for(int row = 0; row < matrix.length; row++)
		   for(int col = 0; col < matrix[row].length; col++)
			   result[row][col] = matrix[col][row];
	   
	   return result;
   }
   
   public static int subByte(int origByte)
   {
	   String hex = Integer.toHexString(origByte);
	   assert hex.length() <= 2 : "hex representation too long.";
	   
	   if (hex.length() == 1)
		   hex = "0"+hex;

	   int row = Integer.valueOf(hex.charAt(0)+"", 16);	   
	   int col = Integer.valueOf(hex.charAt(1)+"", 16);
	   return currentSbox[row][col];
   }
   
   public static int[] subWord(int[] origWord)
   {
	   int[] result = new int[origWord.length];
	   
	   for(int i=0; i<origWord.length; i++)
		   result[i] = subByte(origWord[i]);
	   
	   return result;   
   }

   
   public static int[][] keyExpansion(int[][] key)
   {
	   int[][] result = new int[44][4];
	   int row;
	   
	   //first 4 row get copy of key's rows
	   for(row=0; row<key.length; row++)
		   result[row] = Arrays.copyOf(key[row], key[row].length);
	   
	   //begin filling first expansion box
	   assert row==4;
	   
	   while(row<result.length)
	   {
		   result[row] = result[row-1]; //current row built off previous row
		   
		   if(row%4 == 0) //start of a new expansion box
		   {
			   int iteration = row/4;
			   result[row] = vectorXor(subWord(leftRotate(result[row], 1)), rcon[iteration-1]);
		   }
		   
		   //xored with same row from previous key expansion box.
		   result[row] = vectorXor(result[row-4], result[row]);
		   
		   row++;
	   }
	   
	   return result;
   }
   
   public static int[] vectorXor(int[] left, int[] right)
   {
	   assert left.length == right.length : "left and right are not equal in size.";
	   
	   int result[] = new int[left.length];
	   for(int i = 0; i < left.length; i++)
	   {
		   result[i] = left[i]^right[i];  
	   }
	   
	   return result;
   }
}
