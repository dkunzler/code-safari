package de.devland.coder.http;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by deekay on 10.07.2016.
 */

@Getter
@Setter
public class RepositorySearchResult {
    private int total_count;
    private boolean incomplete_results;
    List<Repository> items;
}
