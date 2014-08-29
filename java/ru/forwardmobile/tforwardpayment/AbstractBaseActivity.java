package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.reports.BalanceActivity;
import ru.forwardmobile.tforwardpayment.reports.SimplePaymentListActivity;

/**
 * Created by vaninv on 25.07.2014.
 */
public abstract class AbstractBaseActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operators, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(AbstractBaseActivity.this, BalanceActivity.class);
            AbstractBaseActivity.this.startActivity(intent);
            return true;
        }
        if (id == R.id.action_report) {
            CharSequence reports[] = new CharSequence[] {"Запрос остатка средств", "Текущие платежи", "Принятые платежи"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Выберите отчет:");
            builder.setItems(reports, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on reports[which]
                    Intent intent;
                    switch (which){
                        case 0:
                            intent = new Intent(AbstractBaseActivity.this, BalanceActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(AbstractBaseActivity.this, SimplePaymentListActivity.class);
                            intent.putExtra(SimplePaymentListActivity.LIST_TYPE, SimplePaymentListActivity.UNPROCESSED_PAYMENT_LIST_TYPE);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(AbstractBaseActivity.this, SimplePaymentListActivity.class);
                            intent.putExtra(SimplePaymentListActivity.LIST_TYPE, SimplePaymentListActivity.ALL_PAYMENT_LIST_TYPE);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });
            builder.show();
            return true;
        }
        if (id == R.id.settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "meVe0se2.ttf");

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
