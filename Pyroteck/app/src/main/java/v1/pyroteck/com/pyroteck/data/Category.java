package v1.pyroteck.com.pyroteck.data;

import java.util.ArrayList;

/**
 * Created by Nilesh on 01/05/15.
 */
public class Category {
    String title;
    SubCategory subCategory;

    ArrayList<SubCategory> arrSubcategory = new ArrayList<SubCategory>();

    public ArrayList<SubCategory> getArrSubcategory() {
        return arrSubcategory;
    }

    public void setArrSubcategory(ArrayList<SubCategory> arrSubcategory) {
        this.arrSubcategory = arrSubcategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
