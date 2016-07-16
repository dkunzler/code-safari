package de.devland.coder.di.components;

import dagger.Subcomponent;
import de.devland.coder.di.ActivityScope;
import de.devland.coder.di.modules.ActivityModule;
import de.devland.coder.ui.SwipeActivity;

/**
 * Created by deekay on 16.07.2016.
 */
@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SwipeActivity activity);
}
