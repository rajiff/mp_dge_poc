package com.dicegame.gamelib;

import java.util.concurrent.ThreadLocalRandom;

/*enum DiceFaces {
	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);

	private int faceValue;

	DiceFaces(int fv) { faceValue = fv; }
	int getValue() { return faceValue; }
}*/

public class RollingDice implements IRollingDice {
	int minFace = 1; // default
	int maxFace = 6; // default
	
	// At least ensure is not same as last two previous, if we need more we need to use an array
	static int prevRandom = 0;
	static int prevRandom2 = 0;

	RollingDice() {}
	
	RollingDice(int min, int max) {
		minFace=min;
		maxFace=max;
	}
	
	/**
	 * Simulates dice rolling
	 * Randomly generate value in the given range
	 * value of rolled dice face
	 * @return int 
	 */
	public int rollNext() {		
		// Range is Inclusive of the origin and limit
		int newRandom = 0;
		
		do {
			// Gives randome between origin (inclusive) and bound (exclusive).
			newRandom = ThreadLocalRandom.current().nextInt(minFace, (maxFace + 1)); // bound is set to 1 point above to get even max value of dice
		} while( prevRandom == newRandom || prevRandom2 == newRandom); // do it again, if generated same as immediate two previous randoms
		
		prevRandom2 = prevRandom;
		prevRandom = newRandom;
		
		return newRandom;
	}
}