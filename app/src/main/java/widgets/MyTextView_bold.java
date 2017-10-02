package widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cli.knowledgebase.R;


/**
 * Created by user on 10/19/2015.
 */
public class MyTextView_bold extends TextView {
    public MyTextView_bold(Context context) {

        super(context);

    }

    public MyTextView_bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setHintTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        setTypeface(null, Typeface.BOLD);
        setCustomFont(context, attrs);
     //   setTextIsSelectable(true);
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
                        /*"fonts/Helvetica_Neu_Bold.ttf"*/"fonts/Roboto-Bold.ttf"/*"fonts/comic_sans_bold.ttf"*/);
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
