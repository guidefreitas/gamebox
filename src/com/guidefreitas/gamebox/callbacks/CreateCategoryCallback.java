package com.guidefreitas.gamebox.callbacks;

import com.guidefreitas.gamebox.Category;

public abstract class CreateCategoryCallback {
	public abstract void done(Category category, CreateCategoryException e);
}
