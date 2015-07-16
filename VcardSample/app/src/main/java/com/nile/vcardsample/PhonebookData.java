package com.nile.vcardsample;

import java.util.ArrayList;

/**
 * Created by Nilesh on 24/06/15.
 */
public class PhonebookData {

    String lookup,contact_id,displayName;
    ArrayList<Data> arrData = new ArrayList<>();

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<Data> getArrData() {
        return arrData;
    }

    public void setArrData(ArrayList<Data> arrData) {
        this.arrData = arrData;
    }
}
