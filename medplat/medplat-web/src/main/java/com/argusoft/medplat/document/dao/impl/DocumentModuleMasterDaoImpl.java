/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.document.dao.DocumentModuleMasterDao;
import com.argusoft.medplat.document.dto.DocumentModuleMasterDto;
import com.argusoft.medplat.document.model.DocumentModuleMaster;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jay
 */
@Repository
public class DocumentModuleMasterDaoImpl extends GenericDaoImpl<DocumentModuleMaster, Integer> implements DocumentModuleMasterDao {

    @Override
    public List<DocumentModuleMasterDto> retrieveModuleName(String test) {
        String query = "SELECT id ,module_name  as \"moduleName\", base_path as \"basePath\" from document_module_master where module_name = :modulename";

        NativeQuery<DocumentModuleMasterDto> q = getCurrentSession().createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("moduleName", StandardBasicTypes.STRING)
                .addScalar("basePath", StandardBasicTypes.STRING);
        q.setParameter("modulename", test);
        return q.setResultTransformer(Transformers.aliasToBean(DocumentModuleMasterDto.class)).list();
    }

}
