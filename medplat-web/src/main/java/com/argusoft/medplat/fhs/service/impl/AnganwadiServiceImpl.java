package com.argusoft.medplat.fhs.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.AnganwadiDao;
import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import com.argusoft.medplat.fhs.mapper.AnganwadiMapper;
import com.argusoft.medplat.fhs.model.Anganwadi;
import com.argusoft.medplat.fhs.service.AnganwadiService;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 *     Define all services for anganwadi.
 * </p>
 * @author shrey
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class AnganwadiServiceImpl implements AnganwadiService {

    @Autowired
    AnganwadiDao anganwadiDao;

    @Autowired
    ImtechoSecurityUser imtechoSecurityUser;

    @Autowired
    LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnganwadiDto> getAnganwadisByUserId(Integer locationId, Integer limit, Integer offset) {
        return anganwadiDao.getAnganwadisByUserId(imtechoSecurityUser.getId(), locationId, limit, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(Integer id, Boolean isActive) {
        anganwadiDao.toggleActive(id, isActive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAnganwadi(AnganwadiDto anganwadiDto) {
        if (!this.isIcdsAvailable(anganwadiDto.getIcdsCode())) {
            throw new ImtechoUserException("ICDS CODE already assigned - If you think this is incorrect, then please contact TeCHO Support", 409);
        }
        anganwadiDao.create(AnganwadiMapper.anganwadiDtoToEntity(anganwadiDto, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnganwadiDto getAnganwadiById(Integer id) {
        AnganwadiDto anganwadiDto = AnganwadiMapper.anganwadiEntityToDto(anganwadiDao.retrieveById(id));
        anganwadiDto.setLocation(this.getLocationString(anganwadiDto.getParent()));
        return anganwadiDto;
    }

    /**
     * Retrieves parent location details.
     * @param locationId Location id.
     * @return Returns parent location by id.
     */
    private String getLocationString(Integer locationId) {
        List<String> parentLocations = locationHierchyCloserDetailDao.retrieveParentLocations(locationId);
        return String.join(">", parentLocations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAnganwadi(AnganwadiDto anganwadiDto) {
        Anganwadi entity = anganwadiDao.retrieveById(anganwadiDto.getId());
        if (anganwadiDto.getIcdsCode() != null && !anganwadiDto.getIcdsCode().equals(entity.getIcdsCode())
                && !this.isIcdsAvailable(anganwadiDto.getIcdsCode())) {
                throw new ImtechoUserException("ICDS CODE already assigned - If you think this is incorrect, then please contact TeCHO Support", 409);
        }
        anganwadiDao.update(AnganwadiMapper.anganwadiDtoToEntity(anganwadiDto, entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIcdsAvailable(String code) {
        PredicateBuilder<Anganwadi> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("icdsCode"), code));
            return predicates;
        };
        return anganwadiDao.findByCriteriaList(predicateBuilder).isEmpty();
    }
}
