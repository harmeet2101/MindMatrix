package Retrofit_Network_Utils;

import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import Constants.Const;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.Internal.logger;

//import java.util.prefs.Preferences;


public class ApiClient {

    public static final String BASE_URL = RetrofitUtils.getBaseUrl();
    private static Retrofit retrofit_base = null;
    private static Retrofit retrofit_download = null;


    public static Retrofit getBaseClient(Context context) {


        if (retrofit_base == null) {


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                   /* .addInterceptor(new AddCookiesInterceptor(context))
                    .addInterceptor(new ReceivedCookiesInterceptor(context))*/
                    .build();


            retrofit_base = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit_base;
    }


    public static Retrofit getClient(Context context) {


        if (retrofit_download == null) {


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                   /* .addInterceptor(new AddCookiesInterceptor(context))
                    .addInterceptor(new ReceivedCookiesInterceptor(context))*/
                    .build();


            retrofit_download = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Const.ServiceType.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit_download;
    }


    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            logger.info(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));


            return response;
        }
    }


    static final class CookieInterceptor implements Interceptor {
        private volatile String cookie;

        public void setSessionCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();


            cookie = new CookieManager().getCookieStore().toString();

            if (this.cookie != null) {
                request = request.newBuilder()
                        .header("Cookie", this.cookie)
                        .build();
            }
            return chain.proceed(request);
        }
    }


}