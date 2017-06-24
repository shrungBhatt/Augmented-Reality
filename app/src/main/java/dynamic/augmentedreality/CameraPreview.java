package dynamic.augmentedreality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by admin on 23/06/2017.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    public final String TAG = "CameraPreview";

    Thread mThread = null;
    boolean mFlag = false;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Bitmap mBitmap;
    float x=0,y=0;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(parameters);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0,null);
    }


    public void surfaceCreated(SurfaceHolder holder) {

        // The Surface has been created, now tell the camera where to draw the preview.
        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
//            Canvas canvas = null;
//
//            try {
//                canvas = holder.lockCanvas(null);
//                onDraw(canvas);
//            } catch (Exception e) {
//               Log.e(TAG,"Error" + e.getMessage());
//            } finally {
//                if (canvas != null) {
//                    holder.unlockCanvasAndPost(canvas);
//                }
//            }


        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    @Override
    public void run() {
        while(mFlag){
            if(!mHolder.getSurface().isValid()){
                continue;
            }
            Canvas canvas = mHolder.lockCanvas();
            canvas.drawBitmap(mBitmap,x,y,null);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        mFlag = false;
        while(true) {
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        } mThread = null;

    }

    public void resume(){
        mFlag = true;
        mThread = new Thread(this);
        mThread.start();
    }
}
