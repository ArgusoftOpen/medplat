
package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.common.dao.ForgotPasswordOtpDao;
import com.argusoft.medplat.common.dao.SequenceDao;
import com.argusoft.medplat.common.databean.OtpDataBean;
import com.argusoft.medplat.common.model.ForgotPasswordOtp;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.OtpService;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoResponseEntity;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fcm.dao.FirebaseTokenDao;
import com.argusoft.medplat.fcm.model.FirebaseTokenEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dao.MobileMenuMasterDao;
import com.argusoft.medplat.mobile.dto.LoggedInUserPrincipleDto;
import com.argusoft.medplat.mobile.dto.MenuDataBean;
import com.argusoft.medplat.mobile.dto.MobUserInfoDataBean;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.training.dao.CertificateDao;
import com.argusoft.medplat.training.dao.TrainingDao;
import com.argusoft.medplat.training.dto.CertificateDto;
import com.argusoft.medplat.training.mapper.CertificateMapper;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.location.constants.LocationConstants;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.dao.UserHealthInfrastructureDao;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.dao.UserLoginDetailMasterDao;
import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.dto.UserLocationDto;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.mapper.UserLocationMappper;
import com.argusoft.medplat.web.users.mapper.UserMapper;
import com.argusoft.medplat.web.users.model.*;
import com.argusoft.medplat.web.users.service.RoleManagementService;
import com.argusoft.medplat.web.users.service.UserService;
import com.argusoft.medplat.web.users.service.UserTokenService;
import com.argusoft.medplat.training.model.Certificate;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements methods of UserService
 *
 * @author vaishali
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserLocationDao userLocationDao;
    @Autowired
    private LocationMasterDao locationMasterDao;
    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    private BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
    @Autowired
    CertificateDao certificateDao;
    @Autowired
    private ForgotPasswordOtpDao forgotPasswordOtpDao;
    @Autowired
    ImtechoSecurityUser imtechoSecurityUser;
    @Autowired
    RoleManagementService roleManagementService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private TrainingDao trainingDao;
    @Autowired
    private TimerEventDao timerEventDao;
    @Autowired
    private UserLoginDetailMasterDao userLoginDetailMasterDao;
    @Autowired
    private UserHealthInfrastructureDao healthInfrastructureDao;
    @Autowired
    private SequenceDao sequenceDao;
//    @Autowired
//    private DrTechoUserDao drTechoUserDao;
    @Autowired
    protected SessionFactory sessionFactory;
    @Autowired
    private MobileMenuMasterDao mobileMenuMasterDao;
    @Autowired
    private FirebaseTokenDao firebaseTokenDao;

    @Autowired
    private OtpService otpService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    Random rand = new Random();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> createOrUpdate(UserMasterDto userDto) {
        Map<String, Integer> ids = new LinkedHashMap<>();
        UserMaster user = UserMapper.convertUserDtoToMaster(userDto);
        UserMaster userRetrieved = userDao.retrieveByUserName(userDto.getUserName());
        handleException(userRetrieved, user);

        if (user.getRoleId() != null && (user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)
                || user.getRoleId().equals(ConstantUtil.MYTECHO_USER_ROLE_ID))) {
            handleInvalidOtp(user, userDto);
        } else {
            if (Boolean.TRUE.equals(this.checkIfUserExistsWithSameAadharOrPhone(user.getAadharNumber(), user.getContactNumber(), user.getId(), null)) && user.getId() == null) {
                throw new ImtechoUserException("Aadhar Number or Phone Number is alerady linked to another User", 101);
            }
        }

        if (user.getPassword() == null && user.getId() != null) {
            user.setPassword(userDao.retrieveById(user.getId()).getPassword());
        }
        if (user.getId() == null) {
            validatePassword(user.getPassword());
            user.setPassword(basicPasswordEncryptor.encryptPassword(userDto.getPassword()));
            user.setId(userDao.create(user));
            ids.put("userId", user.getId());
//            if (user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
//                DrTechoUser drTechoUserModel = DrTechoUserMapper.convertUserMasterDtoToDrTechoUserModel(null, userDto);
//                drTechoUserModel.setUserId(user.getId());
//                int drTechoUserId = drTechoUserDao.create(drTechoUserModel);
//                ids.put("drTechoUserId", drTechoUserId);
//            }
        } else {
            updateDrtechoUser(user, userDto);
        }
        handleLocationDetailsForUser(user, userDto);

        if (userDto.getInfrastructureIds() != null && !user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {     // for Dr techo user, we will add entry in health infra when user approve
            //Edit case of health infrastructure
            handleHealthInfraStructureForDrTecho(user, userDto);
        }
        return ids;
    }

    /**
     * Handle exception.
     *
     * @param userRetrieved Is user retrieved or not.
     * @param user          User details.
     */
    private void handleException(UserMaster userRetrieved, UserMaster user) {
        if (userRetrieved != null && !userRetrieved.getId().equals(user.getId())) {
            throw new ImtechoUserException("User with same username exists", 101);
        }
    }

    /**
     * Update drtecho user details.
     *
     * @param user    Entity of user.
     * @param userDto User details.
     */
    private void updateDrtechoUser(UserMaster user, UserMasterDto userDto) {
        if (userDto.getAddedLocations() == null && (!user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID))) {
            throw new ImtechoUserException("Please add Area of intevation", 101);
        }
        UserMaster um = userDao.retrieveById(user.getId());
        user.setPassword(um.getPassword());
        userDao.merge(user);
//        if (user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
//            DrTechoUser drTechoUser = drTechoUserDao.retrieveByUserId(user.getId());
//            if (drTechoUser != null) {
//                drTechoUser = DrTechoUserMapper.convertUserMasterDtoToDrTechoUserModel(drTechoUser, userDto);
//                drTechoUserDao.update(drTechoUser);
//            }
//        }
    }

    /**
     * Handle invalid otp.
     *
     * @param user    Entity of user.
     * @param userDto User details.
     */
    private void handleInvalidOtp(UserMaster user, UserMasterDto userDto) {
        if (userDto.getId() == null) {
            if (otpService.verifyOtp(userDto.getMobileNumber(), userDto.getOtp())) {
                otpService.invalidateOtp(userDto.getMobileNumber(), userDto.getOtp());
            } else {
                throw new ImtechoUserException("OPT is expired.", 101);
            }
        }

        if (Boolean.TRUE.equals(this.checkIfUserExistsWithSameAadharOrPhone(user.getAadharNumber(), user.getContactNumber(), user.getId(), user.getRoleId())
                && user.getId() == null)) {
            throw new ImtechoUserException("Aadhar Number or Phone Number is alerady linked to another User", 101);
        }
    }

    /**
     * Handle location details for user.
     *
     * @param user    Entity of user.
     * @param userDto User details.
     */
    private void handleLocationDetailsForUser(UserMaster user, UserMasterDto userDto) {
        if (userDto.getDeletedLocations() != null) {
            for (UserLocationDto userLocationDto : userDto.getDeletedLocations()) {
                if (userLocationDto.getId() != null) {
                    UserLocation userLocation = UserLocationMappper.convertUserLocationDtoToMaster(userLocationDto);
                    userLocation.setUserId(user.getId());
                    userLocationDao.markAsInactive(userLocation);
                }
            }
        }
        if (userDto.getAddedLocations() != null) {
            createOrUpdateLocation(userDto, user);
        } else {
            if (!user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
                throw new ImtechoUserException("Please add Area of intevation", 101);
            }
        }
    }

    /**
     * Handle health infra structure for drtecho.
     *
     * @param user    Define user entity.
     * @param userDto User details.
     */
    private void handleHealthInfraStructureForDrTecho(UserMaster user, UserMasterDto userDto) {
        if (userDto.getId() != null) {
            addHealthInfraStructure(user, userDto);
        } else {
            //Create case of health infrastructure

            for (Integer infrastructureId : userDto.getInfrastructureIds()) {
                UserHealthInfrastructure healthInfrastructure = new UserHealthInfrastructure();
                healthInfrastructure.setUserId(user.getId());
                healthInfrastructure.setHealthInfrastructureId(infrastructureId);
                healthInfrastructure.setState(UserHealthInfrastructure.State.ACTIVE);
                if (userDto.getDefaultHealthInfrastructure() != null && userDto.getDefaultHealthInfrastructure().equals(infrastructureId)) {
                    healthInfrastructure.setIsDefault(Boolean.TRUE);
                } else {
                    healthInfrastructure.setIsDefault(Boolean.FALSE);
                }
                healthInfrastructureDao.create(healthInfrastructure);
            }
        }
    }

    /**
     * Add health infrastructure details.
     *
     * @param user    Define user entity.
     * @param userDto User details.
     */
    private void addHealthInfraStructure(UserMaster user, UserMasterDto userDto) {
        List<UserHealthInfrastructure> healthInfrastructures = healthInfrastructureDao.retrieveByUserId(user.getId());
        for (UserHealthInfrastructure healthInfrastructure : healthInfrastructures) {
            healthInfrastructure.setState(UserHealthInfrastructure.State.INACTIVE);
            healthInfrastructure.setIsDefault(Boolean.FALSE);
            for (Integer healthInfrastructureId : userDto.getInfrastructureIds()) {
                if (healthInfrastructureId.equals(healthInfrastructure.getHealthInfrastructureId())) {
                    healthInfrastructure.setState(UserHealthInfrastructure.State.ACTIVE);
                    if (userDto.getDefaultHealthInfrastructure() != null && userDto.getDefaultHealthInfrastructure().equals(healthInfrastructure.getHealthInfrastructureId())) {
                        healthInfrastructure.setIsDefault(Boolean.TRUE);
                    }
                }
            }
            healthInfrastructureDao.merge(healthInfrastructure);
        }
        for (Integer healthInfrastructureId : userDto.getInfrastructureIds()) {
            Boolean ifExists = false;
            for (UserHealthInfrastructure healthInfrastructure : healthInfrastructures) {
                if (healthInfrastructureId.equals(healthInfrastructure.getHealthInfrastructureId())) {
                    ifExists = true;
                    break;
                }
            }
            if (Boolean.FALSE.equals(ifExists)) {
                UserHealthInfrastructure healthInfrastructure = new UserHealthInfrastructure();
                healthInfrastructure.setHealthInfrastructureId(healthInfrastructureId);
                healthInfrastructure.setUserId(user.getId());
                healthInfrastructure.setState(UserHealthInfrastructure.State.ACTIVE);
                if (userDto.getDefaultHealthInfrastructure() != null && userDto.getDefaultHealthInfrastructure().equals(healthInfrastructureId)) {
                    healthInfrastructure.setIsDefault(Boolean.TRUE);
                } else {
                    healthInfrastructure.setIsDefault(Boolean.FALSE);
                }
                healthInfrastructureDao.create(healthInfrastructure);
            }
        }
    }

    /**
     * An util method to create or update location of given user
     *
     * @param userDto An instance of UserMasterDto
     * @param user    An instance of UserMaster
     */
    private void createOrUpdateLocation(UserMasterDto userDto, UserMaster user) {
        for (UserLocationDto userLocationDto : userDto.getAddedLocations()) {
            if (userLocationDto.getId() == null) {
                UserLocation userLocation = userLocationDao.getUserLocationByUserIdLocationId(user.getId(), userLocationDto.getLocationId());
                if (userLocation == null) {
                    userLocation = UserLocationMappper.convertUserLocationDtoToMaster(userLocationDto);
                }
                userLocation.setState(UserLocation.State.ACTIVE);
                userLocation.setUserId(user.getId());
                userLocationDao.createOrUpdate(userLocation);
            }
        }
    }

    public UserMasterDto retrieveByLoginCode(String code) {
        UserMaster retrieveByUserName = userDao.retrieveByLoginCode(code);
        if (retrieveByUserName == null) {
            return null;
        } else {
            return retrieveByUserName(retrieveByUserName.getUserName());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserMasterDto retrieveByUserName(String userName) {
        UserMaster retrieveByUserName = userDao.retrieveByUserName(userName);
        if (retrieveByUserName != null) {
            UserMasterDto userMasterDto = UserMapper.convertUserMasterToDto(retrieveByUserName);
            Integer locationId = null;
            Integer minLocationLevel = null;
            String query = "select\n" +
                    "\tdistinct on\n" +
                    "\t( level ) level as location_level,\n" +
                    "\tparent_id as location_id\n" +
                    "from\n" +
                    "\tlocation_type_master ltm ,\n" +
                    "\tlocation_hierchy_closer_det lhc\n" +
                    "where\n" +
                    "\tlhc.child_id in ( select\n" +
                    "\t\tloc_id\n" +
                    "\tfrom\n" +
                    "\t\tum_user_location\n" +
                    "\twhere\n" +
                    "\t\tuser_id = :userId\n" +
                    "\t\tand state = 'ACTIVE' )\n" +
                    "\tand lhc.parent_loc_type = ltm.type\n" +
                    "\tand parent_id != - 1\n" +
                    "group by\n" +
                    "\tlevel,\n" +
                    "\tparent_id\n" +
                    "having\n" +
                    "\tcount( distinct parent_id ) = 1\n" +
                    "order by\n" +
                    "\tlevel desc,\n" +
                    "\tparent_id\n" +
                    "limit 1";
            NativeQuery<Object> sQLQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
            sQLQuery.setParameter("userId", userMasterDto.getId());
            sQLQuery.addScalar("location_level", StandardBasicTypes.INTEGER);
            sQLQuery.addScalar("location_id", StandardBasicTypes.INTEGER);
            List<Object> result = sQLQuery.list();
            for (Object object : result) {
                Object[] resultArray = (Object[]) object;
                minLocationLevel = (Integer) resultArray[0];
                locationId = (Integer) resultArray[1];
            }
            userMasterDto.setRoleCode(retrieveByUserName.getRole().getCode());
            userMasterDto.setMinLocationLevel(minLocationLevel);
            userMasterDto.setMinLocationId(locationId);
            if (locationId != null) {
                LocationMaster locationMaster = locationMasterDao.retrieveById(locationId);
                userMasterDto.setMinLocationName(locationMaster.getName());
            }
            return userMasterDto;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserMaster retriveUserByUserName(String userName) {
        return userDao.retrieveUserByUserNAme(userName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserMasterDto retrieveById(Integer id) {
        UserMaster user = userDao.retrieveById(id);
        if (user != null) {
            UserMasterDto userDto = UserMapper.convertUserMasterToDto(user);
            List<UserLocation> retrieveLocationsByUserId = userLocationDao.retrieveLocationsByUserId(id);
            List<UserLocation> usersLocations = userLocationDao.retrieveLocationsByUserId(id);
            List<Integer> retrieveLocations = retrieveLocationsByUserId.stream().map(UserLocation::getLocationId).collect(Collectors.toList());
            List<Integer> childLocations = new ArrayList<>();
            for (UserLocation userLocation : usersLocations) {
                childLocations.addAll(locationHierchyCloserDetailDao.retrieveChildLocationIds(userLocation.getLocationId()));
            }
            if (!childLocations.containsAll(retrieveLocations)) {
                throw new ImtechoUserException("Something went wrong. Please try again", 401);
            }
            List<UserLocationDto> userLocationDtos = new LinkedList<>();
            for (UserLocation userLocation : retrieveLocationsByUserId) {
                UserLocationDto userLocationDto = UserLocationMappper.convertUserMasterToDto(userLocation);
                userLocationDto.setLocationFullName(this.getLocationString(userLocation));
                userLocationDtos.add(userLocationDto);

            }
            userDto.setAddedLocations(userLocationDtos);
            List<UserHealthInfrastructure> retrieveByUserId = healthInfrastructureDao.retrieveByUserId(id);
            List<Integer> healthInfrastructureIds = new LinkedList<>();
            for (UserHealthInfrastructure healthInfrastructure : retrieveByUserId) {
                healthInfrastructureIds.add(healthInfrastructure.getHealthInfrastructureId());
                if (Boolean.TRUE.equals(healthInfrastructure.getIsDefault())) {
                    userDto.setDefaultHealthInfrastructure(healthInfrastructure.getHealthInfrastructureId());
                }
            }
            userDto.setInfrastructureIds(healthInfrastructureIds);

//            if (user.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
//                DrTechoUser drTechoUser = drTechoUserDao.retrieveByUserId(user.getId());
//                if (drTechoUser != null) {
//                    DrTechoUserMapper.convertDrTechoUserModelToUserMasterDto(drTechoUser, userDto);
//                }
//            }
            return userDto;
        } else {
            throw new ImtechoUserException("User does not exist with this id", 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> retrieveAll(Boolean isActive) {
        List<UserMaster> userMasters = userDao.retrieveAll(isActive);
        return UserMapper.convertUserMasterListToDtoList(userMasters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(UserMasterDto userDto, Boolean isActive) {
        UserMaster userMaster = userDao.retrieveById(userDto.getId());
        if (Boolean.TRUE.equals(isActive)) {
            if (userDto.getContactNumber() != null) {
                List<UserMasterDto> conflictContactUserInfo = userDao.conflictUserOfContactNumber(userDto.getContactNumber(), userDto.getId());

                if (!conflictContactUserInfo.isEmpty() && userDto.getContactNumber() != null) {
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("This Mobile number is already assigned to ");
                    for (int i = 0; i < conflictContactUserInfo.size(); i++) {
                        errorMsg.append(conflictContactUserInfo.get(i).getFirstName()).append(" ").append(conflictContactUserInfo.get(i).getLastName()).append("(").append(conflictContactUserInfo.get(i).getUserName()).append(")");
                        if (i < conflictContactUserInfo.size() - 1) {
                            errorMsg.append(" , ");
                        }
                    }
                    throw new ImtechoUserException(errorMsg.toString(), 0);
                }
            }
            List<UserMasterDto> conflictLocationUserInfo = userDao.conflictUserOfLocation(userDto.getId());
            checkConflictLocation(conflictLocationUserInfo);

            userMaster.setState(UserMaster.State.ACTIVE);
        } else {
            userMaster.setState(UserMaster.State.INACTIVE);
        }
        userDao.merge(userMaster);
    }


    /**
     * An util method to generate message for conflict location
     *
     * @param conflictLocationUserInfo A list of UserMasterDto
     */
    private void checkConflictLocation(List<UserMasterDto> conflictLocationUserInfo) {
        if (!conflictLocationUserInfo.isEmpty()) {
            Integer maxLocations = conflictLocationUserInfo.get(0).getMaxPositions();

            if (maxLocations != null && maxLocations <= conflictLocationUserInfo.size()) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("This Location is already assigned to ");
                for (int i = 0; i < conflictLocationUserInfo.size(); i++) {
                    errorMessage.append(conflictLocationUserInfo.get(i).getFirstName() + " " + conflictLocationUserInfo.get(i).getLastName() + "(" + conflictLocationUserInfo.get(i).getUserName() + ")");
                    if (i < conflictLocationUserInfo.size() - 1) {
                        errorMessage.append(" , ");
                    }
                }
                throw new ImtechoUserException(errorMessage.toString(), 0);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> retrieveByCriteria(Integer userId, Integer roleId, Integer locationId, String searchString, String orderBy, Integer limit, Integer offset, String order,String status) {
        List<Integer> roles = new ArrayList<>();
        if (roleId != null) {
            roles.add(roleId);
        } else {
            List<RoleMasterDto> retrieveRolesManagedByRoleId = roleManagementService.retrieveRolesManagedByRoleId(imtechoSecurityUser.getRoleId());
            for (RoleMasterDto role : retrieveRolesManagedByRoleId) {
                roles.add(role.getId());
            }
        }

        return userDao.retrieveByCriteria(userId, roles, locationId, searchString, orderBy, limit, offset, order,status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> getUsersByIds(Set<Integer> userIds) {
        return UserMapper.convertUserMasterListToDtoList(userDao.getUsersByIds(userIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> getUsersByLocationsAndRoles(List<Integer> locationIds,
                                                           List<Integer> roleIds) {
        List<UserMasterDto> userMasterDtos = new LinkedList<>();
        if (locationIds != null && roleIds != null) {
            List<UserMaster> users = userDao.getUsersByLocationsAndRoles(locationIds, roleIds);
            if (!users.isEmpty()) {
                for (UserMaster user : users) {
                    if ((user.getState().toString()).equals(UserMaster.Fields.ACTIVE)) {
                        userMasterDtos.add(UserMapper.convertUserMasterToDto(user));
                    }
                }

                return userMasterDtos;
            }
        }

        return userMasterDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> getUsersByLocationAndRoleAndCourse(Integer locationId, Integer roleId, Integer courseId) {

        List<Integer> locationIds = new LinkedList<>();
        locationIds.add(locationId);
        List<Integer> roleIds = new LinkedList<>();
        roleIds.add(roleId);

        List<UserMasterDto> userMasterDtos = this.getUsersByLocationsAndRoles(locationIds, roleIds);
        Set<Integer> additionalAttendeeIds = new HashSet<>();
        for (UserMasterDto userMasterDto : userMasterDtos) {
            additionalAttendeeIds.add(userMasterDto.getId());
        }

        List<CertificateDto> allCertificates = CertificateMapper.entityToDtoCertificateList(certificateDao.getCertificatesByCourseAndType(courseId, Certificate.Type.COURSECOMPLETION));

        Set<Integer> completedTraineeIds = new HashSet<>();
        for (CertificateDto certificate : allCertificates) {
            completedTraineeIds.add(certificate.getUserId());
        }

        Set<Integer> unTrainedAdditionalUserIds = new HashSet<>();
        for (Integer attendee : additionalAttendeeIds) {
            if (!completedTraineeIds.contains(attendee)) {
                unTrainedAdditionalUserIds.add(attendee);
            }
        }

        //to check whether any users have future trainings
        unTrainedAdditionalUserIds.removeAll(trainingDao.getAttendeesForUpcomingTrainingsByRoleAndCourse(roleId, courseId));

        if (!unTrainedAdditionalUserIds.isEmpty()) {
            Set<Integer> personIds = new HashSet<>(unTrainedAdditionalUserIds);
            List<UserMaster> usersByIds = userDao.getUsersByIds(new HashSet<>(personIds));
            if (!usersByIds.isEmpty()) {
                List<UserMasterDto> personInfos = new ArrayList<>();
                for (UserMaster user : usersByIds) {
                    personInfos.add(UserMapper.convertUserMasterToDto(user));
                }
                return personInfos;
            }
        }

        return new ArrayList<>();
    }

    /**
     * An util method to get location string
     *
     * @param userLocation An instance of UserLocation
     * @return A comma separated location string
     */
    private String getLocationString(UserLocation userLocation) {
        List<String> parentLocations = locationHierchyCloserDetailDao.retrieveParentLocations(userLocation.getLocationId());
        return String.join(",", parentLocations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean verifyPassword(String oldPassword, Integer userId) {
        UserMasterDto user = this.retrieveById(userId);
        return basicPasswordEncryptor.checkPassword(oldPassword, user.getPassword());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword(String newPassword, Integer userId) {
        validatePassword(newPassword);
        UserMaster user = userDao.retrieveById(userId);
        user.setPassword(basicPasswordEncryptor.encryptPassword(newPassword));
        userDao.merge(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePasswordOldtoNew(String oldPassword, String newPassword) {
        Integer userId = imtechoSecurityUser.getId();
        UserMaster userMaster = userDao.retrieveById(userId);
        String oldPasswordFromDatabase = userMaster.getPassword();

        if (basicPasswordEncryptor.checkPassword(oldPassword, oldPasswordFromDatabase)) {
            if (newPassword.equals(oldPassword)) {
                throw new ImtechoUserException("New password should not be same as old password", 0);
            }
            validatePassword(newPassword);
            userMaster.setPassword(basicPasswordEncryptor.encryptPassword(newPassword));
            userMaster.setFirstTimePasswordChanged(true);
            userDao.update(userMaster);
        } else {
            throw new ImtechoUserException("Please enter valid old password", 0);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateAndGenerateOtp(String username) {
        UserMaster usermaster = userDao.retrieveByUserName(username);
        if (usermaster == null) {
            throw new ImtechoUserException("Invalid Username", 0);
        }
        if (Objects.isNull(usermaster.getContactNumber())) {
            throw new ImtechoUserException("Mobile Number is not available. Please contact Administration", 0);
        }

        Integer userId = usermaster.getId();
        ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOtpDao.retrieveById(userId);
        if (forgotPasswordOtp != null) {
            Date oldDate = forgotPasswordOtp.getModifiedOn();
            Date currentDate = new Date();
            if ((currentDate.getTime() - oldDate.getTime()) < 30000) {
                throw new ImtechoUserException("Try after 30 Seconds", 0);
            }

        }

        int randInt = rand.nextInt(8999) + 1000;
        if (forgotPasswordOtp == null) {
            forgotPasswordOtp = new ForgotPasswordOtp();
        }
        forgotPasswordOtp.setUserId(userId);
        forgotPasswordOtp.setOtp(String.valueOf(randInt));
        forgotPasswordOtp.setModifiedOn(new Date());
        forgotPasswordOtpDao.createOrUpdate(forgotPasswordOtp);

        String mobileNumber = usermaster.getContactNumber();

        String message = randInt + " is the One Time Password (OTP) for your Techo+ Account. "
                + "Please DO NOT share this OTP with anyone.";
        // send mobile verification
        smsService.sendSms(mobileNumber, message, true, "FORGOT_PASSWORD_OTP");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyOtp(String username, String otp, Integer noOfAttempts) {
        UserMaster usermaster = userDao.retrieveByUserName(username);
        Integer userId = usermaster.getId();
        Integer LOGIN_ATTEMPTS = getLoginAttempts();

        if (noOfAttempts >= LOGIN_ATTEMPTS) {
            usermaster.setState(UserMaster.State.INACTIVE);
            userDao.update(usermaster);
            userDao.commit();
            throw new ImtechoUserException("Your user name is inactive. Please contact Administration", 0);
        }

        ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOtpDao.retrieveById(userId);

        if (!otp.equals(forgotPasswordOtp.getOtp())) {
            throw new ImtechoUserException("Incorrect OTP. After" + LOGIN_ATTEMPTS + " unsuccessful attempts, your user will be inactive.", 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(String username, String otp, String password) {
        UserMaster userMaster = userDao.retrieveByUserName(username);
        if (userMaster.getRoleId() != null && userMaster.getRoleId().equals(ConstantUtil.MYTECHO_USER_ROLE_ID)) {
            OtpDataBean retrievedOtp = sequenceDao.retrieveOtp(userDao.retrieveByUserName(username).getContactNumber());
            if (retrievedOtp != null && retrievedOtp.getState().equals(UserMaster.Fields.ACTIVE) && retrievedOtp.getOtp().equals(otp)) {
                userMaster.setPassword(basicPasswordEncryptor.encryptPassword(password));
                userDao.update(userMaster);
                sequenceDao.invalidateOtp(userMaster.getContactNumber());
                return;
            }
            throw new ImtechoUserException("OTP Validation Failed, Retry", 0);
        } else {
            Integer userId = userMaster.getId();
            ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOtpDao.retrieveById(userId);

            if (!otp.equals(forgotPasswordOtp.getOtp())) {
                throw new ImtechoUserException("OTP Validation Failed, Retry", 0);
            } else if (password != null) {
                this.changePassword(password, userId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkIfUserExistsWithSameAadharOrPhone(String aadharNumber, String contactNumber, Integer userId, Integer roleId) {
        List<UserMaster> usersMatchingAadhar;
        List<UserMaster> usersMatchingPhone;
        if (aadharNumber != null && contactNumber == null) {
            usersMatchingAadhar = userDao.retrieveByAadhar(aadharNumber);
            if (!usersMatchingAadhar.isEmpty() && !usersMatchingAadhar.get(0).getId().equals(userId)) {
                throw new ImtechoUserException("Aadhar Number is already Linked to another User", 101, usersMatchingAadhar.get(0).getUserName());
            }
        }
        if (aadharNumber == null && contactNumber != null) {
            if (roleId != null) {
                usersMatchingPhone = userDao.retrieveByPhone(contactNumber, roleId);
            } else {
                usersMatchingPhone = userDao.retrieveByPhone(contactNumber, null);
            }

            if (!usersMatchingPhone.isEmpty() && !usersMatchingPhone.get(0).getId().equals(userId)) {
                throw new ImtechoUserException("Phone Number is already Linked to another User", 101, usersMatchingPhone.get(0).getUserName());
            }
        }
        if (aadharNumber != null && contactNumber != null) {
            generateMsg(aadharNumber, contactNumber, userId);
        }
        return false;
    }

    /**
     * An util method to generate msg related to aadhar number and contact number
     *
     * @param aadharNumber  An aadhar number
     * @param contactNumber An contact number
     * @param userId        An user id
     */
    private void generateMsg(String aadharNumber, String contactNumber, Integer userId) {
        List<UserMaster> usersMatchingAadhar = userDao.retrieveByAadhar(aadharNumber);
        List<UserMaster> usersMatchingPhone = userDao.retrieveByPhone(contactNumber, null);

        if (!usersMatchingAadhar.isEmpty() && !usersMatchingPhone.isEmpty() && !usersMatchingAadhar.get(0).getId().equals(userId)) {
            throw new ImtechoUserException("Aadhar Number is already Linked to another User", 101, usersMatchingAadhar.get(0).getUserName());
        } else if (!usersMatchingAadhar.isEmpty() && !usersMatchingPhone.isEmpty() && !usersMatchingPhone.get(0).getId().equals(userId)) {
            throw new ImtechoUserException("Phone Number is already Linked to another User", 101, usersMatchingPhone.get(0).getUserName());
        } else if (!usersMatchingAadhar.isEmpty() && !usersMatchingPhone.isEmpty() && !usersMatchingAadhar.get(0).getId().equals(userId) && !usersMatchingPhone.get(0).getId().equals(userId)) {
            throw new ImtechoUserException("Aadhar Number & Phone Number is already Linked to another User", 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImtechoResponseEntity validateAOI(Integer userId, Integer roleId, List<Integer> locationIds, Integer toBeAdded) {

        List<Integer> parentLocationIds = locationHierchyCloserDetailDao.retrieveParentLocationIds(toBeAdded);
        List<Integer> childLocationIds = locationHierchyCloserDetailDao.retrieveChildLocationIds(toBeAdded);
        if (!CollectionUtils.isEmpty(locationIds) && (CollectionUtils.containsAny(childLocationIds, locationIds) || CollectionUtils.containsAny(parentLocationIds, locationIds))) {
            return new ImtechoResponseEntity("Duplicacy of area of intervention found. Kindly add another.", 2);
        }

        //check if max locations for role
        if (roleId != null) {
            List<UserMasterDto> users = userDao.retrieveUsersByRoleAndLocation(roleId, toBeAdded, userId);
            UserLocation userLocation = new UserLocation();
            userLocation.setLocationId(toBeAdded);
            String locationString = this.getLocationString(userLocation);
            if (!CollectionUtils.isEmpty(users)) {
                Integer maxLocations = users.get(0).getMaxPositions();
                if (maxLocations == null) {
                    return new ImtechoResponseEntity(locationString, 1);
                } else if (maxLocations > users.size()) {
                    return new ImtechoResponseEntity(locationString, 1, users);
                } else {
                    return new ImtechoResponseEntity(" " + maxLocations, 2, users);
                }
            } else {
                return new ImtechoResponseEntity(locationString, 1);
            }
        }

        return new ImtechoResponseEntity("There is some issue with server. Please check!", 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean validateUserName(String userName, Integer userId) {
        return userDao.validateUserName(userName, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean validatePhoneNumberForDrTecho(String mobileNumber) {
        return CollectionUtils.isEmpty(userDao.retrieveByPhone(mobileNumber, ConstantUtil.DRTECHO_USER_ROLE_ID));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MobUserInfoDataBean validateMobileUserNew(String username, String password, String firebaseToken, boolean firstTimeLoggedIn) {
        MobUserInfoDataBean mobileUserDataBean = new MobUserInfoDataBean();
        List<String> locationTypes = new ArrayList<>();
        List<Integer> parentIds = new ArrayList<>();
        UserMaster mobileUser = userDao.retrieveByUserName(username);
        Integer LOGIN_ATTEMPTS = getLoginAttempts() - 1;
        int LOGIN_TIMEOUT = (int) Math.floor((float) (getLoginTimeout()) / 60000);

        if (mobileUser == null) {
            mobileUserDataBean.setLoginFailureMsg(MobileConstantUtil.INVALID_USERNAME);
            return mobileUserDataBean;
        }

        if (mobileUser.getNoOfAttempts() != null && mobileUser.getNoOfAttempts() > (LOGIN_ATTEMPTS)) {
            mobileUserDataBean.setLoginFailureMsg("Your account is locked for " + LOGIN_TIMEOUT + " minutes. Please try again later");
            return mobileUserDataBean;
        }

        if (!mobileUser.getState().toString().equals(UserMaster.Fields.ACTIVE)) {
            mobileUserDataBean.setLoginFailureMsg(MobileConstantUtil.ACCOUNT_DISABLED);
            return mobileUserDataBean;
        }

        if (!basicPasswordEncryptor.checkPassword(password, mobileUser.getPassword())) {
            updateNoOfAttempts(username, false);
            mobileUserDataBean.setLoginFailureMsg(MobileConstantUtil.INVALID_USERNAME_OR_PASSWORD_STR);
            return mobileUserDataBean;
        } else {
            updateNoOfAttempts(username, true);
        }


        List<MenuDataBean> mobileMenu = mobileMenuMasterDao.retrieveAllMenusForMobile(null, mobileUser.getRoleId());
        if (mobileMenu == null || mobileMenu.isEmpty()) {
            mobileUserDataBean.setLoginFailureMsg(MobileConstantUtil.MENU_NOT_FOUND);
            return mobileUserDataBean;
        }

        List<UserLocation> locationsAssignedToUser = userLocationDao.retrieveLocationsByUserId(mobileUser.getId());
        if (CollectionUtils.isEmpty(locationsAssignedToUser)) {
            mobileUserDataBean.setLoginFailureMsg(MobileConstantUtil.LOCATION_NOT_ASSIGNED);
            return mobileUserDataBean;
        }


        for (UserLocation userLocation : locationsAssignedToUser) {
            parentIds.add(userLocation.getLocationId());
        }

        mobileUserDataBean.setAuthenticated(true);

        addLocationTYpeAsPerRole(mobileUser, locationTypes, parentIds, mobileUserDataBean);
        this.getUserToken(mobileUserDataBean, mobileUser.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.ASHA));
        this.addFirebaseToken(mobileUser.getId(), firebaseToken);
        return mobileUserDataBean;
    }

    private void addLocationTYpeAsPerRole(UserMaster mobileUser, List<String> locationTypes, List<Integer> parentIds, MobUserInfoDataBean mobileUserDataBean) {
        switch (mobileUser.getRole().getName()) {
            case MobileConstantUtil.Roles.FHW:
            case MobileConstantUtil.Roles.CHO_HWC:
            case MobileConstantUtil.Roles.MPHW:
                locationTypes.add(LocationConstants.LocationType.VILLAGE);
                locationTypes.add(LocationConstants.LocationType.ANGANWADI_AREA);
                locationTypes.add("W");
                break;
            case MobileConstantUtil.Roles.RBSK_MO:
                locationTypes.add(LocationConstants.LocationType.BLOCK);
                locationTypes.add(LocationConstants.LocationType.ZONE);
                break;
            case MobileConstantUtil.Roles.FEMALE_HEALTH_SUPERVISOR:
            case MobileConstantUtil.Roles.USER_ROLE_SUPERVISOR_STAFF_NURSE_CORP:
                locationTypes.add(LocationConstants.LocationType.PHC);
                locationTypes.add(LocationConstants.LocationType.UHC);
                break;
            default:
                locationTypes.add(LocationConstants.LocationType.VILLAGE);
                locationTypes.add(LocationConstants.LocationType.AREA);
                locationTypes.add(LocationConstants.LocationType.ASHA_AREA);
                locationTypes.add(LocationConstants.LocationType.ANM_AREA);
                break;
        }

        List<LocationMaster> userLocations = locationMasterDao.retrieveLocationsByLocationIdAndType(parentIds, locationTypes);

        if (userLocations != null && !userLocations.isEmpty()) {
            userLocations.sort((t, t1) -> t.getName().compareToIgnoreCase(t1.getName()));
            convertUserModelToMobUserInfoDataBean(mobileUserDataBean, mobileUser, userLocations);
        }
    }


    private void convertUserModelToMobUserInfoDataBean(MobUserInfoDataBean userInfo, UserMaster user, List<LocationMaster> userLocations) {

        int[] villageCode;
        String[] villageName;
        if (user != null) {
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUserName());
            userInfo.setPassword(user.getPassword());
            userInfo.setLanguageCode(user.getPrefferedLanguage());
            if (user.getRole().getName().equalsIgnoreCase("RBSK MO (Male)") ||
                    user.getRole().getName().equalsIgnoreCase("RBSK MO (Lady)")) {
                userInfo.setUserRole("RBSK MO");
            } else {
                userInfo.setUserRole(user.getRole().getName());
            }

            userInfo.setServerDate(new Date().getTime());
            if (Objects.nonNull(userLocations)) {
                villageCode = new int[userLocations.size()];
                villageName = new String[userLocations.size()];

                for (int i = 0; i < userLocations.size(); i++) {
                    villageCode[i] = userLocations.get(i).getId();
                    villageName[i] = userLocations.get(i).getName();
                }
                if (!user.getRole().getName().equalsIgnoreCase("Lab Technician")) {
                    userInfo.setCurrentVillageCode(villageCode);
                    userInfo.setCurrentAssignedVillagesName(villageName);
                }
            }
            userInfo.setContactNumber(user.getContactNumber());
            userInfo.setfName(user.getFirstName() + " " + user.getLastName());
            Date dob = user.getDateOfBirth();
            if (dob != null) {
                userInfo.setDob(dob.getTime());
            }
            userInfo.setIsNagarPalikaUser(true);
        }
    }

    private void getUserToken(MobUserInfoDataBean user, boolean checkTokenValidity) {
        String token;
        UserToken userToken = userTokenService.getUserTokenByUserId(user.getId());
        if (checkTokenValidity) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -6);
            if (userToken == null || userToken.getCreatedOn().before(calendar.getTime())) {
                userTokenService.deactivateAllActiveUserTokens(user.getId());
                userToken = new UserToken();
                token = basicPasswordEncryptor.encryptPassword(user.getUsername() + ":" + user.getPassword() + new Date().getTime());
                userToken.setUserId(user.getId());
                userToken.setUserToken(token);
                userToken.setIsActive(true);
                userToken.setIsArchieve(false);
                userToken.setCreatedOn(Calendar.getInstance().getTime());
                userTokenService.createUserToken(userToken);
            }
            user.setUserToken(userToken.getUserToken());
        } else {
            if (userToken == null) {
                userToken = new UserToken();
                token = basicPasswordEncryptor.encryptPassword(user.getUsername() + ":" + user.getPassword() + new Date().getTime());
                userToken.setUserId(user.getId());
                userToken.setUserToken(token);
                userToken.setIsActive(true);
                userToken.setIsArchieve(false);
                userToken.setCreatedOn(Calendar.getInstance().getTime());
                userTokenService.createUserToken(userToken);
            } else {
                token = userToken.getUserToken();
            }
            user.setUserToken(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> getAllActiveUsers() {
        return userDao.getAllActiveUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String fetchAvailableUsername(String givenUserName) {
        if (userDao.retrieveByUserName(givenUserName) == null) {
            return givenUserName;
        } else {
            int counter = 1;
            while (true) {
                if (userDao.retrieveUserByUserNAme(givenUserName.concat(String.valueOf(counter))) == null) {
                    return givenUserName.concat(String.valueOf(counter));
                }
                counter++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveFullNameById(Integer attendeeId) {
        UserMaster userMaster = userDao.retrieveById(attendeeId);
        if (userMaster == null) {
            return null;
        }
        return userMaster.getFirstName().concat(" ").concat(userMaster.getLastName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNoOfAttempts(String username, Boolean isPasswordValid) {
        UserMaster userMaster = userDao.retrieveByUserName(username);
        Integer LOGIN_ATTEMPTS = getLoginAttempts() - 1;
        Integer LOGIN_TIMEOUT = getLoginTimeout();
        if (userMaster == null) { //FOR MYTECHO USER
            return;
        }
        if (userMaster.getNoOfAttempts() != null && userMaster.getNoOfAttempts() >= LOGIN_ATTEMPTS && Boolean.FALSE.equals(isPasswordValid)) {
            TimerEvent timerEvent = new TimerEvent();
            timerEvent.setRefId(userMaster.getId());
            timerEvent.setProcessed(Boolean.FALSE);
            timerEvent.setStatus(TimerEvent.STATUS.NEW);
            timerEvent.setType(TimerEvent.TYPE.UNLOCK_USER);
            timerEvent.setSystemTriggerOn(new Date((Calendar.getInstance().getTimeInMillis()) + (LOGIN_TIMEOUT)));
            timerEventDao.createUpdate(timerEvent);
            userMaster.setNoOfAttempts(userMaster.getNoOfAttempts() + 1);
        } else if (Boolean.TRUE.equals(isPasswordValid)) {
            UserLoginDetailMaster loginDetailMaster = new UserLoginDetailMaster();
            loginDetailMaster.setUserId(userMaster.getId());
            loginDetailMaster.setNoOfAttempts(userMaster.getNoOfAttempts());
            loginDetailMaster.setLoggingFromWeb(true);
            userLoginDetailMasterDao.create(loginDetailMaster);
            userMaster.setNoOfAttempts(0);
        } else if ((userMaster.getNoOfAttempts() == null || userMaster.getNoOfAttempts() == 0) && Boolean.FALSE.equals(isPasswordValid)) {
            userMaster.setNoOfAttempts(1);
        } else {
            userMaster.setNoOfAttempts(userMaster.getNoOfAttempts() + 1);
        }
        userDao.merge(userMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activateAccount(Integer id) {
        UserMaster userMaster = userDao.retrieveById(id);
        userMaster.setNoOfAttempts(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggedInUserPrincipleDto revalidateUserToken(String[] userPassMap) {
        String username = userPassMap[0];
        UserMaster userObj = userDao.retrieveByUserName(username);
        LoggedInUserPrincipleDto loggedInUserPrincipleDto = new LoggedInUserPrincipleDto();
        if (userObj != null && userObj.getState().toString().equals(UserMaster.Fields.ACTIVE)) {
            userTokenService.deactivateAllActiveUserTokens(userObj.getId());
            UserToken userToken = new UserToken();
            String newToken = basicPasswordEncryptor.encryptPassword(userObj.getUserName() + ":" + userObj.getPassword() + new Date().getTime());
            userToken.setUserId(userObj.getId());
            userToken.setUserToken(newToken);
            userToken.setIsActive(true);
            userToken.setIsArchieve(false);
            userToken.setCreatedOn(Calendar.getInstance().getTime());
            userTokenService.createUserToken(userToken);
            loggedInUserPrincipleDto.setNewToken(userToken.getUserToken());
            return loggedInUserPrincipleDto;
        } else {
            loggedInUserPrincipleDto.setNewToken("-1");
            return loggedInUserPrincipleDto;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String[]> getUserIdAndActiveTokenFromToken(List<String> userTokens) {
        Map<String, String[]> responseObject = new HashMap<>();
        for (String aToken : userTokens) {
            String[] objectArray = new String[2];
            UserToken userTokenObject = userTokenService.getUserTokenByUserToken(aToken, true);
            if (userTokenObject != null) {
                UserToken userTokenByUserId = userTokenService.getUserTokenByUserId(userTokenObject.getUserId());
                objectArray[0] = userTokenObject.getUserId().toString();
                objectArray[1] = userTokenByUserId.getUserToken();
                responseObject.put(aToken, objectArray);
            } else {
                objectArray[0] = "-1";
                responseObject.put(aToken, objectArray);
                return null;
            }
        }
        return responseObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserTokenValid(String token) {
        UserToken userTokenObject = userTokenService.getUserTokenByUserToken(token);
        if (userTokenObject == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -6);
        return !userTokenObject.getCreatedOn().before(calendar.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserMaster getUserByValidToken(String token) {
        UserToken userToken = userTokenService.getUserTokenByUserToken(token);
        UserMaster userObj = null;
        if (userToken != null) {
            userObj = userDao.retrieveById(userToken.getUserId());
        }
        return userObj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImtechoResponseEntity validateHealthInfra(Integer toBeRemoved, List<Integer> healthInfraIds) {
        List<HealthInfrastructureDetails> matchingHealthInfras = healthInfrastructureDao.healthInfraExistsUnderLocation(toBeRemoved, healthInfraIds);
        if (matchingHealthInfras != null && !matchingHealthInfras.isEmpty()) {
            List<String> healthInfraNames = matchingHealthInfras.stream().map(HealthInfrastructureDetails::getName)
                    .collect(Collectors.toList());
            String names = String.join(",", healthInfraNames);

            return new ImtechoResponseEntity("The location cannot be removed as health Infrastructure - " + names + " exists under the given location.Please remove the infrastructure to proceed.", 400);
        } else {
            return new ImtechoResponseEntity("Can be removed ", 200);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRandomLoginCode() {
        // generate 4 digit login code
        String randomCode = String.format("%04d", rand.nextInt(9999));
        if (this.retrieveByLoginCode(randomCode) == null) {
            return randomCode;
        } else {
            return getRandomLoginCode();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateLoginCode(Integer userId) {

        UserMaster userMaster = userDao.retrieveById(userId);
        if (userMaster != null && userMaster.getLoginCode() != null) {
            throw new ImtechoUserException("Login code is already Exist", 1);
        }

        String newLoginCode = this.getRandomLoginCode();
        if (userMaster != null) {
            userMaster.setLoginCode(newLoginCode);
            userDao.createOrUpdate(userMaster);

            String mobileNumber;
            mobileNumber = userMaster.getContactNumber();
            String message = "Your login code for State Of Health application is: " + newLoginCode;
            smsService.sendSms(mobileNumber, message, true, "STATE_OF_HEALTH_LOGIN_CODE");
        }
        return newLoginCode;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnonymous() {
        Authentication authencation = SecurityContextHolder.getContext().getAuthentication();
        return !(imtechoSecurityUser != null &&
                (authencation != null && authencation.getPrincipal() != null && !authencation.getPrincipal().equals("anonymousUser")));
    }

    /**
     * Check if password is strong enough otherwise throw expcetion
     *
     * @param password Password to check
     */
    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new ImtechoUserException("Password must contain minimum 8 characters", 0);
        }

    }

    @Override
    public Integer getLoginAttempts() {
        try {
            SystemConfiguration loginAttempts = systemConfigurationService.retrieveSysConfigurationByKey("LOGIN_ATTEMPTS");
            if (loginAttempts == null) {
                SystemConfiguration systemConfiguration = new SystemConfiguration();
                systemConfiguration.setIsActive(Boolean.TRUE);
                systemConfiguration.setSystemKey("LOGIN_ATTEMPTS");
                if (ConstantUtil.IMPLEMENTATION_TYPE.equals(ConstantUtil.TELANGANA_IMPLEMENTATION)) {
                    systemConfiguration.setKeyValue(Integer.toString(5));
                } else {
                    systemConfiguration.setKeyValue(Integer.toString(3));
                }

                systemConfigurationService.createSystemConfiguration(systemConfiguration);

                return Integer.parseInt(systemConfigurationService.retrieveSysConfigurationByKey("LOGIN_ATTEMPTS").getKeyValue());
            }
            return Integer.parseInt(loginAttempts.getKeyValue());
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.CONFIG, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Integer getLoginTimeout() {
        try {
            SystemConfiguration loginTimeout = systemConfigurationService.retrieveSysConfigurationByKey("LOGIN_TIMEOUT");
            if (loginTimeout == null) {
                SystemConfiguration systemConfiguration = new SystemConfiguration();
                systemConfiguration.setIsActive(Boolean.TRUE);
                systemConfiguration.setSystemKey("LOGIN_TIMEOUT");
                if (ConstantUtil.IMPLEMENTATION_TYPE.equals(ConstantUtil.TELANGANA_IMPLEMENTATION)) {
                    systemConfiguration.setKeyValue(Integer.toString(5 * 60000));
                } else {
                    systemConfiguration.setKeyValue(Integer.toString(15 * 60000));
                }

                systemConfigurationService.createSystemConfiguration(systemConfiguration);

                return Integer.parseInt(systemConfigurationService.retrieveSysConfigurationByKey("LOGIN_TIMEOUT").getKeyValue());
            }
            return Integer.parseInt(loginTimeout.getKeyValue());
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.CONFIG, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public void updateLanguagePreference(String token, String languageCode) {
        UserToken userToken = userTokenService.getUserTokenByUserToken(token);
        if (userToken != null) {
            UserMaster userMaster = userDao.retrieveById(userToken.getUserId());
            userMaster.setPrefferedLanguage(languageCode);
            userDao.update(userMaster);
        } else {
            throw new ImtechoUserException("Unauthorized", 403);
        }
    }

    @Override
    public void addFirebaseToken(Integer id, String firebaseToken) {
        boolean isAdded = firebaseTokenDao.isAddedFirebaseToken(id, firebaseToken);
        if (!isAdded) {
            firebaseTokenDao.create(new FirebaseTokenEntity(id, firebaseToken));
        }
    }

}
