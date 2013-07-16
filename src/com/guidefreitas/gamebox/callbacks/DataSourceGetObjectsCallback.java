package com.guidefreitas.gamebox.callbacks;

import java.util.List;

import com.guidefreitas.gamebox.DataSourceGetDataException;

public abstract class DataSourceGetObjectsCallback<T> {
	public abstract void done(List<T> objects, DataSourceGetDataException e);
}
