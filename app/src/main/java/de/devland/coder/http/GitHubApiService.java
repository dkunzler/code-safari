package de.devland.coder.http;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by deekay on 10.07.2016.
 */

public interface GitHubApiService {

    int MAX_RESULTS_PER_PAGE = 100;

    String TOKEN = "<insert Github API Token here>";

    @GET("/repositories")
    Call<List<Repository>> getRepos(@Header("Authorization") String auth, @Query("since") int since);

    @GET("/repos/{user}/{repo}/commits")
    Call<List<Commit>> getCommits(@Header("Authorization") String auth, @Path("user") String user, @Path("repo") String repo);

    @GET("/repos/{user}/{repo}/git/trees/{sha}?recursive=1")
    Call<Tree> getFiles(@Header("Authorization") String auth, @Path("user") String user, @Path("repo") String repo, @Path("sha") String sha);

}
