package cs108.stanford.edu.bunnyworldeditor;

import java.util.HashMap;

public class mySingleton {
    private static final mySingleton ourInstance = new mySingleton();

//    public PageView currentPageView;

    public HashMap<String, Shape> shapeDict;
    // public HashMap<String, Page> pageDict;
    //public ArrayList<Shape> possessionList;
    // public boolean isEditMode;
    // public Shape copiedShape;
    // public int copyNum;
    // public boolean saved;
    public Docs docStored;

    static mySingleton getInstance() {
        return ourInstance;
    }

    private mySingleton() {
        // shapeDict = new HashMap<String, Shape>();
        // possessionList = new ArrayList<Shape>();
        // pageDict = new HashMap<String, Page>();
        // Page page1 = new Page("page1");
        // pageDict.put("page1", page1);
        // currentPage = page1;
        // saved=true;
        docStored = new Docs();
    }

    public HashMap<String, Shape> getShapeDict() {
        return shapeDict;
    }
    public Docs getDocStored() {
        return docStored;
    }

    public void setDocStored(Docs docStored) {
        this.docStored = docStored;
    }
}