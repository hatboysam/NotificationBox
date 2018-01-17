package com.habosa.notificationbox.messages;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samstern on 1/15/18.
 */
public class Message implements Parcelable {

    private final String mId;
    private Bundle mExtras;

    public Message(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public Bundle getExtras() {
        return mExtras;
    }

    public void setExtras(Bundle mExtras) {
        this.mExtras = mExtras;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeBundle(this.mExtras);
    }

    protected Message(Parcel in) {
        this.mId = in.readString();
        this.mExtras = in.readBundle();
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
