/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.document.dao.DocumentDao;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.model.DocumentMaster;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jay
 */
@Repository
public class DocumentMasterDaoImpl extends GenericDaoImpl<DocumentMaster, Long> implements DocumentDao {

    @Override
    public void updateTemporaryStatus(Long id) {
        String query = "UPDATE document_master SET is_temporary= true WHERE id = :id ";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter("id", id);
        q.executeUpdate();
    }

    @Override
    public List<DocumentDto> getTemporaryData() {
        String query = "SELECT id as \"id\" ,is_temporary  as \"isTemporary\", file_name as \"fileName\" "
                + ",file_name_th as \"FileNameTh\" ,extension as \"extension\" ,"
                + "actual_file_name as \"actualFileName\",module_id as \"moduleId\" "
                + "from document_master where is_temporary = TRUE";

        NativeQuery<DocumentDto> q = getCurrentSession().createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("isTemporary", StandardBasicTypes.BOOLEAN)
                .addScalar("fileName", StandardBasicTypes.STRING)
                .addScalar("FileNameTh", StandardBasicTypes.STRING)
                .addScalar("extension", StandardBasicTypes.STRING)
                .addScalar("actualFileName", StandardBasicTypes.STRING)
                .addScalar("moduleId", StandardBasicTypes.INTEGER);
        return q.setResultTransformer(Transformers.aliasToBean(DocumentDto.class)).list();
    }

}
