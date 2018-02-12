package com.habosa.notificationbox.util;

/**
 * Created by samstern on 2/11/18.
 */
public class Resource<T> {

    public enum State {
        LOADING,
        SUCCESS,
        FAILURE
    }

    private final State mState;
    private final T mResult;
    private final Exception mException;

    public static <T> Resource<T> forSuccess(T result) {
        return new Resource<>(State.SUCCESS, result, null);
    }

    public static <T> Resource<T> forLoading() {
        return new Resource<>(State.LOADING, null, null);
    }

    public static <T> Resource<T> forFailure(Exception e) {
        return new Resource<>(State.FAILURE, null, e);
    }

    Resource(State state, T result, Exception e) {
        mState = state;
        mResult = result;
        mException = e;
    }


    public State getState() {
        return mState;
    }

    public T getResult() {
        return mResult;
    }

    public Exception getException() {
        return mException;
    }

}
