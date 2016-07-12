package de.devland.coder.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by deekay on 10.07.2016.
 */

public interface GitHubDownloadService {
    @GET("/{user}/{repo}/{sha}/{path}")
    Call<ResponseBody> downloadCode(@Path("user") String user, @Path("repo") String repo, @Path("sha") String sha, @Path("path") String path);
}
