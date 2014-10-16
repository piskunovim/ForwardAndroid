package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.spp.ProvidersDataSourceFactory;
import ru.forwardmobile.util.http.Dates;

/**
 * Created by Василий Ванин on 26.09.2014.
 */
public class PaymentReportAdapter extends ArrayAdapter<IPayment> {


    public PaymentReportAdapter(Collection<IPayment> payments, Context ctx) {
        super(ctx, 0);
        for(IPayment p: payments) {
            add(p);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null) {
            convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.payment_report_row, null);

            ViewHolder holder = new ViewHolder();
            holder.statusTitle = (TextView) convertView.findViewById(R.id.payment_row_status);
            holder.startDateTitle = (TextView) convertView.findViewById(R.id.payment_row_start);
            holder.endDateTitle = (TextView) convertView.findViewById(R.id.payment_row_end);
            holder.phoneNumberTitle = (TextView) convertView.findViewById(R.id.payment_row_fields);
            holder.psTitle = (TextView) convertView.findViewById(R.id.payment_row_ps);
            holder.amountValue = (TextView) convertView.findViewById(R.id.payment_row_amount);

            convertView.setTag(holder);
        }

        IPayment   payment = getItem(position);
        ViewHolder holder  = (ViewHolder) convertView.getTag();

        holder.statusTitle.setText(payment.getStatusName());
        holder.startDateTitle.setText(Dates.Format(payment.getStartDate(), "dd/MM/yyyy HH:mm"));
        holder.endDateTitle.setText(Dates.Format(payment.getDateOfProcess(),"dd/MM/yyyy HH:mm"));
        holder.phoneNumberTitle.setText(payment.getTarget().getValue().getDisplayValue());
        holder.psTitle.setText(payment.getPsTitle());
        holder.amountValue.setText(payment.getValue() + "/" + payment.getFullValue());

        return convertView;
    }

    protected class ViewHolder {
            protected TextView statusTitle;
            protected TextView startDateTitle;
            protected TextView endDateTitle;
            protected TextView phoneNumberTitle;
            protected TextView psTitle;
            protected TextView amountValue;
    }

}
