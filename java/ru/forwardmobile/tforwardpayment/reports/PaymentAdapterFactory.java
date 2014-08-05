package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.widget.ListAdapter;

import java.util.List;

/**
 * DI-container for payment list adapters
 * Created by Vasiliy Vanin on 28.07.2014.
 */
public class PaymentAdapterFactory {
    public static ListAdapter getAdapter(List<PaymentInfo> items, Context ctx) {
        return new CommonPaymentListAdapter(items, ctx);
    }
}
