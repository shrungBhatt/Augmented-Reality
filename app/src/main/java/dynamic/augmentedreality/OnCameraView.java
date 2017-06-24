package dynamic.augmentedreality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 24/06/2017.
 */

public class OnCameraView extends View {

    private Bitmap mBitmap;

    public OnCameraView(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_name);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(mBitmap,0,0,null);
    }
}
