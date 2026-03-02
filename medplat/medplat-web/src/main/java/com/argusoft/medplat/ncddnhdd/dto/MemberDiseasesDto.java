package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.ncddnhdd.model.MemberBreastDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberCervicalDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberOralDetail;

public class MemberDiseasesDto {
    private MemberHyperTensionDto memberHypertensionDto;
    private MemberDiabetesDto memberDiabetesDto;
    private MemberOralDetail memberOralDto;
    private MemberBreastDetail memberBreastDto;
    private MemberCervicalDetail memberCervicalDto;

    public MemberHyperTensionDto getMemberHypertensionDto() {
        return memberHypertensionDto;
    }

    public void setMemberHypertensionDto(MemberHyperTensionDto memberHypertensionDto) {
        this.memberHypertensionDto = memberHypertensionDto;
    }

    public MemberDiabetesDto getMemberDiabetesDto() {
        return memberDiabetesDto;
    }

    public void setMemberDiabetesDto(MemberDiabetesDto memberDiabetesDto) {
        this.memberDiabetesDto = memberDiabetesDto;
    }

    public MemberOralDetail getMemberOralDto() {
        return memberOralDto;
    }

    public void setMemberOralDto(MemberOralDetail memberOralDto) {
        this.memberOralDto = memberOralDto;
    }

    public MemberBreastDetail getMemberBreastDto() {
        return memberBreastDto;
    }

    public void setMemberBreastDto(MemberBreastDetail memberBreastDto) {
        this.memberBreastDto = memberBreastDto;
    }

    public MemberCervicalDetail getMemberCervicalDto() {
        return memberCervicalDto;
    }

    public void setMemberCervicalDto(MemberCervicalDetail memberCervicalDto) {
        this.memberCervicalDto = memberCervicalDto;
    }


}
