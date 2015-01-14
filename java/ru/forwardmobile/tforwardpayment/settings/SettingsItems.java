package ru.forwardmobile.tforwardpayment.settings;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.phonefilter.PhoneNumberFilter;
import ru.forwardmobile.tforwardpayment.phonefilter.PhoneNumberTextWatcher;

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

        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,0,10);

        Button.setLayoutParams(params);
        Button.setBackgroundResource(R.drawable.gardient_login_form_button);
        Button.setTextColor(Color.parseColor("#FFFFFF"));
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

    public void createEditPhone(Context context){
        Value = new EditText(context);
        Value.addTextChangedListener(new PhoneNumberTextWatcher());
        Value.setFilters(new InputFilter[]{new PhoneNumberFilter(), new InputFilter.LengthFilter(12)});
        //Value.setText(text);
        Value.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void createTextView(Context context, String text){
        Title = new TextView(context);
        Title.setText(text);
        Title.setTextSize(20);
        Title.setPadding(0, 40, 0, 0);
        Title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


}
