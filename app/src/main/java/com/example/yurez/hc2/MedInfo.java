package com.example.yurez.hc2;

import java.util.ArrayList;

class MedInfo
{
    String name = "";
    String medType = "";
    int numMedType = 0;
    String plan = "";
    Long startDate;
    Long finalDate = -1L;
    ArrayList<DoseTime> doseTimes;
    String whenToTake = "";
    int numWhenToTake = 0;
    String adminMethod = "";
    int numAdminMethod = 0;
    Integer remAmount = -1;
    String note = "";
    Integer state = 0;
    Integer countPos = 0;
    Integer countNeg = 0;
}
