package ru.forwardmobile.util.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vaninv on 02.07.2014.
 */
public abstract class AbstractTask extends AsyncTask<Object,Object,Object> {

    protected static final String   DEFAULT_DIALOG_TITLE = "Загрузка";
    protected static final String   DEFAULT_DIALOG_MESSAGE = "Пожалуйста подождите";

    private Context             ctx             = null;
    private ProgressDialog      progressDialog  = null;
    private ITaskListener       listener        = null;

    public AbstractTask(ITaskListener listener, Context ctx) {

        this.listener = listener;

        if(ctx != null) {
            this.ctx = ctx;
            this.progressDialog = new ProgressDialog(ctx);
            this.progressDialog.setTitle(DEFAULT_DIALOG_TITLE);
            this.progressDialog.setMessage(DEFAULT_DIALOG_MESSAGE);
        }
    }

    public AbstractTask(){
        this(null,null);
    }

    // Лучше создавать отдельный конструктор, если какой-то из параметров можно не задавать,
    // чтобы пользователи твоего кода знали, что экземпляр будут работоспособен и с таким набором
    // параметров.
    // Если же параметр в кострукторе опускается, но его можно и обязательно нужно задать потом, то
    // это уже косяк. Лучше тогда делать конструктор вообще без параметров
    public AbstractTask(ITaskListener listener){
        this(listener, null);
    }

    public void setProgressDialog(ProgressDialog dialog) {
        this.progressDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            progressDialog.show();
            progressDialog.setCancelable(false);
        }
        super.onPreExecute();
    }

    @Override
    protected final void onPostExecute(Object result) {
        if(progressDialog != null)
            try {
                progressDialog.dismiss();
            }
            catch (Exception ex){

            }

        if (listener != null) {
            listener.onTaskFinish(result);
        }

        super.onPostExecute(result);
    }

    protected Context getContext() {
        return ctx;
    }
}
