package com.warodom.sqllite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.warodom.sqllite.model.Board;
import com.warodom.sqllite.utils.DbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase mDb;
    DbHelper mHelper;
    Cursor mCursor;
    CustomAdapter adapter;

    ArrayList<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boards = new ArrayList<Board>();
        mHelper = new DbHelper(this);
        mDb = mHelper.getWritableDatabase();

//        mHelper.onUpgrade(mDb, 1, 1);
        boards = new ArrayList<Board>();
        boards = mHelper.getAllMessages();
        loadBoard2List(boards);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.addMessage(new Board("title3","NewName","New message ja"));
                boards = mHelper.getAllMessages();
                adapter.updateAdapter(boards);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Board board = data.getParcelableExtra("BOARD");
                mHelper.updateMessage(board);
                boards = mHelper.getAllMessages();
                adapter.updateAdapter(boards);
                Toast.makeText(this, "Id: "+ board.getId() +" is updated", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadBoard2List(ArrayList<Board> boards) {
//        adapter = new CustomAdapter(getApplicationContext(), boards, mHelper);
        adapter = new CustomAdapter(getApplicationContext(), boards, mHelper, MainActivity.this);
        System.out.println("Adapter " + adapter.getCount());
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
    }
}
