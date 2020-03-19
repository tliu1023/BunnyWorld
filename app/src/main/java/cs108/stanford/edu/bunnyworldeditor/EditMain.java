package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EditMain extends AppCompatActivity {
    Docs d = new Docs();
    // final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);

        // open database
        System.out.println("enter EditMain");

        // initialize docs
        Intent intent = getIntent();
        if(intent.getBooleanExtra("new", false)){
            // if create a new game, initialize a new docs
            mySingleton.getInstance().setDocStored(d);
        }
        if(intent.getBooleanExtra("fromLoad",false)){
            // else load from previous example
            System.out.println("start loading");
            String sd = intent.getStringExtra("shapeDict");
            String pd = intent.getStringExtra("pageDict");
            String cp = intent.getStringExtra("curPage");
            Boolean ie = intent.getBooleanExtra("isEdit", false);
            Boolean is = intent.getBooleanExtra("isSaved", false);

            Gson gson = new Gson();
            HashMap<String, Shape> shapeDict = gson.fromJson(sd, new TypeToken<HashMap<String, Shape>>() {}.getType());
            HashMap<String, Page> pageDict = gson.fromJson(pd, new TypeToken<HashMap<String, Page>>() {}.getType());
            Page curPage = gson.fromJson(cp, new TypeToken<Page>() {}.getType());

            d.setShapeDict(shapeDict);
            d.setPageDict(pageDict);
            d.setCurPage(curPage);
            d.setEdit(ie);
            d.setSaved(is);
            System.out.println("Read docs finished");
            mySingleton.getInstance().setDocStored(d);
        }

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

        PageView pv = findViewById(R.id.pageView);
        pv.invalidate();

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
                finish();
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
                finish();
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
                    finish();
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
                Docs temp = mySingleton.getInstance().getDocStored();
                temp.isSaved = false;
                mySingleton.getInstance().setDocStored(temp);

                if (mySingleton.getInstance().docStored.curPage != null) {
                    Intent intent = new Intent(EditMain.this, SaveEditGame.class);
                    startActivity(intent);
                }
            }
        });

        clearShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////
                // need to change
                // only clear current page
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

