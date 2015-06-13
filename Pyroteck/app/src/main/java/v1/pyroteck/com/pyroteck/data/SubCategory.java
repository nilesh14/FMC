package v1.pyroteck.com.pyroteck.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nilesh on 01/05/15.
 */
public class SubCategory implements Serializable{
    public String title;
    public String material;
    public String description;
    int imageID;
    public String availableForms;
    public String availableColors;
    ArrayList<SizeData> arrSizeData = new ArrayList<SizeData>();
    boolean showTable,checkCustomeSize = false;

    public boolean isCheckCustomeSize() {
        return checkCustomeSize;
    }

    public void setCheckCustomeSize(boolean checkCustomeSize) {
        this.checkCustomeSize = checkCustomeSize;
    }

    public String getAvailableForms() {
        return availableForms;
    }

    public void setAvailableForms(String availableForms) {
        this.availableForms = availableForms;
    }

    public String getAvailableColors() {
        return availableColors;
    }

    public void setAvailableColors(String availableColors) {
        this.availableColors = availableColors;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public void setShowTable(boolean showTable) {
        this.showTable = showTable;
    }

    public ArrayList<SizeData> getArrSizeData() {
        return arrSizeData;
    }

    public void setArrSizeData(ArrayList<SizeData> arrSizeData) {
        this.arrSizeData = arrSizeData;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
