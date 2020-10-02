package com.example.mobileproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.Button;

public class Fire extends androidx.appcompat.widget.AppCompatButton {
    Paint CirclePaint;
    int CenterPositionX;
    int CenterPositionY;
    int CircleRadius;

    public Fire(Context context) {
        super(context);
    }

    public Fire(Context context,int centerPositionX, int centerPositionY, int circleRadius) {
        super(context);
        CenterPositionX = centerPositionX;
        CenterPositionY = centerPositionY;
        CircleRadius = circleRadius;

        CirclePaint = new TextPaint();
        CirclePaint.setColor(Color.RED);
        CirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(CenterPositionX, CenterPositionY, CircleRadius, CirclePaint);
        CirclePaint.setARGB(100, 255, 0, 0);
        CirclePaint.setTextAlign(Paint.Align.CENTER);
        CirclePaint.setTextSize(100);

        canvas.drawText("Fire", CenterPositionX, CenterPositionY+25, CirclePaint);
        super.draw(canvas);
    }
}
