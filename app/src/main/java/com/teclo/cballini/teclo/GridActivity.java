package com.teclo.cballini.teclo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private GridAdapter gridAdapter;
    private ArrayList<Bitmap> imgItems = new ArrayList<>();
    private int col_MAX = 7;
    private int row_MAX = 9;
    private Piece[][] gridPieces = new Piece[col_MAX][row_MAX];
    private boolean firstClick = true;
    private int caseX;
    private int caseY;
    private int nPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = (GridView) findViewById(R.id.gridGame);

        initItems();
        initPieces();

        gridAdapter = new GridAdapter(this, R.layout.grid_item, imgItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(firstClick){
            if (gridPieces[posToY(position)][posToX(position)].getTeam()!=-1) {
                Log.i("test", "ok");
                caseX = posToX(position);
                caseY = posToY(position);
                firstClick = false;
            }
        }
        else{
            movement(caseY, caseX, posToY(position), posToX(position));
            firstClick = true;
        }
    }

    //grille vide
    private void initItems(){
        imgItems = new ArrayList<>();
        for (int y=0; y<col_MAX; y++){
            for(int x=0; x<row_MAX; x++){
                gridPieces[y][x] = new Piece();
                imgItems.add(gridPieces[y][x].getImg());
            }
        }
    }

    //placement des pièces initial
    private void initPieces(){
        initTeamOne();
        initTeamTwo();
        initNeutral();

        updateImgItems();
    }

    //mise à jour de la grille de bitmap
    private void updateImgItems(){
        for (int y=0; y<col_MAX; y++){
            for(int x=0; x<row_MAX; x++){
                imgItems.set(xyToPos(y,x), gridPieces[y][x].getImg());
            }
        }
    }

    //placement initial des pièces neutres
    private void initNeutral() {
        int mCol = col_MAX/2;
        int mRow = row_MAX/2;

        for(int y=(mCol);y<=(mCol +2); y++) {
            for (int x = (mRow); x <=(mRow +2); x++) {
                if (((y%2 != 0) && (x%2 == 0)) || ((y%2 == 0) && (x%2 != 0))) {
                    Piece p = new Piece();
                    p.setTeam(0);
                    p.setCol(y-1);
                    p.setRow(x-1);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.neutral);
                    p.setImg(bm);
                    gridPieces[y-1][x-1] = p;
               }
            }
        }
    }

    //placement initial des pièces j2
    private void initTeamTwo() {
        for (int y=0; y<col_MAX; y++){
            for (int x=0; x<=1; x++){
                if((x==0 && (y!=0 && y!=col_MAX/2 && y!=col_MAX-1)) || (x==1 && (y==0 || y==col_MAX-1))){
                    Piece p = new Piece();
                    p.setTeam(2);
                    p.setCol(y);
                    p.setRow(x);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.teamtwo);
                    p.setImg(bm);
                    gridPieces[y][x] = p;
                }
            }
        }
    }

    //placement initial des pièces j1
    private void initTeamOne() {
        for (int y=0; y<col_MAX; y++){
            for (int x=row_MAX-2; x<row_MAX; x++){
                if((x==row_MAX-2 && (y==0 || y==col_MAX-1)) || (x==row_MAX-1 && (y!=0 && y!=col_MAX/2 && y!=col_MAX-1))){
                    Piece p = new Piece();
                    p.setTeam(1);
                    p.setCol(y);
                    p.setRow(x);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.teamone);
                    p.setImg(bm);
                    gridPieces[y][x] = p;
                }
            }
        }
    }

    //coordonées vers position liste
    private int xyToPos(int y, int x){
        return x*col_MAX+y;
    }

    //position liste vers coordonée y
    private int posToY(int pos){
        return pos%col_MAX;
    }

    //position liste vers coordonée x
    private int posToX(int pos){
        return pos/col_MAX;
    }

    //fait bouger une pièce d'une case à une autre
    private void movement(int y, int x, int ny, int nx){
        if(isMoveOk(y,x,ny,nx)){
            Log.i("test", "déplacement possible");
            gridPieces[ny][nx] = new Piece(gridPieces[y][x]);
            gridPieces[y][x] = new Piece();
            updateImgItems();
            gridView.setAdapter(gridAdapter);
        }
        else{
            String pushDir = pushOk(y,x,ny,nx);
            //move ko mais en poussant des pièces ?
            if(pushDir.equals("")){
                Log.i("test", "push possible");
            }
        }
    }

    //vérifie mouvement valide
    private boolean isMoveOk(int y, int x, int ny, int nx){
        int gap = xyToPos(ny,nx)-xyToPos(y,x);
        //case sélectionnée vide
        if(gridPieces[ny][nx].getTeam()==-1){
            //si case sélectionnée est autour
            if(gap==1 || gap==-1 || gap ==col_MAX || gap==-col_MAX){
                return true;
            }
        }

        return false;
    }

    //vérifie si on peut pousser les pièces
    private String pushOk(int y, int x, int ny, int nx){
        String pushDir = "";
        nPos = xyToPos(ny,nx);
        int gap = nPos-xyToPos(y,x);
        if(gap==1){ //droite
            while (nPos<xyToPos(ny,nx)+(col_MAX-y-1) && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "d";
                }
                nPos++;
            }
        }
        else if(gap==-1){ //gauche
            while (nPos>xyToPos(ny,nx)-y && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "g";
                }
                nPos--;
            }
        }
        else if(gap==col_MAX){ //bas
            while (nPos<col_MAX*row_MAX && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "b";
                }
                nPos=nPos+col_MAX;
            }

        }
        else if(gap==-col_MAX) { //haut
            while (nPos>=0 && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "h";
                }
                nPos=nPos-col_MAX;
            }
        }

        return pushDir;
    }

    //pousse les pièces dans le sens du mouvement
    private void pushPieces(int ny, int nx, String pushDir){
        switch (pushDir){
            case "d":
                for (int gap=nPos;gap>nPos-ny;gap--){
                    gridPieces[posToY(nPos)][posToX(nPos)] = new Piece(gridPieces[posToY(nPos)-1][posToX(nPos)]);
                }
                gridPieces[ny-1][nx] = new Piece();
                break;
            case "g":
                break;
            case "b":
                break;
            case "h":
                break;
            default:
                break;
        }
    }
}
