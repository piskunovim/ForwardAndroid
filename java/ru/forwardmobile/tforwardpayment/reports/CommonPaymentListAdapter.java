package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.util.http.Dates;

/**
 * Created by Василий Ванины on 28.07.2014.
 */
public class CommonPaymentListAdapter extends BaseAdapter {

    private List<PaymentInfo> info  = null;
    private Context           ctx   = null;

    public CommonPaymentListAdapter(List<PaymentInfo> info, Context ctx) {
        this.info = info;
        this.ctx  = ctx;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.payment_list_row, null);

            ViewHolder holder = new ViewHolder();
            holder.target = (TextView) view.findViewById(R.id.pay_fields);
            holder.ps     = (TextView) view.findViewById(R.id.pay_psid);
            holder.status = (TextView) view.findViewById(R.id.pay_status);
            holder.sum    = (TextView) view.findViewById(R.id.pay_sum);
            holder.date   = (TextView) view.findViewById(R.id.pay_date_begin);

            view.setTag( holder );
        }

        ViewHolder  holder = (ViewHolder) view.getTag();
        PaymentInfo item   = (PaymentInfo) getItem(i);

        holder.target.setText( item.getTarget().getValue() );
        holder.ps.setText(item.getPsName());
        holder.status.setText(item.getStatusName());
        holder.sum.setText(item.getValue() + " р./ " + item.getFullValue() + " р.");
        holder.date.setText(Dates.Format(item.getStartDate(),"dd.MM.yyyy HH:mm:ss"));

        return view;
    }


    class ViewHolder {
        TextView target = null;
        TextView ps     = null;
        TextView status = null;
        TextView sum    = null;
        TextView date   = null;
    }
}
