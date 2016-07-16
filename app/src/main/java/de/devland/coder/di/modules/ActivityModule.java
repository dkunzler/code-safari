package de.devland.coder.di.modules;

import android.app.Activity;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import de.devland.coder.di.ActivityScope;
import lombok.RequiredArgsConstructor;

/**
 * Created by deekay on 16.07.2016.
 */
@Module
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class ActivityModule {

    private final Activity activity;

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(activity);
    }
}
