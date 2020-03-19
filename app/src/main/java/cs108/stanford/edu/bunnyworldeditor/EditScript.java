package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditScript extends AppCompatActivity {

    private Docs d;
    private Shape currShape;

    public Spinner triggerSpinner;
    public Spinner onDropShapeSpinner;
    public Spinner actionSpinner;
    public Spinner nameSpinner;
    public HashMap<String,String[]> scriptMap;
    private String currentSentence;
    public TextView showScript;
    public TextView onDropListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_script);

        ///////////////////////////////////////
        // how to track the curPage?
        ///////////////////////////////////////
        d = mySingleton.getInstance().docStored;
        currShape = d.curPage.getSelectedShape();

        // set activity width and height
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width, height);


        scriptMap = new HashMap<>();
        currentSentence = "";

        triggerSpinner = findViewById(R.id.trigSpinner);
        onDropShapeSpinner = findViewById(R.id.onDropSpinner);
        actionSpinner = findViewById(R.id.actionSpinner);
        nameSpinner = findViewById(R.id.chooseNameSpinner);
        onDropListName = findViewById(R.id.dropText);
        showScript = findViewById(R.id.script_output);

        Button saveScriptButton = findViewById(R.id.save_script);
        Button addScriptButton = findViewById(R.id.add_script);
        Button clearScriptButton = findViewById(R.id.clear_script);
        Button cancelScriptButton = findViewById(R.id.cancel_script);

//        onDropShapeSpinner.setVisibility(View.GONE);

        if (currShape != null) {
            String scriptString = currShape.getScript();
            showScript.setText(scriptString);

            // save the current script into HashMap
            if (!scriptString.equalsIgnoreCase("")) {
                String[] splitSentences = scriptString.split(";");
                for (String sentence : splitSentences) {
                    String[] words = sentence.split(" ");
                    if (words.length >= 1) {
                        String[] header = Arrays.copyOfRange(words, 0, 2);
                        String headerString = joinString(header, "");
                        scriptMap.put(headerString, words);
                    }
                }
            }

            triggerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String triggerSelected = parent.getSelectedItem().toString();
                    switch (triggerSelected) {
                        case "on drop":
                            ///////////////////////////////////////////////
                            // there is a bug with currShape
                            // when click on another shape, the currshape will lost
//                            ArrayList<Shape> shapes = d.curPage.getShapes();
//                            String[] shapeNameList = new String[shapes.size() - 1];
//                            for(int i = 0; i < shapes.size()-1; i++){
//                                if(!currShape.getId().equals(shapes.get(i).getId())){
//                                    shapeNameList[i] = shapes.get(i).getId();
//                                }
//                            }
                            ///////////////////////////////////////////////
                            Set<String> shapeNameSet = d.shapeDict.keySet();
                            String[] shapeNameList = shapeNameSet.toArray(new String[shapeNameSet.size()]);
                            ArrayAdapter<String> shapeNameAdapter = new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, shapeNameList);
                            onDropShapeSpinner.setAdapter(shapeNameAdapter);
                            onDropShapeSpinner.setVisibility(View.VISIBLE);
                            onDropListName.setVisibility(View.VISIBLE);
                            break;
                        case "on click":
                        case "on enter":
                            onDropShapeSpinner.setVisibility(View.GONE);
                            onDropListName.setVisibility(View.GONE);
                            onDropShapeSpinner.setAdapter(null);
                            break;
                    }
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String actionSelected = parent.getSelectedItem().toString();
                    switch (actionSelected) {
                        case "goto":
                            Set<String> pageNamesSet = d.pageDict.keySet();
                            String[] pageNamesList = pageNamesSet.toArray(new String[pageNamesSet.size()]);
                            ArrayAdapter<String> pageNamesAdapter = new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, pageNamesList);
                            nameSpinner.setAdapter(pageNamesAdapter);
                            nameSpinner.setVisibility(View.VISIBLE);
                            break;

                        case "play":
                            String[] waveList = {"carrotcarrotcarrot", "evillaugh", "fire", "hooray",
                                    "munch", "munching", "woof"};
                            ArrayAdapter<String> waveArr = new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, waveList);
                            nameSpinner.setAdapter(waveArr);
                            nameSpinner.setVisibility(View.VISIBLE);
                            break;
                        case "hide":
                        case "show":
                            ///////////////////////////////////////////////
                            // there is a bug with currShape
                            // when click on another shape, the currshape will lost
//                            ArrayList<Shape> shapes = d.curPage.getShapes();
//                            String[] shapeNameList = new String[shapes.size() - 1];
//                            for(int i = 0; i < shapes.size()-1; i++){
//                                if(!currShape.getId().equals(shapes.get(i).getId())){
//                                    shapeNameList[i] = shapes.get(i).getId();
//                                }
//                            }
                            ///////////////////////////////////////////////
                            Set<String> shapeNameSet = d.shapeDict.keySet();
                            String[] shapeNameList = shapeNameSet.toArray(new String[shapeNameSet.size()]);
                            ArrayAdapter<String> shapeNameAdapter = new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_spinner_dropdown_item, shapeNameList);
                            nameSpinner.setAdapter(shapeNameAdapter);
                            break;
                    }
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            addScriptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] scriptArray = getChoice();
                    if (scriptArray != null) {

                        // get first to word, headerAdded
                        StringBuilder sb = new StringBuilder();
                        sb.append(scriptArray[0]);
                        sb.append(" ");
                        sb.append(scriptArray[1]);
                        if (String.valueOf(scriptArray[1]).equals("drop")) {
                            sb.append(" ");
                            sb.append(scriptArray[2]);
                        }
                        String headerAdded = sb.toString();
                        boolean newInsert = true;

                        for (Map.Entry<String, String[]> entry : scriptMap.entrySet()) {
                            String headKey = entry.getKey();
                            String[] orig = entry.getValue();
                            if (headKey.matches(headerAdded)) {
                                newInsert = false;

                                String[] newSentence = new String[orig.length + 2];

                                for (int i = 0; i < orig.length; i++) {
                                    newSentence[i] = orig[i];
                                }
                                if (!String.valueOf(scriptArray[1]).equals("drop")) {
                                    newSentence[orig.length] = scriptArray[2];
                                    newSentence[orig.length + 1] = scriptArray[3];
                                } else {
                                    newSentence[orig.length] = scriptArray[3];
                                    newSentence[orig.length + 1] = scriptArray[4];
                                }

                                scriptMap.put(headKey, newSentence);
                                StringBuilder sb1 = new StringBuilder();

                                for (String key : scriptMap.keySet()) {
                                    String[] sentence = scriptMap.get(key);
                                    sb1.append(joinString(sentence, " "));
                                    sb1.append(";");
                                }
                                currentSentence = sb1.toString();
                            }
                        }

                        if (newInsert) {
                            scriptMap.put(headerAdded, scriptArray);
                            StringBuilder sb1 = new StringBuilder();
                            for (String key : scriptMap.keySet()) {
                                String[] sentence = scriptMap.get(key);
                                sb1.append(joinString(sentence, " "));
                                sb1.append(";");
                            }
                            currentSentence = sb1.toString();
                        }
                        showScript.setText(currentSentence);
                    }
                }
            });

            clearScriptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (currShape.getScript().isEmpty()) {
//                        if (currentSentence.isEmpty()) {
//                            Toast.makeText(v.getContext(), "Already Empty", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(v.getContext(), "Not Saved, already Empty", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
                    scriptMap.clear();
                    currentSentence = "";
                    currShape.setScript(currentSentence);
                    showScript.setText(currentSentence);
                    Toast.makeText(v.getContext(), "Already Empty", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

            cancelScriptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scriptMap.clear();
                    currentSentence = "";
                    showScript.setText(currentSentence);
                    Intent intent = new Intent(EditScript.this, EditMain.class);
                    finish();
                    startActivity(intent);

                }
            });

            saveScriptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Page p = d.curPage;
                    Shape currShape = d.curPage.getSelectedShape();
                    currShape.setScript(currentSentence);

                    HashSet<String> relatedshapes = d.curPage.relatedShapes;
                    System.out.println("RelatedShapes: " + relatedshapes.toString());
                    System.out.println("Page Name: " + d.curPage.name);
                    System.out.println("Current Shape: " + currShape.getId());
                    for (String scriptName : scriptMap.keySet()) {
                        String[] sentence = scriptMap.get(scriptName);
                        if (!String.valueOf(sentence[1]).equals("drop") && sentence.length % 2 == 0) {
                            for (int ix = 3; ix <= sentence.length - 1; ix += 2) {
                                if (String.valueOf(sentence[ix - 1]).equals("goto") || String.valueOf(sentence[ix - 1]).equals("hide") || String.valueOf(sentence[ix - 1]).equals("show")) {
                                    // mySingleton.getInstance().docStored.curPage.relatedShapes.add(String.valueOf(sentence[ix]));
                                    relatedshapes.add(String.valueOf(sentence[ix]));
                            }
                            }
                        } else {
                            for (int ix = 2; ix <= sentence.length - 1; ix += 2) {
                                // mySingleton.getInstance().docStored.curPage.relatedShapes.add(String.valueOf(sentence[ix]));
                                relatedshapes.add(String.valueOf(sentence[ix]));
                            }
                        }
                    }
                    p.setRelatedShapes(relatedshapes);
                    p.setSelectedShape(currShape);
                    d.setCurPage(p);
                    mySingleton.getInstance().setDocStored(d);

                    Intent intent = new Intent(EditScript.this, EditMain.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }
    String[] getChoice(){
        String[] sentence = null;
        if (sanityCheck()){
            if (onDropShapeSpinner != null && onDropShapeSpinner.getSelectedItem() != null){
                sentence = new String[5];
                sentence[0] = triggerSpinner.getSelectedItem().toString().split(" ")[0];
                sentence[1] = triggerSpinner.getSelectedItem().toString().split(" ")[1];
                sentence[2] = onDropShapeSpinner.getSelectedItem().toString();
                sentence[3] = actionSpinner.getSelectedItem().toString();
                sentence[4] = nameSpinner.getSelectedItem().toString();

            } else {
                sentence = new String[4];
                sentence[0] = triggerSpinner.getSelectedItem().toString().split(" ")[0];
                sentence[1] = triggerSpinner.getSelectedItem().toString().split(" ")[1];
                sentence[2] = actionSpinner.getSelectedItem().toString();
                sentence[3] = nameSpinner.getSelectedItem().toString();
            }
        } else {
            Toast.makeText(this, "Invalid Selection", Toast.LENGTH_SHORT).show();
        }
        return sentence;
    }

    private boolean sanityCheck() {
        return triggerSpinner != null && triggerSpinner.getSelectedItem() != null &&
                actionSpinner != null && actionSpinner.getSelectedItem() != null &&
                nameSpinner != null && nameSpinner.getSelectedItem() != null;
    }

    private String joinString(String[] words, String divider){
        StringBuilder sb = new StringBuilder();
        for(String word : words){
            sb.append(word);
            sb.append(divider);
        }

        String join = sb.toString();
        join = join.substring(0, join.length() - divider.length());
        return join;

    }

//    private int getSpinnerIdx(Spinner spinner, String str){
//        int idx = 0;
//        for (int i = 0; i < spinner.getCount(); i++){
//            if (spinner.getItemAtPosition(i) != null){
//                if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
//                    idx = i;
//                    break;
//                }
//            }
//        }
//        return idx;
//    }

}