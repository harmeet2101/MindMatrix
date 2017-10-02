package app;

import android.graphics.Bitmap;


import com.cli.knowledgebase.BuildConfig;
import com.cli.knowledgebase.R;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.splunk.mint.Mint;

import Utils.AndyUtils;

/**
 * Created by Pavan on 28-06-2015.
 */
public class AppController extends android.support.multidex.MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    public static final String TAG = AppController.class.getSimpleName();


    private static AppController mInstance;
    private static ImageLoader imageLoader;


    @Override
    public void onCreate() {
        super.onCreate();


        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .detectAll()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .detectAll()
                    .build());
        }
*/

        AndyUtils.getHashKey(this);


        if (BuildConfig.DEBUG) {

            Mint.initAndStartSession(this, this.getString(R.string.splunk_testing));
        } else {

            Mint.initAndStartSession(this, this.getString(R.string.splunk_release));
        }


        mInstance = this;


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).
                defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(2)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public static synchronized ImageLoader getImageLoader() {

        return imageLoader;


    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        imageLoader.destroy();
    }
}

