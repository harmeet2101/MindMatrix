package Retrofit_Network_Utils;

import android.util.Log;


import com.cli.knowledgebase.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Constants.Const;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by ganesha on 12/10/16.
 */

public class RetrofitUtils {


    public static String getStringFromStream(Response<ResponseBody> response) {

        String response_string = "";

        if (response == null)
            return response_string;


        if (response.body() != null) {

            Log.d(TAG, "respose " + response.body().contentLength());

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = null;
            reader = new BufferedReader(
                    new InputStreamReader(response.body().byteStream()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            response_string = sb.toString();

        }

        return response_string;
    }


    public static String getBaseUrl() {

        if (BuildConfig.DEBUG) {

            return Const.ServiceType.BASE_URL;

        } else {

            return Const.ServiceType.BASE_URL;


        }
    }

}
