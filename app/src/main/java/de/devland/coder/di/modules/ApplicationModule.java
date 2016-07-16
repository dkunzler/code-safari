package de.devland.coder.di.modules;

import android.os.Handler;
import android.os.HandlerThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.devland.coder.App;
import de.devland.coder.DefaultPrefs;
import de.devland.coder.di.ForBackground;
import de.devland.coder.di.ForMain;
import de.devland.coder.http.GitHubApiService;
import de.devland.coder.http.GitHubDownloadService;
import de.devland.esperandro.Esperandro;
import lombok.RequiredArgsConstructor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deekay on 16.07.2016.
 */
@Module
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class ApplicationModule {

    private final App app;

    @Provides
    App provideApp() {
        return app;
    }

    @Provides
    DefaultPrefs provideDefaultPrefs() {
        return Esperandro.getPreferences(DefaultPrefs.class, app);
    }

    @Provides
    GitHubApiService provideApiService() {
        Retrofit apiRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return apiRetrofit.create(GitHubApiService.class);
    }

    @Provides
    GitHubDownloadService provideDownloadService() {
        Retrofit downloadRetrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .build();
        return downloadRetrofit.create(GitHubDownloadService.class);
    }

    @Provides
    @Singleton
    @ForMain
    Handler provideMainThreadHandler() {
        return new Handler();
    }

    @Provides
    @Singleton
    @ForBackground
    Handler provideBackgroundThreadHandler() {
        HandlerThread backgroundThread = new HandlerThread("background");
        backgroundThread.start();
        return new Handler(backgroundThread.getLooper());
    }
}
