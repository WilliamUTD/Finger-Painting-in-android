package com.wjh160030.fingerpaint;


import android.content.*;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class PaintView extends View {

    //****************************************************************
    //declaration of variables needed.
    public int width;
    public int height;
    private Bitmap bitmap;
    private Canvas canvas = new Canvas();
    private customPath path;
    private float mX, mY;
    private static final float TOLERANCE = 3;
    Context context;
    private Paint currentPaint;
    private ArrayList<customPath> drawnPaths = new ArrayList<>();
    private ArrayList<customPath> removedPaths = new ArrayList<>();
    float MIN_BRUSH_SIZE = 3f;
    float BRUSH_SIZE;
    //****************************************************************



    //****************************************************************
    //This method sets the default and sets up the paint view
    //sets context and paint
    //****************************************************************
    public PaintView(Context c, AttributeSet att){
        super(c, att);
        this.context = c;
        path = new customPath();


        currentPaint = new Paint();
        currentPaint.setColor(Color.BLACK);
        currentPaint.setAntiAlias(true);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeWidth(MIN_BRUSH_SIZE);
    }

    //****************************************************************
    //This one seems necessary but im not sure entirely why
    //****************************************************************
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        width = w;
        height = h;

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }


    //****************************************************************
    //Most necessary part, draws the path when invalidate() is called
    //****************************************************************
    @Override
    public void onDraw(Canvas can){
        super.onDraw(can);
        can.drawBitmap(bitmap, 0,0,currentPaint);
        //drawing paths in the canvas
        can.drawPath(path, currentPaint);


    }


    //****************************************************************
    //This method is when the touch event starts, reset the path and
    //move the position to reflect the finger position
    //****************************************************************
    private void startTouch(float x, float y){
        path.reset();
        path.moveTo(x,y);
         mX=x;
         mY=y;
    }

    //****************************************************************
    //While finger is moving, update the positions
    //****************************************************************
    private void moveTouch(float x, float y){
        float dx = Math.abs(x-mX);
        float dy = Math.abs(y-mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE){
            path.quadTo(mX,mY,(x+mX)/2,(y+mY)/2);
            mX=x;
            mY=y;
        }
    }

    //****************************************************************
    //This method clears the canvas to start with, then it takes the list
    //of Paths and redraws all but the last one.
    //****************************************************************
    public void undo() {

        if (drawnPaths.size() > 0) {
            //remove the last drawn line from the list
            removedPaths.add(drawnPaths.remove(drawnPaths.size()-1));
            //clearing canvas to redraw al but last path
            setDrawingCacheEnabled(false);
            onSizeChanged(width,height,width,height);
            invalidate();
            //refresh the cache
            setDrawingCacheEnabled(true);
            //now redraw the paths
            for(customPath p : drawnPaths){
                canvas.drawPath(p, p.myPaint);
            }
        }
    }

    public void redo(){
        //add the path to drawn paths and remove it from removed paths
        if(removedPaths.size() > 0){
            drawnPaths.add(removedPaths.remove(removedPaths.size()-1));
            customPath addBack = drawnPaths.get(drawnPaths.size()-1);
            canvas.drawPath(addBack, addBack.myPaint);
        }
    }


    //****************************************************************
    //paints the path to mX and mY
    //This creates the final line of from the finger movement
    //this is where the finger is lifted off the screen, so the path is completed
    //and adding the path to the list and drawing it
    //****************************************************************
    public void upTouch(){
        path.lineTo(mX, mY);
        canvas.drawPath(path,currentPaint);
        drawnPaths.add(new customPath(path, new Paint(currentPaint)));
        path = new customPath(new Paint(currentPaint));

    }


    //*************************************************************************
    //This method handles the motion events that occur from finger directions
    //Depedning on MotionEvent, different metohds are called.
    //*************************************************************************
    public boolean onTouchEvent(MotionEvent me){
        float x = me.getX();
        float y = me.getY();
        //switch statement for the various actions.
        switch(me.getAction()){
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
        }
        return true;
    }


    //****************************************************************
    //This method will take a color int and set the new color
    //****************************************************************
    void setColor(int color){
        currentPaint.setColor(color);
    }

    //********************************************************************
    //this takes the reading from progress bar and updates the brush size
    //********************************************************************
    void setBrushSize(float size){
        BRUSH_SIZE = MIN_BRUSH_SIZE+size;
        currentPaint.setStrokeWidth(BRUSH_SIZE);
    }

}
