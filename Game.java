import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = true;   // Debugging flag.
    public static final int MAX_LOCALES = 8;        // Total number of rooms/locations we have in the game.
    public static int currentLocale = 0;            // Player starts in locale 0.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static Locale[] locations;               // An uninitialized array of type Locale. See init() for initialization.
    public static int[][] nav;                     // An uninitialized array of type int int.
    public static int moves = 0;                    // Counter of the player's moves.
    public static int score = 0;                    // Tracker of the player's score.
    public static float achievement = 0;              // This is the ratio of scores to moves.
    public static  Item [] playerItem;              // An uninitialized array of type Item.
    public static  Item [] inventory = new Item[5];  // An array of items that the player took.
    public static int takenItem = 0;                // Number of items in the playerTook list.

    public static void main(String[] args) {
        if (DEBUGGING) {
            // Display the command line args.
            System.out.println("Starting with args:");
            System.out.println("Title of Game -- SIMPLE GAME");
            System.out.println("This Game starts from the Desert. You are currently in the Desert.");

            for (int i = 0; i < args.length; i++) {
                System.out.println(i + ":" + args[i]);
            }
        }

        // Set starting locale, if it was provided as a command line parameter.
        if (args.length > 0) {
            try {
                int startLocation = Integer.parseInt(args[0]);
                // Check that the passed-in value for startLocation is within the range of actual locations.
                if (startLocation >= 0 && startLocation <= MAX_LOCALES) {
                    currentLocale = startLocation;
                }
            } catch (NumberFormatException ex) {   // catch(Exception ex)
                System.out.println("Warning: invalid starting location parameter: " + args[0]);
                if (DEBUGGING) {
                    System.out.println(ex.toString());
                }
            }
        }

        // Get the game started.
        init();
        updateDisplay();

        // Game Loop
        while (stillPlaying) {
            getCommand();
            navigate();
            updateDisplay();
        }

        // Code to execute when user types quit or q to end the game.
        System.out.println("Thank you for playing.");
    }

    private static void init() {



        // Initialize any uninitialized globals.
        command = new String();
        stillPlaying = true;   // TODO: Do we need this?

        //location instances of the Locale class and Inventory instances of the Item Class.
        Locale loc0 = new Locale(0);
        loc0.setName("Desert");
        loc0.setDesc("It is windy on the Desert.");
        loc0.setNext("You can go east or south but cannot go west or north."); // where user can move to from currentLocation.

        Item item0 = new Item(0, "map");
        item0.setName("map");
        item0.setDesc("You can use the map for further directions. ");
        item0.setItem(loc0);

        Locale loc1 = new Locale(1);
        loc1.setName("The Jungle");
        loc1.setDesc("You have to be careful, there are some elephants here.");
        loc1.setNext("You can go west, east or south but cannot go north.");

        Item item1 = new Item(1,"");
        item1.setName("");
        item1.setDesc("");
        item1.setItem(loc1);


        Locale loc2 = new Locale(2);
        loc2.setName("The Cave");
        loc2.setDesc("The cave is safe");
        loc2.setNext("You can go west or south but cannot go east or north.");

        Item item2 = new Item(2,"Gold Coin");
        item2.setName("Gold Coin");
        item2.setDesc("This is a prestigious coin");
        item2.setItem(loc2);

        Locale loc3 = new Locale(3);
        loc3.setName("Red Sea");
        loc3.setDesc("You are few miles away from Egypt.");
        loc3.setNext("You can go north, south or east but cannot go west.");

        Item item3 = new Item(3,"");
        item3.setName("");
        item3.setDesc("");
        item3.setItem(loc3);

        Locale loc4 = new Locale(4);
        loc4.setName("Magic Shoppe");
        loc4.setDesc("This is your magic center ");
        loc4.setNext("You can go north, south, east or west");

        Item item4 = new Item(4,"");
        item4.setName("broom, hat, cup and a vessel");
        item4.setDesc("There are four magic items for sale ");
        item4.setItem(loc4);

        Locale loc5 = new Locale(5);
        loc5.setName("Rain Forest");
        loc5.setDesc("It is raining here.");
        loc5.setNext("You can go north, south or west but cannot go east");

        Item item5 = new Item(5,"Umbrella");
        item5.setName("Umbrella");
        item5.setDesc("It is raining,you can take the umbrella ");
        item5.setItem(loc5);

        Locale loc6 = new Locale(6);
        loc6.setName("Island");
        loc6.setDesc("Welcome to the mysterious Island.");
        loc6.setNext("You can go north and east but cannot go south or west");

        Item item6 = new Item(6,"Boat");
        item6.setName("Boat");
        item6.setDesc("There is a nice boat on this Island ");
        item6.setItem(loc6);

        Locale loc7 = new Locale(7);
        loc7.setName("Lake");
        loc7.setDesc("You are at the sacred Lake.");
        loc7.setNext("You can go north, west or east but cannot go south");

        Item item7= new Item(0,"");
        item7.setName("");
        item7.setDesc("");
        item7.setItem(loc7);

        Water loc8 = new Water(8); // Locale(8);
        loc8.setName("River");
        loc8.setDesc("Welcome to the deepest river in the world.");
        loc8.setNext("You can go north or west but cannot go south or east");
        loc8.setNearestLagoon("Kole Lagoon");

        Item item8 = new Item(8,"");
        item8.setName("");
        item8.setDesc("");
        item8.setItem(loc8);

        // Items in each location
        playerItem = new Item[9];
        playerItem[0] = item0;  // Map
        playerItem[1] = item1; //null
        playerItem[2] = item2; //Gold Coin
        playerItem[3] = item3; //null
        playerItem[4] = item4; //broom, hat, cup and a vessel
        playerItem[5] = item5;  // Umbrella
        playerItem[6] = item6;  //Boat
        playerItem[7] = item7;  //null
        playerItem[8] = item8;  //null



        // Set up the location array.
        locations = new Locale[9];
        locations[0] = loc0; // "Desert";   //  ^
        locations[1] = loc1; // "The Jungle";  //  N
        locations[2] = loc2; // "Mountains";  //  |
        locations[3] = loc3; // "Red Sea";   //
        locations[4] = loc4; // "The Cave";   //
        locations[5] = loc5; // "Rain Forest";   //
        locations[6] = loc6; // "Island";   //
        locations[7] = loc7; // "Lake";   //
        locations[8] = loc8; // "River";   //


        // Set up the navigation matrix.
        nav = new int[][]{
                                  /* N   S   W   E */
                                  /* 0   1   2   3 */
            /* nav[0] for loc 0 */ {-1, 3, -1, 1},
            /* nav[1] for loc 1 */ {-1, 4, 0, 2},
            /* nav[2] for loc 2 */ {-1, 5, 1, -1},
            /* nav[3] for loc 3 */ { 0, 6, -1, 4},
            /* nav[4] for loc 4 */ { 1, 7, 3, 5},
            /* nav[5] for loc 5 */ { 2, 8, 4, -1},
            /* nav[6] for loc 6 */ { 3, -1, -1, 7},
            /* nav[7] for loc 7 */ { 4, -1, 6, 8},
            /* nav[8] for loc 8 */ { 5, -1, 7, -1},
        };
    }




    private static void updateDisplay() {
        System.out.println(locations[currentLocale].getName());
        System.out.println(locations[currentLocale].getDesc());
        System.out.println(locations[currentLocale].getNext());

       if (!playerItem[currentLocale].getHasTaken()) {
        System.out.println(playerItem[currentLocale].getName());
        System.out.println(playerItem[currentLocale].getDesc());



      }
    }

    private static void getCommand() {
        System.out.print("[" + moves + " moves, score " + score + "," + " achievement " + achievement + "] ");
        Scanner inputReader = new Scanner(System.in);
        command = inputReader.nextLine();  // command is global.
    }

    private static void navigate() {
        final int INVALID = -1;
        int dir = INVALID;  // This will get set to a value > 0 if a direction command was entered.

        if (command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n")) {
            dir = 0;
        } else if (command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s")) {
            dir = 1;
        } else if (command.equalsIgnoreCase("west") || command.equalsIgnoreCase("w")) {
            dir = 2;
        } else if (command.equalsIgnoreCase("east") || command.equalsIgnoreCase("e")) {
            dir = 3;
        } else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q")) {
            quit();
        } else if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h")) {
            help();
        } else if (command.equalsIgnoreCase("take") || command.equalsIgnoreCase("t")) {
            take();
        } else if (command.equalsIgnoreCase("inventory") || command.equalsIgnoreCase("i")) {
            invt();
        } else if (command.equalsIgnoreCase("map") || command.equalsIgnoreCase("m")) {
            map();
        } else {
            System.out.println(" You entered an invalid command");
            System.out.println(" The valid commands are n/north to go north, s/south to go south ");
            System.out.println(" e/east to go east, w/west to go west. ");
            System.out.println(" h/help for help, q/quit to quit the game, m/map to display the map of the game ");
            System.out.println(" t/take to take an item into your inventory and i/inventory to view items in your inventory ");
            System.out.println(" ");

        }; // code to execute if user enters an invalid command

        if (dir > -1) {   // This means a dir was set.
            int newLocation = nav[currentLocale][dir];
            if (newLocation == INVALID) {
                System.out.println("You cannot go that way.");
            } else {
                currentLocale = newLocation;
                moves = moves + 1;
                if (!locations[currentLocale].getHasVisited())
                {
                    score = score + 5;
                    locations[currentLocale].setHasVisited(true);
                }

                achievement = (float)score / moves;

            }
        }
    }

    private static void help() {
        System.out.println("The commands are as follows:");
        System.out.println("   n/north");
        System.out.println("   w/west");
        System.out.println("   e/east");
        System.out.println("   s/south");
        System.out.println("   m/map");
        System.out.println("   i/inventory");
        System.out.println("   t/take");
        System.out.println("   q/quit");
    }

    private static void map() {


            if (playerItem[currentLocale].getHasTaken()) {

            System.out.println("[" + locations[0].getName() + " ]-----[" + locations[1].getName() + "]-------[" + locations[2].getName() + "]    ");
            System.out.println("  |         |          |     ");
            System.out.println("  |         |          |        ");
            System.out.println("[" + locations[3].getName() + "]-----[" + locations[4].getName() + "]-----[" + locations[5].getName() + "] ");
            System.out.println("  |        |          |               ");
            System.out.println("  |        |          |        ");
            System.out.println("[" + locations[6].getName() + " ]-----[" + locations[7].getName() + "]---------[" + locations[8].getName() + "]       ");

                playerItem[currentLocale].setHasTaken(true);
        }
      }


    private static void take() {

              if (!playerItem[currentLocale].getHasTaken()) {

               System.out.println( playerItem[currentLocale].getName());

                inventory[takenItem] = playerItem[currentLocale];
                takenItem = takenItem + 1;
                  playerItem[currentLocale].setHasTaken(true);

          }
        }

   private static void invt() {
            System.out.println("You have " + takenItem + " item(s) in your inventory" );
            System.out.println("Inventory : ");
       for (int i = 0; i <takenItem; i++) {
           System.out.print(i + ":" + inventory[i]);
       }

       System.out.println(" ");
   }

    private static void quit() {
        stillPlaying = false;
    }
}
