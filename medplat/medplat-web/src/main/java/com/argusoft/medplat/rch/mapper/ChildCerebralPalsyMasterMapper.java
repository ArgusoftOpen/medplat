/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.ChildCerebralPalsyMasterDto;
import com.argusoft.medplat.rch.model.ChildCerebralPalsyMaster;

/**
 * <p>
 * Mapper for child cerebral palsy questions in order to convert dto to model or model to dto.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class ChildCerebralPalsyMasterMapper {
    private ChildCerebralPalsyMasterMapper() {
    }

    /**
     * Convert child cerebral palsy questions details into entity.
     *
     * @param childCerebralPalsyMasterDto Child cerebral palsy questions details.
     * @return Returns cerebral palsy questions entity.
     */
    public static ChildCerebralPalsyMaster convertDtoToEntity(ChildCerebralPalsyMasterDto childCerebralPalsyMasterDto) {
        ChildCerebralPalsyMaster childCerebralPalsyMaster = new ChildCerebralPalsyMaster();
        childCerebralPalsyMaster.setMemberId(childCerebralPalsyMasterDto.getMemberId());
        childCerebralPalsyMaster.setChildServiceId(childCerebralPalsyMasterDto.getChildServiceId());
        childCerebralPalsyMaster.setDob(childCerebralPalsyMasterDto.getDob());
        childCerebralPalsyMaster.setHoldHeadStraight(childCerebralPalsyMasterDto.getHoldHeadStraight());
        childCerebralPalsyMaster.setHandsInMouth(childCerebralPalsyMasterDto.getHandsInMouth());
        childCerebralPalsyMaster.setLookWhenSpeak(childCerebralPalsyMasterDto.getLookWhenSpeak());
        childCerebralPalsyMaster.setMakeNoiseWhenSpeak(childCerebralPalsyMasterDto.getMakeNoiseWhenSpeak());
        childCerebralPalsyMaster.setLookInDirectionOfSound(childCerebralPalsyMasterDto.getLookInDirectionOfSound());
        childCerebralPalsyMaster.setSitWithoutHelp(childCerebralPalsyMasterDto.getSitWithoutHelp());
        childCerebralPalsyMaster.setKneelDown(childCerebralPalsyMasterDto.getKneelDown());
        childCerebralPalsyMaster.setAvoidStrangers(childCerebralPalsyMasterDto.getAvoidStrangers());
        childCerebralPalsyMaster.setUnderstandNo(childCerebralPalsyMasterDto.getUnderstandNo());
        childCerebralPalsyMaster.setEnjoyPeekaboo(childCerebralPalsyMasterDto.getEnjoyPeekaboo());
        childCerebralPalsyMaster.setRespondsOnNameCalling(childCerebralPalsyMasterDto.getRespondsOnNameCalling());
        childCerebralPalsyMaster.setLiftToys(childCerebralPalsyMasterDto.getLiftToys());
        childCerebralPalsyMaster.setMimicOthers(childCerebralPalsyMasterDto.getMimicOthers());
        childCerebralPalsyMaster.setDrinkFromGlass(childCerebralPalsyMasterDto.getDrinkFromGlass());
        childCerebralPalsyMaster.setRunIndependently(childCerebralPalsyMasterDto.getRunIndependently());
        childCerebralPalsyMaster.setHoldThingsWithFinger(childCerebralPalsyMasterDto.getHoldThingsWithFinger());
        childCerebralPalsyMaster.setLookWhenNameCalled(childCerebralPalsyMasterDto.getLookWhenNameCalled());
        childCerebralPalsyMaster.setSpeakSimpleWords(childCerebralPalsyMasterDto.getSpeakSimpleWords());
        childCerebralPalsyMaster.setUnderstandInstructions(childCerebralPalsyMasterDto.getUnderstandInstructions());
        childCerebralPalsyMaster.setTellNameOfThings(childCerebralPalsyMasterDto.getTellNameOfThings());
        childCerebralPalsyMaster.setFlipPages(childCerebralPalsyMasterDto.getFlipPages());
        childCerebralPalsyMaster.setKickBall(childCerebralPalsyMasterDto.getKickBall());
        childCerebralPalsyMaster.setClimbUpDownStairs(childCerebralPalsyMasterDto.getClimbUpDownStairs());
        childCerebralPalsyMaster.setSpeakTwoSentences(childCerebralPalsyMasterDto.getSpeakTwoSentences());
        childCerebralPalsyMaster.setLikePlayingWithOtherChildren(childCerebralPalsyMasterDto.getLikePlayingWithOtherChildren());
        return childCerebralPalsyMaster;
    }

}
