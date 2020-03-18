//package cs108.stanford.edu.bunnyworldeditor;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class EditPage extends AppCompatActivity {
//    int bgId;
//    Button closeButton;
//    AlertDialog.Builder builder;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialoig_edit_page);
//
//        setTitle("Page Editor");
//
//        Resources res = getResources();
//        String imageNames[] = {"bg1", "bg2", "bg3", "bg4", "bg5"};
//        final EditPage.pageViewData[] pageViewList = new EditPage.pageViewData[imageNames.length];
//        for (int idx = 0; idx < imageNames.length; idx++) {
//            int imageId = res.getIdentifier(imageNames[idx], "drawable", getPackageName());
//            pageViewList[idx] = new EditPage.pageViewData(imageNames[idx], imageId);
//        }
//
//        ArrayAdapter<EditPage.pageViewData> adapter =
//                new ArrayAdapter<EditPage.pageViewData>(this, R.layout.edit_shape_view, pageViewList){
//                    @Override
//                    public View getView(int position,
//                                        View convertView,
//                                        ViewGroup parent){
//                        EditPage.pageViewData currentData = pageViewList[position];
//                        if(convertView == null){
//                            convertView = getLayoutInflater()
//                                    .inflate(R.layout.edit_shape_view, null, false);
//                        }
//
//                        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
//                        TextView imageName = (TextView) convertView.findViewById(R.id.imageName);
//                        imageView.setImageResource(currentData.imageId);
//                        imageName.setText(currentData.imageName);
//
//                        return convertView;
//                    }
//                };
//        ListView pageListView = (ListView) findViewById(R.id.pageBackgroundListView);
//        pageListView.setAdapter(adapter);
//
//        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EditPage.pageViewData currentData = pageViewList[position];
//                bgId=currentData.imageId;
//                String toShow = "Selected background "+currentData.imageName+".";
//                Toast.makeText(EditPage.this, toShow, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // show current page name
//        EditText editedName = (EditText) findViewById(R.id.newPageName);
//        editedName.setText(mySingleton.getInstance().docStored.curPage.name);
//    }
//
//    public class pageViewData{
//        String imageName;
//        int imageId;
//
//        public pageViewData(String imageName, int imageId){
//            this.imageName = imageName;
//            this.imageId = imageId;
//        }
//    }
//
//
//    public void onDeletePage(View view) {
//        closeButton = (Button) findViewById(R.id.button_delete_page);
//        builder = new AlertDialog.Builder(this);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "Are you sure to delete this page?";
//                String title = "Delete Page Alert";
//                builder.setMessage(message) .setTitle(title);
//
//                //Setting message manually and performing action on button click
//                builder.setMessage(message);
//                builder.setCancelable(false);
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Page page = mySingleton.getInstance().docStored.curPage;
//                        try {
//                            mySingleton.getInstance().docStored.delPage(mySingleton.getInstance().docStored.curPage.name);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent intent = new Intent(EditPage.this, EditMain.class);
//                        Toast.makeText(getApplicationContext(), "Page deleted.",
//                                Toast.LENGTH_SHORT).show();
//                        startActivity(intent);
//
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//                        Toast.makeText(getApplicationContext(), "Page deletion canceled",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //Creating dialog box
//                AlertDialog alert = builder.create();
//                //Setting the title manually
//                alert.setTitle(title);
//                alert.show();
//            }
//        });
//    }
//
//
//    public void onCancel(View view){
//        Intent intent = new Intent(EditPage.this, EditMain.class);
//        startActivity(intent);
//    }
//
//    public void onChangePage(View view){
//        //set background image id
//        if(bgId != 0){
//            if (mySingleton.getInstance().docStored.curPage.name!= "page1")  {
//                System.out.println("invalid menu option");
//            }
//            mySingleton.getInstance().docStored.curPage.backgroundId = bgId;
//        }
//
//        Intent intent = new Intent(EditPage.this, EditMain.class);
//        //set new page name
//        EditText editedName = (EditText) findViewById(R.id.newPageName);
//        String editedNameStr = editedName.getText().toString();
//        if(editedNameStr.length() != 0){
//            if (!String.valueOf(mySingleton.getInstance().docStored.curPage.name).equals(editedNameStr)) {
//                try {
//                    mySingleton.getInstance().docStored.renamePage(mySingleton.getInstance().docStored.curPage,
//                            editedNameStr);
//                    //go back
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//        startActivity(intent);
//    }
//
//}


//package cs108.stanford.edu.bunnyworldeditor;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class EditPage extends AppCompatActivity {
//    int bgId;
//    Button closeButton;
//    AlertDialog.Builder builder;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialoig_edit_page);
//
//        setTitle("Page Editor");
//
//        Resources res = getResources();
//        String imageNames[] = {"bg1", "bg2", "bg3", "bg4", "bg5"};
//        final EditPage.pageViewData[] pageViewList = new EditPage.pageViewData[imageNames.length];
//        for (int idx = 0; idx < imageNames.length; idx++) {
//            int imageId = res.getIdentifier(imageNames[idx], "drawable", getPackageName());
//            pageViewList[idx] = new EditPage.pageViewData(imageNames[idx], imageId);
//        }
//
//        ArrayAdapter<EditPage.pageViewData> adapter =
//                new ArrayAdapter<EditPage.pageViewData>(this, R.layout.edit_shape_view, pageViewList){
//                    @Override
//                    public View getView(int position,
//                                        View convertView,
//                                        ViewGroup parent){
//                        EditPage.pageViewData currentData = pageViewList[position];
//                        if(convertView == null){
//                            convertView = getLayoutInflater()
//                                    .inflate(R.layout.edit_shape_view, null, false);
//                        }
//
//                        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
//                        TextView imageName = (TextView) convertView.findViewById(R.id.imageName);
//                        imageView.setImageResource(currentData.imageId);
//                        imageName.setText(currentData.imageName);
//
//                        return convertView;
//                    }
//                };
//        ListView pageListView = (ListView) findViewById(R.id.pageBackgroundListView);
//        pageListView.setAdapter(adapter);
//
//        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EditPage.pageViewData currentData = pageViewList[position];
//                bgId=currentData.imageId;
//                String toShow = "Selected background "+currentData.imageName+".";
//                Toast.makeText(EditPage.this, toShow, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // show current page name
//        EditText editedName = (EditText) findViewById(R.id.newPageName);
//        editedName.setText(mySingleton.getInstance().docStored.curPage.name);
//    }
//
//    public class pageViewData{
//        String imageName;
//        int imageId;
//
//        public pageViewData(String imageName, int imageId){
//            this.imageName = imageName;
//            this.imageId = imageId;
//        }
//    }
//
//
//    public void onDeletePage(View view) {
//        closeButton = (Button) findViewById(R.id.button_delete_page);
//        builder = new AlertDialog.Builder(this);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "Are you sure to delete this page?";
//                String title = "Delete Page Alert";
//                builder.setMessage(message) .setTitle(title);
//
//                //Setting message manually and performing action on button click
//                builder.setMessage(message);
//                builder.setCancelable(false);
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Page page = mySingleton.getInstance().docStored.curPage;
//                        try {
//                            mySingleton.getInstance().docStored.delPage(mySingleton.getInstance().docStored.curPage.name);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent intent = new Intent(EditPage.this, EditMain.class);
//                        Toast.makeText(getApplicationContext(), "Page deleted.",
//                                Toast.LENGTH_SHORT).show();
//                        startActivity(intent);
//
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//                        Toast.makeText(getApplicationContext(), "Page deletion canceled",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //Creating dialog box
//                AlertDialog alert = builder.create();
//                //Setting the title manually
//                alert.setTitle(title);
//                alert.show();
//            }
//        });
//    }
//
//
//    public void onCancel(View view){
//        Intent intent = new Intent(EditPage.this, EditMain.class);
//        startActivity(intent);
//    }
//
//    public void onChangePage(View view){
//        //set background image id
//        if(bgId != 2131230816){
//            if (mySingleton.getInstance().docStored.curPage.name!= "page1")  {
//                System.out.println("invalid menu option");
//            }
//            mySingleton.getInstance().docStored.curPage.backgroundId = bgId;
//        }
//
//        Intent intent = new Intent(EditPage.this, EditMain.class);
//        //set new page name
//        EditText editedName = (EditText) findViewById(R.id.newPageName);
//        String editedNameStr = editedName.getText().toString();
//        if(editedNameStr.length() != 0){
//            if (!String.valueOf(mySingleton.getInstance().docStored.curPage.name).equals(editedNameStr)) {
//                try {
//                    mySingleton.getInstance().docStored.renamePage(mySingleton.getInstance().docStored.curPage,
//                            editedNameStr);
//                    //go back
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//        startActivity(intent);
//    }
//
//}





package cs108.stanford.edu.bunnyworldeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditPage extends AppCompatActivity {
    int bgId;
    Button closeButton;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoig_edit_page);

        setTitle("Page Editor");

        Resources res = getResources();
        String imageNames[] = {"bg1", "bg2", "bg3", "bg4", "bg5"};
        final EditPage.pageViewData[] pageViewList = new EditPage.pageViewData[imageNames.length];
        for (int idx = 0; idx < imageNames.length; idx++) {
            int imageId = res.getIdentifier(imageNames[idx], "drawable", getPackageName());
            pageViewList[idx] = new EditPage.pageViewData(imageNames[idx], imageId);
        }

        ArrayAdapter<EditPage.pageViewData> adapter =
                new ArrayAdapter<EditPage.pageViewData>(this, R.layout.edit_shape_view, pageViewList){
                    @Override
                    public View getView(int position,
                                        View convertView,
                                        ViewGroup parent){
                        EditPage.pageViewData currentData = pageViewList[position];
                        if(convertView == null){
                            convertView = getLayoutInflater()
                                    .inflate(R.layout.edit_shape_view, null, false);
                        }

                        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
                        TextView imageName = (TextView) convertView.findViewById(R.id.imageName);
                        imageView.setImageResource(currentData.imageId);
                        imageName.setText(currentData.imageName);

                        return convertView;
                    }
                };
        ListView pageListView = (ListView) findViewById(R.id.pageBackgroundListView);
        pageListView.setAdapter(adapter);

        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditPage.pageViewData currentData = pageViewList[position];
                bgId=currentData.imageId;
                String toShow = "Selected background "+currentData.imageName+".";
                Toast.makeText(EditPage.this, toShow, Toast.LENGTH_SHORT).show();
            }
        });

        // show current page name
        EditText editedName = (EditText) findViewById(R.id.newPageName);
        editedName.setText(mySingleton.getInstance().docStored.curPage.name);
    }

    public class pageViewData{
        String imageName;
        int imageId;

        public pageViewData(String imageName, int imageId){
            this.imageName = imageName;
            this.imageId = imageId;
        }
    }


    public void onDeletePage(View view) {
        closeButton = (Button) findViewById(R.id.button_delete_page);
        builder = new AlertDialog.Builder(this);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Are you sure to delete this page?";
                String title = "Delete Page Alert";
                builder.setMessage(message) .setTitle(title);

                //Setting message manually and performing action on button click
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Page page = mySingleton.getInstance().docStored.curPage;
                        try {
                            mySingleton.getInstance().docStored.delPage(mySingleton.getInstance().docStored.curPage.name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(EditPage.this, EditMain.class);
                        Toast.makeText(getApplicationContext(), "Page deleted.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Page deletion canceled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(title);
                alert.show();
            }
        });
    }


    public void onCancel(View view){
        Intent intent = new Intent(EditPage.this, EditMain.class);
        startActivity(intent);
    }

    public void onChangePage(View view){
        //set background image id
        if(bgId != 2131230816){
            if (mySingleton.getInstance().docStored.curPage.name!= "page1")  {
                System.out.println("invalid menu option");
            }
            mySingleton.getInstance().docStored.curPage.backgroundId = bgId;
        }

        Intent intent = new Intent(EditPage.this, EditMain.class);
        //set new page name
        EditText editedName = (EditText) findViewById(R.id.newPageName);
        String editedNameStr = editedName.getText().toString();
        if(editedNameStr.length() != 0){
            if (!String.valueOf(mySingleton.getInstance().docStored.curPage.name).equals(editedNameStr)) {
                try {
                    mySingleton.getInstance().docStored.renamePage(mySingleton.getInstance().docStored.curPage,
                            editedNameStr);
                    //go back
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        startActivity(intent);
    }

}
