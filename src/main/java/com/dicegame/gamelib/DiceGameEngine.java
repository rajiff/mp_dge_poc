package com.dicegame.gamelib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class DiceGameEngine {
	// It is a list, it should maintain order players, unless changed explicitly
	ArrayList<String> playerList;
	int maxGamePoints;
	int penaltyOnScore;
	int bonusOnScore;
	int minDiceFace;
	int maxDiceFace;

	LinkedHashMap<String, ArrayList<Integer>> playerScoresMap;

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

		playerScoresMap = new LinkedHashMap<String, ArrayList<Integer>>();
	}

	// Fully set by the client
	public DiceGameEngine(String[] playerNames, int maxPoints, int penalty, int bonus, int minFace, int maxFace) {
		playerList = new ArrayList<String>(Arrays.asList(playerNames));
		maxGamePoints = maxPoints;

		penaltyOnScore = penalty;
		bonusOnScore = bonus;

		minDiceFace = minFace;
		maxDiceFace = maxFace;

		playerScoresMap = new LinkedHashMap<String, ArrayList<Integer>>();
	}

	public String[] getPlayers() {
		return playerList.toArray(String[]::new);
	}

	public int getMaxGamePoints() {
		return maxGamePoints;
	}

	public int getPlayerScore(String playerName) {
		int score = 0;

		if(playerScoresMap.containsKey(playerName)) {
			score = playerScoresMap.get(playerName).stream().mapToInt(Integer::intValue).sum();
		}
		return score;
	}

	public void initPlayingSequence(boolean shufflePlayerOrder) {
		// Explicitly change the order of players
		if(shufflePlayerOrder)
			Collections.shuffle(playerList);
	}

	public void initPlayingSequence() {
		initPlayingSequence(true); // by default too shuffle the players
	}

	public void playGame() {
		// By default use simple rolling dice.
		IRollingDice rollingDice = new RollingDice();
		playGame(rollingDice);
	}

	// This will help mock the dice for testability purposes
	public void playGame(IRollingDice rollingDice) {
		System.out.println("\n****** Game STARTING *********");
		System.out.println("Playing order of player is " + playerList.toString() + "\n");

		playNewRound(rollingDice);
	}

	public void playNewRound(IRollingDice rollingDice) {
		System.out.println("--- New Round ---");

		playerList.forEach((player) -> {
			// Give the turn to a player
			playPlayerTurn(player, rollingDice);
		});

		printplayerScores();
	}

	public void playPlayerTurn(String playerName, IRollingDice rollingDice) {
		// @TBD skip turn if penalty

		// Roll the dice
		int score = rollingDice.rollNext();
		System.out.println("\t" + playerName + " you scored " + score + " in this turn");

		// @TBD Check if it is bonusFace, then roll one more time

		// Update player with new score
		if (playerScoresMap.containsKey(playerName) ) {
			playerScoresMap.get(playerName).add(score);
		} else {
			// First time iteration, add the player to board
			playerScoresMap.put(playerName, new ArrayList<Integer>());
			playerScoresMap.get(playerName).add(score);
		}
	}

	void printplayerScores() {
		// @TBD sort them to get the leader board
		System.out.println("==== Player scores ====");

		playerScoresMap.forEach((player, scores) -> {
			System.out.println(player + " scored :- " +
				(scores.stream().mapToInt(Integer::intValue).sum() +
					" [" + scores.stream().map(Object::toString).collect(Collectors.joining(",")) + "]" ));
		});

		System.out.println("========");
	}
}