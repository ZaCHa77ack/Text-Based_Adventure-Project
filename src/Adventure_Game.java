/*
    Zachary Turner, Michael Ellis, Jackson Carlstrom, Nathaniel English
    CSCI 4300 - Software Engineering
    Final Project
 */

// Swing and AWT are being utilized to run the text-based adventure game in a GUI and allow for user input via the
// keyboard through the input field.
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Adventure_Game extends JFrame
{
    private List<Room> rooms;
    public Player player;
    private Enemy currentEnemy = null;
    private boolean waitingForPlayerName;
    private JTextArea textArea;
    private JTextField inputField;
    private boolean combatInProgress = false;
    private JScrollPane scroll;
    private ArrayList<Enemy> currentEnemies;

    // Commands that can be used on the main menu.
    public enum Command
    {
        START, QUIT, RESTART, TAKE, INVENTORY, CHECK, EQUIP, DROP, USE, UNEQUIP, STATUS, INSPECT,
        ATTACK, DEFEND, NORTH, SOUTH, EAST, WEST, LOOK, INVALID
        // GOBLIN_ENCOUNTER
    }

    // The different game states (QUIT exits the game)
    public enum GameState
    {
        MAIN_MENU, IN_GAME, END_GAME, QUIT
    }

    // Sets the game state of the text-based adventure game.
    private GameState gameState;

    // Call the initGUI() method to generate a window for the game.
    public Adventure_Game()
    {
        initGUI();
        setResizable(false);
        setLocationRelativeTo(null);
        waitingForPlayerName = false;
        List<Enemy> enemies = new ArrayList<>();
    }

    // initGUI() method that will run the game and accept user input and output text.
    private void initGUI()
    {
        // Set the title and window size. (The window size is adjusted depending on the screen resolution.).
        setTitle("Slay the Minotaur");
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension screen = tool.getScreenSize();
        double scale1 = 0.6;
        double scale2 = 0.75;
        int width = (int) (screen.width * scale1);
        int height = (int) (screen.height * scale2);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // textArea will generate
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.YELLOW);

        // The following lines set the window size to some percentage of a default size: 1920 x 1080. The screen and font size
        // are adjusted accordingly.
        double baseWidth = 1920;
        double baseHeight = 1080;
        double baseFont = 12;

        double widthRatio = screen.getWidth() / baseWidth;
        double heightRatio = screen.getHeight() / baseHeight;
        double fontScale = Math.min(widthRatio, heightRatio);
        int fontSize = (int) (baseFont * fontScale);
        textArea.setFont(new Font("Courier New", Font.PLAIN, fontSize));

        // Add in a scroll pane with a vertical scrollbar.
        scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        textArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // Add in an input field for the player to type in commands.
        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String input = inputField.getText();
            displayText("\n>> " + input + "\n");
            inputField.setText("");
            try {
                processInput(input);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.YELLOW);
        inputField.setMargin(new Insets(0, 10, 0, 10));
        inputField.setFont(new Font("Sans-Serif", Font.PLAIN, 16));

        // Set the focus to the inputField on startup.
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                inputField.requestFocusInWindow();
            }
        });

        // Declare the components of the GUI.
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(inputField, BorderLayout.SOUTH);
        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(0, 10));
        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(0, 10));
        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(10, 0));
        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(10, 0));

        // Add the components to the main frame
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(eastPanel, BorderLayout.EAST);
        getContentPane().add(westPanel, BorderLayout.WEST);
    }

    // Display messages and content to the player.
    public void displayText(String text)
    {
        textArea.append(text + "\n");
    }

    // processInput method that will set the player's game state according to input.
    public void processInput(String input) throws InterruptedException
    {
            if (waitingForPlayerName)
            {
                player = new Player(input, 1, 150, 8, 13, 10, 95, new ArrayList<>());
                gameState = GameState.IN_GAME;
                waitingForPlayerName = false;

                // A little easter egg when entering the player name.
                if (player.getName().equalsIgnoreCase("adventurer"))
                {
                    displayText("Be more creative. Enter something else.");
                    waitingForPlayerName = true;
                }
                else if (player.getName().equalsIgnoreCase("something else"))
                {
                    displayText("Very clever. Now give me your name!");
                    waitingForPlayerName = true;
                }
                else if (player.getName().equalsIgnoreCase("your name"))
                {
                    displayText("Enough with your silly games! Your name is now Joker, for the clown that you are!");
                    player.setName("Joker");

                    waitingForPlayerName = false;
                    gameState = GameState.IN_GAME;
                    displayText("Welcome, " + player.getName() + "! Let the adventure begin!\n");
                    displayText("""
                            The following commands may be used outside of combat:
                            - GO (direction)
                            - TAKE (item)
                            - STATUS
                            - INVENTORY
                            - INSPECT (item)
                            - EQUIP (item)
                            - UNEQUIP (item)
                            - USE (item)
                            - DROP (item)
                            - RESTART (restarts the game)
                            - QUIT
                            """);

                    displayText("""
                            The following commands are available when in combat:
                            - STATUS
                            - CHECK (enemy)
                            - EQUIP
                            - UNEQUIP
                            - ATTACK (enemy)
                            - DEFEND
                            - USE (item)
                            """);
                }
                else
                {
                    displayText("Welcome, " + player.getName() + "! Let the adventure begin!\n");
                    displayText("""
                            -------------------------------------------------------
                            The following commands may be used outside of combat:
                            - GO (direction)
                            - TAKE (item)
                            - STATUS
                            - INVENTORY
                            - INSPECT (item)
                            - EQUIP (item)
                            - UNEQUIP (item)
                            - USE (item)
                            - DROP (item)
                            - LOOK AROUND
                            - RESTART (restarts the game)
                            - QUIT (quit the game)
                            -------------------------------------------------------
                            """);

                    displayText("""
                            -------------------------------------------------------
                            The following commands are available when in combat:
                            - STATUS
                            - CHECK (enemy name)
                            - EQUIP
                            - UNEQUIP
                            - ATTACK (enemy name)
                            - DEFEND
                            - USE (item)
                            --------------------------------------------------------
                            """);
                    createRooms();
                    Room startingRoom = getStartingRoom();
                    player.setCurrentRoom(startingRoom);
                    displayRoom(player.getCurrentRoom());
                }
                return;
            }
        switch (gameState)
        {
            case MAIN_MENU ->
            {
                Command mainMenuCommand = parseCommand(input);
                handleMainMenu(mainMenuCommand);
            }
            case IN_GAME -> handleInGame(input);
            case QUIT ->
            {
                System.exit(0);
            }
            case END_GAME ->
            {
                Command command = parseCommand(input);
                if (command == Command.RESTART)
                {
                    restartGame();
                }
                else if (command == Command.QUIT)
                {
                    System.exit(0);
                }
            }
            default ->
            {
            }
        }
    }

    // handleMainMenu method that will perform the appropriate actions when START, QUIT, or an invalid command are entered.
    private void handleMainMenu(Command command)
    {
        switch (command)
        {
            case START ->
            {
                displayText("What is your name, adventurer?");
                gameState = GameState.MAIN_MENU;
                waitingForPlayerName = true;
            }
            case QUIT ->
            {
                System.exit(0);
            }
            default -> displayText("Insolent fool! Make your choice!");
        }
    }

    // handleStartMenu method is used to start the game and then retrieve the player's typed name.


    // handleInGame method is used to handle player inputs when GameState = IN_GAME.
    // It can handle the following cases and differentiate between if the player being
    // in and out of combat.
    private void handleInGame(String input) throws InterruptedException {
        String itemName;
        Command command = parseCommand(input);
        if (!player.getCurrentRoom().getEnemies().isEmpty())
        {
            switch (command)
            {
                case USE:
                    itemName = getItemNameFromInput(input);
                    useItem(itemName);
                    break;
                case EQUIP:
                    itemName = getItemNameFromInput(input);
                    Item itemToEquip = player.findItemByName(itemName);
                    if (itemToEquip instanceof Armor)
                    {
                        player.unequipArmor(player.getEquippedArmor());
                        displayText(player.equipArmor((Armor) itemToEquip));
                    }
                    else if (itemToEquip instanceof Weapon)
                    {
                        player.unequipWeapon(player.getEquippedWeapon());
                        displayText(player.equipWeapon((Weapon) itemToEquip));
                    } else
                    {
                        displayText("You cannot do that.");
                    }
                    break;
                case UNEQUIP:
                    itemName = getItemNameFromInput(input);
                    Item itemToUnequip = player.findItemByName(itemName);
                    if (itemToUnequip instanceof Armor)
                    {
                        displayText(player.unequipArmor((Armor) itemToUnequip));
                    } else if (itemToUnequip instanceof Weapon)
                    {
                        displayText(player.unequipWeapon((Weapon) itemToUnequip));
                    } else
                    {
                        displayText("You cannot do that.");
                    }
                    break;
                case ATTACK:
                    if (combatInProgress)
                    {
                        String enemyName = getEnemyNameFromInput(input);
                        if (enemyName != null)
                        {
                            List<Enemy> enemiesWithSameName = player.getCurrentRoom().findEnemyByName(enemyName);

                            if (!enemiesWithSameName.isEmpty())
                            {
                                Enemy target = enemiesWithSameName.get(0);
                                player.attack(target, this);

                                if (!target.isAlive())
                                {
                                    int expGained = target.getExperienceValue();
                                    boolean leveledUp = player.gainExperience(expGained);
                                    displayText("You have slain " + target.getName() + " and gained " + expGained + " experience!\n");

                                    if (leveledUp)
                                    {
                                        displayText("Congratulations! You have reached level " + player.getLevel() + "\n");
                                    }
                                    player.getCurrentRoom().removeEnemy(target);

                                    if (target.getName().equalsIgnoreCase("Karovard"))
                                    {
                                        endGame();
                                        break;
                                    }

                                    if (player.getCurrentRoom().getEnemies().isEmpty())
                                    {
                                        combatInProgress = false;
                                        displayText("The room falls silent...");
                                    }
                                }
                                Thread.sleep(500);
                                for (Enemy enemy : player.getCurrentRoom().getEnemies())
                                {
                                    if (enemy.isAlive())
                                    {
                                        EnemyAction enemyAction = enemy.chooseAction();
                                        enemyAction.execute(enemy, player);

                                        if (!player.isAlive())
                                        {
                                            displayText("You were defeated by the " + enemy.getName() + ".\n" +
                                                    "Enter RESTART to play the game again from the beginning.\n");
                                            combatInProgress = false;
                                            gameState = GameState.END_GAME;
                                        }
                                    }
                                }
                            }
                            else
                            {
                                displayText("There is no enemy by that name.");
                            }
                        }
                        else
                        {
                            displayText("Specify the enemy you wish to attack.");
                        }
                    }
                    else
                    {
                        displayText("There is no enemy to attack.");
                    }
                    break;
                case DEFEND:
                    if (combatInProgress)
                    {
                        player.setRemainingDefenseTurns(1);
                        player.defend(this, currentEnemy);

                        for (Enemy enemy : player.getCurrentRoom().getEnemies())
                        {
                            EnemyAction enemyAction = currentEnemy.chooseAction();
                            enemyAction.execute(currentEnemy, player);

                            player.decreaseDefenseTurns();
                        }
                        Thread.sleep(500);
                    }
                    else
                    {
                        displayText("There is no enemy to defend against.");
                    }
                    break;
                case STATUS:
                    displayPlayerStatus();
                    break;
                case CHECK:
                    if (combatInProgress)
                    {
                        String enemyName = getEnemyNameFromInput(input);
                        if (enemyName != null)
                        {
                            List<Enemy> enemiesWithSameName = player.getCurrentRoom().findEnemyByName(enemyName);

                            if (!enemiesWithSameName.isEmpty())
                            {
                                Enemy target = enemiesWithSameName.get(0);
                                checkEnemy(target);
                            }
                            else
                            {
                                displayText("There is no enemy here by that name.");
                            }
                        }
                        else
                        {
                            displayText("Specify the enemy you want to check.");
                        }
                    }
                    else
                    {
                        displayText("there is no enemy to check.");
                    }
                    break;
                default:
                    displayText("Invalid command. Try something else.");
                    break;
            }
        }
        else
        {
            switch (command)
            {
                case INVENTORY -> displayInventory();
                case TAKE ->
                {
                    itemName = getItemNameFromInput(input);
                    if (itemName != null)
                    {
                        takeItem(itemName);
                    }
                    else
                    {
                        displayText("Specify what it is you wish to take.");
                    }
                }
                case DROP ->
                {
                    itemName = getItemNameFromInput(input);
                    if (itemName != null)
                    {
                        Item itemToDrop = player.findItemByName(itemName);
                        if (itemToDrop != null)
                        {
                            dropItem(itemName);
                        }
                        else
                        {
                            displayText("You don't have that.");
                        }
                    }
                    else
                    {
                        displayText("Specify what it is you wish to drop.");
                    }
                }
                case USE ->
                {
                    itemName = getItemNameFromInput(input);
                    if (itemName != null)
                    {
                        useItem(itemName);
                    }
                    else
                    {
                        displayText("Specify the item you wish to use.");
                    }
                }
                case EQUIP ->
                {
                    itemName = getItemNameFromInput(input);
                    Item itemToEquip = player.findItemByName(itemName);
                    if (itemToEquip instanceof Armor)
                    {
                        displayText(player.equipArmor((Armor) itemToEquip));
                    }
                    else if (itemToEquip instanceof Weapon)
                    {
                        displayText(player.equipWeapon((Weapon) itemToEquip));
                    }
                    else
                    {
                        displayText("You cannot do that.");
                    }
                }
                case UNEQUIP ->
                {
                    itemName = getItemNameFromInput(input);
                    Item itemToUnequip = player.findItemByName(itemName);
                    if (itemToUnequip instanceof Armor)
                    {
                        displayText(player.unequipArmor((Armor) itemToUnequip));
                    }
                    else if (itemToUnequip instanceof Weapon)
                    {
                        displayText(player.unequipWeapon((Weapon) itemToUnequip));
                    }
                    else
                    {
                        displayText("You cannot do that.");
                    }
                }
                case STATUS -> displayPlayerStatus();
                case INSPECT ->
                {
                    itemName = getItemNameFromInput(input);
                    Item item = player.findItemByName(itemName);
                    if (item != null)
                    {
                        displayText(item.getDescription());
                    }
                    else
                    {
                        displayText("You don't have an item called \"" + itemName + "\" in your inventory.");
                    }
                }
                case LOOK ->
                {
                    displayRoom(player.getCurrentRoom());
                }
                // case GOBLIN_ENCOUNTER -> startGoblinEncounter();
                case QUIT ->
                {
                    gameState = GameState.MAIN_MENU;
                    textArea.setText(null);
                    start();
                }
                case NORTH, SOUTH, EAST, WEST -> movePlayer(command);
                case RESTART -> restartGame();
                default -> displayText("Invalid command. Try something else.");
            }
        }
    }

    // parseCommand method that will determine which command must be run based on user input.
    // For now, it can handle the commands listed below.
    public Command parseCommand(String input)
    {
        String[] parts = input.trim().toLowerCase().split(" ");
        String keyword = parts[0];

        String word = parts[0].toLowerCase();

        if (word.equalsIgnoreCase("check"))
        {
            return Command.CHECK;
        }

        // Allows for the commands related to traveling to different rooms to work.
        if (parts.length > 1 && keyword.equalsIgnoreCase("go"))
        {
            keyword = "go " + parts[1];
        }

        if (input.equalsIgnoreCase("look around"))
        {
            return Command.LOOK;
        }

        return switch (keyword)
        {
            case "start" -> Command.START;
            case "quit" -> Command.QUIT;
            case "restart" -> Command.RESTART;
            case "inventory" -> Command.INVENTORY;
            case "equip" -> Command.EQUIP;
            case "use" -> Command.USE;
            case "drop" -> Command.DROP;
            case "take" -> Command.TAKE;
            case "unequip" -> Command.UNEQUIP;
            case "status" -> Command.STATUS;
            case "inspect" -> Command.INSPECT;
            case "attack" -> Command.ATTACK;
            case "defend" -> Command.DEFEND;
            //case "gob" -> Command.GOBLIN_ENCOUNTER;
            case "go north" -> Command.NORTH;
            case "go south" -> Command.SOUTH;
            case "go east" -> Command.EAST;
            case "go west" -> Command.WEST;
            default -> Command.INVALID;
        };
    }

    // start() method starts the game and sets the game state to MAIN_MENU.
    public void start()
    {
        gameState = GameState.MAIN_MENU;
        // The game's title will be output as ASCII art at the top of the GUI.
        String TITLE_ASCII_ART =
                " $$$$$$\\  $$\\                           $$$$$$$$\\ $$\\                       $$\\      $$\\ $$\\                      $$\\\n" +
                        "$$  __$$\\ $$ |                          \\__$$  __|$$ |                      $$$\\    $$$ |\\__|                     $$ |\n" +
                        "$$ /  \\__|$$ | $$$$$$\\  $$\\   $$\\          $$ |   $$$$$$$\\   $$$$$$\\        $$$$\\  $$$$ |$$\\ $$$$$$$\\   $$$$$$\\ $$$$$$\\    $$$$$$\\  $$\\   $$\\  $$$$$$\\\n" +
                        "\\$$$$$$\\  $$ | \\____$$\\ $$ |  $$ |         $$ |   $$  __$$\\ $$  __$$\\       $$\\$$\\$$ $$ |$$ |$$  __$$\\ $$  __$$\\\\_$$  _|   \\____$$\\ $$ |  $$ |$$  __$$\\\n" +
                        " \\____$$\\ $$ | $$$$$$$ |$$ |  $$ |         $$ |   $$ |  $$ |$$$$$$$$ |      $$ \\$$$  $$ |$$ |$$ |  $$ |$$ /  $$ | $$ |     $$$$$$$ |$$ |  $$ |$$ |  \\__|\n" +
                        "$$\\   $$ |$$ |$$  __$$ |$$ |  $$ |         $$ |   $$ |  $$ |$$   ____|      $$ |\\$  /$$ |$$ |$$ |  $$ |$$ |  $$ | $$ |$$\\ $$  __$$ |$$ |  $$ |$$ |\n" +
                        "\\$$$$$$  |$$ |\\$$$$$$$ |\\$$$$$$$ |         $$ |   $$ |  $$ |\\$$$$$$$\\       $$ | \\_/ $$ |$$ |$$ |  $$ |\\$$$$$$  | \\$$$$  |\\$$$$$$$ |\\$$$$$$  |$$ |\n" +
                        " \\______/ \\__| \\_______| \\____$$ |         \\__|   \\__|  \\__| \\_______|      \\__|     \\__|\\__|\\__|  \\__| \\______/   \\____/  \\_______| \\______/ \\__|\n" +
                        "                        $$\\   $$ |\n" +
                        "                        \\$$$$$$  |\n" +
                        "                         \\______/\n";
        displayText(TITLE_ASCII_ART + "\n\n");
        displayText("Welcome, brave adventurer, to a new challenge!");
        displayText("Enter 'START' to begin or 'QUIT' to exit.");
    }

    // InputHandler class that utilizes the ActionListener to accept user input typed into the input field.
    private class InputHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String userInput = inputField.getText();
            try {
                processInput(userInput);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            inputField.setText("");
        }
    }



    // Displays the player's inventory when the command 'inventory' is used.
    private void displayInventory()
    {
        List<Item> inventory = player.getInventory();
        if (player.getInventory().isEmpty())
        {
            displayText("\nYour inventory is empty.");
        }
        else
        {
            StringBuilder inventoryText = new StringBuilder("Inventory:\n");
            for (Item item : inventory)
            {
                inventoryText.append("- ").append(item.getName()).append("\n");
            }
            displayText(inventoryText.toString());
        }
    }

    // Allows the player to take an item from a slain enemy or an item they have dropped from their inventory.
    // (Work in progress)
    private void takeItem(String itemName)
    {
        Room currentRoom = player.getCurrentRoom();
        Item itemToTake = currentRoom.findItemByName(itemName);

        if (itemToTake != null)
        {
            player.addItemToInventory(itemToTake);
            currentRoom.removeItem(itemToTake);
            displayText("You took the " + itemToTake.getName() + ".");
        }
        else
        {
            displayText("There is no " + itemName + " in this room.");
        }
    }

    // allows the player to drop items from their inventory.
    private void dropItem(String itemName)
    {
        Item itemToDrop = player.findItemByName(itemName);

        if (itemToDrop == null)
        {
            displayText("You don't have that in your inventory.");
        }
        else if (itemToDrop == player.getEquippedWeapon() || itemToDrop == player.getEquippedArmor())
        {
            displayText("You must unequip that item before you can drop it.");
        }
        else
        {
            player.getInventory().remove(itemToDrop);
            player.getCurrentRoom().addItem(itemToDrop);
            displayText("You dropped the " + itemToDrop.getName());
        }
    }

    // allows the player to use items, such as health potions.
    private void useItem(String itemName)
    {
        Item itemToUse = player.findItemByName(itemName);

        if (itemToUse != null)
        {
            if (itemToUse instanceof Health_Potion health_potion)
            {
                if (player.getHealth() == player.getMaxHealth())
                {
                    displayText("It won't have any effect.");
                }
                else
                {
                    health_potion.use(player, this);
                    player.removeItemFromInventory(itemToUse);
                }
            }
            else
            {
                displayText("You cannot use that item.");
            }
        }
        else
        {
            displayText("Specify what item you wish to use.");
        }
    }

    // gets the name of the item from the user's input.
    private String getItemNameFromInput(String input)
    {
        String[] inputParts = input.split(" ",2);
        if (inputParts.length > 1)
        {
            return inputParts[1].trim();
        }
        else
        {
            return "";
        }
    }

    private String getEnemyNameFromInput(String input)
    {
        String[] inputParts = input.split(" ",2);
        if (inputParts.length > 1)
        {
            return inputParts[1];
        }
        return null;
    }

    // Display the player's stats in-game when the STATUS command is input.
    private void displayPlayerStatus()
    {
        String weaponName = player.getEquippedWeapon() != null ? player.getEquippedWeapon().getName() : "None";
        String armorName = player.getEquippedArmor() != null ? player.getEquippedArmor().getName() : "None";

        displayText("Name: " + player.getName() + "\n" +
                "Level: " + player.getLevel() + "\n" +
                "Experience: " + player.getExperience() + "\n" +
                "Experience to next level: " + player.getExperienceToNextLevel() + "\n" +
                "Health: " + player.getHealth() + "\n" +
                "Damage: " + player.getMinDamage() + " - " + player.getMaxDamage() + "\n" +
                "AC: " + player.getArmor() + "\n" +
                "Weapon: " + weaponName + "\n" +
                "Armor: " + armorName);
    }

    private void checkEnemy(Enemy enemy)
    {
        displayText(enemy.getName() + "\n" +
                "Health: " + enemy.getHealth() + "\n" +
                "Damage: " + enemy.getMinDamage() + " - " + enemy.getMaxDamage() + "\n" +
                "AC: " + enemy.getArmor() + "\n" +
                enemy.getDescription());
    }

    // Purely for testing purposes to see if combat mechanics work.
    /*
    public void startGoblinEncounter()
    {
        currentEnemy = new Goblin(this);
        displayText("A wild " + currentEnemy.getName() + " appears!");
        startCombat(currentEnemy);
    }
     */

    private void createRooms()
    {
        // Rooms are initialized here with the following parameters:
        // Name and Description
        Room room1 = new Room("Room1", "You stand at the entrance of the lair of the Minotaur, your nose" +
                " is filled with a foul stench, but you must steel yourself as your adventure has only just begun." +
                " There's a door to the EAST and a door to the SOUTH.");
        Room room2 = new Room("Room2", "You approach a small guard post where the minotaur's minions have" +
                " started to set up, ready to fend against any assailants who dare approach. " +
                "There's a door to the EAST and a door to the WEST.");
        Room room3 = new Room("Room3", "You find yourself at a barracks within this dungeon, or at least " +
                "a sad excuse for one. This room bears the living quarters of the goblins you have become so used to" +
                " seeing down here. There's a door to the WEST and a door to the SOUTH.");
        Room room4 = new Room("Room4", "You step foot into a study of sorts, books of the darkest forms " +
                "of arcane mastery stretching as far as you can see. You witness also a few materials you assume are " +
                "used for spell-casting. There's a door to the NORTH and a door to the SOUTH.");
        Room room5 = new Room("Room5", "You stand before a brewery, a magical brewery to be exact. " +
                "The room has many pleasant and dire scents that would concern those uninitiated in the arcane, you hold" +
                " your nose for as long as you can not wanting to risk whatever magical reaction these scents could " +
                "trigger. There's a door to the NORTH and a door to the EAST.");
        Room room6 = new Room("Room6", "You behold a room of many animals dissected and spell scrolls " +
                "littered throughout the room, the makings of a powerful beast though unholy methods lie on the "
                + "cobblestone upon which you stand. There's a door to the EAST and a door to the WEST");
        Room room7 = new Room("Room7", "You approach the Tombs of Warriors, where good men who fought in" +
                " many wars were buried in glorious celebration. You see arcane glyphs that, upon reading, you realize " +
                "is a hex to resurrect the dead. There's a door to the NORTH and a door to the SOUTH");
        Room room8 = new Room("Room8", "You stand within a grandiose colosseum, with the very Minotaur " +
                "you came to defeat standing at the arena's center. You leap down to face the beast once and for all and" +
                " it takes note of your presence. It snorts and draws its weapon as you feel the end of this journey will" +
                " not come easily.");


        // Assign exits to rooms here.
        room1.addExit(Command.EAST, room2);
        room1.addExit(Command.SOUTH, room4);
        room2.addExit(Command.WEST, room1);
        room2.addExit(Command.EAST, room3);
        room3.addExit(Command.WEST, room2);
        room3.addExit(Command.SOUTH, room7);
        room4.addExit(Command.SOUTH, room5);
        room4.addExit(Command.NORTH, room1);
        room5.addExit(Command.NORTH, room4);
        room5.addExit(Command.EAST, room6);
        room6.addExit(Command.EAST, room8);
        room6.addExit(Command.WEST, room5);
        room7.addExit(Command.NORTH, room3);
        room5.addExit(Command.SOUTH, room8);

        // rooms will store all rooms as a list.
        rooms = Arrays.asList(room1, room2, room3, room4, room5);

        // set the player's current room
        player.setCurrentRoom(room1);

        // Declare items here.
        // Name, description, and modifier
        Weapon steelSword = new Weapon("Steel Sword", "A sharp steel sword, perfect for cutting enemies to " +
                "ribbons. (+20 damage)", 20);
        Weapon ironMace = new Weapon("Iron Mace", "An iron mace that has seen some wear and tear. " +
                "(+7 damage)", 7);
        Weapon swordOf1000Truths = new Weapon("Sword of 1,000 Truths", "the most powerful sword to ever exist.", 1000000);
        Armor plateMail = new Armor("Plate mail", "A pristine set of plate mail armor. " +
                "It suits you nicely. (+25 armor)", 25);
        Health_Potion health_potion = new Health_Potion("Health Potion", 150, "Restores 150 health");

        /*
            Add items to rooms here.
            Arguments to use:
            (Name, Description, Damage Modifier) for Weapons
            (Name, Description, Armor Value) for Armor
            (Name, healing amount, Description) for Health Potions
         */
        room1.addItem(steelSword);
        room5.addItem(plateMail);
        room1.addItem(health_potion);
        room1.addItem(ironMace);
        room1.addItem(swordOf1000Truths);

        // Add enemies to rooms here. They will be instantiated in the rooms they are set to.
        currentEnemies = new ArrayList<>();
        room2.addEnemy(new Goblin(this));
        room3.addEnemy(new Goblin(this));
        room3.addEnemy(new Goblin(this));
        room7.addEnemy(new Skeleton_Knight(this));
        room7.addEnemy(new Goblin(this));
        currentEnemies.addAll(room1.getEnemies());
        room4.addEnemy(new Cultist(this));
        room5.addEnemy(new Cultist(this));
        room5.addEnemy(new Goblin(this));
        room6.addEnemy(new Chimera(this));
        room8.addEnemy(new Minotaur(this));

    }

    // Return the room the player starts in.
    private Room getStartingRoom()
    {
        return rooms.get(0);
    }

    // Method that allows the player to move from one room to another.
    private void movePlayer(Command direction)
    {
        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom != null)
        {
            player.setCurrentRoom(nextRoom);
            displayRoom(nextRoom);

            if (!nextRoom.getEnemies().isEmpty())
            {
                startEncounter();
            }
        }
        // In the case that the player chooses a direction that does not have an exit.
        else
        {
            displayText("You run face-first into a wall.");
        }
    }

    // Returns the description of the room the player is currently in.
    public void displayRoom(Room room)
    {
        if (room != null)
        {
            displayText(room.getDescription());

            List<Item> items = room.getItems();
            if (!items.isEmpty())
            {
                displayText("\nYou see the following items: ");
                for (Item item : items)
                {
                    displayText("- " + item.getName());
                }
            }
        }
        else
        {
            displayText("Error: Invalid Room.");
        }
    }

    // If an enemy is assigned to a room, a fight is initiated upon the player entering the room.
    // For now, enemies respawn upon the room being revisited.
    private void startEncounter()
    {
        Room currentRoom = player.getCurrentRoom();
        List<Enemy> enemies = currentRoom.getEnemies();

        if (!enemies.isEmpty())
        {
            displayText("\nYou were set upon by the following enemies:");
            for (Enemy enemy : enemies)
            {
                displayText("- " + enemy.getName());
            }

            combatInProgress = true;
            currentEnemy = enemies.get(0);
        }
        else
        {
            displayText("The room is quiet...");
        }
    }

    public void endGame()
    {
        displayText("-------------------------------------------------------------------------------");
        displayText("Congratulations, you have defeated Karovard the Minotaur and saved the realm!");
        displayText("\nEnter 'QUIT' to exit the game or 'RESTART' to play again.\n");
        displayText("-------------------------------------------------------------------------------");
        gameState = GameState.END_GAME;
    }

    public void restartGame()
    {
        combatInProgress = false;
        gameState = GameState.MAIN_MENU;
        textArea.setText(null);
        start();
    }

    // The main program that will run the game.
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            Adventure_Game adventure = new Adventure_Game();
            adventure.setVisible(true);
            adventure.start();
        });
    }
}