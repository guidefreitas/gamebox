package com.guidefreitas.gamebox.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class HardWorkAsync extends  AsyncTask<Integer, String, Integer> {
	
	private ProgressDialog progress;
	private Context context;
	
	public HardWorkAsync(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute(){
		progress = new ProgressDialog(context);
        progress.setMessage("Aguarde...");
        progress.show();
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		for (int i = 0; i < params.length; i++) {
            try {
                //Simula processo...
                Thread.sleep(params[i]);
                //Atualiza a interface através do onProgressUpdate
                publishProgress(i + "...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
	}
	
	@Override
    protected void onPostExecute(Integer result) {
        //Cancela progressDialogo
        progress.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //Atualiza mensagem
        progress.setMessage(values[0]);
    }

}
