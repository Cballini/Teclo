package com.teclo.cballini.teclo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private GridAdapter gridAdapter;
    private ArrayList<Bitmap> imgItems = new ArrayList<>();
    private int col_MAX = 7;
    private int row_MAX = 9;
    private Piece[][] gridPieces = new Piece[col_MAX][row_MAX];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = (GridView) findViewById(R.id.gridGame);
        initItems();
        initPieces();
        gridAdapter = new GridAdapter(this, R.layout.grid_item, imgItems);
        gridView.setAdapter(gridAdapter);

        /*ImageView im = (ImageView) findViewById(R.id.caseGame);
        im.setOnClickListener(this);*/
    }

    public void onClick(View v){
        Log.i("test", "ok");
    }

    private void initItems(){
        imgItems = new ArrayList<>();
        for (int y=0; y<col_MAX; y++){
            for(int x=0; x<row_MAX; x++){
                Bitmap item = Bitmap.createBitmap(24,24,Bitmap.Config.RGB_565);
                item.eraseColor(Color.WHITE);
                gridPieces[y][x] = new Piece();
                imgItems.add(item);
            }
        }
    }

    private void initPieces(){
        initTeamOne();
        initTeamTwo();
        initNeutral();

        for (int y=0; y<col_MAX; y++){
            for(int x=0; x<row_MAX; x++){
                if(gridPieces[y][x].getTeam() != -1) {
                    int nb = col_MAX*x+(y+1);
                    imgItems.set(col_MAX*x+y, gridPieces[y][x].getImg());
                }
            }
        }
    }

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

}
