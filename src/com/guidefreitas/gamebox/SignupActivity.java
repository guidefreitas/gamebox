package com.guidefreitas.gamebox;

import com.guidefreitas.gamebox.callbacks.CreateUserCallback;
import com.guidefreitas.gamebox.callbacks.CreateUserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {
	
	private Button btCreateAccount;
	private EditText etEmail;
	private EditText etPassword;
	private EditText etConfirmPassword;

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        btCreateAccount = (Button) findViewById(R.id.btCreateAccount);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        
        btCreateAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createAccount();
			}
		});
        
        
	}
	
	private void createAccount(){
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();
		String confirmPassword  = etConfirmPassword.getText().toString();
		
		if(email.isEmpty()){
			showErrorMessage("Email not informed");
			return;
		}
		
		if(password.isEmpty()){
			showErrorMessage("Password not informed");
			return;
		}
		
		if(confirmPassword.isEmpty()){
			showErrorMessage("Password confirmation not informed");
			return;
		}
		
		if(!password.equals(confirmPassword)){
			showErrorMessage("Password and password confirmation should be equal");
			return;
		}
		
		
		btCreateAccount.setEnabled(false);
		btCreateAccount.setText(R.string.loading);
		AuthManager.getInstance(this).createUser(email, password, new CreateUserCallback() {
			
			@Override
			public void done(String email, CreateUserException e) {
				if(e == null){
					NavigateToMain();
				}else{
					showErrorMessage(e.getMessage());
				}
				btCreateAccount.setText(R.string.create_account);
				btCreateAccount.setEnabled(true);
				
			}
		});
	}
	
	private void showErrorMessage(String message){
		 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 etEmail.startAnimation(shake);
		 etPassword.startAnimation(shake);
		 etConfirmPassword.startAnimation(shake);
		 btCreateAccount.startAnimation(shake);
		
		 Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		 toast.show();
	 }
	
	private void NavigateToMain(){
		 Intent intent = new Intent(this, MainActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
	 }
}
