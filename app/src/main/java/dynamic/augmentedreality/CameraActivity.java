package dynamic.augmentedreality;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private OnCameraView mOnCameraView;
    private Button mCaptureButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(mPreview);



        mCaptureButton = (Button)findViewById(R.id.button_capture);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCameraView = new OnCameraView(CameraActivity.this);
                FrameLayout onCameraPreview = (FrameLayout)findViewById(R.id.on_camera_view);
                onCameraPreview.addView(mOnCameraView);
            }
        });


    }







    @Override
    protected void onPause() {
        super.onPause();
//        mPreview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPreview.resume();
    }
}
