package de.devland.coder;

import android.app.Application;
import android.os.Handler;

import javax.inject.Inject;

import de.devland.coder.di.ForBackground;
import de.devland.coder.di.components.ApplicationComponent;
import de.devland.coder.di.components.DaggerApplicationComponent;
import de.devland.coder.di.modules.ApplicationModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import lombok.Getter;

/**
 * Created by deekay on 29.06.2016.
 */

public class App extends Application {

    private static App instance;

        @Inject
    @ForBackground
    protected Handler backgroundHandler;

    @Getter
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeInjector();
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static App get() {
        return instance;
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

}
