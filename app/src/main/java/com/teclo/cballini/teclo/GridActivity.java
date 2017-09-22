package com.teclo.cballini.teclo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private GridAdapter gridAdapter;
    private ArrayList<Bitmap> imgItems = new ArrayList<>();
    private int col_MAX = 7;
    private int row_MAX = 9;
    private int nbPiecesTeams = 6;
    private int nbPiecesNeutral = 5;
    private Piece[][] gridPieces = new Piece[col_MAX][row_MAX];
    private boolean firstClick = true;
    private int caseX;
    private int caseY;
    private int nPos;
    private int turn;
    private Team j1;
    private Team j2;
    private Team nTeam;


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
            int teamP = gridPieces[posToY(position)][posToX(position)].getTeam();
            if (teamP !=-1 && teamP==turn) {
                Log.i("test", "ok");
                caseX = posToX(position);
                caseY = posToY(position);
                firstClick = false;
            }
        }
        else{
            movement(caseY, caseX, posToY(position), posToX(position));
            //changement de joueur
            if(turn == j1.getIdTeam()){
                turn = j2.getIdTeam();
                Toast toast = Toast.makeText(getApplicationContext(),j2.getNameTeam()+" à toi de jouer",Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                turn = j1.getIdTeam();
                Toast toast = Toast.makeText(getApplicationContext(),j1.getNameTeam()+" à toi de jouer",Toast.LENGTH_SHORT);
                toast.show();
            }
            firstClick = true;
        }
    }

    /************************
        DEBUT INITIALISATION
     ************************/
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
        initTeam();
        initPiecesTeamOne();
        initPiecesTeamTwo();
        initPiecesNeutral();

        updateImgItems();
    }

    //placement initial des pièces neutres
    private void initPiecesNeutral() {
        int mCol = col_MAX/2;
        int mRow = row_MAX/2;

        for(int y=(mCol);y<=(mCol +2); y++) {
            for (int x = (mRow); x <=(mRow +2); x++) {
                if (((y%2 != 0) && (x%2 == 0)) || ((y%2 == 0) && (x%2 != 0))) {
                    Piece p = new Piece();
                    p.setTeam(nTeam.getIdTeam());
                    p.setCol(y-1);
                    p.setRow(x-1);
                    p.setImg(nTeam.getImg());
                    gridPieces[y-1][x-1] = p;
               }
            }
        }
    }

    //placement initial des pièces j2
    private void initPiecesTeamTwo() {
        for (int y=0; y<col_MAX; y++){
            for (int x=0; x<=1; x++){
                if((x==0 && (y!=0 && y!=col_MAX/2 && y!=col_MAX-1)) || (x==1 && (y==0 || y==col_MAX-1))){
                    Piece p = new Piece();
                    p.setTeam(j2.getIdTeam());
                    p.setCol(y);
                    p.setRow(x);
                    p.setImg(j2.getImg());
                    gridPieces[y][x] = p;
                }
            }
        }
    }

    //placement initial des pièces j1
    private void initPiecesTeamOne() {
        for (int y=0; y<col_MAX; y++){
            for (int x=row_MAX-2; x<row_MAX; x++){
                if((x==row_MAX-2 && (y==0 || y==col_MAX-1)) || (x==row_MAX-1 && (y!=0 && y!=col_MAX/2 && y!=col_MAX-1))){
                    Piece p = new Piece();
                    p.setTeam(j1.getIdTeam());
                    p.setCol(y);
                    p.setRow(x);
                    p.setImg(j1.getImg());
                    gridPieces[y][x] = p;
                }
            }
        }
    }

    //initialisation des équipes
    private void initTeam(){
        Bitmap bm;
        j1 = new Team();
        j1.setIdTeam(1);
        j1.setNameTeam(getResources().getString(R.string.j1));
        j1.setNbPieces(nbPiecesTeams);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.teamone);
        j1.setImg(bm);
        turn = j1.getIdTeam();
        Toast toast = Toast.makeText(getApplicationContext(),j1.getNameTeam()+" à toi de jouer", Toast.LENGTH_SHORT);
        toast.show();

        j2 = new Team();
        j2.setIdTeam(2);
        j2.setNameTeam(getResources().getString(R.string.j2));
        j2.setNbPieces(nbPiecesTeams);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.teamtwo);
        j2.setImg(bm);

        nTeam = new Team();
        nTeam.setIdTeam(0);
        nTeam.setNbPieces(nbPiecesNeutral);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.neutral);
        nTeam.setImg(bm);
    }
    /**********************
        FIN INITIALISATION
     **********************/


    /***************
        DEBUT UTILS
     ***************/
    //mise à jour de la grille de bitmap
    private void updateImgItems(){
        for (int y=0; y<col_MAX; y++){
            for(int x=0; x<row_MAX; x++){
                imgItems.set(xyToPos(y,x), gridPieces[y][x].getImg());
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

    /**************
        FIN UTILS
     **************/


    /********************
        DEBUT MOUVEMENT
     ********************/
    //fait bouger une pièce d'une case à une autre
    private void movement(int y, int x, int ny, int nx){
        if(isMoveOk(y,x,ny,nx)){
            Log.i("test", "déplacement possible");
            gridPieces[ny][nx] = new Piece(gridPieces[y][x]);
            gridPieces[y][x] = new Piece();
            caseX = nx;
            caseY =ny;
            capture();
            updateImgItems();
            gridView.setAdapter(gridAdapter);
        }
        else{
            String pushDir = pushOk(y,x,ny,nx);
            //move ko mais en poussant des pièces ?
            if(!pushDir.equals("")){
                pushPieces(ny,nx,pushDir);
                Log.i("test", "push possible");
                caseX = nx;
                caseY =ny;
                capture();
                updateImgItems();
                gridView.setAdapter(gridAdapter);
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
                    nPos--;
                }
                nPos++;
            }
        }
        else if(gap==-1){ //gauche
            while (nPos>xyToPos(ny,nx)-y && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "g";
                    nPos++;
                }
                nPos--;
            }
        }
        else if(gap==col_MAX){ //bas
            while (nPos<col_MAX*row_MAX && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "b";
                    nPos=nPos-col_MAX;
                }
                nPos=nPos+col_MAX;
            }
        }
        else if(gap==-col_MAX) { //haut
            while (nPos>=0 && pushDir.equals("")){
                if(gridPieces[posToY(nPos)][posToX(nPos)].getTeam()==-1){
                    pushDir = "h";
                    nPos=nPos+col_MAX;
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
                for (int i=nPos;i>nPos-ny;i--){
                    gridPieces[posToY(i)][posToX(i)] = new Piece(gridPieces[posToY(i)-1][posToX(i)]);
                }
                gridPieces[ny-1][nx] = new Piece();
                break;
            case "g":
                for (int i=nPos;i<nPos+(col_MAX-ny);i++){
                    gridPieces[posToY(i)][posToX(i)] = new Piece(gridPieces[posToY(i)+1][posToX(i)]);
                }
                gridPieces[ny+1][nx] = new Piece();
                break;
            case "b":
                for (int i=nPos; i>=xyToPos(ny,nx); i-=col_MAX){
                    gridPieces[posToY(i)][posToX(i)] = new Piece(gridPieces[posToY(i)][posToX(i)-1]);
                }
                gridPieces[ny][nx-1] = new Piece();
                break;
            case "h":
                for (int i=nPos; i<=xyToPos(ny,nx);i+=col_MAX){
                    gridPieces[posToY(i)][posToX(i)] = new Piece(gridPieces[posToY(i)][posToX(i)+1]);
                }
                gridPieces[ny][nx+1] = new Piece();
                break;
            default:
                break;
        }
    }
    /*****************
        FIN MOUVEMENT
     *****************/


    /************************
        DEBUT PRISE DE PIECE
     ************************/
    //Vérifie si la pièce déplacée entoure une pièce d'une autre team
    private void capture(){
        int team = gridPieces[caseY][caseX].getTeam();
        int teamR = (caseY<col_MAX) ? gridPieces[caseY+1][caseX].getTeam() : -1;
        int teamL = (caseY>0) ? gridPieces[caseY-1][caseX].getTeam() : -1;
        int teamB = (caseX<row_MAX) ? gridPieces[caseY][caseX+1].getTeam() : -1;
        int teamH = (caseX>0) ? gridPieces[caseY][caseX-1].getTeam() : -1;

        //capture à droite pièce
        if(teamR!=-1 && teamR!=team){
            int teamRR = (caseY+1<col_MAX) ? gridPieces[caseY+2][caseX].getTeam() : team;
            int teamRH = (caseX>0) ? gridPieces[caseY+1][caseX-1].getTeam() : team;
            int teamRB = (caseX<row_MAX) ? gridPieces[caseY+1][caseX+1].getTeam() : team;
            if(teamRR==team && teamRH==team && teamRB==team){
                gridPieces[caseY+1][caseX].setTeam(team);
                gridPieces[caseY+1][caseX].setImg(gridPieces[caseY][caseX].getImg());
            }
        }
        //capture à gauche pièce
        if(teamL!=-1 && teamL!=team){
            int teamLL = (caseY-1<0) ? gridPieces[caseY-2][caseX].getTeam() : team;
            int teamLH = (caseX>0) ? gridPieces[caseY-1][caseX-1].getTeam() : team;
            int teamLB = (caseX<row_MAX) ? gridPieces[caseY-1][caseX+1].getTeam() : team;
            if(teamLL==team && teamLH==team && teamLB==team){
                gridPieces[caseY-1][caseX].setTeam(team);
                gridPieces[caseY-1][caseX].setImg(gridPieces[caseY][caseX].getImg());
            }
        }
        //capture en bas pièce
        if(teamB!=-1 && teamB!=team){
            int teamBR = (caseY<col_MAX) ? gridPieces[caseY+1][caseX+1].getTeam() : team;
            int teamBL = (caseY>0) ? gridPieces[caseY-1][caseX+1].getTeam() : team;
            int teamBB = (caseX+1<row_MAX) ? gridPieces[caseY][caseX+2].getTeam() : team;
            if(teamBR==team && teamBL==team && teamBB==team){
                gridPieces[caseY][caseX+1].setTeam(team);
                gridPieces[caseY][caseX+1].setImg(gridPieces[caseY][caseX].getImg());
            }
        }
        //capture en haut pièce
        if(teamH!=-1 && teamH!=team){
            int teamHR = (caseY<col_MAX) ? gridPieces[caseY+1][caseX-1].getTeam() : team;
            int teamHL = (caseY>0) ? gridPieces[caseY-1][caseX-1].getTeam() : team;
            int teamHH = (caseX-1>0) ? gridPieces[caseY][caseX-2].getTeam() : team;
            if(teamHR==team && teamHL==team && teamHH==team){
                gridPieces[caseY][caseX-1].setTeam(team);
                gridPieces[caseY][caseX-1].setImg(gridPieces[caseY][caseX].getImg());
            }
        }
    }

    /**********************
        FIN PRISE DE PIECE
     **********************/
}
