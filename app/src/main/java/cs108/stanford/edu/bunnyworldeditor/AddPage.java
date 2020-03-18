package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_page);
        setTitle("Add Page");
        // Set pop window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.3));
    }

    public void onAddNewPage(View view){
        EditText newPageName = findViewById(R.id.newPageName);
        String name = newPageName.getText().toString();
        // page name sanity check
        if(name.trim().isEmpty()){
            int id = mySingleton.getInstance().getDocStored().pageDict.size() + 1;
            name = "page" + id;
        }
        try{
            Docs d = mySingleton.getInstance().getDocStored();
            d.addPage(name);
            mySingleton.getInstance().setDocStored(d);
        }catch (Exception e){
            Toast.makeText(this, "Cannot add page", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, EditMain.class);
        finish();
        startActivity(intent);
    }

    public void onCancelAdding(View view){
        Intent intent = new Intent(this, EditMain.class);
        finish();
        startActivity(intent);
    }
}