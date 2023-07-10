/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_child_cerebral_palsy_master entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_child_cerebral_palsy_master")
public class ChildCerebralPalsyMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "child_service_id", nullable = false)
    private Integer childServiceId;

    @Column(name = "dob", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "hold_head_straight")
    private Boolean holdHeadStraight; //1

    @Column(name = "hands_in_mouth")
    private Boolean handsInMouth; //2

    @Column(name = "look_when_speak")
    private Boolean lookWhenSpeak; //3

    @Column(name = "make_noise_when_speak")
    private Boolean makeNoiseWhenSpeak; //4

    @Column(name = "look_in_direc_of_sound")
    private Boolean lookInDirectionOfSound; //5

    @Column(name = "sit_without_help")
    private Boolean sitWithoutHelp; //6

    @Column(name = "kneel_down")
    private Boolean kneelDown; //7

    @Column(name = "avoid_strangers")
    private Boolean avoidStrangers; //8

    @Column(name = "understand_no")
    private Boolean understandNo; //9

    @Column(name = "enjoy_peekaboo")
    private Boolean enjoyPeekaboo; //10

    @Column(name = "responds_on_name_calling")
    private Boolean respondsOnNameCalling; //11

    @Column(name = "lifts_toys")
    private Boolean liftToys; //12

    @Column(name = "mimic_others")
    private Boolean mimicOthers; //13

    @Column(name = "drink_from_glass")
    private Boolean drinkFromGlass; //14

    @Column(name = "run_independently")
    private Boolean runIndependently; //15

    @Column(name = "hold_things_with_finger")
    private Boolean holdThingsWithFinger; //16

    @Column(name = "look_when_name_called")
    private Boolean lookWhenNameCalled; //17

    @Column(name = "speak_simple_words")
    private Boolean speakSimpleWords; //18

    @Column(name = "understand_instructions")
    private Boolean understandInstructions; //19

    @Column(name = "tell_name_of_things")
    private Boolean tellNameOfThings; //20

    @Column(name = "flip_pages")
    private Boolean flipPages; //21

    @Column(name = "kick_ball")
    private Boolean kickBall; //22

    @Column(name = "climb_updown_stairs")
    private Boolean climbUpDownStairs; //23

    @Column(name = "speak_two_sentences")
    private Boolean speakTwoSentences; //24

    @Column(name = "like_playing_other_children")
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
