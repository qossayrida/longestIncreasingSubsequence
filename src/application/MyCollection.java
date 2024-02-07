package application;

import java.util.Random;

public class MyCollection {
	
	 public static void CreateDPTable (int[] LEDs, int[][] DP, byte[][] source) {
	    // Initialize the number of power sources.
	    int numberPowerSource = LEDs.length;

	    // Loop through all possible combinations of LEDs and power sources.
	    for (int i = 1; i <= numberPowerSource; i++) {
	        for (int j = 1; j <= numberPowerSource; j++) {
	            if (i == LEDs[j - 1]) {
	                // Increment DP value if current LED matches and set source accordingly.
	                DP[i][j] = DP[i - 1][j - 1] + 1;
	                setTwoBits(source[i], j, (byte) 2); // 2 indicates value from DP[i-1][j-1].
	            } else {
	                // Compare values from top and left cells of DP table.
	                int fromTop = DP[i - 1][j];
	                int fromLeft = DP[i][j - 1];

	                if (fromTop >= fromLeft) {
	                    DP[i][j] = fromTop;
	                    setTwoBits(source[i], j, (byte) 1); // 1 indicates value from DP[i-1][j].
	                } else {
	                    DP[i][j] = fromLeft;
	                    setTwoBits(source[i], j, (byte) 0); // 0 indicates value from DP[i][j-1].
	                }
	            }
	        }
	    }
	}

	public static void findLEDs (int[] connections, int[] LEDs, int[][] DP, byte[][] source) {
	    // Initialize indices and count for backtracking.
	    int i = DP.length - 1, j = i;
	    int count = 0;

	    // Backtrack to find the sequence of LEDs.
	    while (i > 0 && j > 0) {
	        byte value = getTwoBits(source[i], j);
	        if (value == 2) {
	            connections[count++] = i;
	            i--;
	            j--;
	        } else if (value == 1) {
	            i--;
	        } else {
	            j--;
	        }
	    }
	}

	public static void setTwoBits(byte[] arr, int position, byte value) {
	    // Calculate the shift based on position.
	    int shift = position % 4 * 2;
	    // Set two bits in the array at the specified position.
	    arr[position / 4] |= (value << shift);
	}

	public static byte getTwoBits(byte[] arr, int position) {
	    // Calculate the shift based on position.
	    int shift = position % 4 * 2;
	    // Retrieve two bits from the array at the specified position.
	    return (byte) ((arr[position / 4] >> shift) & 3);
	}

	public static boolean checkIfExists(int[] array, int number) {
	    // Check if the number exists in the array.
	    for (int i = 0; i < array.length; i++) 
	        if (array[i] == number) 
	            return true;

	    return false;
	}

	public static void shuffle(int[] array) {
	    // Create an instance of the Random class.
	    Random rand = new Random();
	    int length = array.length;

	    // Shuffle the array elements randomly.
	    for (int i = length - 1; i > 0; i--) {
	        // Get a random index from 0 to i.
	        int j = rand.nextInt(i + 1);

	        // Swap the elements at indices i and j.
	        int temp = array[i];
	        array[i] = array[j];
	        array[j] = temp;
	    }
	}
	
	public static int[] longestIncreasingSubsequence(int[] LEDs, int[] lengths,int[] sequences) {       
        int n = LEDs.length;
        int maxLengthIndex = 0;
        
        for (int i = 0; i < n; i++) {
            lengths[i] = 1;
            sequences[i] = -1;
        }
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (LEDs[j] < LEDs[i]  && lengths[i] < lengths[j] + 1) {
                    lengths[i] = lengths[j] + 1;
                    sequences[i] = j;
                }
            }
            if (lengths[maxLengthIndex]<lengths[i]) {
                maxLengthIndex = i;
            }
        }
        
        int sequenceLength = lengths[maxLengthIndex];
        int[] connections = new int[sequenceLength];
        int currentIndex = maxLengthIndex;
        
        for (int i = sequenceLength - 1; i >= 0; i--) {
            connections[i] = LEDs[currentIndex];
            currentIndex = sequences[currentIndex];
        }
        
        return connections;
    }

}


 
