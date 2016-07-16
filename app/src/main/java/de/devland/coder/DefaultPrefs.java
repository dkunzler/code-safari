package de.devland.coder;

import de.devland.coder.http.GitHubApiService;
import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.annotations.Default;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by deekay on 16.07.2016.
 */
@SharedPreferences
public interface DefaultPrefs extends SharedPreferenceActions {

    @Default(ofInt = 15)
    int numberOfLines();
    void numberOfLines(int numberOfLines);

    @Default(ofString = "dkunzler")
    String githubUser();
    void githubUser(String githubUser);

    @Default(ofString = GitHubApiService.TOKEN)
    String githubToken();
    void githubToken(String githubToken);

}
