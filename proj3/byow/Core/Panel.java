package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Panel {
    public static final int WIDTH = 79;
    public static final int HEIGHT = 49;
    public static final int SEED = 442;
    private TETile nothing;
    private TETile floor;
    private TETile vwall;
    private TETile hwall;
    private TETile dwall;
    TETile[][] world;
    List<Room> rooms;


    public Panel() {
        this.nothing = Tileset.GRASS;
        this.floor = Tileset.FLOOR;
        this.vwall = Tileset.VWALL;
        this.hwall = Tileset.HWALL;
        this.dwall = Tileset.DWALL;
        this.world = new TETile[WIDTH][HEIGHT];
        Random rand = new Random(SEED);
        this.rooms = Room.generateRooms(rand, 30, world);
    }

    public Panel(TETile nothing, TETile floor, TETile vwall, TETile hwall, TETile dwall) {
        this.nothing = nothing;
        this.floor = floor;
        this.vwall = vwall;
        this.hwall = hwall;
        this.dwall = dwall;
    }

    public void init() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = nothing;
            }
        }
    }

    public void draw() {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);

        for (Room room : rooms) {
            int x = room.getPositions().get(0);
            int y = room.getPositions().get(1);
            int width = room.getPositions().get(2);
            int height = room.getPositions().get(3);
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    world[i][j] = room.getFloorTile();
                }
            }
        }
        addDoor();
        addWall();
        //breakWall();
        ter.renderFrame(world);
    }

    private void addDoor() {
        for (Room room : rooms) {
            int x = room.getPositions().get(0);
            int y = room.getPositions().get(1);
            int width = room.getPositions().get(2);
            int height = room.getPositions().get(3);
            Random rand = new Random(SEED);
            // lower horizontal door
            int offset = RandomUtils.uniform(rand, x + 1, x + width);
            boolean flag = RandomUtils.bernoulli(rand, Math.min(width * 0.1, 1));
            if (flag && world[offset][y - 1] != floor) {
                world[offset][y - 1] = Tileset.TREE;
            }
            // upper horizontal door
            offset = RandomUtils.uniform(rand, x + 1, x + width);
            flag = RandomUtils.bernoulli(rand, Math.min(width * 0.1, 1));
            if (flag && world[offset][y + height] != floor) {
                world[offset][y + height] = Tileset.TREE;
            }
            // leftmost vertical
            offset = RandomUtils.uniform(rand, y + 1, y + height);
            flag = RandomUtils.bernoulli(rand, Math.min(height * 0.1, 1));
            if (flag && world[x - 1][offset] != floor) {
                world[x - 1][offset] = Tileset.TREE;
            }
            // rightmost vertical
            offset = RandomUtils.uniform(rand, y + 1, y + height);
            flag = RandomUtils.bernoulli(rand, Math.min(height * 0.1, 1));
            if (flag && world[x + width][offset] != floor) {
                world[x + width][offset] = Tileset.TREE;
            }
        }
    }

    private void addWall() {
        for (Room room : rooms) {
            int x = room.getPositions().get(0);
            int y = room.getPositions().get(1);
            int width = room.getPositions().get(2);
            int height = room.getPositions().get(3);
            for (int i = x - 1; i <= x + width; i++) { // horizontal
                if (world[i][y-1] != floor && world[i][y-1] != Tileset.TREE) {
                    world[i][y-1] = hwall;
                }
                if (world[i][y + height] != floor && world[i][y + height] != Tileset.TREE) {
                    world[i][y + height] = hwall;
                }
            }
            for (int j = y - 1; j <= y + height; j++) { // horizontal
                if (world[x-1][j] != floor && world[x-1][j] != Tileset.TREE) {
                    world[x-1][j] = vwall;
                }
                if (world[x + width][j] != floor && world[x + width][j] != Tileset.TREE) {
                    world[x + width][j] = vwall;
                }
            }
        }
    }

    private void breakWall() {
        for (int j = 1; j < world[0].length - 1; j++) {
            for (int i = 1; i < world.length - 1; i++) {
                // vertical thick wall
                if (world[i][j] == vwall && world[i + 1][j] == vwall) {
                    if (isWall(world[i][j + 1]) && isWall(world[i + 1][j + 1])) {
                        world[i][j] = floor;
                        world[i + 1][j] = floor;
                    }
                } else if (world[i][j] == hwall && world[i][j + 1] == hwall) {
                    if (isWall(world[i + 1][j]) && isWall(world[i + 1][j + 1])) {
                        world[i][j] = floor;
                        world[i][j + 1] = floor;
                    }
                }
            }
        }
    }
    private boolean isWall(TETile tile) {
        return tile == vwall || tile == hwall || tile == dwall;
    }


    public void floodFillHelper(int x, int y, TETile fill, TETile tile, List<List<Integer>> coor) {
        if (x < 1 || y < 1 || x >= world.length - 1|| y >= world[0].length - 1) {
            return;
        }
        if (world[x][y] != tile) {
            return;
        }

        world[x][y] = fill;
        List<Integer> pos = new ArrayList<Integer>();
        pos.add(x);
        pos.add(y);
        coor.add(pos);
        floodFillHelper(x + 1, y, fill, tile, coor);
        floodFillHelper(x - 1, y, fill, tile, coor);
        floodFillHelper(x, y + 1, fill, tile, coor);
        floodFillHelper(x, y - 1, fill, tile, coor);
    }

    public List<List<Integer>> findRooms() {
        List<List<Integer>> result = new ArrayList<>();
        for (int j = 0; j < world[0].length - 1; j++) {
            for (int i = 0; i < world.length - 1; i++) {
                if (world[i][j] == floor) {
                    floodFillHelper(i, j, Tileset.SAND, Tileset.FLOOR, result);
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {
        Panel panel = new Panel();
        panel.init();
        panel.draw();
    }

}
