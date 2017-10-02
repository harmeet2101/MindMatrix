package Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cli.knowledgebase.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WINDOW_SERVICE;


@SuppressLint("NewApi")
public class AndyUtils {
    static float density = 1;
    private static ProgressDialog mProgressDialog;


    private static Dialog mDialog;


    public static ProgressDialog pDialog;

    public static void showLoading(Context context, boolean isCancelable, String loading_text) {

        if (pDialog == null || !pDialog.isShowing()) {


            SpannableString ss1 = new SpannableString(loading_text);
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
            ss1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.app_colour)), 0, ss1.length(), 0);

            SpannableString ss2 = new SpannableString(context.getString(R.string.please_wait));
            ss2.setSpan(new RelativeSizeSpan(1f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), 0, ss2.length(), 0);


            pDialog = new ProgressDialog(context);
            pDialog.setTitle(ss1);
            pDialog.setMessage(ss2);
            pDialog.setCancelable(isCancelable);
            try {
                pDialog.show();
            } catch (Exception e) {

                e.printStackTrace();

            }
        }


    }

    public static void cancelLoading() {

        try {
            if (pDialog != null)
                pDialog.dismiss();

        } catch (Exception e) {

        }
        pDialog = null;
    }


    public static boolean shouldAskPermission() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

  /*  public static void showSimpleProgressDialog(Context context) {
        showSimpleProgressDialog(context, null, "Loading...", false);
    }*/


    public static void removeCustomProgressDialog() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean eMailValidation(String emailstring) {
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }


    public static void showToast(String msg, Context ctx) {

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }


    public static int calculateInSampleSize1(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize1(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static List<String> ArrayUnion(String[] array1, String[] array2) {


        List<String> list1 = Arrays.asList(array1);
        List<String> list2 = Arrays.asList(array2);
// Prepare a union
        List<String> union = new ArrayList<String>(list1);
        union.addAll(list2);
// Prepare an intersection
        List<String> intersection = new ArrayList<String>(list1);
        intersection.retainAll(list2);
// Subtract the intersection from the union
        union.removeAll(intersection);
// Print the result

        return union;

    }


    public static int getScreenHeight(Context mActivity) {


        WindowManager manager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y;

    }

    public static int getScreenWidth(Context mActivity) {


        WindowManager manager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x;

    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    public static String getAlphaNumaricString(int len) {

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(random.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getLocale() {

        return Locale.getDefault().toString();

    }

    public static boolean isValidURL(String urlStr) {
        try {
            new URL(urlStr);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static void getHashKey(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    public static int getRandomNumber(int low, int high) {

        Random r = new Random();
        return r.nextInt(high - low) + low;

    }


    static PopupWindow popupWindow;


    private static int getStatusBarHeight(Context con) {
        int result = 0;
        int resourceId = con.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = con.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
