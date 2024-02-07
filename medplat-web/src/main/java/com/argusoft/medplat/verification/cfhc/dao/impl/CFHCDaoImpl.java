
package com.argusoft.medplat.verification.cfhc.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.verification.cfhc.dao.CFHCDao;
import com.argusoft.medplat.verification.cfhc.model.CFHCUpdate;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     Implements methods of CFHCDao
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
@Repository
public class CFHCDaoImpl extends GenericDaoImpl<CFHCUpdate, Integer> implements CFHCDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMember(CFHCUpdate cFHCUpdate) {
        super.createOrUpdate(cFHCUpdate);
    }

    public String getLocationHierarchy(Integer locationid) {

        String query = "select get_location_hierarchy(:location_id) as location_name";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("location_id", locationid);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromListValue(Integer id) {
        String query = "select value from listvalue_field_value_detail where id=(:id)";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("id", id);
        return sQLQuery.uniqueResult();
    }

}
