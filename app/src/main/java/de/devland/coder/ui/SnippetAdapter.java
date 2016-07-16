package de.devland.coder.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.devland.coder.R;
import de.devland.coder.di.ForBackground;
import de.devland.coder.di.ForMain;
import de.devland.coder.http.Commit;
import de.devland.coder.http.File;
import de.devland.coder.http.GitHubApiService;
import de.devland.coder.http.GitHubDownloadService;
import de.devland.coder.http.Repository;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;

/**
 * Created by deekay on 13.07.2016.
 */
public class SnippetAdapter extends BaseAdapter {

    @Inject
    protected Activity activity;
    @Inject
    protected LayoutInflater inflater;

    @Inject
    protected GitHubDownloadService gitHubDownloadService;
    @Inject
    protected GitHubApiService gitHubApiService;

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
            viewHolder.dataText = (TextView) rowView.findViewById(R.id.textView_snippet);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProgressDialog progressDialog = ProgressDialog.show(activity, null, "Please wait for next code snippet...", true);
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                final String snippet = getRandomCodeSnippet(0);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        viewHolder.dataText.setText(snippet);
                    }
                });
            }
        });

        return rowView;
    }

    @SneakyThrows(IOException.class)
    private String getRandomCodeSnippet(int recursionCounter) {
        if (recursionCounter > 10) {
            return "too much recursion";
        }
        recursionCounter++;

        String auth = "Basic " + Base64.encodeToString(String.format("%s:%s", "dkunzler", GitHubApiService.TOKEN).getBytes(), Base64.NO_WRAP);
        Random rand = new Random();

        List<Repository> items;
        do {
            int since = Math.abs(rand.nextInt(100000));
            items = gitHubApiService.getRepos(auth, since).execute().body();
        } while (items == null || items.isEmpty());


        Repository repo = items.get(0);

        List<Commit> commits = gitHubApiService.getCommits(auth, repo.getOwner().getLogin(), repo.getName()).execute().body();
        if (commits == null || commits.isEmpty()) {
            return getRandomCodeSnippet(recursionCounter);
        }
        String masterSha = commits.get(0).getSha();

        List<File> filesOfRepo = gitHubApiService.getFiles(auth, repo.getOwner().getLogin(), repo.getName(), masterSha).execute().body().getTree();
        if (filesOfRepo.isEmpty()) {
            return getRandomCodeSnippet(recursionCounter);
        }
        File randomFile;
        do {
            int fileNumber = rand.nextInt(filesOfRepo.size());
            randomFile = filesOfRepo.get(fileNumber);
        } while (randomFile == null || !randomFile.getType().equals("blob"));

        Log.d("Tag", repo.getOwner().getLogin() + " " + repo.getName() + " " + masterSha + " " + randomFile.getPath());
        ResponseBody body = gitHubDownloadService.downloadCode(repo.getOwner().getLogin(), repo.getName(), masterSha, randomFile.getPath()).execute().body();
        String fullCode = new String(body.bytes(), Charset.forName("UTF-8"));
        String[] lines = fullCode.split("\\n");

        int startLine = rand.nextInt(lines.length);
        int endLine = Math.min(startLine + 15, lines.length - 1);

        StringBuilder result = new StringBuilder();
        for (int i = startLine; i <= endLine; i++) {
            result.append(lines[i]);
            result.append("\n");
        }

        Log.d("Tag", result.toString());
        return result.toString();
    }
}
