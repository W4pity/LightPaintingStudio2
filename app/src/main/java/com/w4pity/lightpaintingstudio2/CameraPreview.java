package com.w4pity.lightpaintingstudio2;

/**
 * Created by W4pity on 12/05/2016.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private SurfaceHolder cameraPreviewHolder;
    private Camera camera;
    private Context context;
    private boolean cameraConfigured = false;
    private boolean inPreview = false;
    private Size previewSize;
    private byte yuv[];

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        cameraPreviewHolder = getHolder();
        cameraPreviewHolder.addCallback(this);
        camera = Camera.open(Options.camera);
        // we need this for compatibility
        cameraPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void releaseCamera() {
        if (inPreview) {
            camera.stopPreview();
            camera.release();
            camera = null;
            inPreview = false;
            cameraConfigured = false;
        }
    }

    public void resumePreview() {
        if (!inPreview)
            try {
                camera = Camera.open(Options.camera);

            }
            catch (Exception e)
            {
                Log.d("opencamera", "resumePreview "+e.toString());
            }
    }

    private void initPreview(int width, int height) {
        if (camera != null && cameraPreviewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(cameraPreviewHolder);
            } catch (Throwable t) {
                Log.e("CameraPreview", "Exception in setPreviewDisplay()", t);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
            if (!cameraConfigured) {
                Parameters parameters = camera.getParameters();
                previewSize = getBestPreviewSize(width, height, parameters);
                if (previewSize != null) {
                    parameters.setPreviewSize(previewSize.width,
                            previewSize.height);
                    //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    camera.setParameters(parameters);
                    camera.setDisplayOrientation(90);
                    cameraConfigured = true;


                }
            }
        }
    }

    private Size getBestPreviewSize(int width, int height, Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }

            }
        }
        return result;
    }

    Bitmap bit;

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.startPreview();
            yuv = new byte[getBufferSize()];
            camera.addCallbackBuffer(yuv);
            camera.setPreviewCallbackWithBuffer(new PreviewCallback() {
                public synchronized void onPreviewFrame(byte[] data, Camera c) {
                    if (camera != null) {
                        camera.addCallbackBuffer(yuv);
                        try {
                           /* bit = getBitmap();*/

                        } catch (Exception e) {

                        }
                    }
                }
            });
            inPreview = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        initPreview(width, height);
        startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private int getBufferSize() {
        int pixelformat = ImageFormat.getBitsPerPixel(camera.getParameters()
                .getPreviewFormat());
        int bufSize = previewSize.width * previewSize.height * pixelformat / 8;
        return bufSize;
    }

static Bitmap[] tabBit= new Bitmap[Options.nbFrame];
    long timeGetBitmap = 0;
    public boolean getBitmap(int a) {
        if(Options.invervalPhoto<System.currentTimeMillis()-timeGetBitmap) {
            long time = System.currentTimeMillis();
            Bitmap bitmap = null;
            YuvImage yuvimage = new YuvImage(yuv, ImageFormat.NV21, previewSize.width,
                    previewSize.height, null);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, outStream);
            bitmap = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0,
                    outStream.size());
            yuvimage = null;
            outStream = null;

            /////////semi size
            //  Bitmap bSemi = Bitmap.createBitmap(bitmap.getWidth()/2, bitmap.getHeight()/2, Bitmap.Config.ARGB_8888);
            // bSemi = bSemi.copy(bSemi.getConfig(), true);
            int coef = 2;
        /*for(int i = 0; i<bitmap.getHeight()/2; i+=1)
            for(int j = 0; j<bitmap.getWidth()/2; j+=1)
            {
                bSemi.setPixel(j, i, bitmap.getPixel(j*2, i*2));
            }*/


            tabBit[a] = bitmap;
            timeGetBitmap = System.currentTimeMillis();
            Log.d("timegetbitmap", "getBitmap " + (System.currentTimeMillis()-time));
            return true;

        }
        else
            return false;
       // return bitmap;

    }
}