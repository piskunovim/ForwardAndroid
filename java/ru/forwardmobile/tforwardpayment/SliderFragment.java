package ru.forwardmobile.tforwardpayment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.support.v4.app.Fragment;


public class SliderFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor;

    static SliderFragment newInstance(int page) {
        SliderFragment pageFragment = new SliderFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_slider_fragment, null);

        TextView fDate = (TextView) view.findViewById(R.id.fDate);
        TextView fDescription = (TextView) view.findViewById(R.id.fDescription);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        Calendar c = Calendar.getInstance();
        date = c.getTime();

        fDate.setText(dateFormat.format(date));
        fDate.setTextColor(Color.parseColor("#000000"));
        fDate.setTextSize(11);
        //fDate.setBackgroundColor(backColor);

        fDescription.setText("Уважаемые Агенты! Предлагаем приобрести карту расширения SmartStickCard (SSC) для купюроприемников CashCode SM. Купюроприемник с установленной картой больше не требует регулярного обновления прошивок. Последние, актуальные версии прошивок загружаются удаленно на терминал системой «Форвард Мобайл» и записываются в купюроприемник автоматически. Данная карта, обладая большим объемом памяти (в 10 раз больше) и высокой производительностью, значительно повышает уровень защиты от фальшивых купюр и «рыбалки». Карточка должна постоянно находиться в купюроприемнике. Стоимость карты: до 100 шт. - 850 руб./1 шт., от 101 шт. - 750 руб./шт. Уточнить подробности, а также сделать предварительный заказ, Вы можете по тел. (918) 641-09-04" + pageNumber);
        fDescription.setTextColor(Color.parseColor("#000000"));
        fDescription.setTextSize(10);
        //fDescription.setBackgroundColor(backColor);

        /*
        RelativeLayout sliderPages = (RelativeLayout) view.findViewById(R.id.sliderPages);
        sliderPages.setBackgroundColor(backColor);

        if (pageNumber == 0){
            view = ClearDots(view);
            LinearLayout firstDot = (LinearLayout) view.findViewById(R.id.firstDot);
            firstDot.setBackgroundColor(Color.parseColor("#3498db"));
        }
        else if(pageNumber == 1)
        {
            view = ClearDots(view);
            LinearLayout secondDot = (LinearLayout) view.findViewById(R.id.secondDot);
            secondDot.setBackgroundColor(Color.parseColor("#3498db"));
        }
        else{
            view = ClearDots(view);
            LinearLayout thirdDot = (LinearLayout) view.findViewById(R.id.thirdDot);
            thirdDot.setBackgroundColor(Color.parseColor("#3498db"));
        }
*/
        return view;
    }

    public View ClearDots(View view)
    {
        LinearLayout f,s,t;

        f = (LinearLayout) view.findViewById(R.id.firstDot);
        f.setBackgroundColor(Color.parseColor("#cccccc"));

        s = (LinearLayout) view.findViewById(R.id.secondDot);
        s.setBackgroundColor(Color.parseColor("#cccccc"));

        t = (LinearLayout) view.findViewById(R.id.thirdDot);
        t.setBackgroundColor(Color.parseColor("#cccccc"));

        return view;
    }
}