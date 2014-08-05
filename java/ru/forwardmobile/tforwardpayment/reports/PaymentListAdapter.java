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

public class PaymentListAdapter extends BaseAdapter {

    private final List<PaymentInfo> items;
    private final Context ctx;

    public PaymentListAdapter(List<PaymentInfo> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view == null) {
            view = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.simple_list_row, null);

            holder = new ViewHolder();

            holder.position = (TextView) view.findViewById(R.id.simple_row_number);
            holder.target   = (TextView) view.findViewById(R.id.simple_row_target);
            holder.value    = (TextView) view.findViewById(R.id.simple_row_value);
            holder.startDate= (TextView) view.findViewById(R.id.simple_row_start_date);
            holder.status   = (TextView) view.findViewById(R.id.simple_row_status);
            holder.error    = (TextView) view.findViewById(R.id.simple_row_error);

            view.setTag(holder);
        }

        PaymentInfo item = (PaymentInfo) getItem(i);
        holder = (ViewHolder) view.getTag();

        holder.position.setText(String.valueOf(i+1));
        holder.target.setText(item.getTarget().getValue());

        holder.value.setText(item.getValue() + "р. / " + item.getFullValue() + "р.");
        holder.startDate.setText(Dates.Format(item.getStartDate(), "dd.MM.yyyy HH:mm:ss"));
        holder.status.setText(item.getStatusName());
        holder.error.setText(item.getErrorDescription());

        return view;
   }

   private class ViewHolder {
       public TextView position;
       public TextView target;
       public TextView value;
       public TextView startDate;
       public TextView status;
       public TextView error;
   }

}
