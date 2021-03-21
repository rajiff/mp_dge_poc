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


