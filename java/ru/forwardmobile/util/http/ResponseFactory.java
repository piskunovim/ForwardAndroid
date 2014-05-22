package ru.forwardmobile.util.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ResponseFactory {

        public static IResponse getResponse() {
                ResponseImpl response = new ResponseImpl();
                return response;
        }
        
        public static IResponse getResponse(byte[] data) {
                ResponseImpl response = new ResponseImpl();
                
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                
                byte c = 0;
                boolean headers = true;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                StringBuilder headerBuffer = new StringBuilder();
                while ( in.available() > 0 ) {
                        c = (byte)in.read();
                        switch(c) {
                        case('\r'): break;
                        case('\n'): if ( headers ) {
                                           if ( headerBuffer.length() > 0 ) {
                                                   
                                                   String header = headerBuffer.toString();
                                                   
                                                   response.addHeader(header);
                                                   headerBuffer = new StringBuilder();
                                                   
                                                   if ( response.getHeaders().size() == 1 ) {
                                                           
                                                           String[] parts = header.split("\\s");
                                                           if ( parts.length > 1 ) {
                                                                   response.setCode(Integer.parseInt(parts[1]));
                                                           }
                                                   }
                                                   
                                           } else {
                                                   headers = false;
                                           }
                                    } else {
                                            buffer.write(c);
                                    }; break;
                        default: if ( headers ) {
                                        headerBuffer.append((char)c);
                                 } else {
                                        buffer.write(c);
                                 }
                        }
                }
                
                if ( headers && ( headerBuffer.length() > 0 ) ) {
                        response.addHeader(headerBuffer.toString());
                }
                response.setData(buffer.toByteArray());
                
                
                // Василий. Если данные передаются чанками, то нужно 
                // их немножко преобразовать
                doChunkFilter(response);
                
                return response;
        }

        private static void doChunkFilter(ResponseImpl response) {
            
            for(String header: response.getHeaders()) {
                if("Transfer-Encoding: chunked".equals(header) ) {
                    
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ByteArrayInputStream is = new ByteArrayInputStream(response.getData());
                    
                    byte c;
                    boolean inChunk = false;
                    StringBuilder chunkHeader = new StringBuilder();
                    int chunkLength = 0;
                    int readedLength = 0;
                    
                    while(is.available() > 0) {
                        c = (byte)is.read();
                        switch(c) {
                            case('\r'): break;
                            case('\n'): if(inChunk) {
                                            os.write(c);
                                            readedLength++;
                                        }else {
                                            if(chunkHeader.length() > 0) {
                                                chunkLength = Integer.parseInt(chunkHeader.toString(),16);
                                                chunkHeader = new StringBuilder();
                                                inChunk = true;
                                            }
                                        }; break;
                            default: if(inChunk) {
                                        os.write(c);
                                        readedLength++;
                                     } else {
                                        chunkHeader.append((char) c);
                                     }
                                     break;
                        }
                        
                       
                        if(inChunk && readedLength == chunkLength) {
                            chunkLength = 0;
                            readedLength = 0;
                            inChunk = false;
                        }
                    }
                    
                    response.setData(os.toByteArray());
                }
            }
        }
}
