package ru.forwardmobile.util;

import android.app.ProgressDialog;

/**
 * Created by PiskunovI on 08.01.2015.
 */
public class DialogSignleton {
    static private ProgressDialog dialog;
    static private boolean isTaskRunning = false;

    static public ProgressDialog getDialog(){
        return dialog;
    }

    static  public  void setDialog(ProgressDialog _dialog){
        dialog = _dialog;
    }

    static  public boolean getTaskRunning(){
        return isTaskRunning;
    }

    static public void setTaskRunning(boolean _isTaskRunning){
        isTaskRunning = _isTaskRunning;
    }
}
