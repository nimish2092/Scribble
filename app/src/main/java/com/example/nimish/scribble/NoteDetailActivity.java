package com.example.nimish.scribble;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String NEW_NOTE_EXTRA = "New Note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        createAndAddFragment();
    }

    public void createAndAddFragment(){

        Intent intent = getIntent();
        MainActivity.FragmenttoLaunch fragmenttoLaunch = (MainActivity.FragmenttoLaunch) intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmenttoLaunch){
            case EDIT:
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.editFragmentTitle);
                fragmentTransaction.add(R.id.note_container,noteEditFragment,"NOTE_EDIT_FRAGMENT");
                break;
            case VIEW:
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.viewFragmentTitle);
                fragmentTransaction.add(R.id.note_container,noteViewFragment,"NOTE_VIEW_FRAGMENT");
                break;
            case CREATE:
                NoteEditFragment noteCreateFragment = new NoteEditFragment();
                setTitle(R.string.createFragmentTitle);

                Bundle bundle = new Bundle();
                bundle.putBoolean(NEW_NOTE_EXTRA, true);
                noteCreateFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.note_container, noteCreateFragment, "NOTE_CREATE_FRAGMENT");
                break;
        }


        fragmentTransaction.commit();
    }
}
