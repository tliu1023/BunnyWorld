package cs108.stanford.edu.bunnyworldeditor;

import java.util.ArrayList;
import java.util.HashMap;

public class mySingleton {
    private static final mySingleton ourInstance = new mySingleton();

//    public PageView currentPageView;

    public HashMap<String, Shape> shapeDict;
    public HashMap<String, Page> pageDict;
    public ArrayList<Shape> possessionList;
    public Page curPage;
    public boolean isEditMode;
    public Shape copiedShape;
    public int copyNum;
    public boolean saved;
    public Docs docStored;

    static mySingleton getInstance() {
        return ourInstance;
    }

    private mySingleton() {
        shapeDict = new HashMap<String, Shape>();
        possessionList = new ArrayList<Shape>();
        pageDict = new HashMap<String, Page>();
        Page page1 = new Page("page1");
        pageDict.put("page1", page1);
//        currentPage = page1;
        saved=true;
        docStored = new Docs();
    }

    public HashMap<String, Shape> getShapeDict() {
        return shapeDict;
    }

    public void setShapeDict(HashMap<String, Shape> shapeDict) {
        this.shapeDict = shapeDict;
    }

    public HashMap<String, Page> getPageDict() {
        return pageDict;
    }

    public void setPageDict(HashMap<String, Page> pageDict) {
        this.pageDict = pageDict;
    }

    public ArrayList<Shape> getPossessionList() {
        return possessionList;
    }

    public void setPossessionList(ArrayList<Shape> possessionList) {
        this.possessionList = possessionList;
    }

    public Page getCurPage() {
        return curPage;
    }

    public void setCurPage(Page curPage) {
        this.curPage = curPage;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public Shape getCopiedShape() {
        return copiedShape;
    }

    public void setCopiedShape(Shape copiedShape) {
        this.copiedShape = copiedShape;
    }

    public int getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(int copyNum) {
        this.copyNum = copyNum;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Docs getDocStored() {
        return docStored;
    }

    public void setDocStored(Docs docStored) {
        this.docStored = docStored;
    }
}