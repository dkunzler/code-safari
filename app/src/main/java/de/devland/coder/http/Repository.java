package de.devland.coder.http;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by deekay on 10.07.2016.
 */

@Getter
@Setter
public class Repository {
    private long id;
    private String name;
    private String full_name;
    private User owner;
    private String language;
}
