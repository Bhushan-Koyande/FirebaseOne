package com.example.firebaseone;

public class ListItem {
    private int mImageResource;
    private String mName;
    private String mPhone;
    private String mAddress;

    public ListItem(int mImageResource,String mName,String mPhone,String mAddress){
        this.mImageResource=mImageResource;
        this.mName=mName;
        if(mPhone==null){
            mPhone="No contact available";
        }
        this.mPhone=mPhone;
        this.mAddress=mAddress;

    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmName() {
        return mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmAddress(){
        return mAddress;
    }
}
