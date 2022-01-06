# A P2P Sudoku Game
<h4>
    Tiziano Citro <br>
    t.citro5@studenti.unisa.it <br>
    Repository GitHub: https://github.com/TizianoCitro/sudoku-p2p
</h4>
<h5>
    String used to compute the homework ID: tizianocitro-29 <br>
    Computed string: d1435476d58ec4af01d52b7d85143698 <br>
    Character: d <br>
    Value: 4 <br>
    Project: Sudoku Game
</h5>

## Application
A P2P Sudoku challenge game. Each user can place a number of the sudoku game and if the number is not already placed the user takes 1 point. If it is already placed and it is right, the user takes
0 point, but in any other scenario the user loses 1 point. Users join games based on a 9 x 9 matrix, where all users who join a game are automatically notified whenever other users increment their score by placing a correct number and also when the game is completed.

## Functionalities
The application provides the functionalities described by the `SudokuGame` interface to each peer that connects to the network.
```java
public interface SudokuGame {
    
	Integer[][] generateNewSudoku(String _game_name);
    
	boolean join(String _game_name, String _nickname);

	List<String> joined();
    
	Integer[][] getSudoku(String _game_name);
    
	Integer placeNumber(String _game_name, int _i, int _j, int _number);
    
	boolean leave(String _game_name);
    
	boolean leaveNetwork();
}
```
Let's see each of them more in detail.

### Menu
When starting the application, to the user will be prompt the menu from which the user can choose what to do.

<img src="media/Menu.png">

### GenerateNewSudoku
It allows users to create a new Sudoku game by providing a name for it, in a way that the name has to be unique.
If it is not, then users won't be able to create the game.

From the menu, we can digit `1` and then enter the name we want for our game - `SUDOKU` in our case, thus we can create a new Sudoku game as following:

<img src="media/GenerateNewSudoku.png">

As you can see, the grid has some `X`, this is where you want to put your number. On how to do that, we will get there in a little while.
We have yet to join the game we have just created. So, let's do it next.

### Join
It allows users to join an already existing game by preventing them from joining a game that does not exist, that they have already joined or a game that they have not joined yet.

Let's try by joining the game we have created just before. To do so, we can digit `2` and then enter the name of the game we want to join. 
That's not all, at this point we have to enter the nickname we want to join the game with.

<img src="media/Join.png">

### Joined
It provides to the users the possibility to see games that have already been joined. If users have not joined a game yet, nothing will be retrieved.

We can see the games we have joined by simply digit `3`.

<img src="media/Joined.png">

### GetSudoku
It can show the grid of the Sudoku game to the user, so the user can choose the number to place while visualizing the grid.

Let's now see the grid of the game we have joined. To do so, we have to digit `4` and then enter the name of the game we have joined (`SUDOKU` in our case). 

<img src="media/GetSudoku.png">

### PlaceNumber
It allows users to place a number on the grid of a Sudoku game that they have joined already. Otherwise, users will not be able to place numbers.
When placing a number, if the number is not already placed the user takes 1 point. If it is already placed and it is right, the user takes 0 point, but in any other scenario the user loses 1 point.

Now it's time to place a number in our Sudoku game and earn some points.
Digit `5` and then enter the name of the game you want to place your number on.
Using the above grid we're going to place the number `4` at row `6` and column `2`.

<img src="media/PlaceNumber.png">

As you can see, we have just earned one point by placing the correct number.

### Leave
It makes it possible for users to leave a game they have joined already. Of course, they cannot leave games if they have not joined at least one game.

Time is come to leave the game we have played on so far. So, let's digit `6` and choose the game we want to leave from the list of games that will be prompt to us.

<img src="media/Leave.png">

### LeaveNetwork
It makes it possible for users to leave the whole network and by doing so leaving all the games they had joined.

Now that we have left the game, let's also leave the network. To do so digit `7`and enter `Y` to confirm.

<img src="media/LeaveNetwork.png">

## Solution
The application has been developed in `Java` by using `Maven` to manage and resolve application dependencies.
More in detail, the application uses several dependencies and the most important are:
- `TomP2P`: used to create and manage a Distributed HashTable (DHT) for enabling a peer-to-peer network, which each peer can join and then gain access to the resources stored in the DHT
- `JUnit`: used to achieve unit testing of the developed functionalities
- `TextIO`: used to manage the terminal for user interaction
- `Lombok`: used to minimize the boilerplate while creating classes

Dependencies are specified in the file pom.xml (Project Object Model). Some of them are shown in the following example:

```xml
<dependency>
    <groupId>net.tomp2p</groupId>
    <artifactId>tomp2p-all</artifactId>
    <version>5.0-Beta8</version>
</dependency>

<dependency>
<groupId>org.junit.jupiter</groupId>
<artifactId>junit-jupiter-engine</artifactId>
<version>5.5.2</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>LATEST</version>
    <optional>true</optional>
</dependency>
```
The project is organized in two main folder: `src` and `test` as shown below:

<img src="media/src.png">
<img src="media/test.png">

Let's go deep and provide a brief description of each package and class by diving more and more on the most important classes and functionalities.
In any case, each class and method is documented through the Javadoc, so if you need more explanation, feel free to go and read the documentation.

## SRC
Let's start with the src folder which contains the code of the whole application.

### Game
The class Game provides the main method that starts the whole application.
It is also responsible for managing user interaction and performing the proper action to the corresponding user input, as well as showing results to the user in the proper way.

### Listener
The listener package provides the interface `MessageListener` and the class `MessageListenerImpl`
that provides the capability to listen for messages sent through the network.

<img src="media/Listener.png">

More in detail, the listener receives the messages peers sent on the network when scoring a point by placing the correct number on the grid of the game and when the game is completed. 
When a message is received, the listener just output it on terminal for the user to see.

### Sudoku
In this package there are the most important classes, such as `SudokuGameImpl` or `Sudoku` and so on.

<img src="media/Sudoku.png">

### SudokuGame and SudokuGameImpl
SudokuGame is the interface we presented in a previous paragraph and SudokuGameImpl is the class that implements this interface and provides the actual functionalities.
It is responsible for managing the DHT where we store:
- the sudoku game grid as a matrix of the numbers on it
- the players that join a certain game, in order to allow us to notify them by sending direct messages every time a player scores a point or when a game is completed
- the nicknames used by the players when they join a certain game
- the scores of each player, thus we can keep track of them for showing the results when a game is completed

Let's see how we can interact with the DHT. In the following snippet we will show the `putSudoku` method that stores the grid of a Sudoku game in the DHT given a name for the game and the grid as a matrix of numbers.
Then we will show the `retrieveSudoku` method that retrieves a grid from the DHT given the name of a game. 

```java
public class SudokuGameImpl implements Sudoku {
    
    @Override
    private boolean putSudoku(String _game_name, Integer[][] cellsNumbers) {
        FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            dht.put(Number160.createHash(_game_name))
                    .data(new Data(cellsNumbers))
                    .start()
                    .awaitUninterruptibly();
            return true;
        }
        return false;
    }

    @Override
    private Integer[][] retrieveSudoku(String _game_name) {
        FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                return (Integer[][]) futureGet.dataMap()
                        .values()
                        .iterator()
                        .next()
                        .object();
            }
        }
        return null;
    }
}
```

### Model
The model package is crucial because it provides the classes that model the objects of the application domain.

<img src="media/Model.png">

The `Cell` class models a single cell in a grid, that is modelled by the `Grid` class. While, a whole game is modelled by the `Sudoku` class.

### Printer
The printer package provides the `SudokuPrettyPrinter` class used to print the grid of the game for the user to interact with it.

<img src="media/Printer.png">

Example of a printed grid for a Sudoku game:

<img src="media/PrintedGrid.png">

### Utils
Classes and enumerations in this package provides utility functionalities required to other classes to manage the effort.
For example, `Scores` enumeration provides the point a user gains or loses after attempting to place a number.

### Verifier
Both `SudokuVerifier` and `GridVerifier` provides method to verify the state of the game before and after user interaction.
As example, they're responsible to check whether the insertion of a number is correct or not.

### Utils
Finally, there's the utils package that provides utility classes and enumerations.
Slightly more in detail, the `GameOptions` enum provides the options from which a user can choose what to do when the application is up and running. 

## Test
Let's address now the test folder which contains the `SudokuGameTest` class where all the unit tests for the application are written.
We are going to go deeper in unit testing in the next paragraph. 


## Unit testing
The unit testing has been performed with the use of `JUnit`.

Tests are implemented in the "SudokuGameTest" class, in which each method of the `SudokuGame` interface (corresponding to the functionality of the application) is tested. 
Each method used for testing purposes is annotated with `@Test`, a JUnit annotation which specifies that the method is running a test case.
The order for test execution is described by the `@Order` annotation and for doing so, it is required to annotate the test class (in our case SudokuGameTest) as following:

```java
@TestMethodOrder(OrderAnnotation.class)
public class SudokuGameTest {

    @Test
    @Order(1)
    public void firstTest() {
    }

    @Test
    @Order(2)
    public void secondTest() {
        
    }
}
```

In this way the test case in firstTest method will be executed before the test case in secondTest method.

Crucial is the method `setup` that runs before all test (as the `@BeforeAll` annotation suggests) and allows us to create the peers we're going to use for our tests.
While the `leaveNetwork` method cleans everything after all tests are executed (`@AfterAll` annotation), the `leaveAllGames` method is executed after each test (`@AfteEach` annotation), so all peers we'll leave the games they have joined during the execution of the previous test. 
```java
public class SudokuGameTest {
    
    @BeforeAll
    public static void setup() {
        peer1 = new SudokuGameImpl(0, MASTER_IP, new MessageListenerImpl(0));
        peer2 = new SudokuGameImpl(1, MASTER_IP, new MessageListenerImpl(1));
        peer3 = new SudokuGameImpl(2, MASTER_IP, new MessageListenerImpl(2));
        peer4 = new SudokuGameImpl(3, MASTER_IP, new MessageListenerImpl(3));
    }

    @AfterAll
    public static void leaveNetwork() {
        peer1.leaveNetwork();
        peer2.leaveNetwork();
        peer3.leaveNetwork();
        peer4.leaveNetwork();
    }

    @AfterEach
    public void leaveAllGames() {
        leaveGames(peer1);
        leaveGames(peer2);
        leaveGames(peer3);
        leaveGames(peer4);
    }
}
```

Now that we have seen the overall configuration, let's go deep and see what each of the test actually does.

## Test cases
Let's go deeper into each of the test cases.

### Test 1: GenerateNewSudokuAndJoin
Tests the creation of a new Sudoku game by a user and the subsequent joining of other users into the created game.

### Test 2: GenerateAlreadyExistingSudoku
Checks that a peer cannot create a new game which will be a duplicate of an already existing game, considering that each game is identified by the name.

### Test 3: JoinNotExistingGame
Tests that it will not be possible for a user to join a game that does not exist.

### Test 4: JoinAlreadyJoinedGame
Tests that it will not be possible for a user to join a game that he or she has joined already.

### Test 5: JoinedGames
Checks the possibility for a user to retrieve the games that he or she has joined, in case at least one game has been joined.

### Test 6: NotJoinedGamesYet
Checks that no game is retrieved when users have not joined at least one game yet.

### Test 7: GetSudokuWhenJoined
Tests the possibility for a user to retrieve the grid of a joined Sudoku game when at least one game has been joined.

### Test 8: GetSudokuWhenGameDoesNotExist
Tests that the grid of a selected game is not retrieved when the user has not joined the game yet.

### Test 9: LeaveGame
Checks the possibility for a user to leave a game that he or she has joined previously.

### Test 10: LeaveNotJoinedGame
Tests that users cannot leave games that they have not joined.

### Test 11: LeaveNotExistingGame
Tests that users cannot leave games that do not exist.

### Test 12: AlreadyPlacedNumber
Tests the functionality of placing a number 
when the user tries to place a number that has already been placed 
and the tried number is correct. Thus, the user will receive 0 point.

### Test 13: PlaceCorrectNumber
Tests the functionality of placing a number 
when the user tries to place a number and the number is correct, which means that
the user will earn 1 point. Other users will be notified, so it tests that
all users who have joined the game have the new number in the grid of the game maintaining the grid consistent.

### Test 14: PlaceIncorrectNumber
Tests the functionality of placing a number
when the user tries to place a number and the number is incorrect, which results
in the user losing 1 point.

### Test 15: AlreadyPlacedNumberButIncorrect
Tests the functionality of placing a number
when the user tries to place a number that has already been placed,
but the tried number is incorrect. Thus, the user will lose 1 point.

### Test 16: PlaceNumberInNotJoinedGame
Tests that users cannot place a number in a game that they have yet to join.

### Test 17: PlaceNumberInNotExistingGame
Checks that users cannot place a number in a game that does not exist.

### Test 18: GenerateNewSudokuWithEmptyName
Tests that users cannot create a new game with an empty name.

### Test 19: JoinSudokuWithEmptyName
Tests that users cannot join when providing an empty name for the game.

### Test 20: JoinSudokuWithoutNickname
Tests that users cannot join a game without providing a nickname.

### Test 21: GetSudokuWhitEmptyName
Tests the possibility to retrieve a joined game grid by providing an empty name for the game.

### Test 22: PlaceNumberInRowLowerThanMin
Checks that users cannot place a number when specifying a wrong row index,
lower than the minimum possible. In our case the minimum is 1.

### Test 23: PlaceNumberInRowHigherThanMax
Checks that users cannot place a number when specifying a wrong row index,
higher than the maximum possible. In our case the maximum is 9.

### Test 24: PlaceNumberInColumnLowerThanMin
Checks that users cannot place a number when specifying a wrong column index,
lower than the minimum possible. In our case the minimum is 1.

### Test 25: PlaceNumberInColumnHigherThanMax
Checks that users cannot place a number when specifying a wrong column index,
higher than the maximum possible. In our case the maximum is 9.

### Test 26: PlaceNumberLowerThanMin
Tests that users cannot place a number when specifying a number 
lower than the minimum possible., with the minimum being 1.

### Test 27: PlaceNumberHigherThanMax
Tests that users cannot place a number when specifying a number
higher than the maximum possible, with the maximum being 9.

### Test results
In order to run tests, run the following command:

```bash
mvn test
```

Here are the results:

<img src="media/Results.png">

## Containerization
The application is provided with a `Dockerfile` that allows to build an image from which it becomes possible
to launch a container with the application up and running.

Here's the Dockerfile:

```dockerfile
FROM maven:3.5-jdk-8-alpine AS build
COPY src /app/src
COPY pom.xml /app/pom.xml
WORKDIR /app
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/SudokuP2P-1-jar-with-dependencies.jar /app/SudokuP2P.jar
ENV MASTERIP=127.0.0.1
ENV ID=0
CMD java -jar SudokuP2P.jar -m $MASTERIP -id $ID
```

### Launch the application with Docker
Make sure you have Docker started and then digit the following command from the root of the project, where the Dockerfile is located:

```bash
docker build --no-cache -t sudoku-p2p .
```

When the build process finishes, run the following command to start a container with the `MASTER` peer:

```bash
docker run -i --name MASTER-PEER -e MASTERIP="127.0.0.1" -e ID=0 sudoku-p2p
```

And then run the following command to start a second peer (we are calling it `PEER-ONE`):


```bash
docker run -i --name PEER-ONE -e MASTERIP="172.17.0.2" -e ID=1 sudoku-p2p
```

Now you should be able to interact with the application and follow along with what we have done through the documentation, for example.