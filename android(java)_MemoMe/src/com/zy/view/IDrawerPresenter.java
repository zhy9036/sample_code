package com.zy.view;

public interface IDrawerPresenter {
	IDrawerPresenter getInstance();
	void dispatchEvent(int totalPages, int curentPage);
}
