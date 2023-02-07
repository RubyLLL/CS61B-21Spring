package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import jh61b.junit.In;

import java.util.*;

public class Room {

    private final int x;
    private final int y; /** the position of the bottom left corner of the room  */
    private final int height;
    private final int width; /** the height and width of the room, room must be odd-sized */
    private int margin; /** the margin of the room*/
    private TETile floor; /** Tileset of the floor */

    Room(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.margin = 0;
        this.floor = Tileset.FLOOR;
    }

    /**
     * Generates a given number of random-sized non-overlapping rooms
     * @param retry number of times to retry generating a new room if the previous one
     *              fails (i.e. overlapping rooms)
     * @param world the {@code world} to place the rooms on
     * @param extra positive number allows bigger {@code rooms}
     * @return a series of random-sized non-overlapping {@code rooms} on the {@code world}
     */
    public static List<Room> generateRooms(Random random,
                                    int retry,
                                    TETile[][] world,
                                    int... extra) {
        List<Room> rooms = new ArrayList<Room>();
        while (retry > 0) {
            int size;
            if (extra.length > 0) {
                size = RandomUtils.uniform(random, 1, 3 + extra[0]) * 2 + 1;
            } else {
                size = RandomUtils.uniform(random, 1, 3) * 2 + 1;
            }

            int rectangularity = RandomUtils.uniform(random, 0, 1 + size / 2) * 2;
            int width = size;
            int height = size;
            if (RandomUtils.bernoulli(random, 0.5)) {
                width += rectangularity;
            } else {
                height += rectangularity;
            }

            int x = RandomUtils.uniform(random, 1, world.length - width);
            int y = RandomUtils.uniform(random,1, world[0].length - height);

            boolean flag = false;
            Room newRoom = new Room(x, y, height, width);
            for (Room room: rooms) {
                if (newRoom.overlap(room)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                retry--;
            } else {
                rooms.add(newRoom);
            }

            //TODO: need a way to random select type of walls and floor,
            // stick with the default for now
        }

        return rooms;
    }

    /**
     * Check if the room is overlapped with the given Room r
     * @param room
     * @return
     */
    public boolean overlap(Room room) {
        int[] room1 = {this.x, this.y, this.x + this.width, this.y + this.height};
        int[] room2 = {room.x, room.y, room.x + room.width, room.y + room.height};

        return !(room1[2] <= room2[0] ||   // left
                room1[3] <= room2[1] ||   // bottom
                room1[0] >= room2[2] ||   // right
                room1[1] >= room2[3]);    // top

    }

    public List<Integer> getPositions() {
        List<Integer> result = new ArrayList<>();
        result.add(x);
        result.add(y);
        result.add(width);
        result.add(height);

        return result;
    }

    public TETile getFloorTile() {
        return floor;
    }

    public String toString() {
        int x2 = x + width;
        int y2 = y + height;
        return "[" + x + ", " + y + ", " + x2 + ", " + y2 + "]";
    }
}
