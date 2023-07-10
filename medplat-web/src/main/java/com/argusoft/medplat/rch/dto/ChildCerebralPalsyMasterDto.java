/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;

/**
 * <p>
 * Used for child cerebral palsy master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class ChildCerebralPalsyMasterDto {

    private Integer id;

    private Integer memberId;

    private Integer childServiceId;

    private Date dob;

    private Boolean holdHeadStraight; //1

    private Boolean handsInMouth; //2

    private Boolean lookWhenSpeak; //3

    private Boolean makeNoiseWhenSpeak; //4

    private Boolean lookInDirectionOfSound; //5

    private Boolean sitWithoutHelp; //6

    private Boolean kneelDown; //7

    private Boolean avoidStrangers; //8

    private Boolean understandNo; //9

    private Boolean enjoyPeekaboo; //10

    private Boolean respondsOnNameCalling; //11

    private Boolean liftToys; //12

    private Boolean mimicOthers; //13

    private Boolean drinkFromGlass; //14

    private Boolean runIndependently; //15

    private Boolean holdThingsWithFinger; //16

    private Boolean lookWhenNameCalled; //17

    private Boolean speakSimpleWords; //18

    private Boolean understandInstructions; //19

    private Boolean tellNameOfThings; //20

    private Boolean flipPages; //21

    private Boolean kickBall; //22

    private Boolean climbUpDownStairs; //23

    private Boolean speakTwoSentences; //24

    private Boolean likePlayingWithOtherChildren; //25

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getChildServiceId() {
        return childServiceId;
    }

    public void setChildServiceId(Integer childServiceId) {
        this.childServiceId = childServiceId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getHoldHeadStraight() {
        return holdHeadStraight;
    }

    public void setHoldHeadStraight(Boolean holdHeadStraight) {
        this.holdHeadStraight = holdHeadStraight;
    }

    public Boolean getHandsInMouth() {
        return handsInMouth;
    }

    public void setHandsInMouth(Boolean handsInMouth) {
        this.handsInMouth = handsInMouth;
    }

    public Boolean getLookWhenSpeak() {
        return lookWhenSpeak;
    }

    public void setLookWhenSpeak(Boolean lookWhenSpeak) {
        this.lookWhenSpeak = lookWhenSpeak;
    }

    public Boolean getMakeNoiseWhenSpeak() {
        return makeNoiseWhenSpeak;
    }

    public void setMakeNoiseWhenSpeak(Boolean makeNoiseWhenSpeak) {
        this.makeNoiseWhenSpeak = makeNoiseWhenSpeak;
    }

    public Boolean getLookInDirectionOfSound() {
        return lookInDirectionOfSound;
    }

    public void setLookInDirectionOfSound(Boolean lookInDirectionOfSound) {
        this.lookInDirectionOfSound = lookInDirectionOfSound;
    }

    public Boolean getSitWithoutHelp() {
        return sitWithoutHelp;
    }

    public void setSitWithoutHelp(Boolean sitWithoutHelp) {
        this.sitWithoutHelp = sitWithoutHelp;
    }

    public Boolean getKneelDown() {
        return kneelDown;
    }

    public void setKneelDown(Boolean kneelDown) {
        this.kneelDown = kneelDown;
    }

    public Boolean getAvoidStrangers() {
        return avoidStrangers;
    }

    public void setAvoidStrangers(Boolean avoidStrangers) {
        this.avoidStrangers = avoidStrangers;
    }

    public Boolean getUnderstandNo() {
        return understandNo;
    }

    public void setUnderstandNo(Boolean understandNo) {
        this.understandNo = understandNo;
    }

    public Boolean getEnjoyPeekaboo() {
        return enjoyPeekaboo;
    }

    public void setEnjoyPeekaboo(Boolean enjoyPeekaboo) {
        this.enjoyPeekaboo = enjoyPeekaboo;
    }

    public Boolean getRespondsOnNameCalling() {
        return respondsOnNameCalling;
    }

    public void setRespondsOnNameCalling(Boolean respondsOnNameCalling) {
        this.respondsOnNameCalling = respondsOnNameCalling;
    }

    public Boolean getLiftToys() {
        return liftToys;
    }

    public void setLiftToys(Boolean liftToys) {
        this.liftToys = liftToys;
    }

    public Boolean getMimicOthers() {
        return mimicOthers;
    }

    public void setMimicOthers(Boolean mimicOthers) {
        this.mimicOthers = mimicOthers;
    }

    public Boolean getDrinkFromGlass() {
        return drinkFromGlass;
    }

    public void setDrinkFromGlass(Boolean drinkFromGlass) {
        this.drinkFromGlass = drinkFromGlass;
    }

    public Boolean getRunIndependently() {
        return runIndependently;
    }

    public void setRunIndependently(Boolean runIndependently) {
        this.runIndependently = runIndependently;
    }

    public Boolean getHoldThingsWithFinger() {
        return holdThingsWithFinger;
    }

    public void setHoldThingsWithFinger(Boolean holdThingsWithFinger) {
        this.holdThingsWithFinger = holdThingsWithFinger;
    }

    public Boolean getLookWhenNameCalled() {
        return lookWhenNameCalled;
    }

    public void setLookWhenNameCalled(Boolean lookWhenNameCalled) {
        this.lookWhenNameCalled = lookWhenNameCalled;
    }

    public Boolean getSpeakSimpleWords() {
        return speakSimpleWords;
    }

    public void setSpeakSimpleWords(Boolean speakSimpleWords) {
        this.speakSimpleWords = speakSimpleWords;
    }

    public Boolean getUnderstandInstructions() {
        return understandInstructions;
    }

    public void setUnderstandInstructions(Boolean understandInstructions) {
        this.understandInstructions = understandInstructions;
    }

    public Boolean getTellNameOfThings() {
        return tellNameOfThings;
    }

    public void setTellNameOfThings(Boolean tellNameOfThings) {
        this.tellNameOfThings = tellNameOfThings;
    }

    public Boolean getFlipPages() {
        return flipPages;
    }

    public void setFlipPages(Boolean flipPages) {
        this.flipPages = flipPages;
    }

    public Boolean getKickBall() {
        return kickBall;
    }

    public void setKickBall(Boolean kickBall) {
        this.kickBall = kickBall;
    }

    public Boolean getClimbUpDownStairs() {
        return climbUpDownStairs;
    }

    public void setClimbUpDownStairs(Boolean climbUpDownStairs) {
        this.climbUpDownStairs = climbUpDownStairs;
    }

    public Boolean getSpeakTwoSentences() {
        return speakTwoSentences;
    }

    public void setSpeakTwoSentences(Boolean speakTwoSentences) {
        this.speakTwoSentences = speakTwoSentences;
    }

    public Boolean getLikePlayingWithOtherChildren() {
        return likePlayingWithOtherChildren;
    }

    public void setLikePlayingWithOtherChildren(Boolean likePlayingWithOtherChildren) {
        this.likePlayingWithOtherChildren = likePlayingWithOtherChildren;
    }

}
