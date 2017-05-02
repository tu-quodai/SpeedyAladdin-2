package com.example.n50.speedyaladdin.Models;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by 12124 on 4/30/2017.
 */

public abstract class GameObjectBase {
    protected Bitmap image;

    protected final int rowCount;
    protected final int colCount;

    protected final int WIDTH;
    protected final int HEIGHT;

    protected final int width;


    protected final int height;
    protected Coordinate mCoor;
//    protected int x;
//    protected int y;

    public GameObjectBase(Bitmap image, int rowCount, int colCount, int x, int y)  {

        this.image = image;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.mCoor = new Coordinate();
        this.mCoor.mX = x;
        this.mCoor.mY= y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height = this.HEIGHT/ rowCount;
    }


    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* width, row* height, width, height);
        return subImage;
    }

    public int getX()  {
        return this.mCoor.mX;
    }

    public int getY()  {
        return this.mCoor.mY;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
