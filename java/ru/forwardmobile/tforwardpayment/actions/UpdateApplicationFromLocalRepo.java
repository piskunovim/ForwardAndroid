package ru.forwardmobile.tforwardpayment.actions;

import android.content.Context;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by Василий Ванин on 22.12.2014.
 */
public class UpdateApplicationFromLocalRepo extends AbstractTask {

    public UpdateApplicationFromLocalRepo(Context context, ITaskListener listener) {
        super(listener, context);
    }

    @Override
    protected Object doInBackground(Object... objects) {

        try {

            Update(TSettings.LOCAL_REPOSITORY_URL);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }

        return 1;
    }

    public void Update(String apkURL) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpGet query;
        HttpResponse response;

        query = new HttpGet(apkURL);
        response = client.execute(query);
        HttpEntity content = response.getEntity();


        String PATH = Environment.getExternalStorageDirectory() + "/download/";
        File file = new File(PATH);
        file.mkdirs();
        File outputFile = new File(file, "app.apk");
        FileOutputStream fos = new FileOutputStream(outputFile);

        InputStream is = content.getContent();

        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len1);
        }
        fos.close();
        is.close();
    }
}
