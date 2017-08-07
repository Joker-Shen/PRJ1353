package com.ontime.prj1353;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.lang.UCharacter;
import android.view.View;

/**
 * Created by shgl1hz1 on 2017/6/20.
 */

public class RoundView extends View {
    Paint paint = new Paint();
    public RoundView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int verticalCenter = getHeight()/2;
        int horizentalCenter= getHeight()/2;
        int circleRadius = 200;

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawCircle(horizentalCenter,verticalCenter,circleRadius,paint);
    }
}
