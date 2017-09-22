package com.teclo.cballini.teclo;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by cballini on 22/09/2017.
 */

public class Team {
    private int idTeam;
    private String nameTeam;
    private int nbPieces;
    private Bitmap img;
    //liste cartes

    public Team(){
        idTeam = -1;
        nameTeam = "";
        nbPieces = 0;
        img = Bitmap.createBitmap(24,24,Bitmap.Config.RGB_565);
        img.eraseColor(Color.WHITE);
    }

    public Team(Team t){
        idTeam = t.getIdTeam();
        nameTeam = t.getNameTeam();
        nbPieces = t.getNbPieces();
        img = t.getImg();
    }


    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public int getNbPieces() {
        return nbPieces;
    }

    public void setNbPieces(int nbPieces) {
        this.nbPieces = nbPieces;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
