package com.dicegame.app;

import com.dicegame.gamelib.DiceGameEngine;

import java.util.Arrays;

public class DiceGameApplication {

	public static void main(String[] args) {
		System.out.println("=========== Welcome to Multi player Dice Game ===========");

		if(args.length < 2) {
			System.out.println("Usage: Please run with valid command line arguments as: <Number of Players> <Max Game points to score>");
			return;
		}

		int nbrOfPlayers = Integer.parseInt(args[0]);
		int maxGamePoints = Integer.parseInt(args[1]);

		if(nbrOfPlayers < 2 || maxGamePoints <= 0) {
			System.out.println("Invalid arguments, below conditions not met");
			System.out.println("\t- Minimum 2 players needed for multiplayer Dice game");
			System.out.println("\t- Max Game points should be non zero positive value");
			System.out.println("Please check and try with valid arguments..!");
			return;
		}

		System.out.println("Game will srat with " + nbrOfPlayers + " players, playing for " + maxGamePoints + " points");

		String[] playerNameList = new String[nbrOfPlayers];
		for(int i=0; i < nbrOfPlayers ; i++) {
			playerNameList[i] = new String("Player-" + (i + 1));
		}
		System.out.println("Names of players are " + Arrays.toString(playerNameList));

		DiceGameEngine diceGame = new DiceGameEngine(playerNameList, maxGamePoints);
		diceGame.initPlayingSequence(true); // Shuffle the player order
		// diceGame.playGame();
	}
}
