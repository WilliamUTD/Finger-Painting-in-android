package com.wjh160030.fingerpaint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintview);

        //setting up the seekbar methods
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paintView.setBrushSize(progress);
                paintView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //auto generated, can not remove without issue
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //auto generated, can not remove without issue
            }
        });
    }

    //*******************************************
    //This function sends the undo request to paint view class
    //********************************************
    public void undo(View v){
        paintView.undo();
    }


    public void redo(View v){
        paintView.redo();
    }

    //****************************************
    //Sends the requested color to the paint view
    //****************************************
    public void setColor(View v){
        switch(v.getId()) {
            //if button is black
            case R.id.black:
                paintView.setColor(Color.BLACK);
                break;
            //if button is blue
            case R.id.Blue:
                paintView.setColor(Color.BLUE);
                break;
            //if button is red
            case R.id.red:
                paintView.setColor(Color.RED);
                break;
            //if button is green
            case R.id.Green:
                paintView.setColor(Color.GREEN);
                break;
        }
    }


}
