package com.argusoft.medplat.common.dto;

import lombok.Data;

@Data
public class EncryptionKeyAndIVDto {
    private String key;
    private String initVector;

    public EncryptionKeyAndIVDto(String key, String initVector) {
        this.key = key;
        this.initVector = initVector;
    }
}
