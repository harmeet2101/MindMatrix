package Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;


import java.util.ArrayList;

import Constants.Const;

/**
 * Created by ganesha on 2/1/17.
 */

public class PermissionUtils {

    public static boolean isPermissionGranted(Context con, String permission) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(con,
                permission) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }


        return true;


    }


    public static void getLocationPermission(Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                Const.RequestCode.GET_LOCATION_PERMISSION);

    }


    public static void getCameraPermission(Activity activity) {


        ArrayList<String> permissionList = new ArrayList<>();

        if (!isPermissionGranted(activity, Manifest.permission.CAMERA))
            permissionList.add(Manifest.permission.CAMERA);

        if (!isPermissionGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        String[] permission = new String[permissionList.size()];

        for (int k = 0; k < permissionList.size(); k++)
            permission[k] = permissionList.get(k);


        ActivityCompat.requestPermissions(
                activity, permission,
                Const.RequestCode.GET_CAMERA_PERMISSION);

    }


    public static void getStoragePermission(Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                Const.RequestCode.GET_STORAGE_PERMISSION);

    }


    public static void getPhonePermission(Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.CALL_PHONE},
                Const.RequestCode.GET_PHONE_PERMISSION);

    }
}
