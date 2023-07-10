package com.argusoft.medplat.training.service.impl;

import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.service.UserService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.course.dao.CourseMasterDao;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.dto.TopicMasterDto;
import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.course.service.CourseMasterService;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.training.dao.TopicCoverageDao;
import com.argusoft.medplat.training.dao.TrainingDao;
import com.argusoft.medplat.training.dto.*;
import com.argusoft.medplat.training.mapper.TopicCoverageMapper;
import com.argusoft.medplat.training.mapper.TrainingMapper;
import com.argusoft.medplat.training.model.Attendance;
import com.argusoft.medplat.training.model.Certificate;
import com.argusoft.medplat.training.model.TopicCoverage;
import com.argusoft.medplat.training.model.Training;
import com.argusoft.medplat.training.service.AttendanceService;
import com.argusoft.medplat.training.service.CertificateService;
import com.argusoft.medplat.training.service.TopicCoverageService;
import com.argusoft.medplat.training.service.TrainingService;
import com.argusoft.medplat.training.util.TrainingUtil;
import com.argusoft.medplat.training.util.ValidationUtils;
import com.argusoft.medplat.training.validation.ValidationResult;
import com.argusoft.medplat.training.validation.impl.ValidationResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.argusoft.medplat.training.util.TrainingUtil.CREATE_VALIDATION;
import static com.argusoft.medplat.training.util.TrainingUtil.UPDATE_VALIDATION;

/**
 * <p>
 * Define services for training.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
@Service("trainingService")
@Transactional
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    TrainingDao trainingDao;

    @Autowired
    CourseMasterService courseMasterService;

    @Autowired
    CourseMasterDao courseMasterDao;

    @Autowired
    UserService userService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    TopicCoverageService topicCoverageService;

    @Autowired
    TopicCoverageDao topicCoverageDao;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    TrainingMapper trainingMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTraining(TrainingScheduleDto trainingScheduleDto) {
        List<ValidationResultInfo> vris;
        try {
            vris = this.validateTraining(CREATE_VALIDATION, trainingScheduleDto, null);
        } catch (Exception ex) {
            throw new ImtechoUserException("Unexpected Exception...........", ex);
        }
        if (this.containsErrors(vris)) {
            throw new ImtechoUserException("Error(s) occurred in validate traininig", 0, vris);
        } else {
            try {
                this.calculateTraining(trainingScheduleDto);
            } catch (Exception ex) {
                throw new ImtechoUserException("exception while create training", 0, ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationResultInfo> validateTraining(String validationType,
                                                       TrainingScheduleDto trainingScheduleDto, Date oldDate) {
        List<ValidationResultInfo> errors = new ArrayList<>();
        if (CREATE_VALIDATION.equals(validationType)) {
            try {
                if (this.isTrainingExist4Trainer(trainingScheduleDto, validationType, oldDate)) {
                    errors.add(
                            ValidationUtils.makeValidationResultInfo("Trainer already having another session on selected days",
                                    "effectiveDate",
                                    ValidationResult.ErrorLevel.ERROR));
                }
                if (this.validateInSufficientAttendees(trainingScheduleDto)) {
                    errors.add(
                            ValidationUtils.makeValidationResultInfo("Attendees are not sufficient to schedule a meeting",
                                    "attendeeIds",
                                    ValidationResult.ErrorLevel.ERROR));
                }
            } catch (Exception ex) {
                throw new ImtechoUserException("OperationFailedException due to InvalidParameterException", 0, ex);
            }
        }
        if (UPDATE_VALIDATION.equals(validationType)) {
            try {
                if (this.isTrainingExist4Trainer(trainingScheduleDto, validationType, oldDate)) {
                    errors.add(
                            ValidationUtils.makeValidationResultInfo("Trainer already having another session on selected days",
                                    "effectiveDate",
                                    ValidationResult.ErrorLevel.ERROR));
                }
            } catch (Exception ex) {
                throw new ImtechoUserException("OperationFailedException due to InvalidParameterException", 0, ex);
            }
        }
        return errors;
    }

    /**
     * Check for insufficient attendees.
     *
     * @param trainingScheduleDto Training schedule details.
     * @return Returns true/false based on insufficient attendees.
     */
    private boolean validateInSufficientAttendees(TrainingScheduleDto trainingScheduleDto) {
        List<UserMasterDto> users = userService.getUsersByLocationsAndRoles(new ArrayList<>(trainingScheduleDto.getOrganizationUnits()), new ArrayList<>(trainingScheduleDto.getPrimaryTargetRole()));
        List<UserMasterDto> allAshaBelongsToThisLocation = new ArrayList<>(users);

        Set<Integer> userIds = new HashSet<>();
        for (UserMasterDto userData : allAshaBelongsToThisLocation) {
            userIds.add(userData.getId());
        }

        List<CertificateDto> allCertificates = new ArrayList<>();
        for (Integer courseId : trainingScheduleDto.getCourses()) {
            List<CertificateDto> certificates = certificateService.getCertificatesByCourseAndType(courseId, Certificate.Type.COURSECOMPLETION);
            allCertificates.addAll(certificates);
        }

        Set<Integer> completedTraineeIds = new HashSet<>();
        for (CertificateDto certificate : allCertificates) {
            completedTraineeIds.add(certificate.getUserId());
        }

        Set<Integer> unTrainedUserIds = new HashSet<>();
        for (Integer attendee : userIds) {
            if (!completedTraineeIds.contains(attendee)) {
                unTrainedUserIds.add(attendee);
            }
        }

        return unTrainedUserIds.isEmpty();
    }

    /**
     * Check for is training exist for trainer.
     *
     * @param trainingScheduleDto Training schedule details.
     * @param validationType      Validation type.
     * @param oldDate             Old training date.
     * @return Returns true/false based on training exist for trainer.
     */
    private boolean isTrainingExist4Trainer(TrainingScheduleDto trainingScheduleDto,
                                            String validationType, Date oldDate) {

        //Trainer should be free on selected date for trainingDto
        boolean validateTrainerAgainstTraining;
        //for primary trainer
        for (Integer primaryTrainer : trainingScheduleDto.getPrimaryTrainers()) {
            try {
                Date calculatenewExpirydate = calculateNewCompletionDate(trainingScheduleDto);
                validateTrainerAgainstTraining = this.validateTrainerAgainstTraining(validationType,
                        trainingScheduleDto.getTrainingId(),
                        primaryTrainer,
                        new Date(trainingScheduleDto.getEffectiveDate()),
                        calculatenewExpirydate, oldDate);
                if (validateTrainerAgainstTraining) {
                    return true;
                }
            } catch (Exception ex) {
                throw new ImtechoUserException("Exception inside isTrainingExist4Trainer() ", 0, ex);
            }
        }
        //For optional Trainer
        for (Integer optionalTrainer : trainingScheduleDto.getOptionalTrainers()) {
            try {
                validateTrainerAgainstTraining = this.validateTrainerAgainstTraining(validationType,
                        trainingScheduleDto.getTrainingId(),
                        optionalTrainer,
                        new Date(trainingScheduleDto.getEffectiveDate()),
                        new Date(trainingScheduleDto.getExpirationDate()), oldDate);
                if (validateTrainerAgainstTraining) {
                    return true;
                }
            } catch (Exception ex) {
                throw new ImtechoUserException("Exception inside isTrainingExist4Trainer()", 0, ex);
            }
        }
        return false;
    }

    /**
     * Calculate new completion date if training rescheduled.
     *
     * @param trainingScheduleDto Training schedule details.
     * @return Returns completion date.
     */
    private Date calculateNewCompletionDate(TrainingScheduleDto trainingScheduleDto) {
        List<Integer> courses = new ArrayList<>(trainingScheduleDto.getCourses());
        int totalDays = 0;
        for (Integer courseId : courses) {
            List<TopicMasterDto> topics = courseMasterService.getTopicsByCourse(courseId);
            for (TopicMasterDto topic : topics) {
                if (topic.getTopicDay() > totalDays) {
                    totalDays = (topic.getTopicDay());
                }
            }
        }

        return TrainingUtil.calculateNewDateExcludingSunday(new Date(trainingScheduleDto.getEffectiveDate()), totalDays);

    }

    /**
     * Calculate training like effective date, expiration date etc.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    public void calculateTraining(TrainingScheduleDto trainingScheduleDto) {
        this.calculateTrainingName(trainingScheduleDto);
        this.calculateDescription(trainingScheduleDto);
        this.calculateEffectiveDate(trainingScheduleDto);
        this.calculateExpirationDate(trainingScheduleDto);
        this.calculateAdditionalAttendees(trainingScheduleDto);
        trainingScheduleDto.setTrainingState(Training.State.DRAFT);
        Integer trainingId = trainingDao.create(trainingMapper.dtoToEntityTrainingScheduleDto(trainingScheduleDto));
        Training training = trainingDao.retrieveById(trainingId);

        if (trainingId != null) {
            try {
                //creating Topic Coverage
                this.createTopics4Course(new Training(training));
                this.createAttendanceForTraining(new Training(training));

            } catch (Exception ex) {
                throw new ImtechoUserException("Opratinon Failed Exception", 0, ex);
            }

        }

    }

    /**
     * Calculate expiration date for training.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    private void calculateExpirationDate(TrainingScheduleDto trainingScheduleDto) {

        List<TopicMasterDto> topics = new ArrayList<>();
        for (Integer courseId : trainingScheduleDto.getCourses()) {
            topics = courseMasterService.getTopicsByCourse(courseId);
        }
        Optional<TopicMasterDto> topicMasterDto = topics.stream().max(Comparator.comparingInt(TopicMasterDto::getTopicDay));
        int noOfDays = 0;
        if (topicMasterDto.isPresent()) {
            noOfDays = topicMasterDto.get().getTopicDay();
        }

        Date expirationDate = TrainingUtil.calculateNewDateExcludingSunday(
                new Date(trainingScheduleDto.getEffectiveDate()),
                noOfDays);

        trainingScheduleDto.setExpirationDate(TrainingUtil.prepareDate(expirationDate, 23, 59, 59, 0).getTime());
    }

    /**
     * Set training description.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    private void calculateDescription(TrainingScheduleDto trainingScheduleDto) {
        if (trainingScheduleDto.getTrainingDescription() == null
                || trainingScheduleDto.getTrainingDescription().isEmpty()) {
            trainingScheduleDto.setTrainingDescription(trainingScheduleDto.getTrainingName());
        }
    }

    /**
     * Set training name.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    private void calculateTrainingName(TrainingScheduleDto trainingScheduleDto) {
        List<CourseMasterDto> courses = courseMasterService.getCoursesByIds(new HashSet<>(trainingScheduleDto.getCourses()));
        StringBuilder trainingName = new StringBuilder("Training for ");
        int counter = 0;
        for (CourseMasterDto course : courses) {
            counter++;
            trainingName.append(course.getCourseName());
            if (counter == courses.size()) {
                break;
            }
            trainingName.append(" and ");
        }
        trainingScheduleDto.setTrainingName(trainingName.toString());
    }

    /**
     * Set effective date.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    private void calculateEffectiveDate(TrainingScheduleDto trainingScheduleDto) {
        try {
            Date effectiveDateAfterTimeTruncate = TrainingUtil.prepareDate(new Date(trainingScheduleDto.getEffectiveDate()), 0, 0, 0, 0);
            trainingScheduleDto.setEffectiveDate(effectiveDateAfterTimeTruncate.getTime());
        } catch (Exception ex) {
            throw new ImtechoUserException("Invalid Paramer Exception", 0, ex);
        }
    }

    /**
     * Set additional attendees.
     *
     * @param trainingScheduleDto Training schedule details.
     */
    private void calculateAdditionalAttendees(TrainingScheduleDto trainingScheduleDto) {

        Set<Integer> additionalAttendeeIds = trainingScheduleDto.getAdditionalAttendees();
        if (!additionalAttendeeIds.isEmpty()) {
            List<CertificateDto> allCertificates = new ArrayList<>();
            for (Integer courseId : trainingScheduleDto.getCourses()) {
                List<CertificateDto> certificates = certificateService.getCertificatesByCourseAndType(courseId, Certificate.Type.COURSECOMPLETION);
                allCertificates.addAll(certificates);
            }
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
            trainingScheduleDto.setAdditionalAttendees(unTrainedAdditionalUserIds);
        }
    }

    /**
     * Check for errors exist or not.
     *
     * @param errors List of validation error.
     * @return Returns tru/false based on error exist or not.
     */
    private boolean containsErrors(List<ValidationResultInfo> errors) {
        for (ValidationResultInfo info : errors) {
            if (info.isError()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate trainer against training.
     *
     * @param validationType            Validation type.
     * @param trainingId                Training id.
     * @param trainerId                 Trainer id.
     * @param newTrainingStartDate      New training start date.
     * @param newTrainingCompletionDate New training completion date.
     * @param oldDate                   Old training date.
     * @return Returns true/false based on trainer against training.
     */
    private boolean validateTrainerAgainstTraining(String validationType,
                                                   Integer trainingId,
                                                   Integer trainerId,
                                                   Date newTrainingStartDate,
                                                   Date newTrainingCompletionDate, Date oldDate) {
        List<TrainingDto> allTrainings = this.getTrainingsByTrainer(trainerId, Boolean.TRUE);
        List<TrainingDto> existingTrainings = setExistingTrainings(allTrainings);
        if (existingTrainings.isEmpty()) {
            return false;
        }
        //these are the trainingIds of all the existing training of the existing training dtos
        List<Integer> traningIds = existingTrainings.stream().map(TrainingDto::getTrainingId).collect(Collectors.toList());
        //Busy days for the training
        List<Date> busyDays = topicCoverageService.getDistinctDatesOfTopicCoveragesByTrainingIds(traningIds);


        if (null != validationType) {
            switch (validationType) {
                case CREATE_VALIDATION:
                    Date temp = newTrainingStartDate;
                    long count = busyDays.stream().filter(d -> d.compareTo(temp) >= 0).
                            filter(d -> d.compareTo(newTrainingCompletionDate) <= 0).count();
                    if (count > 0)
                        return true;
                    break;
                case UPDATE_VALIDATION:
                    //these are the training days greater than equal to new Date
                    List<Date> trainingDays = topicCoverageService.getTopicCoveragesByTrainingIdAndDate(trainingId, oldDate).stream()
                            .map(TopicCoverageDto::getEffectiveDate).collect(Collectors.toList());
                    List<Date> updatedDates = new ArrayList<>(Arrays.asList(newTrainingStartDate));
                    Collections.sort(trainingDays);
                    for (Date effectiveDate : trainingDays) {
                        Date temp1 = newTrainingStartDate;
                        if (trainingDays.stream().filter(d -> d.compareTo(effectiveDate) > 0).noneMatch(d -> d.compareTo(temp1) < 0)) {
                            break;
                        }
                        newTrainingStartDate = TrainingUtil.calculateNewDateExcludingSunday(newTrainingStartDate, 2);
                        updatedDates.add(newTrainingStartDate);
                    }

                    return busyDays.stream().anyMatch(updatedDates::contains);
                default:
            }
        }

        return false;

    }

    /**
     * Set existing trainings.
     *
     * @param allTrainings List of trainings.
     * @return Returns list of trainings.
     */
    private List<TrainingDto> setExistingTrainings(List<TrainingDto> allTrainings) {
        List<TrainingDto> existingTrainings = new ArrayList<>();
        //filter training which is completed , submitted and archived
        for (TrainingDto trainingDto : allTrainings) {
            if (((Training.State.SAVED).equals(trainingDto.getTrainingState()))
                    || (Training.State.DRAFT.equals(trainingDto.getTrainingState())) || (Training.State.SUBMITTED.equals(trainingDto.getTrainingState()))) {
                existingTrainings.add(trainingDto);
            }
        }
        return existingTrainings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingDto> getTrainingsByTrainer(Integer trainerId, boolean fetchOptionalTrainerTrainings) {
        return trainingMapper.entityToTrainingDtoList(trainingDao.getTrainingsByTrainer(trainerId, fetchOptionalTrainerTrainings));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingDto> searchForTraining(Integer trainerId, Long onDateInLong, Long afterDateInLong, Long beforeDateInLong, boolean fetchOptionalTrainerTrainings) {
        Date onDate = null;
        Date afterDate = null;
        Date beforeDate = null;

        if (onDateInLong != null) {
            onDate = new Date(onDateInLong);
        }
        if (afterDateInLong != null) {
            afterDate = new Date(afterDateInLong);
        }
        if (beforeDateInLong != null) {
            beforeDate = new Date(beforeDateInLong);
        }

        if (trainerId != null && onDate != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getEffectiveTrainingsByTrainerOnDate(trainerId, onDate));
        }
        if (trainerId != null && beforeDate != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getTrainingsByTrainerBeforeDate(trainerId, beforeDate));
        }

        if (trainerId != null && afterDate != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getEffectiveTrainingsByTrainerAfterDate(trainerId, afterDate));
        }

        if (trainerId != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getTrainingsByTrainer(trainerId, false));
        }

        if (afterDate != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getEffectiveTrainingsAfterDate(afterDate));
        }

        if (beforeDate != null) {
            return trainingMapper.entityToTrainingDtoList(trainingDao.getTrainingsBeforeDate(beforeDate));
        }

        return trainingMapper.entityToTrainingDtoList(trainingDao.retrieveAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserMasterDto> searchUsers(Integer locationId,
                                           Integer roleId,
                                           Integer courseId) {
        if (locationId != null && roleId != null && courseId != null) {
            return userService.getUsersByLocationAndRoleAndCourse(locationId, roleId, courseId);
        } else if (locationId != null && roleId != null) {
            List<Integer> locationIds = new LinkedList<>();
            locationIds.add(locationId);
            List<Integer> roleIds = new LinkedList<>();
            roleIds.add(roleId);
            return userService.getUsersByLocationsAndRoles(locationIds, roleIds);
        } else if (roleId != null) {
            return userService.retrieveByCriteria(null, roleId, null, null, null, null, null, null,null);
        }

        return new ArrayList<>();
    }

    /**
     * Add topics for course.
     *
     * @param training Training details.
     */
    private void createTopics4Course(Training training) {

        for (Integer courseId : training.getTrainingCourseIds()) {

            try {
                CourseMaster course = courseMasterDao.retrieveById(courseId);
                List<TopicMasterDto> topics = courseMasterService.getTopicByIds(new ArrayList<>(course.getTopicIds()));
                for (TopicMasterDto topic : topics) {
                    TopicCoverageDto topicCoverageDto = this.populateTopicCoverage(training, courseId, topic);

                    topicCoverageDao.create(TopicCoverageMapper.dtoToEntityTopicCoverage(topicCoverageDto, null));
                }
            } catch (Exception ex) {
                throw new ImtechoUserException("Course not found with id:" + courseId, 0, ex);
            }
        }
    }

    /**
     * Calculate topic coverage for particular training.
     *
     * @param training Training details.
     * @param courseId Course id.
     * @param topic    Topic details.
     * @return Returns topic coverage details.
     */
    private TopicCoverageDto populateTopicCoverage(Training training,
                                                   Integer courseId,
                                                   TopicMasterDto topic) {
        TopicCoverageDto topicCoverageDto = new TopicCoverageDto();
        topicCoverageDto.setTopicId(topic.getTopicId());
        topicCoverageDto.setCourseId(courseId);
        topicCoverageDto.setTrainingId(training.getTrainingId());
        topicCoverageDto.setTopicCoverageName(topic.getTopicName());
        topicCoverageDto.setTopicCoverageDescription(topic.getTopicDescription());
        Date newDate = TrainingUtil.calculateNewDateExcludingSunday(training.getTrainingEffectiveDate(), topic.getTopicDay());
        topicCoverageDto.setEffectiveDate(TrainingUtil.prepareDate(newDate, 0, 0, 0, 0));
        topicCoverageDto.setExpirationDate(TrainingUtil.prepareDate(newDate, 23, 59, 59, 0));
        topicCoverageDto.setCompletedOn(TrainingUtil.prepareDate(newDate, 0, 0, 0, 0));
        topicCoverageDto.setTopicCoverageState(TopicCoverage.State.PENDING);
        return topicCoverageDto;
    }

    /**
     * Add attendance for training.
     *
     * @param training Training details.
     */
    private void createAttendanceForTraining(Training training) {

        Set<Integer> userIds = new HashSet<>();
        List<Integer> attendeeIds = new ArrayList<>(training.getTrainingAttendeeIds());
        userIds.addAll(attendeeIds);
        userIds.addAll(training.getTrainingAdditionalAttendeeIds());
        //fetching topics by training
        List<TopicCoverageDto> topics
                = TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoverageByTrainingId(training.getTrainingId()));
        //grouping topics by Date
        Map<Date, List<TopicCoverageDto>> dateAndTopicMap = new LinkedHashMap<>();
        for (TopicCoverageDto info : topics) {
            if (dateAndTopicMap.get(info.getEffectiveDate()) != null) {
                dateAndTopicMap.get(info.getEffectiveDate()).add(info);
            } else {
                ArrayList<TopicCoverageDto> topicCoverages = new ArrayList<>();
                topicCoverages.add(info);
                dateAndTopicMap.put(info.getEffectiveDate(), topicCoverages);
            }
        }
        for (Integer attendeeId : userIds) {
            //preparing attendance object
            this.populateAttendance(attendeeId, training, dateAndTopicMap);
        }

    }

    /**
     * Populate attendance according to training.
     *
     * @param attendeeId      Attendance id.
     * @param training        Training details.
     * @param dateAndTopicMap Map of date and topic.
     */
    private void populateAttendance(Integer attendeeId, Training training, Map<Date, List<TopicCoverageDto>> dateAndTopicMap) {
        String name = userService.retrieveFullNameById(attendeeId);
        //creating attendance for no of days training scheduled
        for (Map.Entry<Date, List<TopicCoverageDto>> entrySet : dateAndTopicMap.entrySet()) {
            TopicCoverageDto topic = entrySet.getValue().get(0);
            AttendanceDto attendance = new AttendanceDto();
            attendance.setUserId(attendeeId);
            attendance.setTrainingId(training.getTrainingId());
            attendance.setPresent(Boolean.TRUE);
            attendance.setEffectiveDate(TrainingUtil.prepareDate(topic.getEffectiveDate(), 0, 0, 0, 0));
            attendance.setExpirationDate(TrainingUtil.prepareDate(topic.getExpirationDate(), 23, 59, 59, 0));
            attendance.setCompletedOn(TrainingUtil.prepareDate(topic.getEffectiveDate(), 0, 0, 0, 0));
            attendance.setType(Attendance.Type.TRAINEE);
            attendance.setState(Attendance.State.ACTIVE);
            attendance.setName(name);
            attendanceService.createAttendance(attendance);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingDto> getTrainingsOfUserLocation(Long afterDateInLong, Long currentDateInLong, String limit, String offset) {
        Date afterDate = null;
        Date currentDate = null;

        if (afterDateInLong != null) {
            afterDate = new Date(afterDateInLong);
        }
        if (currentDateInLong != null) {
            currentDate = new Date(currentDateInLong);
        }

        Integer userId = (user.getId());
        List<Integer> locationIds = trainingDao.getTrainingIdsofUserChildLocationsAndUsersTraining(userId);
        List<Training> result = new ArrayList<>();
        for (Training entity : trainingDao.getTrainingsByTrainingIdsAndDate(locationIds, afterDate, currentDate, limit, offset)) {
            result.add(new Training(entity));
        }

        return trainingMapper.entityToTrainingDtoList(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rescheduleTraining(Integer trainingId, Long oldDateInLong, Long newDateInLong, Boolean isFirst) {
        Date oldDate = null;
        Date newDate = null;

        if (oldDateInLong != null) {
            oldDate = new Date(oldDateInLong);
        }

        if (newDateInLong != null) {
            newDate = new Date(newDateInLong);
        }

        TrainingScheduleDto training = trainingMapper.entityToTrainingScheduleDto(trainingDao.retrieveById(trainingId));
        if (training == null) {
            throw new ImtechoUserException(trainingId + " does not exist", 0);
        }

        List<ValidationResultInfo> vris;
        try {
            vris = this.validateTrainingForReschedule(UPDATE_VALIDATION, trainingId, newDate, oldDate);
        } catch (Exception ex) {
            throw new ImtechoUserException("Unexpected Exception", 0, ex);
        }

        if (this.containsErrors(vris)) {
            throw new ImtechoUserException("Error(s) occurred validating", 0, vris);
        }

        try {
            Date preparedOldDate = TrainingUtil.prepareDate(oldDate, 0, 0, 0, 0);
            //          returns topics in Ascending order
            List<TopicCoverageDto> topicCoverages = topicCoverageService.getTopicCoveragesByTrainingIdAndDate(trainingId, preparedOldDate);

            if (!topicCoverages.isEmpty()) {

                Map<Date, List<TopicCoverageDto>> dateAndTopicMap;
                try {
                    dateAndTopicMap = topicCoverageService.rescheduleTopicCoverages(topicCoverages, newDate);
                } catch (Exception ex) {
                    throw new ImtechoUserException("exception in reshcedule Topic Coverages", 0, ex);
                }
                try {
                    this.rescheduleTrainingStartAndEndDate(trainingId, newDate, dateAndTopicMap.size(), isFirst);
                } catch (Exception ex) {
                    throw new ImtechoUserException("exception in rescheduleTrainingStartAndEndDate", 0, ex);
                }

                try {
                    this.rescheduleAttendance(trainingId, newDate, topicCoverages);
                } catch (Exception ex) {
                    throw new ImtechoUserException("exception in rescheduleAttendance", 0, ex);
                }
            }
        } catch (Exception e) {
            throw new ImtechoUserException("Error while reschelue training", 0, e);
        }

    }

    /**
     * Validate training for reschedule.
     *
     * @param validationType Validation type.
     * @param trainingId     Training id.
     * @param newDate        New training date.
     * @param oldDate        Old training date.
     * @return Returns validate result.
     */
    public List<ValidationResultInfo> validateTrainingForReschedule(String validationType, Integer trainingId, Date newDate, Date oldDate) {

        TrainingScheduleDto trainingDto = trainingMapper.entityToTrainingScheduleDto(trainingDao.retrieveById(trainingId));
        TrainingScheduleDto newTrainigInfo = new TrainingScheduleDto(trainingDto);
        newTrainigInfo.setEffectiveDate(TrainingUtil.prepareDate(newDate, 0, 0, 0, 0).getTime());
        Date calculateNewCompletionDate = this.calculateNewCompletionDate(trainingDto);
        newTrainigInfo.setExpirationDate(TrainingUtil.prepareDate(calculateNewCompletionDate, 23, 59, 59, 0).getTime());

        return this.validateTraining(validationType, newTrainigInfo, oldDate);
    }

    /**
     * Reschedule training according to new date.
     *
     * @param trainingId Training id.
     * @param newDate    New training date.
     * @param noOfDays   No of days for training.
     * @param isFirst    Is first time to reschedule training.
     */
    private void rescheduleTrainingStartAndEndDate(Integer trainingId, Date newDate, int noOfDays, Boolean isFirst) {

        //            calculating start date as per reschedule training
        this.calculateNewStartDate4Training(trainingId, newDate, isFirst);
//            calculating new end as per reschedule training
        if (noOfDays > 0) {

            this.calculateNewCompletionDate4Training(trainingId);
        }

    }

    /**
     * Reschedule attendance according to new date.
     *
     * @param trainingId     Training id.
     * @param newDate        New training date.
     * @param topicCoverages List of topic need to coverage.
     */
    private void rescheduleAttendance(Integer trainingId,
                                      Date newDate,
                                      List<TopicCoverageDto> topicCoverages) {
        Map<Date, List<TopicCoverageDto>> dateAndTopicMap = new LinkedHashMap<>();
        for (TopicCoverageDto info : topicCoverages) {
            if (dateAndTopicMap.get(info.getEffectiveDate()) != null) {
                dateAndTopicMap.get(info.getEffectiveDate()).add(info);
            } else {
                ArrayList<TopicCoverageDto> topics = new ArrayList<>();
                topics.add(info);
                dateAndTopicMap.put(info.getEffectiveDate(), topics);
            }
        }

        Set<Integer> userIds = new HashSet<>();
        //Archiving all the attendances which was modified
        for (Map.Entry<Date, List<TopicCoverageDto>> entrySet : dateAndTopicMap.entrySet()) {
            TopicCoverageDto topic = entrySet.getValue().get(0);
            List<AttendanceDto> attendances
                    = attendanceService.getAttendanceByTrainingForDate(trainingId, TrainingUtil.prepareDate(topic.getEffectiveDate(), 0, 0, 0, 0));
            for (AttendanceDto attendance : attendances) {
                userIds.add(attendance.getUserId());
                attendanceService.deleteAttendance(attendance.getId());
            }
        }
        //Creating new
        List<TopicCoverageDto> topicCoveragesByTrainingIdAndDate
                = TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingIdAndDate(trainingId, TrainingUtil.prepareDate(newDate, 0, 0, 0, 0)));
        Map<Date, List<TopicCoverageDto>> newDateAndTopicMap = new LinkedHashMap<>();
        for (TopicCoverageDto topicCoverage : topicCoveragesByTrainingIdAndDate) {
            if (newDateAndTopicMap.get(topicCoverage.getEffectiveDate()) != null) {
                newDateAndTopicMap.get(topicCoverage.getEffectiveDate()).add(topicCoverage);
            } else {
                ArrayList<TopicCoverageDto> topics = new ArrayList<>();
                topics.add(topicCoverage);
                newDateAndTopicMap.put(topicCoverage.getEffectiveDate(), topics);
            }
        }
        for (Integer userId : userIds) {
            for (Map.Entry<Date, List<TopicCoverageDto>> entrySet : newDateAndTopicMap.entrySet()) {
                TopicCoverageDto topic = entrySet.getValue().get(0);
                AttendanceDto attendance = new AttendanceDto();
                attendance.setUserId(userId);
                attendance.setTrainingId(trainingId);
                attendance.setPresent(Boolean.TRUE);
                attendance.setEffectiveDate(TrainingUtil.prepareDate(topic.getEffectiveDate(), 0, 0, 0, 0));
                attendance.setExpirationDate(TrainingUtil.prepareDate(topic.getExpirationDate(), 23, 59, 59, 0));
                attendance.setCompletedOn(TrainingUtil.prepareDate(topic.getEffectiveDate(), 0, 0, 0, 0));
                attendance.setType(Attendance.Type.TRAINEE);
                attendance.setState(Attendance.State.ACTIVE);
                attendance.setName(userService.retrieveById(userId).getFullName());
                attendanceService.createAttendance(attendance);
            }
        }
    }

    /**
     * Calculate new start state for training.
     *
     * @param trainingId Training id.
     * @param newDate    New start state.
     * @param isFirst    Is first time to reschedule training.
     */
    private void calculateNewStartDate4Training(Integer trainingId,
                                                Date newDate,
                                                Boolean isFirst) {
        if (isFirst != null && isFirst) {
            Training training = trainingDao.retrieveById(trainingId);
            training.setTrainingEffectiveDate(TrainingUtil.prepareDate(newDate, 0, 0, 0, 0));
            trainingDao.update(training);
        }
    }

    /**
     * Calculate new completion date for training.
     *
     * @param trainingId Training id.
     */
    private void calculateNewCompletionDate4Training(Integer trainingId) {
        TopicCoverageDto endDate = Collections.max(topicCoverageService.getTopicCoverageByTrainingId(trainingId), Comparator.comparing(TopicCoverageDto::getEffectiveDate));
        Training training = trainingDao.retrieveById(trainingId);
        training.setTrainingExpirationDate(new Date(TrainingUtil.prepareDate(endDate.getExpirationDate(), 23, 59, 59, 0).getTime()));
        trainingDao.update(training);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTraineeStatus(TrainingStatus updateTraineeStatus) {
        //fetching current state of traninig
        boolean isPending = this.retrieveCurrentTrainingState(updateTraineeStatus.getTrainingId());
        //fetching certificate as a request from ui
        List<TraineeCertificate> certificates = updateTraineeStatus.getTraineeCertificates();
        Training training = trainingDao.retrieveById(updateTraineeStatus.getTrainingId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(training.getTrainingExpirationDate());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        if (new Date().before(calendar.getTime())) {
            throw new ImtechoUserException("Cannot mark completion before 3pm on last day of training", 500);
        }
        for (TraineeCertificate certificate : certificates) {
            //populate Certificate Dto and create entry
            CertificateDto info = this.populateCertificate(certificate, updateTraineeStatus.getTrainingId(), training);
            if (info.getCertificateId() == null) {
                certificateService.createCertificate(info);
            } else {
                certificateService.updateCertificate(info);
            }
        }

        if (Boolean.FALSE.equals(updateTraineeStatus.getIsSubmit()) && isPending) {
            training.setTrainingState(Training.State.SAVED);
            trainingDao.update(training);
        }

        //updating trainee state to submitted 
        if (Boolean.TRUE.equals(updateTraineeStatus.getIsSubmit())) {
            training.setTrainingState(Training.State.SUBMITTED);
            trainingDao.update(training);
        }
    }

    /**
     * Retrieves current state of training.
     *
     * @param traininigId Training id.
     * @return Returns true/false based on current state is DRAFT or not.
     */
    private boolean retrieveCurrentTrainingState(Integer traininigId) {
        Training training = new Training(trainingDao.retrieveById(traininigId));
        return ((Training.State.DRAFT).equals(training.getTrainingState()));
    }

    /**
     * Populate certificates to attendees.
     *
     * @param certificate List of certificates.
     * @param trainingId  Training id.
     * @param training    Training details.
     * @return Returns list of certificates.
     */
    private CertificateDto populateCertificate(TraineeCertificate certificate, Integer trainingId, Training training) {
        CertificateDto info = new CertificateDto();
        info.setTrainingId(trainingId);

        info.setCertificateName(userService.retrieveFullNameById(certificate.getUserId()));

        if (training.getTrainingCourseIds().isEmpty()) {
            throw new ImtechoUserException("Course Ids should not be empty", 0);
        }

        List<Integer> courses = new ArrayList<>(training.getTrainingCourseIds());
        info.setCourseId(courses.get(0));
        info.setUserId(certificate.getUserId());
        info.setCertificateRemarks(certificate.getRemark());
        info.setCertificationDate(TrainingUtil.prepareDate(new Date(), 0, 0, 0, 0));
        if (Boolean.TRUE.equals(certificate.getTrained())) {
            info.setGradeType(Certificate.GradType.TRAINED);
            info.setCertificateType(Certificate.Type.COURSECOMPLETION);
        } else {
            info.setGradeType(Certificate.GradType.FAILED);
            info.setCertificateType(Certificate.Type.COURSECOMPLETION_FAILED);
        }
        info.setCertificateState(Certificate.State.ACTIVE);
        //this is needed only for update
        if (certificate.getCertificateId() != null) {
            info.setCertificateId(certificate.getCertificateId());
        }
        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingStatus> getTrainingStatusesByTrainerAndDate(Integer trainingId, Integer trainerId, Long beforeDateInLong) {
        Date beforeDate = null;

        if (beforeDateInLong != null) {
            beforeDate = new Date(beforeDateInLong);
        }
        List<TrainingStatus> statuses = new ArrayList<>();

        if (trainerId != null && beforeDate != null) {
            Set<Integer> trainingIds = new HashSet<>();
            Set<Integer> courseIds = new HashSet<>();
            List<Training> existingTrainingsForPrimaryTrainer = trainingDao.getTrainingsByTrainer(trainerId, Boolean.FALSE);
            Map<Integer, Training> trainingMap = new HashMap<>();
            //Start new code
            for (Training training : existingTrainingsForPrimaryTrainer) {
                if (!((Training.State.ARCHIVED).equals(training.getTrainingState()))) {
                    trainingIds.add(training.getTrainingId());
                    if (!training.getTrainingCourseIds().isEmpty()) {
                        List<Integer> courses = new ArrayList<>(training.getTrainingCourseIds());
                        courseIds.add(courses.get(0));
                    }
                    trainingMap.putIfAbsent(training.getTrainingId(), training);
                }
            }

            Map<Integer, List<AttendanceDto>> attendanceMapByTraniningId = this.prepareMapForAttendanceWithTranining(new ArrayList<>(trainingIds));
            Map<Integer, List<TopicCoverageDto>> mapTopicCoveragesByTrainingId = this.prepareMapForTopicCoverageWithTranining(new ArrayList<>(trainingIds));
            Map<String, List<CertificateDto>> mapCertificatesByTrainingId = this.prepareMapForCertificateWithTranining(new ArrayList<>(trainingIds), new ArrayList<>(courseIds));

            for (Training training : existingTrainingsForPrimaryTrainer) {
                if (!((Training.State.ARCHIVED).equals(training.getTrainingState()))) {
                    if (this.checkAllTopicsSubmittedForTraining(training.getTrainingId(), mapTopicCoveragesByTrainingId)) {
                        String locationName = training.getTrainingLocationName();
                        List<AttendanceDto> attendances = attendanceMapByTraniningId.get(training.getTrainingId());
                        Map<Integer, AttendanceDto> attendanceMap = new HashMap<>();
                        //No of unique entries for training
                        for (AttendanceDto attendance : attendances) {
                            attendanceMap.putIfAbsent(attendance.getUserId(), attendance);
                        }
                        //user wise attendance entries
                        Map<Integer, List<AttendanceDto>> userAttendanceMap = new HashMap<>();
                        for (AttendanceDto attendance : attendances) {

                            if (userAttendanceMap.get(attendance.getUserId()) != null) {
                                userAttendanceMap.get(attendance.getUserId()).add(attendance);
                            } else {
                                List<AttendanceDto> infos = new ArrayList<>();
                                infos.add(attendance);
                                userAttendanceMap.put(attendance.getUserId(), infos);
                            }
                        }
                        //verifying attendee has fully attended training or not
                        int attendeeCounter = 0;
                        for (AttendanceDto attendance : attendanceMap.values()) {
                            List<AttendanceDto> infos = userAttendanceMap.get(attendance.getUserId());
                            boolean allDayPresent = true;
                            for (AttendanceDto info : infos) {
                                if (!info.isIsPresent()) {
                                    allDayPresent = false;
                                    break;
                                }
                            }
                            //attended Training
                            if (allDayPresent) {
                                attendeeCounter++;
                            }
                        }
                        int completedCount = this.countOfAttendeesWhoCompletedTraining(training, mapCertificatesByTrainingId);
                        TrainingStatus trainingStatus
                                = this.populateTrainingStatus(training.getTrainingId(), training.getTrainingEffectiveDate(), locationName, attendeeCounter, completedCount, attendanceMap.values().size(), trainingMap);
                        statuses.add(trainingStatus);
                    }
                }
            }
//End new code

            statuses.sort(trainingStatusComparator);
            return statuses;
        }

        if (trainingId != null) {
            List<AttendanceDto> attendances = attendanceService.getAttendancesByTraining(trainingId);
            Map<Integer, Integer> userAttendedMap = new HashMap<>();
            for (AttendanceDto attendance : attendances) {
                if (attendance.isIsPresent()) {
                    if (userAttendedMap.get(attendance.getUserId()) == null) {
                        userAttendedMap.put(attendance.getUserId(), 1);
                    } else {
                        Integer counterIncrease = userAttendedMap.get(attendance.getUserId());
                        counterIncrease += 1;
                        userAttendedMap.put(attendance.getUserId(), counterIncrease);
                    }
                } else {
                    if (userAttendedMap.get(attendance.getUserId()) == null) {
                        userAttendedMap.put(attendance.getUserId(), 0);
                    } else {
                        Integer counterIncrease = userAttendedMap.get(attendance.getUserId());
                        counterIncrease += 0;
                        userAttendedMap.put(attendance.getUserId(), counterIncrease);
                    }
                }
            }
            List<TraineeCertificate> traineeCertificates = new LinkedList<>();
            if (userAttendedMap.size() > 0) {
                traineeCertificates = this.prepareTraneeCertificate(trainingId, userAttendedMap);
            }
            TrainingStatus status = new TrainingStatus();
            //setting the training effective date to response
            status.setEffectiveDate(trainingDao.retrieveById(trainingId).getTrainingEffectiveDate());
            if (!traineeCertificates.isEmpty()) {
                status.setTraineeCertificates(new ArrayList<>(traineeCertificates));
            }
            statuses.add(status);
            return statuses;
        }
        return statuses;
    }

    /**
     * Map for attendance with training.
     *
     * @param trainingIds List of training ids.
     * @return Returns map for attendance with training id as key.
     */
    private Map<Integer, List<AttendanceDto>> prepareMapForAttendanceWithTranining(List<Integer> trainingIds) {
        Map<Integer, List<AttendanceDto>> map = new HashMap<>();
        List<AttendanceDto> attendances = attendanceService.getAttendancesByTrainingIds(trainingIds);
        for (AttendanceDto attendance : attendances) {
            if (map.get(attendance.getTrainingId()) != null) {
                map.get(attendance.getTrainingId()).add(attendance);
            } else {
                List<AttendanceDto> attendanceInfos = new ArrayList<>();
                attendanceInfos.add(attendance);
                map.put(attendance.getTrainingId(), attendanceInfos);
            }
        }
        return map;
    }

    /**
     * Map for topic coverage with training.
     *
     * @param trainingIds List of training ids.
     * @return Returns map for topic coverage with training id as key.
     */
    private Map<Integer, List<TopicCoverageDto>> prepareMapForTopicCoverageWithTranining(List<Integer> trainingIds) {
        Map<Integer, List<TopicCoverageDto>> map = new HashMap<>();
        List<TopicCoverageDto> topicCoverages = topicCoverageService.getTopicCoveragesByTrainingIds(trainingIds);
        for (TopicCoverageDto topicCoverage : topicCoverages) {
            if (map.get(topicCoverage.getTrainingId()) != null) {
                map.get(topicCoverage.getTrainingId()).add(topicCoverage);
            } else {
                List<TopicCoverageDto> topicCoverageInfos = new ArrayList<>();
                topicCoverageInfos.add(topicCoverage);
                map.put(topicCoverage.getTrainingId(), topicCoverageInfos);
            }
        }
        return map;
    }

    /**
     * Map for certificate with training.
     *
     * @param trainingIds List of training ids.
     * @return Returns map for certificate with training id as key.
     */
    private Map<String, List<CertificateDto>> prepareMapForCertificateWithTranining(List<Integer> trainingIds,
                                                                                    List<Integer> courseIds) {
        Map<String, List<CertificateDto>> map = new HashMap<>();
        for (Integer courseId : courseIds) {
            List<CertificateDto> certificates = certificateService.getCertificatesByTrainingIdsAndCourseAndType(trainingIds, courseId, Certificate.Type.COURSECOMPLETION);
            for (CertificateDto certificate : certificates) {
                if (map.get(certificate.getTrainingId() + "-" + courseId) != null) {
                    map.get(certificate.getTrainingId() + "-" + courseId).add(certificate);
                } else {
                    List<CertificateDto> certificateInfos = new ArrayList<>();
                    certificateInfos.add(certificate);
                    map.put(certificate.getTrainingId() + "-" + courseId, certificateInfos);
                }
            }
        }
        return map;
    }

    /**
     * Check for all topics submitted or not for particular training.
     *
     * @param trainingId                    Training id.
     * @param mapTopicCoveragesByTrainingId Map for topic coverage with training id.
     * @return Returns true/false based on all topics submitted or not.
     */
    private boolean checkAllTopicsSubmittedForTraining(Integer trainingId,
                                                       Map<Integer, List<TopicCoverageDto>> mapTopicCoveragesByTrainingId) {
        List<TopicCoverageDto> topicCoverages = mapTopicCoveragesByTrainingId.get(trainingId);
        for (TopicCoverageDto topicCoverage : topicCoverages) {
            if (!((TopicCoverage.State.SUBMITTED).equals(topicCoverage.getTopicCoverageState()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count of attendees who completed training.
     *
     * @param training Training details.
     * @param map      Map for certificate.
     * @return Returns count of attendees who completed training.
     */
    private int countOfAttendeesWhoCompletedTraining(Training training, Map<String, List<CertificateDto>> map) {
        //attended and completed training
        List<CertificateDto> certificates = new ArrayList<>();
        if ((Training.State.SUBMITTED).equals(training.getTrainingState())) {
            List<Integer> courses = new ArrayList<>(training.getTrainingCourseIds());
            if (map.get(training.getTrainingId() + "-" + courses.get(0)) != null) {
                List<CertificateDto> certifiedAttendees = map.get(training.getTrainingId() + "-" + courses.get(0));
                certificates.addAll(certifiedAttendees);
            }
        }
        return certificates.size();
    }

    /**
     * Populate training status based on effective date, training id.
     *
     * @param trainingId         Training id.
     * @param effectiveDate      Effective date.
     * @param locationName       Location name.
     * @param attendeeCounter    Attendees count.
     * @param completedCounter   Total number of attendees who complete training.
     * @param totalNoOfAttendees Total number of attendees.
     * @param trainingMap        Map for training.
     * @return Returns training status.
     */
    private TrainingStatus populateTrainingStatus(Integer trainingId,
                                                  Date effectiveDate,
                                                  String locationName,
                                                  int attendeeCounter,
                                                  int completedCounter,
                                                  int totalNoOfAttendees,
                                                  Map<Integer, Training> trainingMap) {
        TrainingStatus trainingStatus = new TrainingStatus();
        trainingStatus.setEffectiveDate(effectiveDate);
        trainingStatus.setLocation(locationName);
        trainingStatus.setAttendeeCount(attendeeCounter);
        trainingStatus.setCompletedCount(completedCounter);
        trainingStatus.setTotalNoOfAttendees(totalNoOfAttendees);
        trainingStatus.setTrainingId(trainingId);
        Training training = trainingMap.get(trainingId);
        trainingStatus.setTrainingState(training.getTrainingState());
        return trainingStatus;
    }

    /**
     * Prepare trainee certificate,
     *
     * @param trainingId      Training id.
     * @param userAttendedMap Map for attendance.
     * @return Returns trainee certificate.
     */
    private List<TraineeCertificate> prepareTraneeCertificate(Integer trainingId,
                                                              Map<Integer, Integer> userAttendedMap) {
        List<TraineeCertificate> traineeCertificates = new ArrayList<>();
        Map<Integer, String> prepareUserNameMap = this.prepareUserNameMap(new ArrayList<>(userAttendedMap.keySet()));
        for (Integer userId : userAttendedMap.keySet()) {
            TraineeCertificate certificate = new TraineeCertificate();
            certificate.setDaysAttendedInPercentage(this.calculatePercentage(trainingId, userAttendedMap.get(userId)));
            certificate.setUserName(prepareUserNameMap.get(userId));
            certificate.setRemark(null);
            if (certificate.getDaysAttendedInPercentage() != 100) {
                certificate.setTrained(Boolean.FALSE);

            } else {
                certificate.setTrained(Boolean.TRUE);

            }
            certificate.setUserId(userId);
            List<CertificateDto> certificatesByUser = certificateService.getCertificatesByTrainingAndUser(trainingId, userId);
            if (!certificatesByUser.isEmpty()) {
                CertificateDto info = certificatesByUser.get(0);
                certificate.setCertificateId(info.getCertificateId());
                certificate.setRemark(info.getCertificateRemarks() != null ? info.getCertificateRemarks() : null);
                certificate.setCertificateType(info.getCertificateType().toString());
                if ((Certificate.GradType.TRAINED).equals(info.getGradeType())) {
                    certificate.setTrained(Boolean.TRUE);
                } else {
                    certificate.setTrained(Boolean.FALSE);
                }
            }
            traineeCertificates.add(certificate);
        }
        return traineeCertificates;
    }

    /**
     * Map for user name.
     *
     * @param userIds List of user ids.
     * @return Returns map for user name.
     */
    private Map<Integer, String> prepareUserNameMap(List<Integer> userIds) {
        Map<Integer, String> personNameMap = new HashMap<>();
        List<UserMasterDto> personByIds = userService.getUsersByIds(new HashSet<>(userIds));
        for (UserMasterDto personById : personByIds) {
            personNameMap.computeIfAbsent(personById.getId(), k -> personById.getFirstName().concat(" ").concat(personById.getLastName()).concat("(").concat(personById.getUserName()).concat(")"));
        }
        return personNameMap;
    }

    /**
     * Calculate percentage.
     *
     * @param trainingId          Training id.
     * @param personsDaysAttended Attendance per day.
     * @return Returns percentage.
     */
    private int calculatePercentage(Integer trainingId, int personsDaysAttended) {

        int totalDays = 0;
        Training training = trainingDao.retrieveById(trainingId);
        for (Integer course : training.getTrainingCourseIds()) {
            List<TopicMasterDto> topics = courseMasterService.getTopicsByCourse(course);
            for (TopicMasterDto topic : topics) {
                if (topic.getTopicDay() > totalDays) {
                    totalDays = topic.getTopicDay();
                }
            }
        }
        float percentage = TrainingUtil.calculatePercentageFloat(totalDays, personsDaysAttended);
        return Math.round(percentage);
    }

    /**
     * Training status comparator.
     */
    private final Comparator<TrainingStatus> trainingStatusComparator = (t1, t2) -> {
        int compareByDate = t2.getEffectiveDate().compareTo(t1.getEffectiveDate());
        if (compareByDate == 0) {
            if (t1.getTrainingState().equals(t2.getTrainingState())) {
                return compareByDate;
            } else if (t1.getTrainingState().equals(Training.State.SUBMITTED)) {
                return 1;
            } else {
                return -1;
            }
        }
        return compareByDate;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingDto> getUserTrainingsOfToday(Integer trainerId, Date date) {
        List<TrainingDto> trainings = this.searchForTraining(trainerId, date.getTime(), null, null, true);
        Integer trainingId = null;
        if (trainings.size() > 1) {
            for (TrainingDto trainingDto : trainings) {
                List<TopicCoverageDto> topics = topicCoverageService.getTopicCoveragesByTrainingOnDate(trainingDto.getTrainingId(), date);
                if (!topics.isEmpty()) {
                    trainingId = trainingDto.getTrainingId();
                    break;
                }
            }
            Integer temp = trainingId;
            return trainings.stream().filter(t -> (t.getTrainingId().equals(temp))).collect(Collectors.toList());
        } else if (trainings.isEmpty()) {
            return new ArrayList<>();
        } else return trainings;
    }

}
