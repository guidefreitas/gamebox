package com.guidefreitas.gamebox.callbacks;

public abstract class CompleteCallback<T> {
	public abstract void done(T object, GameboxException e);
}