package com.example.yurez.hc2;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

class MedInfo implements Parcelable
{
    String name = "";
    String medType = "";
    int numMedType = 0;
    String plan = "";
    Long startDate = -1L;
    Long finalDate = Long.MAX_VALUE;
    ArrayList<DoseTime> doseTimes;
    String whenToTake = "";
    int numWhenToTake = 0;
    String adminMethod = "";
    int numAdminMethod = 0;
    Float remAmount = -1f;
    String note = "";
    Integer state = 0;
    Integer countPos = 0;
    Integer countNeg = 0;


    protected MedInfo(Parcel in)
    {
        if (in != null)
        {
            name = in.readString();
            medType = in.readString();
            numMedType = in.readInt();
            plan = in.readString();
            if (in.readByte() == 0)
            {
                startDate = null;
            } else
            {
                startDate = in.readLong();
            }
            if (in.readByte() == 0)
            {
                finalDate = null;
            } else
            {
                finalDate = in.readLong();
            }
            whenToTake = in.readString();
            numWhenToTake = in.readInt();
            adminMethod = in.readString();
            numAdminMethod = in.readInt();
            if (in.readByte() == 0)
            {
                remAmount = null;
            } else
            {
                remAmount = in.readFloat();
            }
            note = in.readString();
            if (in.readByte() == 0)
            {
                state = null;
            } else
            {
                state = in.readInt();
            }
            if (in.readByte() == 0)
            {
                countPos = null;
            } else
            {
                countPos = in.readInt();
            }
            if (in.readByte() == 0)
            {
                countNeg = null;
            } else
            {
                countNeg = in.readInt();
            }
        }
    }

    public static final Creator<MedInfo> CREATOR = new Creator<MedInfo>()
    {
        @Override
        public MedInfo createFromParcel(Parcel in)
        {
            return new MedInfo(in);
        }

        @Override
        public MedInfo[] newArray(int size)
        {
            return new MedInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(name);
        parcel.writeString(medType);
        parcel.writeInt(numMedType);
        parcel.writeString(plan);
        if (startDate == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeLong(startDate);
        }
        if (finalDate == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeLong(finalDate);
        }
        parcel.writeString(whenToTake);
        parcel.writeInt(numWhenToTake);
        parcel.writeString(adminMethod);
        parcel.writeInt(numAdminMethod);
        if (remAmount == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(remAmount);
        }
        parcel.writeString(note);
        if (state == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeInt(state);
        }
        if (countPos == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeInt(countPos);
        }
        if (countNeg == null)
        {
            parcel.writeByte((byte) 0);
        } else
        {
            parcel.writeByte((byte) 1);
            parcel.writeInt(countNeg);
        }
    }

    public SimpleMedItem packToSimple(int timeCode)
    {
        return new SimpleMedItem(name, medType, -1L, doseTimes.get(timeCode), false, -1);
    }
}
