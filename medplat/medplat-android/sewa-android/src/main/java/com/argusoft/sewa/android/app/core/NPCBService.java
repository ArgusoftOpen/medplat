package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.MemberDataBean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by prateek on 3/6/19.
 */
public interface NPCBService {

    List<MemberDataBean> retrieveMembersForNPCBScreening(Integer ashaArea, String search, long limit, long offset,
                                                         LinkedHashMap<String, String> qrData);

    void updateMemberNpcbScreeningDate(Long memberActualId, Date screeningDate);
}
