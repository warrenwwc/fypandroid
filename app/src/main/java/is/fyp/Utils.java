package is.fyp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by 42480 on 4/24/2017.
 */

public class Utils {

    public static Dialog dialog;

    public static Dialog getLoader(final Context context){

        dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.setCancelable(false);
        ImageView imageView = new ImageView(context);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(R.drawable.my_animation);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(imageView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(500, 500);
        dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.color_drawable_1));

        //dialog.show();
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();

        return dialog;
    }

}
