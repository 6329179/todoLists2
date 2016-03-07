package com.onzin.lists;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoListActivity extends AppCompatActivity {

    // initialize variables, adapter, view list etc
    List<String> todoArray = new ArrayList<String>();

    EditText todoEdit;
    Button todoButton;
    Button clearButton;
    Button backButton;
    ListView todoList;
    TextView listNameText;

    ArrayAdapter<String> todoAdapter;

    int listId;
    String listName;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // collect the extras put in to this activity
        Bundle extras = getIntent().getExtras();
        listId = extras.getInt("listId", listId);
        listName = extras.getString("listName",listName);

        // assign to each view a variable name
        todoEdit = (EditText) findViewById(R.id.todoEdit);
        todoButton = (Button) findViewById(R.id.todoButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        backButton = (Button) findViewById(R.id.backButton);
        todoList = (ListView) findViewById(R.id.todoList);
        listNameText = (TextView) findViewById(R.id.listNameText);

        listNameText.setText(listName);

        // setting up adapter and link it to the data Array
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoArray);

        // linking adapter to the ListView
        todoList.setAdapter(todoAdapter);

        // load the todolist from file
        loadTodo();


        // create a longclick event on a list item to remove item
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                todoArray.remove(position);
                todoAdapter.notifyDataSetChanged();
                sendToast(getString(R.string.messageRemoved));
                storeTodo();
                return false;

            }
        });

        // set a event on button click to add the edittect content
        todoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (todoEdit.getText().toString() == "") sendToast("insert a to-do item");
                else {
                    todoArray.add(todoEdit.getText().toString());
                    todoAdapter.notifyDataSetChanged();
                    todoEdit.setText("");
                    sendToast(getString(R.string.messageAdded));
                    storeTodo();
                }
            }

        });


        // set an event on button click to clear the list
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clearTodo();
                sendToast(getString(R.string.messageCleared));

            }
        });

        // set an event on back button
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(TodoListActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    // functie that stores array to txtfie
    public void storeTodo() {

        // write to a file
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput("todo_" + listId + ".txt", MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // creating text string by first deleting the whole file.
        if (!todoArray.isEmpty()) {
            for (int i = 0; i < todoArray.size(); i++) {
                out.println(todoArray.get(i).toString());
            }
        }

        out.close();
    }


    // function to clear to-do list
    public void clearTodo() {

        // creating text string by first deleting the whole file.
        todoArray.clear();
        storeTodo();
        todoAdapter.notifyDataSetChanged();

    }


    // function to load the old to-do list from a text file
    public void loadTodo() {

        // read a file and store each line in array
        try {
            Scanner scan = new Scanner(openFileInput("todo_" + listId + ".txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                todoArray.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    // function to display a popup messgage
    public void sendToast(String message) {

        // show toast message
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TodoList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.onzin.lists/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TodoList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.onzin.lists/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
