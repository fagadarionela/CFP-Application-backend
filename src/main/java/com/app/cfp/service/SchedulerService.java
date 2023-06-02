package com.app.cfp.service;

import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@EnableScheduling
public class SchedulerService {

    private final SystemService systemService;

    private final MedicalCaseService medicalCaseService;

    private final Random random = new Random();

    @Value("${numberOfCasesAutomaticallyAssignedPerDay}")
    private int numberOfCasesAutomaticallyAssignedPerDay;

    @Autowired
    public SchedulerService(SystemService systemService, MedicalCaseService medicalCaseService) {
        this.systemService = systemService;
        this.medicalCaseService = medicalCaseService;
    }

    //TODO @Cronjob at 14:00:00
    public void assignEducationalCases() {
        //TODO
        //if cases are not enough
        //getAllEducationalCases
        //allocateEducationalCaseToResident0
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Istanbul")
    public void addTempMedicalCases() {
        List<TempMedicalCase> tempMedicalCaseList = systemService.getAllTempMedicalCases();
        for (int i = 0; i < numberOfCasesAutomaticallyAssignedPerDay; i++) {
            int position = random.nextInt(tempMedicalCaseList.size());
            TempMedicalCase tempMedicalCase = tempMedicalCaseList.get(position);
            medicalCaseService.addMedicalCase(createMedicalCase(tempMedicalCase.getCFPImage(), tempMedicalCase.getPresumptiveDiagnosis()));
            systemService.deleteTempMedicalCase(tempMedicalCase.getId());
            tempMedicalCaseList.remove(position);
        }
    }

    private MedicalCase createMedicalCase(byte[] fileData, String presumtiveDiagnosis) {
        MedicalCase medicalCase = new MedicalCase();
        medicalCase.setEncodedInfo("$2a$12$MDnofLJT8LrIILyh8SCle.DN9yKFRqQg8W.dqbq6hlkKqE0bL6mKK");
        medicalCase.setAdditionalInformation("INFORMATII ADITIONALE!");
        medicalCase.setCFPImage(ImageUtility.compressImage(fileData));
        medicalCase.setPresumptiveDiagnosis(presumtiveDiagnosis);

        return medicalCase;
    }
}
