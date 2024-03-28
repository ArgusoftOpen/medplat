/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.dao.AdolescentDao;
import com.argusoft.medplat.rch.dao.AncVisitDao;
import com.argusoft.medplat.rch.model.AdolescentEntity;
import com.argusoft.medplat.rch.model.AncVisit;
import com.argusoft.medplat.rch.model.VisitCommonFields;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementation of methods define in anc dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class AdolescentDaoImpl extends GenericDaoImpl<AdolescentEntity, Integer> implements AdolescentDao {

}
