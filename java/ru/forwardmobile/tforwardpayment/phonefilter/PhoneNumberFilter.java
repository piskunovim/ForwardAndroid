package ru.forwardmobile.tforwardpayment.phonefilter;

import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;

/**
 * Created by PiskunovI on 12.01.2015.
 */
public class PhoneNumberFilter extends NumberKeyListener{

    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_PHONE;
    }

    @Override
    protected char[] getAcceptedChars() {
        return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-' };
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        // Don't let phone numbers start with 1
        if (dstart == 0 && source.equals("1"))
            return "";

        if (end > start) {
            String destTxt = dest.toString();
            String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);

            // Phone number must match xxx-xxx-xxxx
            if (!resultingTxt.matches ("^\\d{1,1}(\\d{1,1}(\\d{1,1}(\\-(\\d{1,1}(\\d{1,1}(\\d{1,1}(\\-(\\d{1,1}(\\d{1,1}(\\d{1,1}(\\d{1,1}?)?)?)?)?)?)?)?)?)?)?)?")) {
                return "";
            }
        }
        return null;
    }
}
