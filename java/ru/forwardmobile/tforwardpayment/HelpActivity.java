package ru.forwardmobile.tforwardpayment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by PiskunovI on 06.02.2015.
 */
public class HelpActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        applyFonts(findViewById(R.id.activity_main_container), null);

        TextView textView = (TextView) findViewById(R.id.help_text);

        textView.setText("Перед тем, как начать работу с приложением, необходимо убедиться, что вы обладаете номером точки и паролем доступа к ней. Для получения точки доступа необходимо обратиться в компанию Форвард Мобайл с соответствующим запросом.  \n" +
                "\n" +
                "Для того, чтобы перейти к главному экрану приложения вам необходимо пройти два этапа авторизации: «Экран входа» и «Экран пользовательского пароля».\n" +
                "\n" +
                "Экран входа включает в себя два поля ввода, в которые требуется ввести ваш номер точки и пароль соответственно. Заполните эти поля согласно закрепленной за вами паре номер_точки-пароль. В дальнейшем, при запуске приложения данная информация запрашиваться не будет. ");
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "Magistral.TTF");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }
}
