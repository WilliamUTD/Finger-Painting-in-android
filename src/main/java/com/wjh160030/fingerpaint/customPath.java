package com.wjh160030.fingerpaint;

import android.graphics.Paint;
import android.graphics.Path;

public class customPath extends Path {

    public Paint myPaint = new Paint();


    //default constuctor to do nothing.
    customPath(){

    }

    //constructor to set paint
    customPath(Paint paint){
        myPaint = paint;
    }

    //constructo to set path and paint
    public customPath(Path p, Paint paint){
        super(p);
        myPaint = paint;
    }

}
