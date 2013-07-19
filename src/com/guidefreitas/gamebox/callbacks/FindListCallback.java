package com.guidefreitas.gamebox.callbacks;

public abstract class FindListCallback<T> {
	public abstract void done(T object, FindException e);
}
