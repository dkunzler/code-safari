package de.devland.coder;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import de.devland.coder.http.Commit;
import de.devland.coder.http.File;
import de.devland.coder.http.GitHubApiService;
import de.devland.coder.http.GitHubDownloadService;
import de.devland.coder.http.Repository;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private GitHubDownloadService gitHubDownloadService;
    private GitHubApiService gitHubApiService;

    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit apiRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit downloadRetrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .build();

        gitHubApiService = apiRetrofit.create(GitHubApiService.class);
        gitHubDownloadService = downloadRetrofit.create(GitHubDownloadService.class);

        HandlerThread backgroundThread = new HandlerThread("network");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                final String snippet = getRandomCodeSnippet();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView) findViewById(R.id.textView_code);
                        tv.setText(snippet);
                    }
                });
            }
        });
    }

    @SneakyThrows(IOException.class)
    private String getRandomCodeSnippet() {
        String auth = "Basic " + Base64.encodeToString(String.format("%s:%s", "dkunzler", GitHubApiService.TOKEN).getBytes(), Base64.NO_WRAP);
        Random rand = new Random();

        List<Repository> items;
        do {
            int since = Math.abs(rand.nextInt(100000));
            items = gitHubApiService.getRepos(auth, since).execute().body();
        } while (items == null || items.isEmpty());


        Repository repo = items.get(0);

        List<Commit> commits = gitHubApiService.getCommits(auth, repo.getOwner().getLogin(), repo.getName()).execute().body();
        if (commits.isEmpty()) {
            return getRandomCodeSnippet();
        }
        String masterSha = commits.get(0).getSha();

        List<File> filesOfRepo = gitHubApiService.getFiles(auth, repo.getOwner().getLogin(), repo.getName(), masterSha).execute().body().getTree();
        if (filesOfRepo.isEmpty()) {
            return getRandomCodeSnippet();
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

        return result.toString();
    }
}
