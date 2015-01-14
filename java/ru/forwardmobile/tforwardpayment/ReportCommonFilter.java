package ru.forwardmobile.tforwardpayment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.forwardmobile.util.http.Dates;


/**
 * Created by PiskunovI on 08.08.2014.
 */
public class ReportCommonFilter extends LinearLayout implements DatePickerDialog.OnDateSetListener, View.OnTouchListener, View.OnClickListener {

    protected TextView          today        = null;
    protected TextView          dateFromView        = null;
    protected TextView          dateToView          = null;
    protected DatePickerDialog  picker              = null;
    protected Button fastChoice          = null;

    private   TextView triggeredItem    = null;




    public ReportCommonFilter(Context ctx, ViewGroup container, Integer state) {
        this(ctx, container, new Date(), state);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ReportCommonFilter(Context ctx, ViewGroup container, Date initDate, Integer state) {
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
        dateToView.setVisibility(GONE);

        Calendar calendar = Calendar.getInstance();
        picker   = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

//------------------------
        if (state == 1){
           // Date beginDate, endDate;
           // Calendar c;
            picker.setTitle("Выберите год:");
            try {
                java.lang.reflect.Field[] datePickerDialogFields = picker.getClass().getDeclaredFields();
                for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                    if (datePickerDialogField.getName().equals("mDatePicker")) {
                        datePickerDialogField.setAccessible(true);
                        DatePicker datePicker = (DatePicker) datePickerDialogField.get(picker);
                        datePicker.setCalendarViewShown(false);
                        datePicker.setMaxDate(new Date().getTime());

                        java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                        for (java.lang.reflect.Field datePickerField : datePickerFields) {
                            if ("mDaySpinner".equals(datePickerField.getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                            if ("mMonthSpinner".equals(datePickerField.getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }

                            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

                                @Override
                                public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
                                    Calendar c;
                                    Date beginDate, endDate;

                                    int day = checkDayOfYear(arg0.getYear());
                                    c = Calendar.getInstance();
                                    c.set(Calendar.YEAR, arg0.getYear());

                                    c.set(Calendar.DAY_OF_MONTH,day);
                                    c.get(Calendar.MONTH);
                                    if (day == 31){
                                        c.set(Calendar.DAY_OF_MONTH,31);
                                        c.set(Calendar.MONTH, 12);
                                        endDate = c.getTime(); //назначаем период нашей даты
                                    }
                                    endDate = c.getTime();

                                    c.set(Calendar.MONTH, 0);
                                    c.set(Calendar.DAY_OF_MONTH,1);
                                    beginDate = c.getTime();

                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    Log.d("ReportCommonFilter beginDate:", df.format(beginDate));
                                    Log.d("ReportCommonFilter endDate:", df.format(endDate));
                                }
                            });
                            /*int day = checkDayOfYear(datePicker.getYear());
                            c = Calendar.getInstance();
                            c.set(Calendar.YEAR, datePicker.getYear());

                            c.set(Calendar.DAY_OF_MONTH,day);
                            c.get(Calendar.MONTH);
                            if (day == 31){
                                c.set(Calendar.DAY_OF_MONTH,31);
                                c.set(Calendar.MONTH, 12);
                                endDate = c.getTime(); //назначаем период нашей даты
                            }
                            endDate = c.getTime();

                            c.set(Calendar.MONTH, 0);
                            c.set(Calendar.DAY_OF_MONTH,1);
                            beginDate = c.getTime();

                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Log.d("ReportCommonFilter beginDate:", df.format(beginDate));
                            Log.d("ReportCommonFilter endDate:", df.format(endDate));*/
                        }
                    }

                }
            } catch (Exception ex) {
            }
        }
        else if (state == 2) {
            picker.setTitle("Выберите месяц:");
           try {
               java.lang.reflect.Field[] datePickerDialogFields = picker.getClass().getDeclaredFields();
               for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                   if (datePickerDialogField.getName().equals("mDatePicker")) {
                       datePickerDialogField.setAccessible(true);
                       DatePicker datePicker = (DatePicker) datePickerDialogField.get(picker);
                       datePicker.setCalendarViewShown(false);
                       datePicker.setMaxDate(new Date().getTime());

                       java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                       for (java.lang.reflect.Field datePickerField : datePickerFields) {
                           if ("mDaySpinner".equals(datePickerField.getName())) {
                               datePickerField.setAccessible(true);
                               Object dayPicker = new Object();
                               dayPicker = datePickerField.get(datePicker);
                               ((View) dayPicker).setVisibility(View.GONE);
                           }

                       }
                   }

               }
           } catch (Exception ex) {
           }
       }
        else if (state == 3)
        {
            picker.setTitle("Выберите день");
            try {
                java.lang.reflect.Field[] datePickerDialogFields = picker.getClass().getDeclaredFields();
                for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(picker);
                    datePicker.setSpinnersShown(false);
                    datePicker.setMaxDate(new Date().getTime());
                }
            }catch (Exception ex){

            }
        }
        else if (state == 4)
        {
            today = new TextView(getContext());
            today.setText(Dates.Format(initDate, "dd.MM.yyyy"));
        }


//--------------------------


        //fastChoice  = new Button(getContext());
        //fastChoice.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 2f));
        //fastChoice.setOnClickListener(this);
        // fastChoice.setText("=//=");
        //addView(fastChoice);


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

        //Dialog fastDialog = new FastChoiceDialog(getContext());
        //fastDialog.show();


    }

    public int checkDayOfMonth(int month, int day){


        return day;
    }

    public int checkDayOfYear(int year){
        Calendar c;
        c = Calendar.getInstance();
        if ( year == c.get(Calendar.YEAR) )   // если год является текущим, то возвращаем текущий день месяца
        {
            Log.d("checkDayOfYear:", "equal");
            return c.get(Calendar.DAY_OF_MONTH);
        }
        return 31; //если год не является текущим, значит год уже завершился

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
}

