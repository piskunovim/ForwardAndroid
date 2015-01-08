package ru.forwardmobile.tforwardpayment.network;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import ru.forwardmobile.tforwardpayment.Settings;
import ru.forwardmobile.tforwardpayment.security.ICryptEngine;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.impl.ResponseSetImpl;
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
    
    public byte[] send(IRequest request, Context context) throws Exception {

        StringBuilder requestBody = new StringBuilder(new String(request.getData()));
        
        // Если для запроса необходима цифровая подпись,
        // запрос подписывается
        if( cryptEngine != null ) {
            addSignature(requestBody, context);
        } 
 
        request.setData(requestBody.toString().getBytes());
        //  Отправка запроса
        request.setHost(Settings.get(context, Settings.SERVER_HOST));
        request.setPort(Settings.getInt(context, Settings.SERVER_PORT));
        request.setPath("/?v=" + Settings.getVersion());

        Log.i("TFORWARD.HttpTransport", request.toString());
        
        ITransport transport = TransportFactory.getTransport();
        IResponse  response  = transport.send(request);        
        
        Log.i("TFORWARD.HttpTransport", response.toString());
        
        //  Ответ от сервера так же может быть разбит на две части
        InputStream is =  new ByteArrayInputStream(response.getData());
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
        }

        return answer.toByteArray();
    }
    
       
    
    private void addSignature(StringBuilder requestBody, Context context) throws Exception {
        // По протоколу добляется перевод строки, 
        requestBody.append(Settings.CRLF)

        // за ним следует идентификатор сертификата.
        .append( Settings.get(context, Settings.CERTIFICATE_ACESS_ID) );

        // Эти данные необходимо подписать
        byte[] signData = requestBody.toString().getBytes();

        // Теперь еще один перевод строки
        requestBody.append(Settings.CRLF);

        // и подпись
        requestBody.append(cryptEngine.generateSignature(signData));        
    }


    public IResponseSet send(ICommandRequest command, Context context) throws Exception  {

        // Generating request text
        StringBuilder body = new StringBuilder();
        if( command . getBody() != null ) {
            body.append(command.getBody());
        } else {

            Iterator<String> iterator = command.getLines().iterator();
            while (iterator.hasNext()) {
                body.append(iterator.next());
                if(iterator.hasNext()) {
                    body.append(Settings.CRLF);
                }
            }
        }

        // Generating signature
        addSignature( body, context );

        // Set status to commands
        command.setSended();

        IRequest request = ServerRequestFactory.getRequest("");
        request.setData( body.toString().getBytes() );

        request.setHost(Settings.get(context, Settings.SERVER_HOST));
        request.setPort(Settings.getInt(context, Settings.SERVER_PORT));
        request.setPath("/?v=" + Settings.getVersion());

        Log.i("TFORWARD.HttpTransport", request.toString());

        ITransport transport = TransportFactory.getTransport();
        IResponse  response  = transport.send(request);

        Log.i("TFORWARD.HttpTransport", response.toString());

        //  Ответ от сервера так же может быть разбит на две части
        InputStream is =  new ByteArrayInputStream(response.getData());
        ByteArrayOutputStream answer    = new ByteArrayOutputStream();
        ByteArrayOutputStream signature = new ByteArrayOutputStream();

        boolean signatureStarted = false;

        int i = is.read();
        int curLine = 1;
        while(i != -1) {
            switch( (char) i) {
                case '\n' : if( cryptEngine != null && curLine >= command.getLines().size() ){   signatureStarted = true; }
                            else {  answer.write(i); curLine++; }
                    break;
                case '\r' : break;
                default: if(signatureStarted)   signature.write(i);
                         else                   answer.write(i);
            };

            i = is.read();
        }

        // И проверка подписи, если она необходима
        if(  cryptEngine != null  ) {
            cryptEngine.verifySignature(answer.toByteArray(), signature.toByteArray());
        }

        IResponseSet responseSet = new ResponseSetImpl();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getData())));
        String responseLine = reader.readLine();
        while (responseLine != null && responseLine.length() > 0) {
            Log.i("TFORWARD.HttpTransport", responseLine + "");
            responseSet.addLine(responseLine.getBytes());
            responseLine = reader.readLine();
        }

        // Newer throws IOException
        reader.close();

        return responseSet;
    }
}
