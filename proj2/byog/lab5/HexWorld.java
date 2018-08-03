package byog.lab5;
import javafx.geometry.Pos;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final long SEED = 28723;
    private static final Random RANDOM = new Random(SEED);

    private static class Position{
        private int xPos, yPos;
        private Position(int x, int y){
            xPos = x;
            yPos = y;
        }

    /** decide the first x position of drawing hexagon(top left)*/
    public int xFirstPosition(int size){
        int halflength = (int) Math.ceil(maxLength(size)/2);
        return  xPos - halflength + 1;
    }

    /** decide the first y position of drawing hexagon(top left)*/
    public int yFirstPosition(int size){
        return yPos - size + 1;
    }
}

    /** add a single hexagon and print it out */
    public static void addHexagon(TETile[][] w, int size, Position p, TETile pattern){
        int max = (int)maxLength(size);
        int x = p.xFirstPosition(size);
        int y = p.yFirstPosition(size);
        int numofIcon;

        for (int i = y; i < y + (size * 2); i++) {
            int ithLine = i - y;             //record the reference position to y
            if (size <= ithLine){
               ithLine = 2 * size - ithLine - 1;
            }
            numofIcon = size + ithLine * 2;
            for (int j = x; j < x + max; j++){
                int jthLine = j - x;         //record the reference position to x
                if(jthLine >= (max - numofIcon)/2 && jthLine < (max + numofIcon)/2){
                    w[j][i] = pattern;
                }
            }
        }
    }

    private static double maxLength(int n){
        return 3*n-2;
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.GRASS;
            default: return Tileset.TREE;
        }
    }

    private static void printHex(TETile[][] s){
        for(int i = 0; i < s.length; i++){
            for (int j = 0; j < s[i].length; j++){
                System.out.print(s[i][j]);
            }
                System.out.print("\n");
        }
    }
/** select a center and calculate six positions around the center. */
//        pGroup[1].xPos = p.xPos;
//        pGroup[1].yPos = p.yPos + 2 * size;
//
//        pGroup[2].xPos = p.xPos + xDistance;
//        pGroup[2].yPos = p.yPos + size;
//
//        pGroup[3].xPos = p.xPos + xDistance;
//        pGroup[3].yPos = p.yPos - size;
//
//        pGroup[4].xPos = p.xPos;
//        pGroup[4].yPos = p.yPos - 2 * size;
//
//        pGroup[5].xPos = p.xPos - xDistance;
//        pGroup[5].yPos = p.yPos - size;
//
//        pGroup[6].xPos = p.xPos - xDistance;
//        pGroup[6].yPos = p.yPos + size;

    private static Position[] add7Pos(int size, Position p){
        Position[] pGroup = new Position[7];
        int xDistance = (int) Math.floor((3 * size + 1) / 2);
        pGroup[0] = new Position(p.xPos, p.yPos);
        pGroup[1] = new Position(p.xPos, p.yPos + 2 * size);
        pGroup[2] = new Position(p.xPos + xDistance,  p.yPos + size);
        pGroup[3] = new Position(p.xPos + xDistance, p.yPos - size);
        pGroup[4] = new Position(p.xPos, p.yPos - 2 * size);
        pGroup[5] = new Position(p.xPos - xDistance, p.yPos - size);
        pGroup[6] = new Position(p.xPos - xDistance, p.yPos + size);
        return pGroup;
    }

    private static void draw19Hexagon(TETile[][] w, int size, Position p){
        Position[] pGroup = add7Pos(size, p);

        for (int j = 0; j < 7; j++){
            add7Hexagon(w, size, pGroup[j]);
        }
    }

    private static void add7Hexagon(TETile[][] w, int size, Position p){
        Position[] pGroup = add7Pos(size, p);
        for (Position pp : pGroup){
            if (w[pp.xPos][pp.yPos] == Tileset.NOTHING){
                addHexagon(w, size, pp, randomTile());
            }
        }

    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        /** Create a position at the center of the black pad and draw 19 Hexagon. */
        Position center = new Position(WIDTH /2,HEIGHT /2);
        draw19Hexagon(world, 5, center);



        // draws the world to the screen
        ter.renderFrame(world);
    }

}
