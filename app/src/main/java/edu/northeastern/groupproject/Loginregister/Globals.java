package edu.northeastern.groupproject.Loginregister;

import android.app.Application;

public class Globals extends Application {
    private int data = 0;
    public int getData(){
        return this.data;
    }

    public void setData(int d){
        this.data = d;
    }


}
