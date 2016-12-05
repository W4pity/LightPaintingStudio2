package com.w4pity.lightpaintingstudio2;

/**
 * Created by W4pity on 12/05/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Peephole extends View{

    private int width;
    private int height;
    private int peepSide;
    private int peepSideX;
    private int peepSideY;
    private Paint text;
    private Paint grayArea;
    private Paint square;
    private Bitmap bitmap;

    public Peephole(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        init();
    }

    private void init(){
        text = new Paint();
        text.setColor(Color.GREEN);
        text.setTextSize((height*5)/100);
        text.setTextAlign(Align.CENTER);
        grayArea = new Paint();
        grayArea.setColor(Color.argb(255/3, Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(Color.GREEN)));
        square = new Paint();
        square.setColor(Color.GREEN);
        peepSide = (int) (height - text.getTextSize()*2);
        peepSideX = width/2-peepSide/2;
        peepSideY = height/2-peepSide/2;
    }

    public int getPeepSide(){
        return peepSide;
    }

    public int getPeepSideX(){
        return peepSideX;
    }

    public int getPeepSideY(){
        return peepSideY;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawText("Sudoku", width/2, text.getTextSize(), text);
        canvas.drawRect(0, 0, width/2-peepSide/2, height, grayArea);
        canvas.drawRect(width/2+peepSide/2, 0, width, height, grayArea);
        canvas.drawRect(width/2-peepSide/2,0,width/2+peepSide/2,height/2-peepSide/2, grayArea);
        canvas.drawRect(width/2-peepSide/2,height/2+peepSide/2,width/2+peepSide/2,height, grayArea);

        canvas.drawLine(width/2-peepSide/2,(height/2-peepSide/2)+peepSide/3, width/2+peepSide/2, (height/2-peepSide/2)+peepSide/3, square);
        canvas.drawLine(width/2-peepSide/2,(height/2-peepSide/2)+(peepSide/3)*2, width/2+peepSide/2,(height/2-peepSide/2)+(peepSide/3)*2, square);

        canvas.drawLine(width/2-peepSide/2+(peepSide/3), height/2-peepSide/2, width/2-peepSide/2+(peepSide/3), height/2+peepSide/2, square);
        canvas.drawLine(width/2-peepSide/2+(peepSide/3)*2, height/2-peepSide/2, width/2-peepSide/2+(peepSide/3)*2, height/2+peepSide/2, square);


        if(bitmap!=null){
            canvas.drawBitmap(bitmap, peepSideX, peepSideY, new Paint());
        }
    }
}