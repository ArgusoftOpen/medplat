package com.argusoft.medplat.code.service.impl;

import com.argusoft.medplat.code.dao.SystemCodeDao;
import com.argusoft.medplat.code.dto.SystemCodeDto;
import com.argusoft.medplat.code.mapper.CodeType;
import com.argusoft.medplat.code.mapper.SystemCodeMapper;
import com.argusoft.medplat.code.mapper.TableType;
import com.argusoft.medplat.code.model.SystemCodeMaster;
import com.argusoft.medplat.code.service.SystemCodeService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Hiren Morzariya
 */
@Service
@Transactional
public class SystemCodeServiceImpl implements SystemCodeService {

    @Autowired
    private SystemCodeDao systemCodeDao;

    @Autowired
    private ImtechoSecurityUser currentUser;

    @Override
    public SystemCodeDto saveOrUpdate(SystemCodeDto dto) {
        SystemCodeMaster codeMaster = null;
        if (dto.getId() != null) {
            codeMaster = systemCodeDao.retrieveByUUID(dto.getId());
        } else {
            dto.setId(UUID.randomUUID());
            List<SystemCodeDto> list = this.getSystemCodesByTypeAndCode(
                    TableType.valueOf(dto.getTableType()),
                    dto.getTableId().toString(),
                    CodeType.valueOf(dto.getCodeType()));
            if (!list.isEmpty()) {
                throw new ImtechoSystemException("SystemCode with same tableType, tableId and Codetype already exist", 400);
            }
        }

        codeMaster = SystemCodeMapper.convertRoleDtoToMaster(dto, codeMaster);
        if (codeMaster.getCreatedBy() == null) {
            codeMaster.setCreatedBy(currentUser.getId());
        }
        codeMaster.setModifiedBy(currentUser.getId());
        systemCodeDao.saveOrUpdate(codeMaster);
        return this.getById(codeMaster.getId());
    }

    @Override
    public SystemCodeDto getById(UUID id) {
        SystemCodeMaster code = systemCodeDao.retrieveByUUID(id);
        if (code == null) {
            throw new ImtechoSystemException("System Code Does not exist with id " + id, 400);
        }
        return SystemCodeMapper.convertCodeMasterToDto(code);
    }

    @Override
    public List<SystemCodeDto> getSystemCodesByTypeAndCode(TableType tableType, String tableId, CodeType codeType) {
        List<SystemCodeMaster> list = systemCodeDao.getSystemCodesByTypeAndCode(tableType, tableId, codeType);
        return SystemCodeMapper.convertMasterListToDto(list);
    }

    @Override
    public List<SystemCodeDto> getSystemCodesByTableType(String tableType) {
        List<SystemCodeMaster> list = systemCodeDao.getSystemCodesByTableType(tableType);
        return SystemCodeMapper.convertMasterListToDto(list);
    }

    @Override
    public List<SystemCodeDto> getSystemCodesByCodeType(String codeType) {
        List<SystemCodeMaster> list = systemCodeDao.getSystemCodesByCodeType(codeType);
        return SystemCodeMapper.convertMasterListToDto(list);
    }

    @Override
    public List<SystemCodeDto> getSystemCodes() {
        List<SystemCodeMaster> list = systemCodeDao.getSystemCodes();
        return SystemCodeMapper.convertMasterListToDto(list);
    }

    @Override
    public void deleteSystemCode(UUID id) {
        systemCodeDao.deleteById(id);
    }

}
