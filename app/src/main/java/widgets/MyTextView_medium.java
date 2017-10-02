package widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cli.knowledgebase.R;


/**
 * Created by Getit on 07-06-2016.
 */
public class MyTextView_medium extends TextView {
    public MyTextView_medium(Context context) {

        super(context);

    }

    public MyTextView_medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHintTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        //setTypeface(null, Typeface.BOLD);
        setCustomFont(context, attrs);
      //  setTextIsSelectable(true);
       // setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
    }

    private Typeface typeface;


    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        String customFont = a.getString(R.styleable.app_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        /*"fonts/HelveticaNeue_Medium.ttf"*/"fonts/Roboto-Medium.ttf"/*"fonts/comic_sans_medium.ttf"*/);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
