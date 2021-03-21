# Multi Player Dice Game 
- A simple object oriented console application to simulate a Dice Game with multiple player

## How to run it

1. Compile and build using below command
```unix
$ mvn clean package
```

You can also only compile not build using below command to check code is in state of building
```unix
$ mvn clearn compile
```

if `mvn clean package` fails due to test cases failure try below command, which disables test cases, it should still allow you to run the application without harm, rest of the steps will be same

```unix
$ mvn clean package -DskipTests=true
```

2. Verify jar file is getting created by verifying the results from below command
```unix
$ ls -l ./target/
```

3. Run the application 
```unix
$ java -jar target/mp_dge_poc-1.0.0.jar
```

4. Run test cases
```unix
$ mvn clean test
```

### Assumptions made
- Minimum 2 players are needed to play the game, as its called multi player game
- Max game point cannot be zero or less
- If a player gets bonus option, while rolling again gets a bonus another bonus is not awarded
- If a player does not confirm the prompt to roll the dice, a turn is skipped 
- Without completing the game, its not possible to assign a rank, however in displaying player rank after turn ends, players are ordered by their rank, however it can change in each turn, though end game's ranks are maintained well and need not be same as player rank shown at the end of last round (difficult to understand ?)
- By default have disabled printing player scores and rank after every roll as viewing it is very misleading or confusing, however it is configurable in the code 
- By default prompting user for rolling of dice is disabled, can be changed at code
- When user consecutively scores 1, then a turn is skipped, however 0 is considered as score of such turn, to break the penalty cycle

### Known issues
- some places exception not handled, sorry