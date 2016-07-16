package de.devland.coder.di.components;

import javax.inject.Singleton;

import dagger.Component;
import de.devland.coder.App;
import de.devland.coder.di.modules.ActivityModule;
import de.devland.coder.di.modules.ApplicationModule;

/**
 * Created by deekay on 16.07.2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(App app);

    ActivityComponent activityComponent(ActivityModule activityModule);
}
