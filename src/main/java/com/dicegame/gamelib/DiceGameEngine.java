package com.dicegame.gamelib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Comparator;

public class DiceGameEngine {
	// It is a list, it should maintain order players, unless changed explicitly
	ArrayList<String> playerList;
	int maxGamePoints;
	int penaltyOnScore;
	int bonusOnScore;
	int minDiceFace;
	int maxDiceFace;

	LinkedHashMap<String, ArrayList<Integer>> playerScoresMap;
	ArrayList<String> playerListByRank;

	// few configs
	boolean PRINT_SCORES_AT_EACH_PLAYER_TURN = Boolean.parseBoolean(System.getProperty("PRINT_SCORES_AT_EACH_PLAYER_TURN")) ? Boolean.parseBoolean(System.getProperty("PRINT_SCORES_AT_EACH_PLAYER_TURN")) : false;
	boolean PRINT_SCORES_AT_END_OF_ROUND = Boolean.parseBoolean(System.getProperty("PRINT_SCORES_AT_END_OF_ROUND")) ? Boolean.parseBoolean(System.getProperty("PRINT_SCORES_AT_END_OF_ROUND")) : true;
	boolean PROMPT_PLAYER_TO_ROLL = Boolean.parseBoolean(System.getProperty("PROMPT_PLAYER_TO_ROLL")) ? Boolean.parseBoolean(System.getProperty("PROMPT_PLAYER_TO_ROLL")) : false;

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
		playerListByRank = new ArrayList<String>();
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
		playerListByRank = new ArrayList<String>();
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

	public boolean hasPlayerCompleted(String playerName) {
		return playerListByRank.contains(playerName);
	}

	public void initPlayingSequence(boolean shufflePlayerOrder) {
		// Explicitly change the order of players
		if(shufflePlayerOrder)
			Collections.shuffle(playerList);
	}

	public void initPlayingSequence() {
		initPlayingSequence(true); // by default too shuffle the players
	}

	public void disablePromptingPlayerToRoll() {
		PROMPT_PLAYER_TO_ROLL = false;
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

		int rounds = 0;

		// Only role of this loop is to decide when to end the game, until then keeps playing rounds
		do {
			rounds++;

			playNewRound(rollingDice);

			if(PRINT_SCORES_AT_END_OF_ROUND) printplayerScores();

		} while(playerListByRank.size() != playerList.size()); // this check is bit volunurable, could be improved

		System.out.println("\n****** Game ENDED (in " + rounds + " rounds) *********\n");
		System.out.println("Players ranks in the order they finished ");
		for(int i=0; i < playerListByRank.size(); i++) {
			System.out.println("\t" + playerListByRank.get(i) + " is at Rank " + (i + 1));
		}
		System.out.println("\n****** Bye *********\n");
	}

	public void playNewRound(IRollingDice rollingDice) {
		System.out.println("--- New Round ---");

		playerList.forEach((player) -> {
			// check if player has already completed, if yes then skip the turn
			// Important to skip for the uses who have completed
			if(hasPlayerCompleted(player)) {
				System.out.println("\tSkipping turn for " + player + " as player has completed game already");
				return;
			}

			// Give the turn to a player
			playPlayerTurn(player, rollingDice);

			if(PRINT_SCORES_AT_EACH_PLAYER_TURN) printplayerScores();
		}); // end of iterating through all players
	}

	public void playPlayerTurn(String playerName, IRollingDice rollingDice) {
		// Encapsulates most logic and most happening part of the game

		int score = 0;

		// Check if player has to serve penalty
		boolean isPenalty = hasPenaltyForPlayer(playerName);

		if(isPenalty) {
			score = 0;
			System.out.println("\t[*] OOPS..! " + playerName + " must serve penalty due previous two consecutive turns, skipping rolling of dice..!");
			// Score for this turn will be 0
		} else {

			boolean confirmRoll = false;
			if(PROMPT_PLAYER_TO_ROLL) {
				confirmRoll = isPlayerConfirmRollingDice(playerName);
			} else {
				confirmRoll = true; // if we are not prompting, it as good as confirmed
			}

			if(confirmRoll) {
				// Roll the dice
				score = rollingDice.rollNext();
				System.out.println("\t" + playerName + " you scored " + score + " in this turn");
			} else {
				System.out.println("\t" + playerName + " chose to skip the turn ");
			}
		}

		int bonusScore = 0;
		if(score == bonusOnScore){
			System.out.println("\t[*] " + playerName + " you got one more chance to roll the dice ");
			bonusScore = rollingDice.rollNext();
			System.out.println("\t[*] " + playerName + " you scored bonus of " + bonusScore);
		}

		// Update player with new score
		if (playerScoresMap.containsKey(playerName) ) {
			playerScoresMap.get(playerName).add(score);
			if(bonusScore > 0) playerScoresMap.get(playerName).add(bonusScore);
		} else {
			// First time iteration, add the player to board
			playerScoresMap.put(playerName, new ArrayList<Integer>());
			playerScoresMap.get(playerName).add(score);
			if(bonusScore > 0) playerScoresMap.get(playerName).add(bonusScore);
		}

		// If player achieves max game points during this turn, add the player to completed player list to assign the rank
		int currentScore = getPlayerScore(playerName);
		if(currentScore >= maxGamePoints) {
			playerListByRank.add(playerName); // should preserve the order of adding, thus the index of the player is the rank

			System.out.println("\t[*] Congratulations " + playerName + " you have achieved max game points with rank of " + playerListByRank.size());
		}
	}

	public boolean hasPenaltyForPlayer(String playerName) {
		boolean hasPenalty = false;

		if ( ! playerScoresMap.containsKey(playerName) ) {
			// Still player has not played any turns
			hasPenalty = false;
		} else {
			if(playerScoresMap.get(playerName).size() >= 2) {
				// its not possible to check if player has not played at least 2 turns or rounds
				int prevScore = playerScoresMap.get(playerName).get(playerScoresMap.get(playerName).size() - 1);
				int lastPrevScore = playerScoresMap.get(playerName).get(playerScoresMap.get(playerName).size() - 2);

				// System.out.println(playerName + " has scores from last two rounds as " + prevScore + "," + lastPrevScore);

				if(prevScore == penaltyOnScore && lastPrevScore == penaltyOnScore) {
					hasPenalty = true;
				}
			} else {
				// not enough turns yet
				hasPenalty = false;
			}
		}
		return hasPenalty;
	}

	void printplayerScores() {
		// Sorting to make the player list look arranged by rank, but actually it is misleading, especially if a later player gats score higher than previously completed player
		sortPlayerScores();

		System.out.println("==== Player scores ====");

		playerScoresMap.forEach((player, scores) -> {
			System.out.println(player + " scored :- " +
				(scores.stream().mapToInt(Integer::intValue).sum() +
					" [" + scores.stream().map(Object::toString).collect(Collectors.joining(",")) + "]" ));
		});

		System.out.println("========");
	}

	boolean isPlayerConfirmRollingDice(String playerName) {
		String keyIn = System.console().readLine(playerName + " its your turn (press 'r' to roll the dice, any other key to skip) : ");

		keyIn = keyIn.toLowerCase();
		char key = keyIn.charAt(0);

		boolean isConfirm = key == 'r';
		return isConfirm;
	}

	void sortPlayerScores() {
		ArrayList<Map.Entry<String, ArrayList<Integer>>> playerList = new ArrayList<Map.Entry<String, ArrayList<Integer>>>();
		for(Map.Entry<String, ArrayList<Integer>> entry: playerScoresMap.entrySet()) {
       playerList.add(entry);
		}

		Comparator<Map.Entry<String, ArrayList<Integer>>> scoreComparator = new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
      @Override
      public int compare(Map.Entry<String, ArrayList<Integer>> elem1, Map.Entry<String, ArrayList<Integer>> elem2) {
          Integer sc1 = elem1.getValue().stream().mapToInt(Integer::intValue).sum();
          Integer sc2 = elem2.getValue().stream().mapToInt(Integer::intValue).sum();

          return sc1.compareTo(sc2);
      }
    };
    Collections.sort(playerList, Collections.reverseOrder(scoreComparator));

		// Flush out unsorted data
    playerScoresMap.clear();

    for(Map.Entry<String, ArrayList<Integer>> entry: playerList) {
			playerScoresMap.put(entry.getKey(), entry.getValue());
		}
	}
}