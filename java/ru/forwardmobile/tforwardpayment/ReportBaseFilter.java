package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import ru.forwardmobile.util.http.Dates;

/**
 * Created by Василий Ванин on 21.07.2014.
 */
public class ReportBaseFilter extends LinearLayout implements DatePickerDialog.OnDateSetListener, View.OnTouchListener, View.OnClickListener {


    protected TextView          dateFromView        = null;
    protected TextView          dateToView          = null;
    protected DatePickerDialog  picker              = null;
    protected Button            fastChoice          = null;

    private   TextView triggeredItem    = null;




    public ReportBaseFilter(Context ctx, ViewGroup container) {
        this(ctx, container, new Date());
    }

    public ReportBaseFilter(Context ctx, ViewGroup container, Date initDate) {
        super(ctx);

        setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        );

        setOrientation(LinearLayout.HORIZONTAL);

        dateFromView = new EditText(getContext());
        dateFromView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1f));
        addView(dateFromView);
        //dateFromView.setOnClickListener(this);
        dateFromView.setOnTouchListener(this);
        dateFromView.setText(Dates.Format(initDate, "dd.MM.yyyy"));

        dateToView   = new EditText(getContext());
        dateToView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1f));
        addView(dateToView);
        //dateToView.setOnClickListener(this);
        dateToView.setOnTouchListener(this);
        dateToView.setText(Dates.Format(initDate, "dd.MM.yyyy"));

        Calendar calendar = Calendar.getInstance();
        picker   = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        fastChoice  = new Button(getContext());
        fastChoice.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 2f));
        fastChoice.setOnClickListener(this);
        fastChoice.setText("=//=");
        addView(fastChoice);


        container.addView(this);
    }

    protected void setDateToView(TextView dateToView) {
        this.dateToView = dateToView;
        //this.dateToView.setOnClickListener(this);
    }

    protected void setDateFromView(TextView dateFromView) {
        this.dateFromView = dateFromView;
        //this.dateFromView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Dialog fastDialog = new FastChoiceDialog(getContext());
        fastDialog.show();


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        picker.hide();
        if(triggeredItem != null) {
            triggeredItem.setText(
                      String.valueOf(day) + "."
                    + String.valueOf(month) + "."
                    + String.valueOf(year)
            );
        }

        triggeredItem = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        triggeredItem = (TextView) view;
        picker.show();
        return false;
    }


    protected class FastChoiceDialog extends AlertDialog implements OnClickListener{

        Button month = null;

        protected FastChoiceDialog(Context context) {
            super(context);

            month = new Button(getContext());
            month.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1f));
            month.setText("Месяц");
            month.setOnClickListener(this);

            addContentView(month, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1f));
        }

        @Override
        public void show() {
            super.show();
        }



        @Override
        public void onPanelClosed(int featureId, Menu menu) {
            super.onPanelClosed(featureId, menu);
        }

        @Override
        public void onClick(View view) {
            if(month.equals(view)) {
                Calendar c = Calendar.getInstance();
                try {
                    Date from  = Dates.fromSqlDate(c.get(Calendar.YEAR)
                            + "-" + c.get(Calendar.MONTH)
                            + "-" + 1);

                    dateFromView.setText(Dates.Format(from,"dd.MM.yyyy"));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
