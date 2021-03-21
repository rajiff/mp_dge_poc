package com.dicegame.gamelib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

public class RollingDiceTest
{
	@Test
	public void rollNextOnceInRangeTest() {
		RollingDice dice = new RollingDice();
		int min = 1, max = 6;
		int val = dice.rollNext();

		System.out.println("Random is " + val);

		assertTrue(max >= val, "Face is above expected max");
  	assertTrue(min <= val, "Face is lower than expected min");
	}

	@Test
	public void rollNextMustNotRepeatSameValueConsecutively() {
		// This test case is to ensure, there is enough randomness to make game interesting and variations

		RollingDice dice = new RollingDice();
		int min = 1, max = 6;

		int val1 = dice.rollNext();
		int val2 = dice.rollNext();
		int val3 = dice.rollNext();
		int val4 = dice.rollNext(); // just curious whats the 4th val is going to be

		System.out.println("I: " + val1 + ", II: " + val2 + ", III: " + val3 + " IV: " + val4);

		assertTrue((val1 != val2 && val1 != val3 && val2 != val3), "Face value are same every time");
		assertTrue((max >= val1 && max >= val2 && max >= val3), "All face values are above expected max");
  	assertTrue((min <= val1 && min <= val2 && min <= val3), "All face value are lower than expected min");
	}
	
	@Test
	public void rollsMinAndMaxToo() {
		RollingDice dice = new RollingDice();
		int min = 1, max = 6;	
		
		java.util.ArrayList<Integer> scoreList = new java.util.ArrayList<Integer>();
		for(int i=0; i< 10; i++) {
			scoreList.add(dice.rollNext());
		}
		
		System.out.println("Scores are "+ scoreList.toString());
		
		assertTrue( (scoreList.contains(min) && scoreList.contains(max)), "Rolling dice must roll min & max value too");
	}
	
	@Test
	@EnabledIfEnvironmentVariable(named = "TEST_CUSTOM_FACE", matches = "YES") //Enable only when want to test, as this test case is not consistent all the time due to probability involed in it
	public void diceSupportsConfigurableMinAndMax() {
		int min = 5, max = 10;	
		RollingDice dice = new RollingDice(min, max);
		
		java.util.ArrayList<Integer> scoreList = new java.util.ArrayList<Integer>();
		// Requires range of diff between min and max to at least produce min and max once, probability of getting min and max in just one or two loop not possible
		// Less the range good, dice is only limited faces right ?
		for(int i=0; i< (max - min); i++) {
			int val = dice.rollNext();
			
			scoreList.add(val);
			
			assertTrue(max >= val, "Face is above expected max");
  		assertTrue(min <= val, "Face is lower than expected min");
		}
		
		System.out.println("Scores in non default min/max dice face are "+ scoreList.toString());
		
		assertTrue( (scoreList.contains(min) && scoreList.contains(max)), "Rolling dice must roll min & max value too");
	}
}
