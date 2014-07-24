package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ru.forwardmobile.tforwardpayment.R;

public class PaymentListAdapter extends BaseAdapter {

    private final List<PaymentInfo> items;
    private final Context ctx;

    public PaymentListAdapter(List<PaymentInfo> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.payment_list_row, viewGroup);
        }


        return null;
    }
}
