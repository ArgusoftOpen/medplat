package com.argusoft.medplat.systemconstraint.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.systemconstraint.dao.SystemConstraintStandardFieldValueMasterDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldValueMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SystemConstraintStandardFieldValueMasterDaoImpl extends GenericDaoImpl<SystemConstraintStandardFieldValueMaster, UUID> implements SystemConstraintStandardFieldValueMasterDao {

    @Override
    public void deleteSystemConstraintStandardFieldValueConfigsByUuids(List<UUID> standardFieldValueUuidsToBeRemoved) {
        if (CollectionUtils.isEmpty(standardFieldValueUuidsToBeRemoved)) return;

        String commaSeparateUuids = standardFieldValueUuidsToBeRemoved.stream().map(String::valueOf).collect(Collectors.joining("', '"));
        String query = "DELETE FROM system_constraint_standard_field_value_master WHERE cast(uuid as text) in ( '" + commaSeparateUuids + "' ) ;";
        Session session = sessionFactory.getCurrentSession();
        session.createSQLQuery(query).executeUpdate();

    }
}
