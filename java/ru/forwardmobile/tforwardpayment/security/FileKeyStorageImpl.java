package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;

/**
 * @author Vasiliy Vanin
 */
public class FileKeyStorageImpl implements IKeyStorage {
    
    private static final int READ_WRITE_PART = 1024;
    private final Context ctx;
    
    public FileKeyStorageImpl(Context ctx) {
        this.ctx = ctx;
    }
    
    public byte[] getKey(String type) {
        
        byte[] buffer = new byte[ READ_WRITE_PART ];
        InputStream file = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        try {
        
            file = ctx.openFileInput(type + ".key");
            int read = file.read(buffer);
            while(read != -1) {
                os.write(buffer, 0, read);
                read = file.read(buffer);
            }
            
            return os.toByteArray();
            
        } catch(Exception ex) { 
            ex.printStackTrace();
        } finally {
            try { if(file != null) file.close(); }catch(Exception ex){}
            try { os.close();}catch(Exception ex){}
        }
        
        return null;
    }

    public void setKey(String type, byte[] key) {
        
        OutputStream os = null;
        InputStream  is = new ByteArrayInputStream(key);
        byte[] buffer = new byte[READ_WRITE_PART];
        
        try {
            
            os = ctx.openFileOutput(type + ".key", Context.MODE_PRIVATE);
            int read = is.read(buffer);
            while(read != -1) {
                os.write(buffer, 0, read);
                read = is.read(buffer);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if(os != null) os.close(); } catch(Exception important) { important.printStackTrace(); }
            try { is.close(); } catch(Exception ignored)
                        { /** ByteArrayInputStream never throws exception */}
        }
    }
    
}
