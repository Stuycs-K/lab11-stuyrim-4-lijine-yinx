import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Game {
  private static final int WIDTH = 80;
  private static final int HEIGHT = 30;
  private static final int BORDER_COLOR = Text.BLACK;
  private static final int BORDER_BACKGROUND = Text.WHITE + Text.BACKGROUND;

  private static ArrayList<Adventurer> enemies = new ArrayList<>();
  private static ArrayList<Adventurer> party = new ArrayList<>();

  public static void main(String[] args) {
    run();
  }

  // helper method
  public static void printColoredChar(char c, int foreground, int background) {
    String coloredChar = Text.colorize(String.valueOf(c), foreground, background);
    System.out.print(coloredChar);
  }

  // Display the borders of your screen that will not change.
  // Do not write over the blank areas where text will appear or parties will
  // appear.
  public static void drawBackground() {
    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    for (int row = 1; row <= HEIGHT; row++) {
      for (int col = 1; col <= WIDTH; col++) {
        if (row == 1 || row == HEIGHT || col == 1 || col == WIDTH) {
          Text.go(row, col);
          printColoredChar('#', BORDER_COLOR, BORDER_BACKGROUND);
        }
      }
    }
    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
  }

  // Display a line of text starting at
  // (columns and rows start at 1 (not zero) in the terminal)
  // use this method in your other text drawing methods to make things simpler.
  public static void drawText(String s, int startRow, int startCol) {
    try {
      Files.write(Paths.get("/tmp/game.log"),
          String.format("startRow=%d,startCol=%ds=%s\n", startRow, startCol, s).getBytes(), StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (Exception e) {

    }
    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    Text.go(startRow, startCol);
    // for (char c : s.toCharArray()) {
    String coloredChar = Text.colorize(s, Text.WHITE, Text.CYAN);
    System.out.print(coloredChar);

    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
  }

  /*
   * Use this method to place text on the screen at a particular location.
   * When the length of the text exceeds width, continue on the next line
   * for up to height lines.
   * All remaining locations in the text box should be written with spaces to
   * clear previously written text.
   * 
   * @param row the row to start the top left corner of the text box.
   * 
   * @param col the column to start the top left corner of the text box.
   * 
   * @param width the number of characters per row
   * 
   * @param height the number of rows
   */
  public static void TextBox(int row, int col, int width, int height, String text) {
    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    String[] words = text.split(" ");
    StringBuilder currentLine = new StringBuilder();
    int currentRow = row;

    String SPACE_FILL_78_COLS = "                                                                              ";
    if (row == HEIGHT - 3) {
      drawText(SPACE_FILL_78_COLS, HEIGHT - 3, 1);
      drawText(SPACE_FILL_78_COLS, HEIGHT - 1, 1);
    }
    if (row == HEIGHT + 1) {
      drawText(SPACE_FILL_78_COLS, HEIGHT + 1, 1);
    }
    if (row == HEIGHT + 1) {
      drawText(SPACE_FILL_78_COLS, HEIGHT + 1, 1);
    }

    for (String word : words) {
      if (currentLine.length() + word.length() + 1 > width) {
        drawText(currentLine.toString(), currentRow, col);
        currentRow++;
        if (currentRow >= row + height) {
          return;
        }
        currentLine = new StringBuilder();
      }
      if (currentLine.length() > 0) {
        currentLine.append(" ");
      }
      currentLine.append(word);
    }

    if (currentRow < row + height) {
      drawText(currentLine.toString(), currentRow, col);
      currentRow++;
    }
    while (currentRow < row + height) {
      String emptyLine = " ".repeat(width);
      drawText(emptyLine, currentRow, col);
      currentRow++;
    }
    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
  }

  // return a random adventurer (choose between all available subclasses)
  // feel free to overload this method to allow specific names/stats.
  public static Adventurer createRandomAdventurer() {
    if (Math.random() < 0.5) {
      return new CodeWarrior("Bob" + (int) (Math.random() * 100));
    } else if (Math.random() < 0.7) {
      return new Mage("Alice" + (int) (Math.random() * 100));
    } else {
      return new Healer("Gandolf" + (int) (Math.random() * 100));
    }
  }

  /*
   * Display a List of 2-4 adventurers on the rows row through row+3 (4 rows max)
   * Should include Name HP and Special on 3 separate lines.
   * Note there is one blank row reserved for your use if you choose.
   * Format:
   * Bob Amy Jun
   * HP: 10 HP: 15 HP:19
   * Caffeine: 20 Mana: 10 Snark: 1
   * ***THIS ROW INTENTIONALLY LEFT BLANK***
   */
  public static void drawParty(ArrayList<Adventurer> party, int startRow) {

    String SPACE_FILL_78_COLS = "                                                                              ";

    if (party.size() == 0) {
      drawText(SPACE_FILL_78_COLS, startRow + 1, 1);
      drawText(SPACE_FILL_78_COLS, startRow + 2, 1);
      return;
    }

    if (startRow == startRow) {
      drawText(SPACE_FILL_78_COLS, startRow, 1);
      drawText(SPACE_FILL_78_COLS, startRow + 1, 1);
      drawText(SPACE_FILL_78_COLS, startRow + 2, 1);
    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    int colWidth = WIDTH / party.size();

    for (int i = 0; i < party.size(); i++) {
      Adventurer a = party.get(i);
      int colStart = i * colWidth + 2;

      String nameStr = String.format("%-15s", a.getName());
      drawText(nameStr, startRow, colStart);

      String hpStr = colorByPercent(a.getHP(), a.getmaxHP());
      drawText("Hp: " + hpStr, startRow + 1, colStart);

      String specialStr = String.format("%-15s: %d/%d", a.getSpecialName(), a.getSpecial(), a.getSpecialMax());
      drawText(specialStr, startRow + 2, colStart);
    }

    drawText(" ", startRow + 3, 1);
    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
  }

  // Use this to create a colorized number string based on the % compared to the
  // max value.
  public static String colorByPercent(int hp, int maxHP) {
    if (maxHP <= 0) {
      return hp + "/" + maxHP;
    }

    double ratio = hp / (double) maxHP;
    String base = String.format("%3d/%3d", hp, maxHP);

    if (ratio < 0.25) {
      return Text.colorize(base, Text.RED);
    } else if (ratio < 0.75) {
      return Text.colorize(base, Text.YELLOW);
    } else {
      return Text.colorize(base, Text.WHITE);
    }

    // COLORIZE THE OUTPUT IF HIGH/LOW:
    // under 25% : red
    // under 75% : yellow
    // otherwise : white
  }

  // Display the party and enemies
  // Do not write over the blank areas where text will appear.
  // Place the cursor at the place where the user will by typing their input at
  // the end of this method.
  public static void drawScreen() {

    drawBackground();

    // draw player party
    drawParty(party, 2);

    // draw enemy party
    drawParty(enemies, 10);

    Text.go(HEIGHT + 1, 1);
  }

  public static String userInput(Scanner in) {
    // Move cursor to prompt location
    Text.go(20, 1);
    // show cursor
    Text.showCursor();

    System.out.print("                                                      ");

    // Move cursor to prompt location
    Text.go(20, 1);
    // show cursor
    Text.showCursor();

    System.out.print("Enter command: ");
    String input = in.nextLine();

    // clear the text that was written
    Text.hideCursor();

    Text.go(21, 1);
    String empty = " ".repeat(WIDTH - 2);
    System.out.print(empty);

    return input;
  }

  public static void quit() {
    Text.reset();
    Text.showCursor();
    Text.go(HEIGHT + 2, 1);
  }

  public static void run() {
    // Clear and initialize
    Text.hideCursor();
    Text.clear();

    // Things to attack:
    // Make an ArrayList of Adventurers and add 1-3 enemies to it.
    // If only 1 enemy is added it should be the boss class.
    // start with 1 boss and modify the code to allow 2-3 adventurers later.

    // ArrayList<Adventurer>enemies = new ArrayList<Adventurer>();
    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    // YOUR CODE HERE
    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
    int chance = (int) (Math.random() * 11);
    // fighting boss
    if (chance >= 5) {
      enemies.add(new Boss(" Goblin King", 120));
      party.add(new CodeWarrior("solo king", 90));
    }
    // fighting a party
    else if (chance < 5) {
      enemies.add(new Healer("Evil_Alice", 100));
      enemies.add(new Mage("Evil_Bob", 80));
      enemies.add(new CodeWarrior("Evil_Charlie", 90));

      party.add(new Healer("Alice", 100));
      party.add(new Mage("Bob", 80));
      party.add(new CodeWarrior("Charlie", 90));
    }
    // Adventurers you control:
    // Make an ArrayList of Adventurers and add 2-4 Adventurers to it.
    // ArrayList<Adventurer> party = new ArrayList<>();
    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    // YOUR CODE HERE
    // party.add(new Healer("Alice", 100));
    // party.add(new Mage("Bob", 80));
    // party.add(new CodeWarrior("Charlie", 90));

    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    boolean partyTurn = true;
    int whichPlayer = 0;
    int whichOpponent = 0;
    int turn = 0;
    String input = "";// blank to get into the main loop.
    Scanner in = new Scanner(System.in);
    // Draw the window border

    // You can add parameters to draw screen!
    drawScreen();// initial state.

    // Main loop

    // display this prompt at the start of the game.
    String preprompt = "Enter command for " + party.get(whichPlayer) + ": attack/support/special/quit";

    drawText(preprompt, HEIGHT + 1, 1);

    while (!(input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit"))) {
      // Read user input
      input = userInput(in);

      // example debug statment
      // TextBox(24,2,1,78,"input: "+input+" partyTurn:"+partyTurn+ "
      // whichPlayer="+whichPlayer+ " whichOpp="+whichOpponent );

      // display event based on last turn's input
      if (partyTurn) {

        // Process user input for the last Adventurer:
        Adventurer currentAdventurer = party.get(whichPlayer);

        if (input.startsWith("attack") || input.startsWith("a")) {
          /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
          // YOUR CODE HERE
          // if (enemies.isEmpty()){
          // TextBox(HEIGHT - 3, 2, 76, 3, "there are no enemies to attack");
          // } else {
          // Adventurer target = enemies.get(0);
          // String attackResult = currentAdventurer.attack(target);
          // TextBox(HEIGHT-2, 2, 76, 3, attackResult);

          // if(target.getHP() <= 0) {
          // enemies.remove(target);
          // TextBox(HEIGHT-2, 2, 76, 3, target.getName() + " has been defeated");
          // }
          // }

          String[] parts = input.split(" ");
          if (parts.length >= 2) {
            try {
              int targetIndex = Integer.parseInt(parts[1]);
              if (targetIndex >= 0 && targetIndex < party.size()) {

                if (enemies.isEmpty()) {
                  TextBox(HEIGHT - 3, 2, 76, 3, "there are no enemies to use attack on");
                } else {

                  Adventurer target = enemies.get(targetIndex);
                  String attackResult = currentAdventurer.attack(target);
                  TextBox(HEIGHT - 3, 2, 76, 3, attackResult);

                  TextBox(HEIGHT - 3, 2, 76, 3, attackResult);

                  if (target.getHP() <= 0) {
                    drawParty(party, targetIndex);
                    enemies.remove(target);
                    TextBox(HEIGHT - 3, 2, 76, 3, target.getName() + " has been defeated");
                  }
                }
                /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
              }

              else {
                TextBox(HEIGHT - 3, 2, 76, 3, "Invalid attack target");
              }
            } catch (NumberFormatException e) {
              TextBox(HEIGHT - 3, 2, 76, 3, "Invalid attack command");
            }
          } else {
            TextBox(HEIGHT - 3, 2, 76, 3, "Invalid attack command");

          }
          /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

        } else if (input.startsWith("special") || input.startsWith("sp")) {
          /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
          // YOUR CODE HERE
          String[] parts = input.split(" ");
          if (parts.length >= 2) {
            try {
              int targetIndex = Integer.parseInt(parts[1]);
              if (targetIndex >= 0 && targetIndex < party.size()) {

                if (enemies.isEmpty()) {
                  TextBox(HEIGHT - 3, 2, 76, 3, "there are no enemies to use special on");
                } else {

                  Adventurer target = enemies.get(targetIndex);
                  String specialResult = currentAdventurer.specialAttack(target);
                  TextBox(HEIGHT - 3, 2, 76, 3, specialResult);

                  TextBox(HEIGHT - 3, 2, 76, 3, specialResult);

                  if (target.getHP() <= 0) {
                    drawParty(party, targetIndex);
                    enemies.remove(target);
                    TextBox(HEIGHT - 3, 2, 76, 3, target.getName() + " has been defeated");
                  }
                }
                /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
              }

              else {
                TextBox(HEIGHT - 3, 2, 76, 3, "Invalid special target");
              }
            } catch (NumberFormatException e) {
              TextBox(HEIGHT - 3, 2, 76, 3, "Invalid special command");
            }
          } else {
            TextBox(HEIGHT - 3, 2, 76, 3, "Invalid special command");

          }
          /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
        }

        else if (input.startsWith("su ") || input.startsWith("support ")) {
          // "support 0" or "su 0" or "su 2" etc.
          // assume the value that follows su is an integer.
          /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
          // YOUR CODE HERE
          String[] parts = input.split(" ");
          if (parts.length >= 2) {
            try {
              int targetIndex = Integer.parseInt(parts[1]);
              if (targetIndex >= 0 && targetIndex < party.size()) {
                Adventurer target = party.get(targetIndex);
                String supportResult = currentAdventurer.support(target);
                TextBox(HEIGHT - 3, 2, 76, 3, supportResult);
              } else {
                TextBox(HEIGHT - 3, 2, 76, 3, "Invalid support target");
              }
            } catch (NumberFormatException e) {
              TextBox(HEIGHT - 3, 2, 76, 3, "Invalid support command");
            }
          } else {
            TextBox(HEIGHT - 3, 2, 76, 3, "Invalid support command");

          }
          /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
        }

        // You should decide when you want to re-ask for user input
        // If no errors:
        whichPlayer++;

        if (whichPlayer < party.size()) {
          // This is a player turn.
          // Decide where to draw the following prompt:
          String prompt = "Enter command for " + party.get(whichPlayer) + ": attack/special/support/quit";
          drawText(prompt, HEIGHT + 1, 1);

        } else {
          // This is after the player's turn, and allows the user to see the enemy turn
          // Decide where to draw the following prompt:
          String prompt = "press enter to see monster's turn";
          TextBox(HEIGHT + 1, 1, WIDTH - 2, 3, prompt);

          partyTurn = false;
          whichOpponent = 0;
        }
        // done with one party member
      } else {
        // not the party turn!
        if (enemies.isEmpty()) {
          TextBox(HEIGHT - 3, 2, 76, 3, "you won");
        }

        if (whichOpponent >= enemies.size()) {
          partyTurn = true;
          whichPlayer = 0;
          turn++;
          String prompt = "enter command for " + party.get(whichPlayer).getName() + ": attack/special/support/quit";
          drawText(prompt, HEIGHT + 1, 1);
          continue;
        }

        Adventurer currentEnemy = enemies.get(whichOpponent);

        if (party.isEmpty()) {
          drawParty(party, 0);
          TextBox(HEIGHT - 3, 2, 76, 3, "you lose as all your party members were defeated");
          break;
        }

        /*
         * Adventurer target = party.get((int) (Math.random() * party.size()));
         * if (enemies.size() == 3) {
         * whichOpponent = 0;
         * while (i < 3) {
         * Adventurer target = party.get((int) (Math.random() * party.size()));
         * if (target.getHP() <= 0) {
         * party.remove(target);
         * TextBox(HEIGHT - 3, 2, 76, 3, target.getName() + " has been defeated");
         * }
         * String enemyAttackResult = enemies.get(i).randomAttack(target);
         * TextBox(HEIGHT - 3, 2, 76, 1, enemyAttackResult);
         * i++;
         * }
         * }
         */ else {
          Adventurer target = party.get((int) (Math.random() * party.size()));

          String enemyAttackResult = currentEnemy.randomAttack(target);
          TextBox(HEIGHT - 3, 2, 76, 1, enemyAttackResult);

          if (target.getHP() <= 0) {
            party.remove(target);
            TextBox(HEIGHT - 3, 2, 76, 3, target.getName() + " has been defeated");
          }

        }

        // String enemyAttackResult = currentEnemy.attack(target);
        // enemy attacks a randomly chosen person with a randomly chosen attack.z`
        // Enemy action choices go here!
        /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
        // YOUR CODE HERE
        /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

        // Decide where to draw the following prompt:
        String prompt = "press enter to see next turn";
        TextBox(HEIGHT + 1, 1, WIDTH - 2, 3, prompt);
        whichOpponent++;

      } // end of one enemy.

      // modify this if statement.
      if (!partyTurn && whichOpponent >= enemies.size()) {
        // THIS BLOCK IS TO END THE ENEMY TURN
        // It only triggers after the last enemy goes.
        whichPlayer = 0;
        turn++;
        partyTurn = true;
        // display this prompt before player's turn
        if (party.size() != 0) {
          String prompt = "Enter command for " + party.get(whichPlayer) + ": attack/special/support/quit";
          drawText(prompt, HEIGHT + 1, 1);
        } else if (party.size() == 0) {
          drawParty(party, 0);
          String prompt = "you lost";
          drawText("                                                                   ", HEIGHT + 1, 1);
          drawText(prompt, HEIGHT + 1, 1);
        }
      }

      // display the updated screen after input has been processed.
      drawScreen();

      if (enemies.isEmpty()) {
        TextBox(HEIGHT - 3, 2, 76, 3, "all enemies have been defeated you won");
      }
      if (party.isEmpty()) {
        drawParty(party, 0);
        TextBox(HEIGHT - 3, 2, 76, 3, "all party members have been defeated you lose");
      }

    } // end of main game loop

    // After quit reset things:
    quit();
  }
}