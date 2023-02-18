package edu.northeastern.groupproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Joke implements Parcelable {
    String category;
    String setup;
    String delivery;

    public Joke(String category, String setup, String delivery) {
        this.category = category;
        this.setup = setup;
        this.delivery = delivery;
    }
    protected Joke(Parcel in){
        this.category=in.readString();
        this.setup=in.readString();
        this.delivery=in.readString();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(setup);
        parcel.writeString(delivery);
    }
    public static final Creator<Joke> CREATOR = new Creator<Joke>() {
        @Override
        public Joke createFromParcel(Parcel in) {
            return new Joke(in);
        }

        @Override
        public Joke[] newArray(int size) {
            return new Joke[size];
        }
    };
}
