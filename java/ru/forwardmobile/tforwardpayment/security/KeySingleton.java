package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;

/**
 * Created by PiskunovI on 25.06.14.
 */
public class KeySingleton extends FileKeyStorageImpl {

    private static KeySingleton instance = null;

    private KeySingleton(Context context){
        super(context);
    }


    public static  KeySingleton getInstance(Context context){

        if(instance == null) {
            instance = new KeySingleton(context);
        }
        return instance;
    }

    private byte[] secretKey = null;

    @Override
    public byte[] getKey(String type) {
        if( IKeyStorage.PUBLIC_KEY_TYPE . equals(type) ) {
            return super.getKey(type);
        } else
        if( IKeyStorage.SECRET_KEY_TYPE . equals(type)) {
            return secretKey;
        } else {
            throw new IllegalArgumentException("Unsupported key type");
        }
    }

    @Override
    public void setKey(String type, byte[] key) {
        if( IKeyStorage.PUBLIC_KEY_TYPE . equals(type) ) {
            super.setKey(type, key);
        } else
        if( IKeyStorage.SECRET_KEY_TYPE . equals(type)) {
            this.secretKey = key;
        } else {
            throw new IllegalArgumentException("Unsupported key type");
        }
    }

    public byte[] getEncKey (){
        return super.getKey(IKeyStorage.SECRET_KEY_TYPE);
    }

    public void setEncKey(byte[] key){
         super.setKey(IKeyStorage.SECRET_KEY_TYPE,key);
    }
}
