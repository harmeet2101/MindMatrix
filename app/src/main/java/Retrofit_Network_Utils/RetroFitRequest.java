package Retrofit_Network_Utils;


import android.content.Context;
import android.widget.Toast;

import com.cli.knowledgebase.R;

import Interfaces.AsyncTaskCompleteListener;
import Utils.AndyUtils;
import Utils.AppLog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by ganesha on 27/10/16.
 */

public class RetroFitRequest {


    public void networkRequest(final Context context, Call<ResponseBody> call,
                               final AsyncTaskCompleteListener taskCompleteListener, final int response_code) {


        if (!AndyUtils.isNetworkAvailable(context)) {

            AndyUtils.cancelLoading();

            Toast.makeText(context, context.getString(R.string.check_network_connection), Toast.LENGTH_LONG).show();


            return;
        }


        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                String response_body = RetrofitUtils.getStringFromStream(response);
                AppLog.Log("Retrofit response", "response " + response_body);

                //  onLoginSuccess(response_body);


                if (response_body.equals("null"))
                    call.clone();

                if (taskCompleteListener != null)
                    taskCompleteListener.onTaskCompleted(response_body, response_code);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                AndyUtils.cancelLoading();


                if (taskCompleteListener != null)
                    taskCompleteListener.onTaskFailure(response_code);

            }
        });

    }
}
