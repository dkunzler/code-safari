package de.devland.coder.model;

import java.util.UUID;

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
    private String id = UUID.randomUUID().toString();
    private String user;
    private String repository;
    private String file;
    private String commitId;
    private int startLine;
    private int numberOfLines;
    private boolean elegant;
    private String content;
}
