package com.argusoft.medplat.listvalues.service.impl;

import com.argusoft.medplat.fhs.dao.impl.MemberDaoImpl;
import com.argusoft.medplat.listvalues.service.ListValueFieldValueDetailService;
import com.argusoft.medplat.mobile.dto.OptionTagDto;
import com.argusoft.medplat.web.users.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ListValueFieldValueDetailServiceImpl implements ListValueFieldValueDetailService {

    @Autowired
    private MemberDaoImpl memberDao;
    @Autowired
    private UserDao userDao;

    public String getListValueNameFormId(Integer id) {
        String query = "select value from listvalue_field_value_detail where id = " + id;
        return "" + memberDao.getCurrentSession().createNativeQuery(query).uniqueResult();
    }

    public List<OptionTagDto> getListValuesFromFieldKey(String fieldKey) {
        return userDao.getListValuesFromFieldKey(fieldKey);
    }
}
