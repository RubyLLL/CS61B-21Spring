package byow.Core;

import byow.TileEngine.TETile;

public class Cell {
    int x;
    int y;
    boolean unvisited;
    TETile type;

    Cell(int x, int y, TETile type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.unvisited = true;
    }

    public void setVisited(boolean b) {
        this.unvisited = b;
    }

    public boolean isVisited() {
        return this.unvisited;
    }

    public boolean isType(TETile tile) {
        return this.type == tile;
    }

    public TETile getType() {
        return this.type;
    }
}
