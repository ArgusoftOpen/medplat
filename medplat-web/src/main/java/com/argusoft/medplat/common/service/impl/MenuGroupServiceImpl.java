
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.MenuConfigDao;
import com.argusoft.medplat.common.dao.MenuGroupDao;
import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.common.service.MenuGroupService;
import com.argusoft.medplat.exception.ImtechoUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implements methods of MenuGroupService
 * @author jaynam
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class MenuGroupServiceImpl implements MenuGroupService {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuConfigDao menuConfigDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer saveGroup(MenuGroup menuGroup) {
        if (menuGroup.getId() != null) {
            menuGroupDao.update(menuGroup);
            return menuGroup.getId();
        } else {
            return menuGroupDao.create(menuGroup);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuGroup> retrieveGroups(String groupName, Integer offset, Integer limit, Boolean subGroupRequired, String groupType) {
        return menuGroupDao.getActiveGroups(groupName, offset, limit, subGroupRequired, groupType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGroupById(Integer id){
        MenuGroup menuGroup = menuGroupDao.retrieveById(id);
        if (Boolean.FALSE.equals(menuGroupDao.isReportAssociatedWithGrouporSubGroup(id)) && Boolean.FALSE.equals(menuGroupDao.isSubgroupAssociatedWithGroup(id))) {
            menuGroup.setIsActive(Boolean.FALSE);
            menuGroupDao.save(menuGroup);
        } else {
            throw new ImtechoUserException("Can't Delete", 0);
        }
    }

}
