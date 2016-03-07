/*
Name: Narender Latchmansingh
Std. nr: 10073264
course: Native App Studio
title: lists
february 2016
*/

package com.onzin.lists;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // initialize variables, adapter, view list etc
    List<String> todoArray = new ArrayList<String>();

    EditText todoEdit;
    Button todoButton;
    ListView todoList;

    ArrayAdapter<String> todoAdapter;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



            // assign to each view a variable name
            todoEdit = (EditText) findViewById(R.id.todoEdit);
            todoButton = (Button) findViewById(R.id.todoButton);
            todoList = (ListView) findViewById(R.id.todoList);

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
                sendToast("To do list removed");
                storeTodo();
                return false;

            }
        });


        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent todoListIntent = new Intent(MainActivity.this, TodoListActivity.class);
                todoListIntent.putExtra("listId", position);
                String listName = todoList.getItemAtPosition(position).toString();
                todoListIntent.putExtra("listName", listName);
                startActivity(todoListIntent);
                finish();
            }

        });

        // set a event on button click to add the edittect content
        todoButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(todoEdit.getText().toString() == "") sendToast("insert a new to-do list");
                else {
                    todoArray.add(todoEdit.getText().toString());
                    todoAdapter.notifyDataSetChanged();
                    todoEdit.setText("");
                    sendToast(getString(R.string.messageAdded));
                    storeTodo();
                }
            }

        });

       // i don't know... helper created by setOnLongClickListener
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // i don't know... helper created by setOnLongClickListener
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.onzin.lists/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    // i don't know... helper created by setOnLongClickListener
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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

    // functie that stores array to txtfie
    public void storeTodo(){

        // write to a file
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput("todolists.txt", MODE_PRIVATE));
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

    // function to display a popup messgage
    public void sendToast(String message){

        // show toast message
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // function to load the old to-do list from a text file
    public void loadTodo(){

        // read a file and store each line in array
        try {
            Scanner scan = new Scanner(openFileInput("todolists.txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                todoArray.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
