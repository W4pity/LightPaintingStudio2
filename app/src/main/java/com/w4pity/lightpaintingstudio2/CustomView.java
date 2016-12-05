package com.w4pity.lightpaintingstudio2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by W4pity on 02/05/2016.
 */
public class CustomView extends View {



    CameraPreview cp;
    ImageView iv;
    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        // fl = (FrameLayout) findViewById(R.id.camera_view);
    }

    public CustomView(Context c) {
        super(c);
    }
    public CustomView(Context c, AttributeSet as) {
        super(c, as);

        setBackgroundColor(Color.BLACK);
    }
    public void init()
    {

    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      /*  try{
            cp = (CameraPreview)findViewById(R.id.cameraPreview1);

            iv = (ImageView)findViewById(R.id.iv);
            Log.d("cancerdraw", "onDraw "+(cp == null) +" "+(iv ==null));
            Bitmap b = cp.getBitmap(1);/////null quelque parte
            canvas.drawBitmap(b, 0, 0, null);

        }
        catch (Exception e)
        {
            Log.d("drawertag", "onDraw "+e.toString());
        }
        invalidate();*/
    }


    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }



}
