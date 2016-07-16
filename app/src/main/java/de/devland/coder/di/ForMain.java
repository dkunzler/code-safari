package de.devland.coder.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by daku on 05.08.2015.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForMain {
}
