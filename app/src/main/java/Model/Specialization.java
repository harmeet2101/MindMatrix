package Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ganesha on 20/3/17.
 */

public class Specialization implements Parcelable {

    int id;
    String category;
    String name;
    String icon;

    public Specialization(int id, String category, String name, String icon) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    protected Specialization(Parcel in) {
        id = in.readInt();
        category = in.readString();
        name = in.readString();
        icon = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(name);
        dest.writeString(icon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Specialization> CREATOR = new Parcelable.Creator<Specialization>() {
        @Override
        public Specialization createFromParcel(Parcel in) {
            return new Specialization(in);
        }

        @Override
        public Specialization[] newArray(int size) {
            return new Specialization[size];
        }
    };
}