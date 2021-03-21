package com.dicegame.gamelib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DiceGameEngine {
	// It is a list, it should maintain order players, unless changed explicitly
	ArrayList<String> playerList;
	int maxGamePoints;
	int penaltyOnScore;
	int bonusOnScore;
	int minDiceFace;
	int maxDiceFace;
	
	public DiceGameEngine(String[] playerNames, int maxPoints) {
		// Cannot be default, has to be provided
		playerList = new ArrayList<String>(Arrays.asList(playerNames));
		maxGamePoints = maxPoints;

		// Usually Dice is 6 faced
		minDiceFace = 1;
		maxDiceFace = 6;

		// by default penalty for scoring minimum possible, bonus for scoring max possible
		penaltyOnScore = minDiceFace;
		bonusOnScore = maxDiceFace;
	}

	// Fully set by the client
	public DiceGameEngine(String[] playerNames, int maxPoints, int penalty, int bonus, int minFace, int maxFace) {
		playerList = new ArrayList<String>(Arrays.asList(playerNames));
		maxGamePoints = maxPoints;

		penaltyOnScore = penalty;
		bonusOnScore = bonus;

		minDiceFace = minFace;
		maxDiceFace = maxFace;
	}

	public String[] getPlayers() {
		return playerList.toArray(String[]::new);
	}

	public int getMaxGamePoints() {
		return maxGamePoints;
	}
	
	public void initPlayingSequence(boolean shufflePlayerOrder) {
		// Explicitly change the order of players
		if(shufflePlayerOrder)
			Collections.shuffle(playerList);
	}

	public void initPlayingSequence() {
		initPlayingSequence(true); // by default too shuffle the players
	}

		
	/* public void playDiceGame() {
		
		// Keep looping with playing turns until every one has achieved max game points
		do {
			
		} while();
	}
	
	public void playNextTurn() {
		
	}
	
	public boolean isPlayerHasPenalty(String playerName) {
		// Check last two turns of the user and confirm if penalty is applicable 
	}
	
	public boolean isPlayerHasBonus(String playerName) {
		// Check last immediate turn of the user and confirm if penalty is applicable 
		// if last two turns of the user has bonus, then no bonus, only max once a bonus is awarded
	}
	
	*/
}