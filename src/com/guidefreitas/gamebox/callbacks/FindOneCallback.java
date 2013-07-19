package com.guidefreitas.gamebox.callbacks;

public abstract class FindOneCallback<T> {
	public abstract void done(T object, FindException e);
}