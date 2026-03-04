package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.MobileLibraryDao;
import com.argusoft.medplat.mobile.model.MobileLibraryMaster;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author prateek on 13 Feb, 2019
 */
@Repository
public class MobileLibraryDaoImpl extends GenericDaoImpl<MobileLibraryMaster, Integer> implements MobileLibraryDao {

    @Override
    public List<MobileLibraryMaster> retrieveMobileLibraryMastersByLastUpdateDate(Integer roleId, Date lastUpdateDate) {
        return super.findByCriteriaList((root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.greaterThan(root.get("modifiedOn"), lastUpdateDate));
            return predicates;
        });
    }

    @Override
    public int updateStateStatus(String fileName, String state) {
        String query = "update mobile_library_master set state = :state, modified_on = now() where file_name = :fileName";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("fileName", fileName);
        sQLQuery.setParameter("state", state);

        return sQLQuery.executeUpdate();
    }
}
