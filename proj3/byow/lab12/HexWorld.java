package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;

    /**
     * Draw a hexagon of size s
     * @param s
     */
    private static String[][] drawHex(int s) {
        String[][] world = new String[s * 2][s + 2 * s - 2];
        StringBuilder sb = new StringBuilder();
        int maxTiles = s + 2 * s - 2;
        for (int j = 0; j < s; j++) {
            int tileNum = s + 2 * j;
            int offset = (maxTiles - tileNum) / 2;
            for (int k = 0; k < offset; k++) {
                world[j][k] = " ";
                //sb.append(" ");
            }
            for (int i = offset; i < tileNum+offset; i++) {
                world[j][i] = "*";
            }
            for (int k = tileNum+offset; k < maxTiles; k++) {
                world[j][k] = " ";
            }
        }
        int offset = 1;
        for (int j = s; j < s * 2; j ++) {
            world[j] = world[j-offset].clone();
            offset += 2;
        }
        return world;
    }

    private static void placeHex(int x, int y, int size, TETile[][] world, TETile tile) {
        String[][] hex = drawHex(size);
        int width = size + 2 * size - 2;
        int height = size * 2;
        int k = 0;
        int s;
        for (int j = y; j < height + y; j++) {
            s = 0;
            for (int i = x; i < width + x; i++) {
                if (hex[k][s].equals("*")) world[i][j] = tile;
                s++;
            }
            k++;
        }
    }

    private static void draw_hex(int s) {
        draw_hex_helper(s-1, s);
    }

    private static void draw_hex_helper(int b, int t) {
        for (int i = 0; i < b; i++) {
            System.out.print(" ");
        }
        for (int i = 0; i < t; i++) {
            System.out.print("*");
        }
        System.out.println("");

        if (b > 0) {
            draw_hex_helper(b-1, t+2);
        }

        for (int i = 0; i < b; i++) {
            System.out.print(" ");
        }
        for (int i = 0; i < t; i++) {
            System.out.print("*");
        }
        System.out.println("");
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
        placeHex(0, 0, 4, world, Tileset.GRASS);
        placeHex(0, 8, 4, world, Tileset.FLOOR);
        placeHex(0, 16, 4, world, Tileset.FLOWER);
        placeHex(7, 4, 4, world, Tileset.MOUNTAIN);
        placeHex(7, 12, 4, world, Tileset.TREE);
        placeHex(7, 20, 4, world, Tileset.WALL);
        placeHex(14, 0, 4, world, Tileset.FLOWER);
        placeHex(14, 8, 4, world, Tileset.FLOWER);
        placeHex(14, 16, 4, world, Tileset.SAND);
        placeHex(14, 24, 4, world, Tileset.SAND);
        placeHex(21, 4,4, world, Tileset.WATER);
        placeHex(21, 12, 4, world, Tileset.WATER);

        // draws the world to the screen
        ter.renderFrame(world);
        draw_hex(4);
    }
}
