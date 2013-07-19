package com.guidefreitas.gamebox;

import com.guidefreitas.gamebox.callbacks.CompleteCallback;
import com.guidefreitas.gamebox.callbacks.GameboxException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity{
	
	private Button btCreateAccount;
	private Button btLogin;
	private EditText etEmail;
	private EditText etPassword;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_login);
	        
	        btCreateAccount = (Button) findViewById(R.id.btCreateAccount);
	        btLogin = (Button) findViewById(R.id.btLogin);
	        etEmail = (EditText) findViewById(R.id.etEmail);
	        etPassword = (EditText) findViewById(R.id.etPassword);
	        
	        btLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Login();
				}
			});
	        
	        btCreateAccount.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					NavigateToCreateAccount();
				}
			});
	 }
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	         moveTaskToBack(true);
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 
	 private void NavigateToCreateAccount(){
		 Intent intent = new Intent(this, SignupActivity.class);
		 startActivity(intent);
	 }
	 
	 private void NavigateToMain(){
		 Intent intent = new Intent(this, MainActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
	 }
	 
	 private void Login(){
		 String email = etEmail.getText().toString();
		 String password = etPassword.getText().toString();
		 
		 final ProgressDialog progress = new ProgressDialog(this);
		 String loading = this.getResources().getString(R.string.loading);
		 progress.setCanceledOnTouchOutside(false);
		 progress.setMessage(loading);
		 progress.show();
		 
		 AuthManager.getInstance().login(email, password, new CompleteCallback<String>() {
			@Override
			public void done(String email, GameboxException e) {
				if(e == null){
					NavigateToMain();
				}else{
					showErrorMessage(e.getMessage());
				}
				
				progress.dismiss();
			}
		 });
	 }
	 
	 private void showErrorMessage(String message){
		 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 etEmail.startAnimation(shake);
		 etPassword.startAnimation(shake);
		 btLogin.startAnimation(shake);
		
		 Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		 toast.show();
	 }

}
