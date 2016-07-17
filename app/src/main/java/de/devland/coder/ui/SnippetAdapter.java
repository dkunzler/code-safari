package de.devland.coder.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.devland.coder.R;
import de.devland.coder.di.ForBackground;
import de.devland.coder.di.ForMain;
import de.devland.coder.model.Snippet;
import de.devland.coder.util.GitHubUtil;

/**
 * Created by deekay on 13.07.2016.
 */
public class SnippetAdapter extends BaseAdapter {

    @Inject
    protected Activity activity;
    @Inject
    protected LayoutInflater inflater;

    @Inject
    protected GitHubUtil gitHubUtil;

    @Inject
    @ForBackground
    protected Handler backgroundHandler;
    @Inject
    @ForMain
    protected Handler mainHandler;

    @Inject
    public SnippetAdapter() {
    }

    public static class ViewHolder {
        public Snippet snippet;

        @BindView(R.id.textView_snippet)
        public TextView dataText;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            ButterKnife.bind(viewHolder, rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProgressDialog progressDialog = ProgressDialog.show(activity, null, "Please wait for next code snippet...", true);
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                final Snippet snippet = gitHubUtil.getRandomCodeSnippet(0);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        viewHolder.snippet = snippet;
                        viewHolder.dataText.setText(snippet.getContent());
                    }
                });
            }
        });

        return rowView;
    }

}
