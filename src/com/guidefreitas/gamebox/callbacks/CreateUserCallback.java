package com.guidefreitas.gamebox.callbacks;

public abstract class CreateUserCallback {
	public abstract void done(String email, CreateUserException e);
}
