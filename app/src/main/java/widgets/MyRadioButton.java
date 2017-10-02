package widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.cli.knowledgebase.R;


/**
 * Created by user on 10/19/2015.
 */
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {

        super(context);

    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
       // setHintTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        //setTypeface(null, Typeface.BOLD);
        setCustomFont(context, attrs);

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
                        /*"fonts/HelveticaNeue_Light.ttf"*/"fonts/Roboto-Light.ttf");
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
