package main.com.cineramamaps.restapi;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import main.com.cineramamaps.BuildConfig;
import main.com.cineramamaps.constant.BaseUrl;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {

    private final Retrofit retrofit;
    private static volatile ApiCall instance;
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static ApiCall get() {
        if (instance == null) {
            synchronized (ApiCall.class) {
                if (instance == null) {
                    instance = new ApiCall();
                }
            }
        }
        return instance;
    }

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        // Add HTTP response cache (10 MB)
        if (appContext != null) {
            File cacheDir = new File(appContext.getCacheDir(), "http_cache");
            Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
            builder.cache(cache);

            // Force cache for GET requests when server doesn't send cache headers
            builder.addNetworkInterceptor(chain -> {
                okhttp3.Response response = chain.proceed(chain.request());
                if (chain.request().method().equals("GET")) {
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=300")
                            .build();
                }
                return response;
            });
        }

        if (BuildConfig.DEBUG) {
            try {
                Class<?> clazz = Class.forName("com.grapesnberries.curllogger.CurlLoggerInterceptor");
                builder.addInterceptor((okhttp3.Interceptor) clazz.newInstance());
            } catch (Exception ignored) {}
        }

        return builder.build();
    }

    private static final OkHttpClient client = buildClient();





    private final Api api;

    private ApiCall() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
         retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.baseurl)
                 .client(client)
                 .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
         api = retrofit.create(Api.class);
    }

    public Api Create(){
        return api;
    }

}
