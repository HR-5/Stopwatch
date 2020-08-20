package com.example.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;


public class MyCanvasView extends View {
    int scnwid, scnht;
    Bitmap bit;
    Canvas mcan;
    Paint paint, smallNum, bigNum,smallCircle,secHand,minHannd,black;
    int mColor,blue,darkblue,red,acc,back;
    ArrayList<Float> coBigx, coBigy, coSmallx, coSmally;
    float radB, radS,cenxB,cenxS,cenyS,cenyB;
    Rect rect;




    public MyCanvasView(Context context) {
        this(context, null);
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        coBigx = new ArrayList<>();
        coSmallx = new ArrayList<>();
        coBigy = new ArrayList<>();
        coSmally = new ArrayList<>();
        rect = new Rect();
        mColor = ResourcesCompat.getColor(getResources(), R.color.white, null);
        blue = ResourcesCompat.getColor(getResources(), R.color.blue, null);
        darkblue = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);
        red = ResourcesCompat.getColor(getResources(), R.color.toolbar, null);
        acc = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        back = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null);
        paint = new Paint();
        smallNum = new Paint();
        bigNum = new Paint();
        smallCircle = new Paint();
        secHand = new Paint();
        minHannd = new Paint();
        black = new Paint();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        scnwid = displayMetrics.widthPixels;
        scnht = displayMetrics.heightPixels;
        radB = (float) (scnwid / 2.4);
        radS = (float) (radB / 3.8);
        setPaint(paint, mColor, 10,50);
        setPaint(smallNum, blue, 1,20);
        setPaint(bigNum, blue, 1,40);
        setPaint(smallCircle, mColor, 5,20);
        setPaint(secHand, red, 7,20);
        setPaint(minHannd, acc, 5,20);
        setPaint(black, back, 5,20);
        black.setStyle(Paint.Style.FILL);
        layout(0, 0,scnwid,scnht-200);
    }

    private void setPaint(Paint paint, int color, double stroke,int size) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(size);
        paint.setStrokeWidth((float) stroke);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bit = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mcan = new Canvas(bit);
    }

    private void findpoints(){
        coBigx.add(0, (float) 0);
        coBigy.add(0, (float) 0);
        coSmally.add(0, (float) 0);
        coSmallx.add(0, (float) 0);
        for (int i = 1; i <= 30; i++) {
            double angle = Math.PI/15*(i);
            float x = (float) (cenxB+(radB-40)*Math.sin(angle));
            float y = (float) (cenyB-(radB-40)*Math.cos(angle));
            coBigx.add(i,x);
            coBigy.add(i,y);
        }
        for (int i = 1; i <= 60; i++) {
            double angle = Math.PI/15*i/2;
            float x = (float) (cenxS+(radS-25)*Math.sin(angle));
            float y = (float) (cenyS-(radS-25)*Math.cos(angle));
            coSmallx.add(i,x);
            coSmally.add(i,y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bit != null)
            canvas.drawBitmap(bit, 0, 0, null);
//        findPoints();
        cenxB = scnwid / 2;
        cenyB = scnht / 2;
        cenxS = scnwid / 2;
        cenyS = (scnht - radB) / 2;
        findpoints();
        mcan.drawLine(cenxB,cenyB,cenxB,cenyB,paint);
        mcan.drawLine(cenxS,cenyS,cenxS,cenyS,smallCircle);
        mcan.drawCircle(cenxB, cenyB, radB, paint);
        mcan.drawCircle(cenxS, cenyS, radS, paint);
        drawnumbers();
        drawnumbersSmall();
//        drawsecond(30);
    }
    private void drawnumbers(){
        for (int i = 1; i <= 30; i++) {
            float rB = radB-45;
            String num = String.valueOf(i);
            if(i%2!=0) {
                num = String.valueOf(i + 30);
                bigNum.setColor(darkblue);
            }
            else
                bigNum.setColor(blue);
            bigNum.getTextBounds(num,0,num.length(),rect);
            double angle = Math.PI/15*(i-7.5);
            float x = (float) (scnwid/2 + Math.cos(angle)*rB - rect.width()/2);
            float y = (float) (scnht/2 + Math.sin(angle)*rB - rect.height()/2);
            mcan.drawText(num,x,y +28,bigNum);
        }
    }
    private void drawnumbersSmall(){
        for (int i = 1; i <= 12; i++) {
            float rS = radS-20;
            String num = String.valueOf(i);
            smallNum.getTextBounds(num,0,num.length(),rect);
            double angle = Math.PI/6*(i-3);
            float x = (float) (cenxS + Math.cos(angle)*rS - rect.width()/2);
            float y = (float) (cenyS + Math.sin(angle)*rS - rect.height()/2);
            mcan.drawText(num,x,y+18,smallNum);
        }
    }
    public void drawsecond(int sec,int min){
        mcan.drawRect(0,cenyB-radB-100,scnwid,cenyB+radB+100,black);
        mcan.drawLine(cenxB,cenyB,coBigx.get(sec),coBigy.get(sec),secHand);
        mcan.drawLine(cenxS,cenyS,coSmallx.get(min),coSmally.get(min),minHannd);
        invalidate();
    }
    public void clear(){
        mcan.drawRect(0,cenyB-radB-100,scnwid,cenyB+radB+100,black);
        invalidate();
    }



}

