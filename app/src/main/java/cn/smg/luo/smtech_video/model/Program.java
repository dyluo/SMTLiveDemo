package cn.smg.luo.smtech_video.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.model
 * @description:
 * @date 2016/4/1 16:05
 */

public class Program implements Parcelable{
    public String videoName;
    public String path;

    public Program(){}

    protected Program(Parcel in) {
        videoName = in.readString();
        path = in.readString();
    }

    public static final Creator<Program> CREATOR = new Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel in) {
            return new Program(in);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoName);
        dest.writeString(path);
    }
}
