package ru.forwardmobile.tforwardpayment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;

/**
 * Created by PiskunovI on 20.05.14.
 */
public class TLoginForm extends Fragment {

    //EditText etName,etPass;
    final String LOG_TAG = "TFORWARD.TLoginForm";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment1, container, false);

        Button btnSingIn = (Button) rootView.findViewById(R.id.singin);
        final EditText etName = (EditText) rootView.findViewById(R.id.epid);
        final EditText etPass = (EditText) rootView.findViewById(R.id.epass);
        btnSingIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Log.d(LOG_TAG, "etName" + etName.getText() + "etPass" + etPass.getText());
                ((MainActivity)getActivity()).SingIn(etName.getText().toString(), etPass.getText().toString());
            }
        });

        return rootView;
    }
}







