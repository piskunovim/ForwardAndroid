package ru.forwardmobile.tforwardpayment.reports;

import java.util.ArrayList;

import android.content.Context;
import ru.forwardmobile.tforwardpayment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




/**
 * Created by PiskunovI on 17.06.14.
 */
public class PaymentListAdapterImpl extends BaseAdapter {

    Context context;
    ArrayList<PaymentListItemsImpl> paymentList;

    public PaymentListAdapterImpl(Context context, ArrayList<PaymentListItemsImpl> list) {

        this.context = context;
        paymentList = list;
    }

    @Override
    public int getCount() {

        return paymentList.size();
    }

    @Override
    public Object getItem(int position) {

        return paymentList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        PaymentListItemsImpl paymentListItems = paymentList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.payment_list_row, null);

        }

        TextView payPsid = (TextView) convertView.findViewById(R.id.pay_psid);
        payPsid.setText(paymentListItems.getPsid());
        TextView payStatus = (TextView) convertView.findViewById(R.id.pay_status);
        payStatus.setText(paymentListItems.getStatus());
        TextView payDate = (TextView) convertView.findViewById(R.id.pay_date);
        payDate.setText(paymentListItems.getStartDate());
        TextView paySum = (TextView) convertView.findViewById(R.id.pay_sum);
        paySum.setText(paymentListItems.getValue());

        return convertView;
    }

}
