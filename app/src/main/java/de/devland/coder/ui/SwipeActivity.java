package de.devland.coder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import javax.inject.Inject;

import de.devland.coder.App;
import de.devland.coder.R;
import de.devland.coder.di.components.ActivityComponent;
import de.devland.coder.di.modules.ActivityModule;

/**
 * Created by deekay on 13.07.2016.
 */

public class SwipeActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    private SwipeFlingAdapterView flingContainer;
    @Inject
    protected SnippetAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        getActivityComponent().inject(this);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(SwipeActivity.this, "left", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(SwipeActivity.this, "right", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            synchronized (this) {
                if (activityComponent == null) {
                    activityComponent = (App.get().getApplicationComponent().activityComponent(new ActivityModule(this)));
                }
            }
        }
        return activityComponent;
    }

}
