package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.Collection;

import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * Created by Василий Ванин on 26.09.2014.
 */
public class PaymentReportAdapter extends ArrayAdapter<IPayment> {

    public PaymentReportAdapter(Collection<IPayment> payments, Context ctx) {
        super(ctx, 0);
    }

}
