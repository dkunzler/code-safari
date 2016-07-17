package de.devland.coder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.devland.coder.App;
import de.devland.coder.R;
import de.devland.coder.di.components.ActivityComponent;
import de.devland.coder.di.modules.ActivityModule;
import de.devland.coder.model.Snippet;
import io.realm.Realm;

/**
 * Created by deekay on 13.07.2016.
 */

public class SwipeActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @BindView(R.id.frame)
    protected SwipeFlingAdapterView flingContainer;
    @Inject
    protected SnippetAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            private View currentView;

            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object o) {
                Snackbar.make(flingContainer, "Marked as not elegant.", Snackbar.LENGTH_SHORT).show();
                SnippetAdapter.ViewHolder viewHolder = (SnippetAdapter.ViewHolder) currentView.getTag();
                viewHolder.snippet.setElegant(false);
                writeToRealm(viewHolder.snippet);
                adapter.notifyDataSetChanged();
            }

            private void writeToRealm(Snippet snippet) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.beginTransaction();
                    realm.copyToRealm(snippet);
                    realm.commitTransaction();
                }
            }

            @Override
            public void onRightCardExit(Object o) {
                Snackbar.make(flingContainer, "Marked as elegant.", Snackbar.LENGTH_SHORT).show();
                SnippetAdapter.ViewHolder viewHolder = (SnippetAdapter.ViewHolder) currentView.getTag();
                viewHolder.snippet.setElegant(true);
                writeToRealm(viewHolder.snippet);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                currentView = flingContainer.getSelectedView();
                currentView.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                currentView.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
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
