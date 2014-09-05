package ru.forwardmobile.tforwardpayment.operators;

import java.net.URLEncoder;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * @author Василий Ванин
 */
public class RequestBuilder {
    
    public String buildRequest(IProcessingAction action, IPayment payment) {
        
        StringBuilder requestBody = new StringBuilder();
        StringBuilder xmlData     = new StringBuilder();  
        
        xmlData.append( "<data>" );
        
        for( IRequestProperty property: action.getRequestProperties() ) {
            
            // A target field should be separated from other fields
            if(IRequestProperty.TARGET_REQUEST_PROPERTY
                    . equals( property.getName() )) {
            
                requestBody.append("target=" + payment.getField( property.getRefField() ).getValue() );
            } else {
                
                // Other fields should be passed as xml-data
                xmlData.append(property.toXml(payment));
            }
        }   
        
        xmlData.append( "</data>" );
        
        try {
            requestBody.append("&data=")
                   .append( URLEncoder.encode(xmlData.toString(),"UTF-8") );
        }catch(Exception ex) {}
        
        requestBody.append("&id=")
                   .append(payment.getId());
        
        return requestBody.toString();
    }
}
