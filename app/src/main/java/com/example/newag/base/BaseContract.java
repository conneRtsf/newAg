package com.example.newag.base;

public interface BaseContract {
    interface BasePresenter<T> {
        void attachView(T view);
        void detachView();
    }
    interface BaseView {

    }
}
