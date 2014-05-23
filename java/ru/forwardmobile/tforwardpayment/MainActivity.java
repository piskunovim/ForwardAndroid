package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import ru.forwardmobile.tforwardpayment.spp.impl.EnumFieldImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.ProviderImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.TextFieldImpl;
import ru.forwardmobile.tforwardpayment.spp.R;

public class MainActivity extends Activity
{
    

    public MainActivity(){
        
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.getResources().getDisplayMetrics();
        
        try {
            
            ProviderImpl megaphone = new ProviderImpl(this, 100, 10.00, 500.00);
            megaphone.addField( new TextFieldImpl(this, "phone", "Номер телефона") );
            megaphone.addField( new EnumFieldImpl(this, "region", "Код региона", "100=Столичный филиал ОАО &quot;МегаФон&quot;"
                   + "|200=Поволжский филиал ОАО &quot;МегаФон&quot;|500=Северо-Западный филиал ОАО &quot;МегаФон&quot;|700=Сибирский филиал ОАО &quot;МегаФон&quot;"
                   + "|800=Дальневосточный филиал ОАО &quot;МегаФон&quot;|900=Уральский филиал ОАО &quot;МегаФон&quot;|150=Кавказский филиал ОАО &quot;МегаФон&quot;"
                   + "|601=Центральный филиал") );
           
            setContentView(megaphone);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
