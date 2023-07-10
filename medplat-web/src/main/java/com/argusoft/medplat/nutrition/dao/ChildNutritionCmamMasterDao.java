package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildNutritionCmamMaster;

/**
 * <p>
 *     Defines database methods for Child Nutrition CMAM
 * </p>
 * @author smeet
 * @since 09/09/2020 5:30
 */
public interface ChildNutritionCmamMasterDao extends GenericDao<ChildNutritionCmamMaster, Integer> {

    ChildNutritionCmamMaster retrieveLastCmamMasterByMemberId(Integer memberId);

    ChildNutritionCmamMaster retrieveActiveChildByChildId(Integer childId);
}
