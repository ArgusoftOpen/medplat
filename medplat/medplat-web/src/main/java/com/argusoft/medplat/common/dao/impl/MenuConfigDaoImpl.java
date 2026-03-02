package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.MenuConfigDao;
import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.util.IJoinEnum;
import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dto.UserMenuItemDto;
import com.argusoft.medplat.web.users.model.RoleMaster;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.web.users.model.UserMenuItem;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *     Implements methods of MenuConfigDao
 * </p>
 * @author charmi
 * @since 31/08/2020 4:30
 */
@Repository
public class MenuConfigDaoImpl extends GenericDaoImpl<MenuConfig, Integer> implements MenuConfigDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getConfigurationTypes() {
        StringBuilder query = new StringBuilder("select \n")
                .append("distinct menu_type \n")
                .append("from menu_config ;");

        return getCurrentSession().createNativeQuery(query.toString()).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuConfigDto getMenuConfigByIdWithUserList(Integer menuConfigId, MenuConfig.MenuConfigJoin... joinEntities) {
        String menuQueryString = "select menu.feature_json as featureJson, menu.id as id, menu.menu_name as name ,"
                + "menu.navigation_state as navigationState from menu_config menu where id =:menuId";
        String queryString = "select menu.role_id as designationId "
                + ",menu.user_id as userId ,menu.feature_json as featureJson, "
                + "menu.menu_config_id as menuConfigId "
                + ",menu.user_menu_id as id,menu.designation_id as designationId,usr.first_name||' '||usr.last_name as fullName, "
                + " um_role.name as roleName from user_menu_item menu "
                + "left outer join um_user usr on usr.id=user_id "
                + "left outer join um_role_master um_role on um_role.id=menu.role_id "
                + "where menu_config_id=:menuId and (usr.state='" + UserMaster.State.ACTIVE + "' or um_role.state='" + RoleMaster.State.ACTIVE + "')";

        Session session = this.getCurrentSession();
        NativeQuery<UserMenuItemDto> query = session.createNativeQuery(queryString);
        NativeQuery<MenuConfigDto> menuQuery = session.createNativeQuery(menuQueryString);
        query.setParameter("menuId", menuConfigId);
        menuQuery.setParameter("menuId", menuConfigId);
        MenuConfigDto menu = menuQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("navigationState", StandardBasicTypes.STRING)
                .addScalar("featureJson", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(MenuConfigDto.class)).uniqueResult();
        List<UserMenuItemDto> result = query
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("designationId", StandardBasicTypes.INTEGER)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("roleName", StandardBasicTypes.STRING)
                .addScalar("featureJson", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(UserMenuItemDto.class)).list();
        MenuConfigDto menuConfig = new MenuConfigDto();
        menuConfig.setFeatureJson(menu.getFeatureJson());
        menuConfig.setId(menu.getId());
        menuConfig.setName(menu.getName());
        menuConfig.setNavigationState(menu.getNavigationState());
        menuConfig.setUserMenuItemDtos(result);

        return menuConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuConfig> getActiveMenusByUserIdAndDesignationAndGroup(Boolean fetchGroupObjects, Integer userId, Integer designationId) {
        Session session = this.getCurrentSession();
        String queryString = "select DISTINCT mc from " + MenuConfig.class.getSimpleName() + " mc ";
        if (null != fetchGroupObjects && fetchGroupObjects.equals(Boolean.TRUE)) {
            queryString += " left join fetch mc." + MenuConfig.Fields.MENU_GROUP + " mg left join fetch mc." + MenuConfig.Fields.SUB_GROUP + " sg ";
        }
        queryString += " left join fetch mc." + MenuConfig.Fields.USER_MENU_ITEM_LIST + " e "
                + " where ((e." + UserMenuItem.Fields.USER_ID + "= :userId) ";
        if (null != designationId) {
            queryString += " OR (e." + UserMenuItem.Fields.ROLE_ID + " = :designationId) ";
        }
        queryString += " ) AND mc." + MenuConfig.Fields.IS_ACTIVE + " = true";
        Query<MenuConfig> query = session.createQuery(queryString);
        query.setParameter("userId", userId);
        if (null != designationId) {
            query.setParameter("designationId", designationId);
        }
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuConfig> getMenuConfigListByType(String type, Boolean isUserAdmin, MenuConfig.MenuConfigJoin... joinEntities) {
        PredicateBuilder<MenuConfig> predicateBuilder = (root, builder, query) -> {
            for (IJoinEnum join : joinEntities) {
                root.fetch(join.getAlias(), join.getJoinType());
            }
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(MenuConfig.Fields.TYPE), type));
            predicates.add(builder.equal(root.get(MenuConfig.Fields.IS_ACTIVE), Boolean.TRUE));
            if (isUserAdmin == null || !isUserAdmin) {
                predicates.add(
                        builder.or(
                                builder.isNull(root.get(MenuConfig.Fields.ONLY_ADMIN)),
                                builder.equal(root.get(MenuConfig.Fields.ONLY_ADMIN), Boolean.FALSE)
                        )
                );
            }
            return predicates;
        };

        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkMenuGroups(UUID menuTypeUUID, String columnName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MenuConfig> cq = cb.createQuery(MenuConfig.class);
        Root<MenuConfig> root = cq.from(MenuConfig.class);

        if (columnName.equalsIgnoreCase("uuid")) {
            cq.where(cb.equal(root.get(MenuConfig.Fields.MENU_TYPE_UUID), menuTypeUUID));
        }

        MenuConfig menu = session.createQuery(cq).uniqueResult();
        if (menu != null) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkTypeIsAvailable(String typeName) {
//        need to implement once Type is dynamic rather than static
        return Boolean.FALSE;
    }



}
