package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.DrugInventoryDao;
import com.argusoft.medplat.ncd.dto.DrugInventoryDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryHealthInfraDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryMedicineDto;
import com.argusoft.medplat.ncd.model.DrugInventoryDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrugInventoryDaoImpl extends GenericDaoImpl<DrugInventoryDetail, Integer> implements DrugInventoryDao {


    @Override
    public List<DrugInventoryMedicineDto> retrieveAllDrugsList() {
        String query = "select id , value as medicineName  from listvalue_field_value_detail" +
                " where field_key='drugInventoryMedicine' ";
//        Session session = sessionFactory.getCurrentSession();
//        NativeQuery<String> sQLQuery = session.createNativeQuery(query);

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryMedicineDto> sQLQuery = session.createNativeQuery(query);
        return sQLQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryMedicineDto.class)).list();
    }

    @Override
    public DrugInventoryDto retrieveMedicine(Integer medicineId, Integer healthId) {

        String query = "select drug.id as \"id\",\n" +
                "drug.quantity_received as \"quantityReceived\",\n" +
                "drug.quantity_issued as \"quantityIssued\",\n" +
                "lfvd.value as \"medicineName\",\n" +
                "drug.medicine_id as \"medicineId\",\n" +
                "drug.health_infra_id as \"healthInfraId\",\n" +
                "drug.is_date as \"issuedDate\",\n" +
                "drug.balance_in_hand as balanceInHand " +
                "from ncd_drug_inventory_detail drug inner join " +
                "listvalue_field_value_detail lfvd on drug.medicine_id = lfvd.id\n" +
                "where health_infra_id=" + healthId + " and medicine_id =" + medicineId +
                " order by id desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryDto> sQLQuery = session.createNativeQuery(query);
        List<DrugInventoryDto> drugList = sQLQuery
                .addScalar("issuedDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("healthInfraId", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .addScalar("medicineId", StandardBasicTypes.INTEGER)
                .addScalar("quantityReceived", StandardBasicTypes.INTEGER)
                .addScalar("quantityIssued", StandardBasicTypes.INTEGER)
                .addScalar("balanceInHand", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryDto.class)).list();

        if (drugList.size() > 0) {
            return drugList.get(0);
        }
        return null;
    }

    @Override
    public List<DrugInventoryHealthInfraDto> retrieveChildHealthInfraByParentId(Integer healthId) {
        String query = "select distinct hid2.name as name ,hid2.id as id from health_infrastructure_details hid \n" +
                "             left join location_hierchy_closer_det lhcd on hid.location_id = lhcd.parent_id \n" +
                "             left join health_infrastructure_details hid2 on hid2.location_id  = lhcd.child_id \n" +
                "             where lhcd.child_loc_type='SC' and hid.id =" + healthId;
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryHealthInfraDto> sQLQuery = session.createNativeQuery(query);
        return sQLQuery.setResultTransformer(Transformers.aliasToBean(DrugInventoryHealthInfraDto.class)).list();
    }

    @Override
    public List<DrugInventoryDto> retrieveAllByDuration(String duration,Integer healthId) {

        String query1 = "SELECT drug.medicine_id as medicineId ," +
                "lfvd.value as  \"medicineName\",\n" +
                "sum(quantity_Received) as \"quantityReceived\",\n" +
                "sum(quantity_issued) as \"quantityIssued\",\n" +
                "health_infra_id as \"healthInfraId\"\n" +
                "FROM public.ncd_drug_inventory_detail drug inner join \n" +
                "listvalue_field_value_detail lfvd on drug.medicine_id = lfvd.id " +
                "where date_trunc('month', is_date) = date_trunc('month', current_timestamp) and " +
                "health_infra_id=" + healthId + " group by drug.medicine_id,lfvd.value,health_infra_id";

        String query2 = "SELECT drug.medicine_id as medicineId," +
                "lfvd.value as  \"medicineName\",\n" +
                "sum(quantity_Received) as \"quantityReceived\",\n" +
                "sum(quantity_issued) as \"quantityIssued\",\n" +
                "health_infra_id as \"healthInfraId\"\n" +
                "FROM public.ncd_drug_inventory_detail drug inner join \n" +
                "listvalue_field_value_detail lfvd on drug.medicine_id = lfvd.id " +
                "where is_date between '2022-04-01' and DATE(NOW())+1 and" +
                " health_infra_id=" + healthId + " group by drug.medicine_id,lfvd.value,health_infra_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryDto> sQLQuery;
        if(duration.equalsIgnoreCase("TM")){
            sQLQuery = session.createNativeQuery(query1);
        }else {
            sQLQuery = session.createNativeQuery(query2);
        }
        return  sQLQuery
                .addScalar("healthInfraId",StandardBasicTypes.INTEGER)
                .addScalar("medicineId",StandardBasicTypes.INTEGER)
                .addScalar("medicineName",StandardBasicTypes.STRING)
                .addScalar("quantityReceived",StandardBasicTypes.INTEGER)
                .addScalar("quantityIssued",StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryDto.class)).list();
    }

    @Override
    public DrugInventoryDto retrieveAMC(Integer healthId, Integer medicineId) {

        String query = "SELECT sum(quantity_issued) as \"quantityIssued\"\n" +
                "FROM public.ncd_drug_inventory_detail \n" +
                "where is_date >  CURRENT_DATE - INTERVAL '3 months' " +
                "and health_infra_id=" + healthId + " and medicine_id=" + medicineId + "\n" +
                "group by medicine_id,health_infra_id";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryDto> sQLQuery = session.createNativeQuery(query);
        return sQLQuery
                .addScalar("quantityIssued", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryDto.class)).uniqueResult();
    }

    @Override
    public List<DrugInventoryDto> retrieveQI(Integer healthId,String medicine) {

        String query="SELECT sum(quantity_issued) as \"quantityIssued\"\n" +
                "FROM public.ncd_drug_inventory_detail \n" +
                "where is_date between '2022-04-01' and DATE(NOW()) " +
                "and health_infra_id="+healthId+" and medicine_name='"+medicine+"' \n" +
                "group by medicine_name,health_infra_id";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryDto> sQLQuery = session.createNativeQuery(query);
        return sQLQuery
                .addScalar("quantityIssued",StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryDto.class)).list();
    }

    @Override
    public List<DrugInventoryMedicineDto> retrieveDrugReceivedByInfraId(Integer healthId) {
        String query = "SELECT drug.medicine_id as id ,lfvd.value as medicineName " +
                " from ncd_drug_inventory_detail drug inner join " +
                "listvalue_field_value_detail lfvd on drug.medicine_id = lfvd.id\n" +
                "where health_infra_id=" + healthId + " and balance_in_hand > 0 " +
                "group by drug.medicine_id,lfvd.value";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryMedicineDto> sQLQuery = session.createNativeQuery(query);
        return sQLQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryMedicineDto.class)).list();
    }

    @Override
    public DrugInventoryDto retrieveMedicineByLocationId(Integer medicineId, Integer locationId) {

        String query = "select drug.id as \"id\",\n" +
                "drug.quantity_received as \"quantityReceived\",\n" +
                "drug.quantity_issued as \"quantityIssued\",\n" +
                "lfvd.value as \"medicineName\",\n" +
                "drug.medicine_id as \"medicineId\",\n" +
                "drug.health_infra_id as \"healthInfraId\",\n" +
                "drug.is_date as \"issuedDate\",\n" +
                "drug.balance_in_hand as balanceInHand\n" +
                "from ncd_drug_inventory_detail drug inner join \n" +
                "listvalue_field_value_detail lfvd on drug.medicine_id = lfvd.id\n" +
                "where medicine_id ="+ medicineId + "and health_infra_id = (select id from health_infrastructure_details where location_id =" +
                " (select parent_id from  location_hierchy_closer_det\n" +
                "where child_id = "+locationId+" and parent_loc_type = 'SC'))\n" +
                "order by id desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<DrugInventoryDto> sQLQuery = session.createNativeQuery(query);
        List<DrugInventoryDto> drugList = sQLQuery
                .addScalar("issuedDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("healthInfraId", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .addScalar("medicineId", StandardBasicTypes.INTEGER)
                .addScalar("quantityReceived", StandardBasicTypes.INTEGER)
                .addScalar("quantityIssued", StandardBasicTypes.INTEGER)
                .addScalar("balanceInHand", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DrugInventoryDto.class)).list();

        if (drugList.size() > 0) {
            return drugList.get(0);
        }
        return null;
    }
}
