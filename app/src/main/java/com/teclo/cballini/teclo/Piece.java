package com.teclo.cballini.teclo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by cballini on 19/09/2017.
 */

public class Piece {
    private int col;
    private  int row;
    private boolean alive;
    private int team;
    private Bitmap img;

    public Piece() {
        col = 0;
        row = 0;
        alive = true;
        team = -1;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getTeam() {
        return team;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
