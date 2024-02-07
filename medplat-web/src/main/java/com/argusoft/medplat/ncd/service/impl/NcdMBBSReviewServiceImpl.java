package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.ncd.dao.MbbsMOReviewDao;
import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;
import com.argusoft.medplat.ncd.service.NcdMBBSReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NcdMBBSReviewServiceImpl implements NcdMBBSReviewService {

    @Autowired
    private MbbsMOReviewDao mbbsMOReviewDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ImtechoSecurityUser user;

    @Override
    public MbbsMOReviewDetail retrieveMbbsMOReviewDetail(Integer memberId) {
        return mbbsMOReviewDao.retrieveLastRecordByMemberId(memberId);
    }

    @Override
    public void saveMbbsMOReview(MbbsMOReviewDto mbbsMOReviewDto) {
        MbbsMOReviewDetail mbbsMOReviewDetail= MemberDetailMapper.dtoToEntityForMbbsMOReviewDetail(mbbsMOReviewDto);
        mbbsMOReviewDao.create(mbbsMOReviewDetail);
    }

    @Override
    public List<MemberDetailDto> retrieveMembersForMbbsMOReview(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, String status) {
        List<MemberDetailDto> memberDetailDtos=new ArrayList<>();
//        List<MemberDetailDto> memberDetailDtoList= memberDao.retrieveNcdMembers(user.getId(), limit, offset, healthInfrastructureType, searchBy, searchString);
//        for (MemberDetailDto detailDto:memberDetailDtoList) {
//            List<String> st = memberDao.retrieveMembersOnStatus(detailDto.getId());
//            if(!st.isEmpty() && st.get(0)!=null) {
//                for (String s:st) {
//                    if(s != null && s.equalsIgnoreCase(status)){
//                        memberDetailDtos.add(detailDto);
//                    }
//                }
//            }
//        }
        return memberDetailDtos;
    }

    @Override
    public MbbsMOReviewDto retrieveLastCommentByMBBS(Integer memberId) {
        return mbbsMOReviewDao.retrieveLastCommentByMBBS(memberId);
    }
}
