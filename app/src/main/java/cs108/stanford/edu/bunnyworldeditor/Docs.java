//package cs108.stanford.edu.bunnyworldeditor;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//
//public class Docs implements Serializable {
//    public HashMap<String, Shape> shapeDict;
//    public HashMap<String, Page> pageDict;
//    public ArrayList<Shape> possession;
//    public ArrayList<String> imageList;
//    public Page curPage;
//    public boolean isEdit;
//    public Shape copiedShape;
//    public int copyNum;
//    public boolean isSaved;
//
//    public Docs(){
//        shapeDict = new HashMap<String, Shape>();
//        possession = new ArrayList<Shape>();
//        pageDict = new HashMap<String, Page>();
//        imageList = new ArrayList<String> ();
//        Page page1 = new Page("page1");
//        pageDict.put("page1", page1);
//        curPage = page1;
//        isSaved = true;
//    }
//
//    public void addPage(String name) throws Exception {
//        name = name.toLowerCase();
//        if (pageDict.containsKey(name)) {
//            throw new Exception("Please use another page name");
//        }
//        Page page = new Page(name);
//        pageDict.put(name, page);
//        curPage = page;
//    }
//
//    public void delPage(String name) throws Exception {
//        name = name.toLowerCase();
//        if (name.equals("page1")) {
//            throw new Exception("page1 cannot be deleted.");
//        }
//        if (pageDict.containsKey(name)) {
//            // set the curpage to page1 if it has been deleted
//            Page page = pageDict.get(name);
//            if (curPage == page) {
//                curPage = pageDict.get("page1");
//            }
//            // delete all the shapes of the page
//            for (Shape shape : page.shapes){
//                delShape(shape);
//            }
//            // update all the scripts of the related shapes
//            updateScriptRelatedToPage(page, "delete", "");
//            pageDict.remove(name);
//        }
//    }
//
//
//    public void renamePage(Page page, String newName) throws Exception {
//        newName = newName.toLowerCase();
//        if (page.name.equals("page1")) {
//            throw new Exception("page1 cannot be renamed.");
//        }
//
//        if (pageDict.containsKey(newName)) {
//            throw new Exception("page name already taken.");
//        }
//
//        updateScriptRelatedToPage(page, "rename", newName);
//        pageDict.remove(page.name);
//        page.setName(newName);
//        pageDict.put(newName, page);
//    }
//
//
//    public void addShape(Shape shape) throws Exception {
//        if (curPage == null) {
//            throw new Exception("No current page");
//        }
//
//        if (shapeDict.containsKey(shape.id)) {
//            throw new Exception("Shape name already taken");
//        }
//
//        // if name not specified, use default
//        if (shape.id.equals("")) {
//            for (int i = 0; i < shapeDict.size(); i++) {
//                String name = "shape" + i;
//                if (!shapeDict.containsKey(name)) {
//                    shape.setId(name);
//                }
//            }
//        }
//        shapeDict.put(shape.id, shape);
//        curPage.addShape(shape);
//        curPage.setSelectedShape(shape);
//    }
//
//
//    public void delShape(Shape shape) throws Exception {
//        if(curPage == null){
//            throw new Exception("No current page.");
//        }
//
//        mySingleton.getInstance().docStored.isSaved = false;
//        HashSet<String> relatedIds = shape.relatedShapes;
//        String id = shape.id;
//        // remove the shape id from all the related shapes
//        for (String relatedId: relatedIds) {
//            if (shapeDict.get(relatedId) != null) {
//                Shape relatedShape = shapeDict.get(relatedId);
//                String script = relatedShape.getScript();
//                relatedShape.relatedShapes.remove(id);
//                String new_script = updateScriptByShapeName("delete", id, "", script);
//                shapeDict.get(relatedId).setScript(new_script);
//            }
//
//        }
//        for (Page page  : pageDict.values()) {
//            if (page.relatedShapes.contains(mySingleton.getInstance().docStored.curPage.name)) {
//                page.relatedShapes.remove(mySingleton.getInstance().docStored.curPage.name);
//            }
//        }
//        shapeDict.remove(id);
//        curPage.removeShape(shape);
//        // if the deleted shape was selected
//        if (curPage.getSelectedShape().id == id) {
//            curPage.setSelectedShape(null);
//        }
//    }
//
//
//    public void renameShape(Shape shape, String newName) throws Exception {
//        if (curPage == null) {
//            throw new Exception("No current page");
//        }
//
//        if(shapeDict.containsKey(newName)){
//            throw new Exception("Shape name already taken");
//        }
//
//        String scripts = shape.getScript();
//
//        updateScriptRelatedToShape(shape, newName);
//
//        shapeDict.remove(shape.id);
//        shape.setId(newName);
//        shapeDict.put(newName, shape);
//    }
//
//
//    public String updateScriptByShapeName(String mode, String name, String newName, String scripts) {
//        String[] ss = scripts.split(";");
//        String newScripts = "";
//        String newS = "";
//        if (mode == "delete") {
//            // delete the shape id in the script
//            for (String s: ss) {
//                List<String> split = Arrays.asList(s.split(" "));
//                // add to the scripts if the script does not contain the shape id
//                if (!split.contains(name)) {
//                    newScripts += s + ";";
//                }
//                else {
//                    newS = "";
//                    // if the script actions contain the shape id
//                    if (split.get(1) == "drop") {
//                        if (split.get(2) != name) {
//                            newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
//                            for (int i = 3; i < split.size(); i++) {
//                                if (i % 2 == 0 && split.get(i) != name) {
//                                    newS += split.get(i-1) + " " + split.get(i) + " ";
//                                }
//                            }
//                        }
//                    }
//                    else {
//                        newS = split.get(0) + " " + split.get(1) + " ";
//                        for (int i = 2; i < split.size(); i++) {
//                            if (i % 2 != 0 && split.get(i) != name) {
//                                newS += split.get(i-1) + " " + split.get(i) + " ";
//                            }
//                        }
//                    }
//                    newScripts += newS + ";";
//                }
//
//            }
//        }
//        else {
//            // rename the shape id in the script
//            for(String s: ss) {
//                List<String> split = Arrays.asList(s.split(" "));
//                if (!split.contains(name)) {
//                    newScripts += s + ";";
//                }
//                else {
//                    newS = "";
//                    if (split.get(1) == "drop") {
//                        newS = split.get(0) + " " + split.get(1) + " ";
//                        for (int i = 2; i < split.size(); i++) {
//                            if (i % 2 == 0 && split.get(i) == name) {
//                                newS += newName + " ";
//                            }
//                            else {
//                                newS += split.get(i) + " ";
//                            }
//                        }
//                    }
//                    else {
//                        newS = split.get(0) + " " + split.get(1) + " ";
//                        for (int i = 2; i < split.size(); i++) {
//                            if (i % 2 != 0 && split.get(i) == name) {
//                                newS += newName + " ";
//                            }
//                            else {
//                                newS += split.get(i) + " ";
//                            }
//                        }
//                    }
//                    newScripts += newS + ";";
//                }
//
//            }
//
//        }
//        return newScripts;
//    }
//
//
//    public void updateScriptRelatedToPage(Page page, String mode, String newName){
//        HashSet<String> relatedShapes = page.relatedShapes;
//        for (String name: relatedShapes) {
//            Shape relatedShape = mySingleton.getInstance().shapeDict.get(name);
//            if (relatedShape != null) {
//                String scripts = relatedShape.getScript();
//                String new_scripts = updateScriptByPageName(mode, name, newName, scripts);
//                mySingleton.getInstance().shapeDict.get(name).setScript(new_scripts);
//            }
//        }
//
//    }
//
//    public String updateScriptByPageName(String mode, String name, String newName, String scripts){
//        String[] ss = scripts.split(";");
//        String newScripts = "";
//        if (mode == "delete") {
//            // delete the page name in the script
//            for (String s: ss) {
//                List<String> split = Arrays.asList(s.split(" "));
//                if (!split.contains(name)) {
//                    newScripts += s + ";";
//                }
//                else {
//                    String newS = "";
//                    if (split.get(1) == "drop") {
//                        newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
//                        for (int i = 3; i < split.size(); i++) {
//                            if (i % 2 == 0 && (split.get(i-1) != "goto" || split.get(i) != name)) {
//                                newS += split.get(i-1) + " " + split.get(i) + " ";
//                            }
//                        }
//                    }
//                    else{
//                        newS = split.get(0) + " " + split.get(1) + " ";
//                        for (int i = 2; i < split.size(); i++) {
//                            if (i % 2 != 0 && (split.get(i-1) != "goto" || split.get(i) != name)) {
//                                newS += split.get(i-1) + " " + split.get(i) + " ";
//                            }
//                        }
//                    }
//                    newScripts += newS + ";";
//                }
//
//            }
//        }
//        else {
//            // rename the page name in the script
//            for (String s: ss) {
//                List<String> split = Arrays.asList(s.split(" "));
//                if (!split.contains(name)) {
//                    newScripts += s + ";";
//                }
//                else {
//                    String newS = "";
//                    if (split.get(1) == "drop") {
//                        newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
//                        for (int i = 3; i < split.size(); i++) {
//                            if (i % 2 == 0 && split.get(i-1) == "goto" && split.get(i) == name) {
//                                newS += newName + " ";
//                            }
//                            else {
//                                newS += split.get(i) + " ";
//                            }
//                        }
//                    }
//                    else{
//                        newS = split.get(0) + " " + split.get(1) + " ";
//                        for (int i = 2; i < split.size(); i++) {
//                            if (i % 2 != 0 && split.get(i-1) == "goto" && split.get(i) == name) {
//                                newS += newName + " ";
//                            }
//                            else {
//                                newS += split.get(i) + " ";
//                            }
//                        }
//                    }
//                    newScripts += newS + ";";
//                }
//
//            }
//        }
//        return newScripts;
//    }
//
//
//
//    public void updateScriptRelatedToShape(Shape shape, String newName){
//        // update the script of the shapes related to the given shape
//        HashSet<String> relatedShapes = shape.relatedShapes;
//        String name = shape.id;
//        for (String relatedShapeName: relatedShapes) {
//            Shape relatedShape = mySingleton.getInstance().shapeDict.get(relatedShapeName);
//            relatedShape.relatedShapes.remove(name);
//            relatedShape.relatedShapes.add(newName);
//            if (relatedShape != null) {
//                String scripts = relatedShape.getScript();
//                String newScripts = updateScriptByShapeName("rename", name, newName, scripts);
//                mySingleton.getInstance().shapeDict.get(name).setScript(newScripts);
//            }
//
//        }
//        for (Page page  : pageDict.values()) {
//            if (page.relatedShapes.contains(name)) {
//                page.relatedShapes.remove(name);
//                page.relatedShapes.add(newName);
//            }
//        }
//    }
//
//
//
//}


package cs108.stanford.edu.bunnyworldeditor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Docs implements Serializable {
    // to do change all parameter to private
    public HashMap<String, Shape> shapeDict;
    public HashMap<String, Page> pageDict;
    public Page curPage;
    public boolean isEdit;
    public boolean isSaved;

    public Docs(){
        shapeDict = new HashMap<>();
        pageDict = new HashMap<>();
        Page page1 = new Page("page1");
        pageDict.put("page1", page1);
        curPage = page1;
        isSaved = true;
    }

    public void addPage(String name) throws Exception {
        name = name.toLowerCase();
        if (pageDict.containsKey(name)) {
            throw new Exception("Please use another page name");
        }
        Page page = new Page(name);
        pageDict.put(name, page);
        curPage = page;
    }

    public void delPage(String name) throws Exception {
        name = name.toLowerCase();
        if (name.equals("page1")) {
            throw new Exception("page1 cannot be deleted.");
        }
        if (pageDict.containsKey(name)) {
            // set the curpage to page1 if it has been deleted
            Page page = pageDict.get(name);
            if (curPage == page) {
                curPage = pageDict.get("page1");
            }
            // delete all the shapes of the page
            for (Shape shape : page.shapes){
                delShape(shape);
            }
            // update all the scripts of the related shapes
            updateScriptRelatedToPage(page, "delete", "");
            pageDict.remove(name);
        }
    }


    public void renamePage(Page page, String newName) throws Exception {
        newName = newName.toLowerCase();
        if (page.name.equals("page1")) {
            throw new Exception("page1 cannot be renamed.");
        }

        if (pageDict.containsKey(newName)) {
            throw new Exception("page name already taken.");
        }

        updateScriptRelatedToPage(page, "rename", newName);
        pageDict.remove(page.name);
        page.setName(newName);
        pageDict.put(newName, page);
    }

    public void addShape(Shape shape) throws Exception {
        if (curPage == null) {
            throw new Exception("No current page");
        }

        if (shapeDict.containsKey(shape.id)) {
            throw new Exception("Shape name already taken");
        }
        // if name not specified, use default
        if (shape.id.equals("")) {
            for (int i = 0; i < shapeDict.size(); i++) {
                String name = "shape" + i;
                if (!shapeDict.containsKey(name)) {
                    shape.setId(name);
                }
            }
        }
        shapeDict.put(shape.id, shape);
        curPage.addShape(shape);
        curPage.setSelectedShape(shape);
    }


    public void delShape(Shape shape) throws Exception {
        if(curPage == null){
            throw new Exception("No current page.");
        }

        mySingleton.getInstance().docStored.isSaved = false;
        HashSet<String> relatedIds = shape.relatedShapes;
        String id = shape.id;
        // remove the shape id from all the related shapes
        for (String relatedId: relatedIds) {
            if (shapeDict.get(relatedId) != null) {
                Shape relatedShape = shapeDict.get(relatedId);
                String script = relatedShape.getScript();
                relatedShape.relatedShapes.remove(id);
                String new_script = updateScriptByShapeName("delete", id, "", script);
                shapeDict.get(relatedId).setScript(new_script);
            }

        }
        for (Page page  : pageDict.values()) {
            if (page.relatedShapes.contains(mySingleton.getInstance().docStored.curPage.name)) {
                page.relatedShapes.remove(mySingleton.getInstance().docStored.curPage.name);
            }
        }
        shapeDict.remove(id);
        curPage.removeShape(shape);
        // if the deleted shape was selected
        if (curPage.getSelectedShape().id == id) {
            curPage.setSelectedShape(null);
        }
    }


    public void renameShape(Shape shape, String newName) throws Exception {
        if (curPage == null) {
            throw new Exception("No current page");
        }

        if(shapeDict.containsKey(newName)){
            throw new Exception("Shape name already taken");
        }

        updateScriptRelatedToShape(shape, newName);

        shapeDict.remove(shape.id);
        shape.setId(newName);
        shapeDict.put(newName, shape);
    }


    public String updateScriptByShapeName(String mode, String name, String newName, String scripts) {
        String[] ss = scripts.split(";");
        String newScripts = "";
        String newS = "";
        if (mode == "delete") {
            // delete the shape id in the script
            for (String s: ss) {
                List<String> split = Arrays.asList(s.split(" "));
                // add to the scripts if the script does not contain the shape id
                if (!split.contains(name)) {
                    newScripts += s + ";";
                }
                else {
                    newS = "";
                    // if the script actions contain the shape id
                    if (split.get(1) == "drop") {
                        if (split.get(2) != name) {
                            newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
                            for (int i = 3; i < split.size(); i++) {
                                if (i % 2 == 0 && split.get(i) != name) {
                                    newS += split.get(i-1) + " " + split.get(i) + " ";
                                }
                            }
                        }
                    }
                    else {
                        newS = split.get(0) + " " + split.get(1) + " ";
                        for (int i = 2; i < split.size(); i++) {
                            if (i % 2 != 0 && split.get(i) != name) {
                                newS += split.get(i-1) + " " + split.get(i) + " ";
                            }
                        }
                    }
                    newScripts += newS + ";";
                }

            }
        }
        else {
            // rename the shape id in the script
            for(String s: ss) {
                List<String> split = Arrays.asList(s.split(" "));
                if (!split.contains(name)) {
                    newScripts += s + ";";
                }
                else {
                    newS = "";
                    if (split.get(1) == "drop") {
                        newS = split.get(0) + " " + split.get(1) + " ";
                        for (int i = 2; i < split.size(); i++) {
                            if (i % 2 == 0 && split.get(i) == name) {
                                newS += newName + " ";
                            }
                            else {
                                newS += split.get(i) + " ";
                            }
                        }
                    }
                    else {
                        newS = split.get(0) + " " + split.get(1) + " ";
                        for (int i = 2; i < split.size(); i++) {
                            if (i % 2 != 0 && split.get(i) == name) {
                                newS += newName + " ";
                            }
                            else {
                                newS += split.get(i) + " ";
                            }
                        }
                    }
                    newScripts += newS + ";";
                }

            }

        }
        return newScripts;
    }


    public void updateScriptRelatedToPage(Page page, String mode, String newName){
        HashSet<String> relatedShapes = page.relatedShapes;
        if (relatedShapes == null) {
            return;
        }
        for (String name: relatedShapes) {
            Shape relatedShape = mySingleton.getInstance().getDocStored().shapeDict.get(name);
            if (relatedShape != null) {
                String scripts = relatedShape.getScript();
                String new_scripts = updateScriptByPageName(mode, name, newName, scripts);
                mySingleton.getInstance().getDocStored().shapeDict.get(name).setScript(new_scripts);
            }
        }

    }

    public String updateScriptByPageName(String mode, String name, String newName, String scripts){
        String[] ss = scripts.split(";");
        String newScripts = "";
        if (mode == "delete") {
            // delete the page name in the script
            for (String s: ss) {
                List<String> split = Arrays.asList(s.split(" "));
                if (!split.contains(name)) {
                    newScripts += s + ";";
                }
                else {
                    String newS = "";
                    if (split.get(1) == "drop") {
                        newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
                        for (int i = 3; i < split.size(); i++) {
                            if (i % 2 == 0 && (split.get(i-1) != "goto" || split.get(i) != name)) {
                                newS += split.get(i-1) + " " + split.get(i) + " ";
                            }
                        }
                    }
                    else{
                        newS = split.get(0) + " " + split.get(1) + " ";
                        for (int i = 2; i < split.size(); i++) {
                            if (i % 2 != 0 && (split.get(i-1) != "goto" || split.get(i) != name)) {
                                newS += split.get(i-1) + " " + split.get(i) + " ";
                            }
                        }
                    }
                    newScripts += newS + ";";
                }

            }
        }
        else {
            // rename the page name in the script
            for (String s: ss) {
                List<String> split = Arrays.asList(s.split(" "));
                if (!split.contains(name)) {
                    newScripts += s + ";";
                }
                else {
                    String newS = "";
                    if (split.get(1) == "drop") {
                        newS = split.get(0) + " " + split.get(1) + " " + split.get(2) + " ";
                        for (int i = 3; i < split.size(); i++) {
                            if (i % 2 == 0 && split.get(i-1) == "goto" && split.get(i) == name) {
                                newS += newName + " ";
                            }
                            else {
                                newS += split.get(i) + " ";
                            }
                        }
                    }
                    else{
                        newS = split.get(0) + " " + split.get(1) + " ";
                        for (int i = 2; i < split.size(); i++) {
                            if (i % 2 != 0 && split.get(i-1) == "goto" && split.get(i) == name) {
                                newS += newName + " ";
                            }
                            else {
                                newS += split.get(i) + " ";
                            }
                        }
                    }
                    newScripts += newS + ";";
                }

            }
        }
        return newScripts;
    }


    public Page getCurPage() {
        return curPage;
    }

    public void setCurPage(Page curPage) {
        this.curPage = curPage;
    }

    public HashMap<String, Page> getPageDict() {
        return pageDict;
    }

    public void setPageDict(HashMap<String, Page> pageDict) {
        this.pageDict = pageDict;
    }

    public void updateScriptRelatedToShape(Shape shape, String newName){
        // update the script of the shapes related to the given shape
        HashSet<String> relatedShapes = shape.relatedShapes;
        if (relatedShapes == null) {
            return;
        }
        String name = shape.id;
        for (String relatedShapeName: relatedShapes) {
            Shape relatedShape = mySingleton.getInstance().getDocStored().shapeDict.get(relatedShapeName);
            relatedShape.relatedShapes.remove(name);
            relatedShape.relatedShapes.add(newName);
            if (relatedShape != null) {
                String scripts = relatedShape.getScript();
                String newScripts = updateScriptByShapeName("rename", name, newName, scripts);
                mySingleton.getInstance().getDocStored().shapeDict.get(name).setScript(newScripts);
            }

        }
        for (Page page  : pageDict.values()) {
            if (page.relatedShapes.contains(name)) {
                page.relatedShapes.remove(name);
                page.relatedShapes.add(newName);
            }
        }
    }



}