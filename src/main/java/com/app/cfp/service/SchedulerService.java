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

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Istanbul")
    public void addTempMedicalCases() {
        List<TempMedicalCase> tempMedicalCaseList = systemService.getAllTempMedicalCases();
        for (int i = 0; i < numberOfCasesAutomaticallyAssignedPerDay; i++) {
            if (tempMedicalCaseList.size() == 0){
                return;
            }
            int position = random.nextInt(tempMedicalCaseList.size());
            TempMedicalCase tempMedicalCase = tempMedicalCaseList.get(position);
            medicalCaseService.addMedicalCase(createMedicalCase(tempMedicalCase.getCFPImage(), tempMedicalCase.getPresumptiveDiagnosis(), tempMedicalCase.getCFPImageName()));
            systemService.deleteTempMedicalCase(tempMedicalCase.getId());
            tempMedicalCaseList.remove(position);
        }
    }

    private MedicalCase createMedicalCase(byte[] fileData, String presumtiveDiagnosis, String CFPImageName) {
        MedicalCase medicalCase = new MedicalCase();
        String randomName = java.util.UUID.randomUUID() + java.util.UUID.randomUUID().toString();

        medicalCase.setEncodedInfo("$2a$12$" + randomName.substring(0, 52));
        medicalCase.setAdditionalInformation("No additional information");
        medicalCase.setCFPImage(fileData);
        medicalCase.setPresumptiveDiagnosis(presumtiveDiagnosis);
        medicalCase.setCFPImageName(CFPImageName);

        return medicalCase;
    }
}
