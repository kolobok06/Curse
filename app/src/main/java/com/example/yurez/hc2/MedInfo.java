package com.example.yurez.hc2;

import java.util.ArrayList;

class MedInfo {
    String name;
    String medType;
    String plan;
    ArrayList<DoseTime> doseTimes;
    Long startDate;
    Long finalDate;
    String whenToTake;
    String adminMethod;
    Integer remAmount;
    //TODO: time, days, etc;

    @Deprecated
    void unpackMed(String[] aDataStr) {
        this.name = aDataStr[0];
        this.medType = aDataStr[2];
        this.plan = aDataStr[3];
        this.startDate = Long.parseLong(aDataStr[4]);
        this.finalDate = Long.parseLong(aDataStr[5]);
        this.whenToTake = aDataStr[6];
        this.adminMethod = aDataStr[7];
        this.remAmount = Integer.parseInt(aDataStr[8]);
    }

    @Deprecated
    String[] packMed() {
        String[] str = new String[9];//!!!!!!!
        str[0] = this.name;
        str[2] = this.medType;
        str[3] = this.plan;
        str[4] = this.startDate.toString();
        str[5] = this.finalDate.toString();
        str[6] = this.whenToTake;
        str[7] = this.adminMethod;
        str[8] = this.remAmount.toString();

        return str;
    }
}
