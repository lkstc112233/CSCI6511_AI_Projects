package com.photoncat.aiproj2.interfaces;

/**
 * A wrapper class for a move. Including x and y. POD type (or so).
 */
public class Move {
    public int x;
    public int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getMoveParam() {
        return String.valueOf(x) + ',' + y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x) ^ Integer.hashCode(-y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }
        return ((Move)o).x == x && ((Move)o).y == y;
    }
}
