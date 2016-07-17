package de.devland.coder.util;

import android.util.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.devland.coder.DefaultPrefs;
import de.devland.coder.http.Commit;
import de.devland.coder.http.File;
import de.devland.coder.http.GitHubApiService;
import de.devland.coder.http.GitHubDownloadService;
import de.devland.coder.http.Repository;
import de.devland.coder.model.Snippet;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;

/**
 * Created by deekay on 16.07.2016.
 */

public class GitHubUtil {

    @Inject
    protected DefaultPrefs defaultPrefs;

    @Inject
    protected GitHubDownloadService gitHubDownloadService;
    @Inject
    protected GitHubApiService gitHubApiService;

    @Inject
    public GitHubUtil() {
    }

    @SneakyThrows(IOException.class)
    public Snippet getRandomCodeSnippet(int recursionCounter) {
        if (recursionCounter > 10) {
            return null;
        }
        recursionCounter++;

        String auth = "Basic " + Base64.encodeToString(String.format("%s:%s", defaultPrefs.githubUser(), defaultPrefs.githubToken()).getBytes(), Base64.NO_WRAP);
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

        ResponseBody body = gitHubDownloadService.downloadCode(repo.getOwner().getLogin(), repo.getName(), masterSha, randomFile.getPath()).execute().body();
        String fullCode = new String(body.bytes(), Charset.forName("UTF-8"));
        String[] lines = fullCode.split("\\n");

        int startLine = rand.nextInt(lines.length);
        int endLine = Math.min(startLine + defaultPrefs.numberOfLines(), lines.length - 1);

        StringBuilder content = new StringBuilder();
        for (int i = startLine; i <= endLine; i++) {
            content.append(lines[i]);
            content.append("\n");
        }

        Snippet result = new Snippet();
        result.setContent(content.toString());
        result.setCommitId(masterSha);
        result.setNumberOfLines(endLine - startLine);
        result.setRepository(repo.getName());
        result.setUser(repo.getOwner().getLogin());
        result.setFile(randomFile.getPath());

        return result;
    }
}
