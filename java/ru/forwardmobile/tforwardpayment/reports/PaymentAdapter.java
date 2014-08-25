package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.R;


/**
 * Created by PiskunovI on 17.06.14.
 * @deprecated
 */
public class PaymentAdapter extends BaseAdapter {

    Context context;
    ArrayList<PaymentListItem> paymentList;

    public PaymentAdapter(Context context, ArrayList<PaymentListItem> list) {

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
        PaymentListItem paymentListItems = paymentList.get(position);

        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.payment_list_row, null);

        }

        TextView payPsid = (TextView) convertView.findViewById(R.id.pay_psid);
        payPsid.setText(paymentListItems.getPsid());
        TextView payFields = (TextView) convertView.findViewById(R.id.pay_fields);
        payFields.setText("  " + paymentListItems.getFields());
        TextView payStatus = (TextView) convertView.findViewById(R.id.pay_status);
        payStatus.setText(paymentListItems.getStatus());
        TextView payDate = (TextView) convertView.findViewById(R.id.pay_date_begin);
        payDate.setText(paymentListItems.getStartDate());
        TextView payDateEnd = (TextView) convertView.findViewById(R.id.pay_date_end);
        payDateEnd.setText(paymentListItems.getProcessDate());
        TextView paySum = (TextView) convertView.findViewById(R.id.pay_sum);
        paySum.setText(paymentListItems.getValue() + " / " + paymentListItems.getFullValue() );
        TextView payErrorCode = (TextView) convertView.findViewById(R.id.pay_error_code);
        payErrorCode.setText(paymentListItems.getErrorCode());
        TextView payErrorDesc = (TextView) convertView.findViewById(R.id.pay_error_description);
        payErrorDesc.setText(paymentListItems.getErrorDescription());

        return convertView;
    }

}
