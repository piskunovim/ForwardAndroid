package ru.forwardmobile.tforwardpayment.settings;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by PiskunovI on 01.10.2014.
 */
public class GroupSettingsItems extends LinearLayout {

    public GroupSettingsItems(Context context) {
        super(context);
    }

    public void addItem(SettingsItems settings, ViewGroup viewGroup){
        if (settings.Title != null)
            viewGroup.addView(settings.Title);
        if (settings.Value != null)
            viewGroup.addView(settings.Value);
        if (settings.Button != null)
            viewGroup.addView(settings.Button);
    }
}
