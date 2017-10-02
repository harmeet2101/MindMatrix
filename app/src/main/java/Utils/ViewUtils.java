package Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cli.knowledgebase.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import Constants.Const;

/**
 * Created by ganesha on 22/11/16.
 */

public class ViewUtils {


    public static void launchActivity(Context con, Bundle bundle, Class aClass) {

        Intent intent = new Intent(con, aClass);

        if (bundle != null)
            intent.putExtra(Const.IntentParams.INTENT_BUNDLE, bundle);

        con.startActivity(intent);


    }


    public static void openWebActivity(Context context, String url) {

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);

    }

    public static void sendLocalBroadCast(Context con, String intent_filter, Bundle bundle) {


        Intent localBraodCastIntent = new Intent(intent_filter);

        if (bundle != null)
            localBraodCastIntent.putExtra(Const.IntentParams.BROADCAST_BUNDLE, bundle);

        LocalBroadcastManager.getInstance(con).sendBroadcast(localBraodCastIntent);

    }


    public static void addFragment(Fragment fragment, boolean addToBackStack, Bundle bundle,
                                   String tag, FragmentManager manager, int container) {


        // FragmentManager manager = this.getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        if (bundle != null)
            fragment.setArguments(bundle);

        if (addToBackStack) {

            ft.addToBackStack(tag);

        }


        ft.replace(container, fragment, tag);
        ft.commitAllowingStateLoss();

        // ft.commit();

        // ft.commitAllowingStateLoss();
    }


    public static void openGallery(Activity activity) {


        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, Const.RequestCode.OPEN_GALLERY);

    }


    public static void openFiles(Activity activity) {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("application/*");
        activity.startActivityForResult(intent, Const.RequestCode.OPEN_FILES);

    }


    public static void callIntent(Context context, String phoneNumber) {


        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }


    public static void openCamera(Activity activity) {


        Uri fileUri = FileUtils.getMediaFileUri(activity);
        Const.imagePAth = fileUri.toString();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, Const.RequestCode.OPEN_CAMERA);
    }

    public static void runCropImage(Uri filePath, Activity activity, Class aClass) {


        if (aClass != null)
            CropImage.activity(filePath).start(activity, aClass);
        else
            CropImage.activity(filePath).start(activity);
    }


    public static void showMediaChoiceDialog(final Activity activity) {


        CharSequence[] selectedOptions = new CharSequence[]{activity.getString(R.string.pick_image_camera),
                activity.getString(R.string.pick_image_gallery), activity.getString(R.string.image_picker_cancel)};


        final CharSequence[] options = selectedOptions;

        //creating the alert builder to display image picker options
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.image_picker_title));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        if (PermissionUtils.isPermissionGranted(activity, Manifest.permission.CAMERA)
                                && PermissionUtils.isPermissionGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            openCamera(activity);
                        } else {
                            PermissionUtils.getCameraPermission(activity);

                        }

                        break;

                    case 1:

                        if (PermissionUtils.isPermissionGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            openGallery(activity);
                        } else {

                            PermissionUtils.getStoragePermission(activity);
                        }

                        break;

                    case 2:

                        dialog.dismiss();

                        break;


                }


            }
        });
        builder.show();


    }


    //private static final float BLUR_RADIUS = 25f;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap getBlurImage(Bitmap image, Context context, float blur_radius) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(blur_radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }


    public static void launchNetworkSettingsView(Context context) {

        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }


    public static void openMailClient(Context context, String email_id,
                                      String subject, String file_path) {


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email_id, null));

        if (!subject.equals(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (!file_path.equals("")) {
            Uri uri = Uri.parse("file://" + file_path);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(emailIntent, 0);

        if (list.size() == 0) {
            // show error dialog

          /*  new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                    .setContentText(context.getString(R.string.email_client_not_exist) + " "
                            + email_id)

                    .setConfirmText(this.getString(R.string.btn_ok))
                    .show();*/

        } else {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

    }


    public static void closeKeyboard(Activity mActivity) {

        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    public static Spannable getSpanableText(Context context, String string, int from, int to,
                                            ForegroundColorSpan foregroundColorSpan) {


        Spannable span = new SpannableString(string);
        span.setSpan(foregroundColorSpan,
                from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                from, to, 0);

        return span;


    }
}
