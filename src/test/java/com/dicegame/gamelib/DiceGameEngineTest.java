package com.dicegame.gamelib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

// For testing purpose only
class MockRollingDice implements IRollingDice {
	public static int nextScore = 1;
	public int rollNext() { return MockRollingDice.nextScore; }
}

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
		
		// instantiation
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		
		assertTrue(Arrays.equals(gameEngine.getPlayers(), playerNameList), "Initially Players sequence mast be same as passed to instantiation");
		System.out.println("Original list " + Arrays.toString(playerNameList));
		System.out.println("Instantiated list " + Arrays.toString(gameEngine.getPlayers()));
		
		gameEngine.initPlayingSequence(false); // Set playing sequence without shuffling the players
		assertTrue(Arrays.equals(gameEngine.getPlayers(), playerNameList), "Without shuffling, player sequence mast be same as passed to instantiation");
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
	
	@Test
	public void testPlayPlayerTurn() {
		String[] playerNameList = {"Player-01", "Player-02"};
		int maxPoints = 21;
		
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		
		IRollingDice mockDice = new MockRollingDice();
		MockRollingDice.nextScore = 5;
		
		gameEngine.playPlayerTurn("Player-01", mockDice); // 1st time
		assertEquals(MockRollingDice.nextScore, gameEngine.getPlayerScore("Player-01"), "Player score of a turn must be same as scored during that turn");
		
		gameEngine.playPlayerTurn("Player-01", mockDice); // 2nd time
		gameEngine.playPlayerTurn("Player-01", mockDice); // 3rd time
		assertEquals((MockRollingDice.nextScore*3), gameEngine.getPlayerScore("Player-01"), "Player total score must be equal to number of times dice was rolled");
		System.out.println("Player-01 has score of " + gameEngine.getPlayerScore("Player-01"));
	}
	
	@Test
	public void testPlayerCompletsDuringTurn(){
		// check if the player gets added to completed list of player on achieving max game points
		
		String[] playerNameList = {"Player-01", "Player-02"};
		int maxPoints = 5;
		
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		
		IRollingDice mockDice = new MockRollingDice();
		
		MockRollingDice.nextScore = 5; //set to 5 for player 1 to make the player complete
		gameEngine.playPlayerTurn("Player-01", mockDice); // Player 1 should now complete
		assertTrue(gameEngine.hasPlayerCompleted("Player-01"), "Player must complete the game on achieving max game points");
		
		MockRollingDice.nextScore = 2; //set less than 5 for player 2 to make the player continue
		gameEngine.playPlayerTurn("Player-02", mockDice); // Player 1 should now complete
		assertTrue( ( ! gameEngine.hasPlayerCompleted("Player-02")), "Player must continue the game if max game points not yet achieved");
	}
	
	@Test
	public void testBonusScore(){
		String[] playerNameList = {"Player-01", "Player-02"};
		int maxPoints = 10;
		
		DiceGameEngine gameEngine = new DiceGameEngine(playerNameList, maxPoints);
		
		IRollingDice mockDice = new MockRollingDice();
				
		MockRollingDice.nextScore = 6;
		gameEngine.playPlayerTurn("Player-01", mockDice); // Player 1 should get bonus turn to roll the dice in which case score after this will be 12
		assertEquals(12, gameEngine.getPlayerScore("Player-01"), "Player must get additional turn and points if scored a bonus");
		
		MockRollingDice.nextScore = 2;
		gameEngine.playPlayerTurn("Player-02", mockDice); // Player 2 should not get any bonus, which means score will be same as the last roll of the dice
		assertEquals(2, gameEngine.getPlayerScore("Player-02"), "Player gets only one turn if no bouns was scored");
	}
}
