package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EditMain extends AppCompatActivity {
    SQLiteDatabase db;
    Docs d;
    // final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // open database
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);

        // initialize docs
        Intent intent = getIntent();
        if(intent.getBooleanExtra("new", false)){
            // if create a new game, initialize a new docs
            d = new Docs();
            mySingleton.getInstance().docStored = d;
        }
        if(intent.getBooleanExtra("fromLoad",false)){
            // else load from previous example
            String gameName = intent.getStringExtra("gameName");
            String query = "SELECT gamesData FROM games WHERE name = '" + gameName + "';";
            Cursor cursor = db.rawQuery(query,null);
            System.out.println("count: " + cursor.getCount());

            // create a docs from database
            String json = "";
            if(cursor.moveToNext()){
                json = cursor.getString(0);
            }
            Gson gson = new Gson();
            d = gson.fromJson(json, Docs.class);
            mySingleton.getInstance().docStored = d;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);

        Button copyShape =  findViewById(R.id.button_copyShape);
        Button clearShape =  findViewById(R.id.button_clear);
        Button editShape =  findViewById(R.id.button_editShape);
        Button gotoPage =  findViewById(R.id.button_gotoPage);
        Button addPage =  findViewById(R.id.button_addPage);
        Button editPage =  findViewById(R.id.button_editPage);
        Button scriptButton = findViewById(R.id.button_script);
        Button addShapeButton = findViewById(R.id.button_addShape);
        Button saveEditGameButton =  findViewById(R.id.button_saveData);
        TextView pageName = findViewById(R.id.page_name);

        pageName.setText(mySingleton.getInstance().docStored.curPage.name);

        editShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySingleton.getInstance().docStored.curPage.selectedShape != null) {
                    Docs temp = mySingleton.getInstance().docStored;
                    temp.isSaved = false;
                    mySingleton.getInstance().setDocStored(temp);
                    finish();
                    startActivity(new Intent(EditMain.this, EditShape.class));
                }
            }
        });

        gotoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs temp = mySingleton.getInstance().docStored;
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);
                startActivity(new Intent(EditMain.this, GotoPage.class));
            }
        });

        addShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs temp = mySingleton.getInstance().docStored;
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);
                finish();
                startActivity(new Intent(EditMain.this, AddShape.class));
            }
        });


        addPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs temp = mySingleton.getInstance().docStored;
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);
                startActivity(new Intent(EditMain.this, AddPage.class));
            }
        });

        editPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySingleton.getInstance().docStored.curPage != null) {
                    Docs temp = mySingleton.getInstance().docStored;
                    temp.isSaved = false;
                    mySingleton.getInstance().setDocStored(temp);
                    startActivity(new Intent(EditMain.this, EditPage.class));
                }
            }
        });

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs temp = mySingleton.getInstance().docStored;
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);
                finish();
                startActivity(new Intent(EditMain.this, EditScript.class));
            }
        });

        saveEditGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs temp = mySingleton.getInstance().docStored;
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);

                if (mySingleton.getInstance().docStored.curPage != null) {
                    Intent intent = new Intent(EditMain.this, SaveEditGame.class);
                    if (intent.getBooleanExtra("new", true)) {
                        intent.putExtra("filename", "demo");
                    }
                    startActivity(intent);
                }
            }
        });

        clearShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Docs newDocs = mySingleton.getInstance().docStored;
                newDocs.shapeDict = new HashMap<>();
                newDocs.curPage.shapes = new ArrayList<>();
                newDocs.curPage.relatedShapes = new HashSet<>();
                newDocs.curPage.selectedShape = null;

                mySingleton.getInstance().setDocStored(newDocs);

                PageView pv = findViewById(R.id.pageView);
                pv.invalidate();
            }
        });

        copyShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mySingleton.getInstance().docStored.curPage.selectedShape == null){
                    try {
                        throw new Exception("No current page.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Shape selectedShape = mySingleton.getInstance().docStored.curPage.selectedShape;
                    String newName = "Copyed_" + selectedShape.id;
                    Shape newShape = new Shape(newName, 160, 160, 0, 0);
                    newShape.setImgPath(selectedShape.getImgPath());

                    try {
                        mySingleton.getInstance().docStored.addShape(newShape);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PageView pv = findViewById(R.id.pageView);
                    pv.invalidate();
                }
            }
        });
    }

    public void drawShape(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }


}

