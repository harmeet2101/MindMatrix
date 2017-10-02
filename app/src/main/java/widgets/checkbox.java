package widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cli.knowledgebase.R;


/**
 * Created by user on 10/19/2015.
 */
public class checkbox extends AppCompatCheckBox {
    public checkbox(Context context) {

        super(context);

    }

    public checkbox(Context context, AttributeSet attrs) {
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
                        /*"fonts/HelveticaNeue_Light.ttf"*/"fonts/Roboto-Regular.ttf");
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
