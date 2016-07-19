package com.example.nimish.scribble;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton noteCatButton;
    private EditText title,message;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject,confirmDialogObject;
    private static final String MODIFIED_CATEGORY = "Modified Category";

    private boolean newNote = false;
    private long noteId = 0;


    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }

        if (savedInstanceState != null){
            savedButtonCategory = (Note.Category) savedInstanceState.get(MODIFIED_CATEGORY);
        }

        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit,container,false);

        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);
        Button savedButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        if (savedButtonCategory != null){
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));

        }else if(!newNote){
        Note.Category noteCat  = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        savedButtonCategory = noteCat;
        noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });


        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY,savedButtonCategory);
    }

    private void buildCategoryDialog(){

        final String[] categories = new String[]{"NOTE","GROCERY","DEADLINE"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                categoryDialogObject.cancel();
                switch (item){
                    case 0:
                        savedButtonCategory = Note.Category.NOTE;
                        noteCatButton.setImageResource(R.drawable.n);
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.GROCERY;
                        noteCatButton.setImageResource(R.drawable.g);
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.DEADLINE;
                        noteCatButton.setImageResource(R.drawable.d);
                        break;
                }
            }
        });

        categoryDialogObject = categoryBuilder.create();

    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Conformation Dialog");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("Save Note","Note Title " + title.getText() + "Note Message: " + message.getText() + "Note Category: " + savedButtonCategory);

                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                if (newNote){

                    dbAdapter.createNote(title.getText() + "", message.getText() + "",
                            (savedButtonCategory == null)? Note.Category.NOTE : savedButtonCategory);
                }else {

                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "", savedButtonCategory);
                }

                dbAdapter.close();

                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDialogObject = confirmBuilder.create();
    }

}
