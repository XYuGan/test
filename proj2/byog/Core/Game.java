package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import javafx.geometry.Pos;

import java.awt.*;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private int RoomNUM;

    private static class Room{
        public int StartXPos;
        public int StartYPos;
        public int L;
        public int W;
        public int XPos;
        public int YPos;
        public boolean connection = false;

        public Room(int Sx, int Sy, int ll, int ww){
            StartXPos = Sx;
            StartYPos = Sy;
            L = ll;
            W = ww;
        }

    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

        ter.initialize(WIDTH, HEIGHT);
        textStartPage();


    }

    public void textStartPage(){
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(100, 100, 100);
        StdDraw.text(WIDTH /2, HEIGHT - 5, "CS61B: xiaotaiyang's game"  );
        Font font2 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(WIDTH /2, HEIGHT - 20, "便便太阳的迷宫"  );
        StdDraw.show();
    }
    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        //long SEED = 2873123;
        String[] newInput = parseInputString(input);
        if(newInput[0] == "q") {
            return null;
        }
        if (newInput[1] == "l") {
            return null;
        } else {
            ter.initialize(WIDTH, HEIGHT);
            long SEED = Long.parseLong(newInput[1]);
            Random r = new Random(SEED);

            int numofRoom = RandomUtils.uniform(r, 30, 60);

            TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
            genEmpty(finalWorldFrame);
            Room[] allRooms = genRoom(r, numofRoom, finalWorldFrame);
            genHallway(r, allRooms, finalWorldFrame);
            genWall(finalWorldFrame);
            ter.renderFrame(finalWorldFrame);
            return finalWorldFrame;
        }
    }

    private String[] parseInputString(String S){
        String[] returnString = new String[4];
        returnString[1] = "";
        char[] charArr = new char[30];
        for (int i = 0; i < S.length(); i++){
            charArr[i] = S.charAt(i);
        }
        returnString[0] = Character.toString(charArr[0]);
        switch(returnString[0]){
            case "n": break;
            case "l":
                return returnString;
            case "q":
                return returnString;

                default: throw new IllegalArgumentException("Please recheck your input");
        }
        int j =1;
        boolean flag = false;
        while(j < charArr.length){
            if (charArr[j] == 's') {
                flag = true;
            }
            if (flag == false) {
                String a = Character.toString(charArr[j]);
                returnString[1] = returnString[1] + a;
            }
            if (flag == true && charArr[j] != ':' && charArr[j] != 'q') {
                String b = Character.toString(charArr[j]);
                returnString[2] = returnString[2] + b;
            }
            if  (charArr[j] == 'q' && charArr[j - 1] == ':') {
                returnString[3] = "q";
            }
            j += 1;
        }
        return returnString;
    }
  /**generate number of rooms as the input number indicate, size is random for each room */
    private Room[] genRoom(Random r, int num, TETile[][] w){
        Room[] multiRooms = new Room[num];
        int i =0;
        while (RoomNUM < num) {
            int startXPos = RandomUtils.uniform(r, 0, WIDTH);
            int startYPos = RandomUtils.uniform(r, 0, HEIGHT);
            int L = RandomUtils.uniform(r, 2,8);
            int W = RandomUtils.uniform(r, 2,8);
            if (genSingleRoom(startXPos, startYPos, L, W, w)){
                multiRooms[i] = new Room(startXPos, startYPos, L, W);
                multiRooms[i].XPos = RandomUtils.uniform(r, startXPos,startXPos + W);
                multiRooms[i].YPos = RandomUtils.uniform(r, startYPos,startYPos + L);
                i += 1;
            }
        }
        return multiRooms;
    }

    private boolean genSingleRoom(int x, int y, int L, int W, TETile[][] w){
        //check if rooms in x direction touch the bound by -5.
        if ( x + W + 5 > WIDTH || x < 5  ){
            return false;
        }
        //check if rooms in y direction touch the bound by -5.
        if ( y + L + 5 > HEIGHT || y < 5  ){
            return false;
        }
        if (traverse(x, x + W, y, y + L, w)){
            return false;
        }
        for (int i = x; i < x + W; i++){
            for (int j = y; j < y + L; j++){
                w[i][j] = Tileset.FLOOR;
            }
        }
        RoomNUM += 1;
        return true;
    }
/** traverse the area to see if it's already filled, otherwise return false. */
    private boolean traverse(int lowerboundX, int upperboundX, int lowerboundY, int upperboundY, TETile[][] w){
        for (int i = lowerboundX; i < upperboundX; i++){
            for (int j = lowerboundY; j < upperboundY; j++){
                if (w[i][j] == Tileset.FLOOR){
                    return true;
                }
            }
        }
        return false;
    }


    private void genEmpty(TETile[][] world){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        RoomNUM = 0;
    }

    private void genHallway(Random r, Room[] multiRooms, TETile[][] w){
        connect(r, multiRooms[0], multiRooms[1], w);
        while (roomConnected(multiRooms) < RoomNUM) {
            int[] connectR = ctConnected(multiRooms);
            int[] unconR = ctUnconnected(multiRooms);
            int i = RandomUtils.uniform(r, connectR.length);
            int j = RandomUtils.uniform(r, unconR.length);
            connect(r, multiRooms[connectR[i]], multiRooms[unconR[j]], w);
        }
    }

    private int roomConnected(Room[] allrooms){
        int count = 0;
        for(Room r : allrooms){
            if (r.connection){
                count += 1;
            }
        }
        return count;
    }

    private int[] ctConnected(Room[] allRooms){
        int num = roomConnected(allRooms);
        int[] connectedRooms = new int[num];
        int j = 0;
        for (int i = 0; i < RoomNUM; i++){
            if (allRooms[i].connection){
                connectedRooms[j] = i;
                j +=1;
            }
        }
        if(j != num) {
            throw new IllegalArgumentException("Length of connected rooms doesn't match.");
        }
        return connectedRooms;
    }

    private int[] ctUnconnected(Room[] allRooms){
        int num = roomConnected(allRooms);
        num = RoomNUM - num;
        int[] unconnectedRooms = new int[num];
        int j = 0;
        for (int i = 0; i < RoomNUM; i++){
            if (!allRooms[i].connection){
                unconnectedRooms[j] = i;
                j +=1;
            }
        }
        if(j != num) {
            throw new IllegalArgumentException("Length of unconnected rooms doesn't match.");
        }
        return unconnectedRooms;
    }

    private void connect(Random r, Room A, Room B, TETile[][] w){
        int Xmin = Math.min(A.XPos, B.XPos);
        int Ymin = Math.min(A.YPos, B.YPos);
        int Xmax = Math.max(A.XPos, B.XPos);
        int Ymax = Math.max(A.YPos, B.YPos);
        int intersectX = RandomUtils.uniform(r, Xmin, Xmax + 1);
        int intersectY = RandomUtils.uniform(r, Ymin, Ymax + 1);
        connectX(A.XPos, intersectX, A.YPos, w);
        connectX(B.XPos, intersectX, B.YPos, w);
        connectY(A.YPos, intersectY, intersectX, w);
        connectY(B.YPos, intersectY, intersectX, w);
        A.connection = true;
        B.connection = true;
    }

    private void connectX(int a, int b, int y, TETile[][] w){
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        for (int i = a; i < b; i++){
            w[i][y] = Tileset.FLOOR;
        }
    }

    private void connectY(int a, int b, int x, TETile[][] w){
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        for (int i = a; i < b; i++){
            w[x][i] = Tileset.FLOOR;
        }
    }

    private void genWall(TETile[][] w){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                haveFloor(w, i, j);
            }
        }
    }

    private void haveFloor(TETile[][] w, int i, int j){
        if (w[i][j] == Tileset.FLOOR){
            if(w[i-1][j-1] != Tileset.FLOOR){
                w[i-1][j-1] = Tileset.WALL;
            }
            if(w[i-1][j] != Tileset.FLOOR){
                w[i-1][j] = Tileset.WALL;
            }
            if(w[i-1][j+1] != Tileset.FLOOR){
                w[i-1][j+1] = Tileset.WALL;
            }
            if(w[i][j-1] != Tileset.FLOOR){
                w[i][j-1] = Tileset.WALL;
            }
            if(w[i-1][j+1] != Tileset.FLOOR){
                w[i-1][j+1] = Tileset.WALL;
            }
            if(w[i+1][j-1] != Tileset.FLOOR){
                w[i+1][j-1] = Tileset.WALL;
            }
            if(w[i+1][j] != Tileset.FLOOR){
                w[i+1][j] = Tileset.WALL;
            }
            if(w[i+1][j+1] != Tileset.FLOOR){
                w[i+1][j+1] = Tileset.WALL;
            }
        }
    }

//
//    public static void main(String[] args){
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//
//        Game testgame = new Game();
//        TETile[][] newWorld = testgame.playWithInputString("1000");
//
//        ter.renderFrame(newWorld);
//
//
//
//    }
}
