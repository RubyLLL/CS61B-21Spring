package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Panel {
    public static final int WIDTH = 79;
    public static final int HEIGHT = 49;
    public static final int SEED = 42;
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

        addWall();
        breakWall();
        //paintWalls(world, walls, Tileset.SAND);
        ter.renderFrame(world);
    }

    private void addWall() {
        for (int j = 0; j < world[0].length - 1; j++) {
            for (int i = 0; i < world.length - 1; i++) {
                if (world[i][j] != world[i + 1][j]) { // draw vertical walls
                    world[i][j] = vwall;
                } else if (world[i][j] != world[i][j + 1]) { // draw horizontal walls
                    world[i][j] = hwall;
                }
            }
        }


        // draw diagonal walls
        for (int j = 0; j < world[0].length - 1; j++) {
            for (int i = 0; i < world.length - 1; i++) {
                if (world[i][j + 1] == vwall
                        && world[i + 1][j] == hwall
                        && world[i + 1][j + 1] != world[i][j]) { // bottom left
                    world[i][j] = dwall;
                } else if (i - 1 > 0 && world[i][j + 1] == vwall
                        && world[i - 1][j] == hwall
                        && world[i - 1][j + 1] != world[i][j]) { // bottom right
                    world[i][j] = dwall;
                } else if (i - 1 > 0 && j - 1 > 0
                        && world[i][j - 1] == vwall
                        && world[i - 1][j] == hwall
                        && world[i - 1][j - 1] != world[i][j]) { // upper right
                    world[i][j] = dwall;
                } else if (j - 1 > 0 && world[i][j - 1] == vwall
                        && world[i + 1][j] == hwall
                        && world[i + 1][j - 1] != world[i][j]) { // upper left
                    world[i][j] = dwall;

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

    public static void main(String[] args) {
        Panel panel = new Panel();
        panel.init();
        panel.draw();
    }

}
