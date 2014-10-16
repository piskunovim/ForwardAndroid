package ru.forwardmobile.tforwardpayment.settings;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * Created by PiskunovI on 01.10.2014.
 */
public class SettingsItems extends LinearLayout{

    TextView Title;
    EditText Value;
    Button Button;

    private String setEditText;
    private String setViewText;

    public SettingsItems(Context context) {
        super(context);

    }

    public void createButton(Context context, String buttonText){
        Button = new Button(context);
        Button.setText(buttonText);
        Button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public Button getButton(){
        return Button;
    }

    public EditText getEditText(){
        return Value;
    }

    public void createEditSettings(Context context, String viewText, String editText){
        setViewText = viewText;
        setEditText = editText;
        createTextView(context,viewText);
        createEditText(context,editText);
    }

    public String getSetEditText(){
        return setEditText;
    }

    public void createEditText(Context context, String text){
        Value = new EditText(context);
        Value.setText(text);
        Value.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void createTextView(Context context, String text){
        Title = new TextView(context);
        Title.setText(text);
        Title.setTextSize(20);
        Title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


}
