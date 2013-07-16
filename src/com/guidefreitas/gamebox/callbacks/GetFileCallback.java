package com.guidefreitas.gamebox.callbacks;

public abstract class GetFileCallback {
	public abstract void done(byte[] data, GetFileException e);
}
