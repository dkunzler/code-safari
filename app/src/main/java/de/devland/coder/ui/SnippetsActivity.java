package de.devland.coder.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.devland.coder.R;

public class SnippetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippets);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SnippetFragment())
                    .commit();
        }
    }
}
