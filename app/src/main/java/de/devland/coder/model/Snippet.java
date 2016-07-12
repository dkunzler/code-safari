package de.devland.coder.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by deekay on 09.07.2016.
 */

@Getter
@Setter
public class Snippet extends RealmObject {
    @PrimaryKey
    private long id;
    private String user;
    private String repository;
    private String commitId;
    private String language;
    private int startLine;
    private int numberOfLines;
    private int elegance;
}
