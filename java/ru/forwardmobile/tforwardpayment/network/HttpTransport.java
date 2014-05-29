package ru.forwardmobile.tforwardpayment.network;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import ru.forwardmobile.tforwardpayment.security.ICryptEngine;
import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.util.http.IRequest;
import ru.forwardmobile.util.http.IResponse;
import ru.forwardmobile.util.http.ITransport;
import ru.forwardmobile.util.http.TransportFactory;


/**
 * @author Vasiliy Vanin
 */
public class HttpTransport 
{
    private       ICryptEngine    cryptEngine = null;

    public void   setCryptEngine(ICryptEngine engine) {
        this.cryptEngine = engine;
    }
    
    public byte[] send(IRequest request) throws Exception {

        StringBuilder requestBody = new StringBuilder(new String(request.getData()));
        
        // Если для запроса необходима цифровая подпись,
        // запрос подписывается
        if( cryptEngine != null ) {
            addSignature(requestBody);
        } 
 
        request.setData(requestBody.toString().getBytes());
        //  Отправка запроса
        request.setHost(TSettings.get(TSettings.SERVER_HOST));
        request.setPort(TSettings.getInt(TSettings.SERVER_PORT));
        request.setPath("/?v=" + TSettings.getVersion());

        Log.i("TFORWARD.HttpTransport", request.toString());
        
        ITransport transport = TransportFactory.getTransport();
        IResponse  response  = transport.send(request);        
        
        Log.i("TFORWARD.HttpTransport", response.toString());
        
        //  Ответ от сервера так же может быть разбит на две части
        InputStream is = new ByteArrayInputStream( response.getData() );
        ByteArrayOutputStream answer    = new ByteArrayOutputStream();
        ByteArrayOutputStream signature = new ByteArrayOutputStream();
        
        boolean signatureStarted = false;

        int i = is.read();
        while(i != -1) {
            switch( (char) i) {
                case '\n' : if( cryptEngine != null )   signatureStarted = true;
                            else                        answer.write(i);
                            break;
                case '\r' : break;
                default: if(signatureStarted)   signature.write(i);
                            else                answer.write(i);
            };
            
            i = is.read();
        }        
        
        // И проверка подписи, если она необходима
        if(  cryptEngine != null  ) {
            cryptEngine.verifySignature(answer.toByteArray(), signature.toByteArray());
        } else {
        }
        
        return answer.toByteArray();
    }
    
       
    
    private void addSignature(StringBuilder requestBody) throws Exception {
        // По протоколу добляется перевод строки, 
        requestBody.append(TSettings.CRLF)

        // за ним следует идентификатор сертификата.
        .append( TSettings.get(TSettings.CERTIFICATE_ACESS_ID) );

        // Эти данные необходимо подписать
        byte[] signData = requestBody.toString().getBytes();

        // Теперь еще один перевод строки
        requestBody.append(TSettings.CRLF);

        // и подпись
        requestBody.append(cryptEngine.generateSignature(signData));        
    }
}
