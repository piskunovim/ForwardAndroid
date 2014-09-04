package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.operators.OperatorsReader;
import ru.forwardmobile.tforwardpayment.operators.OperatorsXmlReader;
import ru.forwardmobile.tforwardpayment.operators.impl.OperatorsEntityManagerImpl;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.BaseFieldView;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by vaninv on 30.05.2014.
 */
public class TestActivity extends Activity {

    final static String LOGGER_TAG = "TFORWARD.PAYMENT";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        //IPayment payment = PaymentFactory.getPayment(1, 5.00, 5.00, Arrays.asList(BaseFieldView.fieldInfo("target","9182074447","Телефон")));
        //IPaymentDao dao = new PaymentDaoImpl(new DatabaseHelper(this));

        //dao.save(payment);
        try {

            InputStream is = getAssets().open("operators.xml");
            OperatorsEntityManagerImpl em
                    = new OperatorsXmlReader().readOperators(is);

            List<IProviderMenuItem> items = em.getMenuItems(105);
            Log.i(LOGGER_TAG, "Sizeof 105: " + items.size());
            for(IProviderMenuItem item: items) {
                Log.i(LOGGER_TAG, item.toString());
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
