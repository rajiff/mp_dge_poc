package com.dicegame.gamelib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class DiceGameEngineTest 
{
	@Test
	public void testDiceGameEngineInstantiation() {
		String[] playerNameList = {"P1", "P2", "P3", "P4", "P5"};
		int maxPoints = 21;
		
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		assertTrue(gameEngine != null, "A instance cannot be null");
		assertTrue(java.util.Arrays.equals(gameEngine.getPlayers(), playerNameList), "Players mast be same as passed to instantiation");
		assertEquals(gameEngine.getMaxGamePoints(), maxPoints);
	}
	
	@Test
	public void testInitPlayingSequence() {
		String[] playerNameList = {"P1", "P2", "P3", "P4", "P5", "P6"};
		int maxPoints = 21;
		
		// instantitation
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		
		assertTrue(Arrays.equals(gameEngine.getPlayers(), playerNameList), "Initially Players sequence mast be same as passed to instantiation");
		System.out.println("Original list " + Arrays.toString(playerNameList));
		System.out.println("Insantiated list " + Arrays.toString(gameEngine.getPlayers()));
		
		gameEngine.initPlayingSequence(false); // Set playing sequence without shuffling the players
		assertTrue(Arrays.equals(gameEngine.getPlayers(), playerNameList), "Wihtout shuffling, player sequence mast be same as passed to instantiation");
		System.out.println("Playing sequence list (no shuffle) " + Arrays.toString(gameEngine.getPlayers()));
		
		gameEngine.initPlayingSequence(true); // Shuffle the players		
		assertTrue( ( ! Arrays.equals(gameEngine.getPlayers(), playerNameList) ), "Players sequence cannot be same as passed at time of instantiation after shuffling");
		System.out.println("Playing sequence list (after shuffle) " + Arrays.toString(gameEngine.getPlayers()));

	  assertEquals(gameEngine.getPlayers().length, playerNameList.length, "Total players must be same as originally instantiated");
		
		String[] shuffledPlayerList = gameEngine.getPlayers();
		Arrays.sort(shuffledPlayerList);
		Arrays.sort(playerNameList);
	  assertTrue( (Arrays.equals(shuffledPlayerList, playerNameList)), "Players sequence must have all original players");
	}
}
