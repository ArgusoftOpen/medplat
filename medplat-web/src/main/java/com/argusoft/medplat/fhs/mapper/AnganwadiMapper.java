package com.argusoft.medplat.fhs.mapper;

import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import com.argusoft.medplat.fhs.model.Anganwadi;

/**
 * <p>
 * Mapper for anganwadi in order to convert dto to model or model to dto.
 * </p>
 *
 * @author shrey
 * @since 26/08/20 11:00 AM
 */
public class AnganwadiMapper {

    private AnganwadiMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert anganwadi entity to dto.
     *
     * @param anganwadi Entity of anganwadi.
     * @return Returns details of anganwadi.
     */
    public static AnganwadiDto anganwadiEntityToDto(Anganwadi anganwadi) {
        AnganwadiDto anganwadiDto = new AnganwadiDto();
        anganwadiDto.setAliasName(anganwadi.getAliasName());
        anganwadiDto.setName(anganwadi.getName());
        anganwadiDto.setState(anganwadi.getState());
        anganwadiDto.setParent(anganwadi.getParent());
        anganwadiDto.setId(anganwadi.getId());
        anganwadiDto.setEmamtaId(anganwadi.getEmamtaId());
        anganwadiDto.setIcdsCode(anganwadi.getIcdsCode());
        return anganwadiDto;
    }

    /**
     * Convert anganwadi dto to entity.
     *
     * @param anganwadiDto Details of anganwadi.
     * @param anganwadi    Entity of anganwadi.
     * @return Returns Entity of anganwadi.
     */
    public static Anganwadi anganwadiDtoToEntity(AnganwadiDto anganwadiDto, Anganwadi anganwadi) {
        if (anganwadi == null) {
            anganwadi = new Anganwadi();
        }
        anganwadi.setAliasName(anganwadiDto.getAliasName());
        anganwadi.setName(anganwadiDto.getName());
        anganwadi.setState(anganwadiDto.getState());
        anganwadi.setParent(anganwadiDto.getParent());
        anganwadi.setId(anganwadiDto.getId());
        anganwadi.setEmamtaId(anganwadiDto.getEmamtaId());
        anganwadi.setIcdsCode(anganwadiDto.getIcdsCode());

        return anganwadi;
    }
}
