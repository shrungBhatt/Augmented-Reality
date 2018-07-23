package dynamic.augmentedreality;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private CameraTextureActivity mCameraTextureActivity;
    private OnCameraView mOnCameraView;
    private Button mShowButton;
    private Button mHideButton;
    private FrameLayout mOnCameraViewFrameLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        mOnCameraViewFrameLayout = (FrameLayout)findViewById(R.id.on_camera_view);

        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

//        mPreview = new CameraPreview(this, mCamera);
        // Create our Preview view and set it as the content of our activity.
        mCameraTextureActivity = new CameraTextureActivity(this,mCamera);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(mCameraTextureActivity);


        mShowButton = (Button)findViewById(R.id.button_show);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCameraView = new OnCameraView(CameraActivity.this);
                mOnCameraViewFrameLayout.addView(mOnCameraView);
            }
        });

        mHideButton = (Button)findViewById(R.id.button_hide);
        mHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCameraViewFrameLayout != null){
                    mOnCameraViewFrameLayout.removeAllViews();
                }

            }
        });


    }

}
