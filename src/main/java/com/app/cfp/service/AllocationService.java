package com.app.cfp.service;

import com.app.cfp.entity.Disease;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import com.app.cfp.entity.VirtualCase;
import com.app.cfp.mapper.VirtualCaseMapper;
import com.app.cfp.repository.MedicalCaseRepository;
import com.app.cfp.repository.VirtualCaseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class AllocationService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AllocationService.class);

    private final DiseaseService diseaseService;
    private final ResidentService residentService;

    private final VirtualCaseMapper virtualCaseMapper;

    private final VirtualCaseRepository virtualCaseRepository;

    private final MedicalCaseRepository medicalCaseRepository;

    private final Random random = new Random();

    private final DateService dateService;

    public Resident allocateMedicalCase(MedicalCase medicalCase) {
        List<String> retinalCases = diseaseService.getAllRetinalCases().stream().map(Disease::getName).toList();
        //real case scenario
        if (retinalCases.contains(medicalCase.getPresumptiveDiagnosis())) {
            // Diagnosis contained in the 19 RCs
            System.out.println("Diagnosis contained in the 19 RCs");
            Map<Resident, Long> residentLongMap = residentService.getAllResidentsWithTheNumberOfCasesOfASpecificDiagnosis(medicalCase.getPresumptiveDiagnosis());
            Set<Map.Entry<Resident, Long>> residentsWithNumberOfCases = residentLongMap.entrySet();
            List<Resident> residents = residentLongMap.keySet().stream().toList();

            //if not every resident has seen 1 case then randomly assign the case to a resident
            //Assign one case from each of the 19 retinal conditions to each resident
            Resident resident = residentThatHasNotSeenOneCase(residentsWithNumberOfCases);
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that not every resident has seen 1 case");
                return resident;
            }

            //if not every resident has seen more than 3 cases then assign the case to the resident that has seen the fewest cases, else random
            //Assign the case to the resident which has seen fewer cases from this retinal conditions, up to 3 case
            resident = residentThatHasNotSeenThreeCases(residentsWithNumberOfCases);
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that not every resident has seen 3 cases");
                return resident;
            }

            //if not every resident has grade >=7 then assign the case to the resident that has the lowest grade, else random
            //Assign the case to the resident with the lowest grade (performance score + difficulty score) until all residents get a grade ≥7 for every retinal condition
            resident = residentThatHasGradeLessThanSeven(residents, medicalCase.getPresumptiveDiagnosis());
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that not every resident has grade >=7");
                return resident;
            }

            //if exists a resident that has the longest t since encounter the diagnosis, then assign case to him
            //Assign the case to the resident with the oldest encounter for that specific condition
            resident = residentThatHasTheLongestTimeSinceEncounterTheDiagnosis(residents, medicalCase.getPresumptiveDiagnosis());
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that there exists a resident that has the longest t since encounter the diagnosis");
                return resident;
            }

            //if exists a resident that has the fewest cases from Educational Topic, then assign case to him
            //Assign the case to the resident with the lowest number of cases from that specific educational topic
            resident = residentThatHasTheFewestCasesFromEducationalTopic(residents, medicalCase.getPresumptiveDiagnosis());
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that there exists a resident that has the fewest cases from Educational Topic");
                return resident;
            }

            //if exists a resident that has the fewest cases from Retinal Condition, then assign case to him
            //Assign the case to the resident with the lowest number of cases from that specific retinal condition
            resident = residentThatHasTheFewestCasesFromRetinalCondition(residents, medicalCase.getPresumptiveDiagnosis());
            if (resident != null) {
                System.out.println("Assigned resident due to the fact that there exists a resident that has the fewest cases from Retinal Condition");
                return resident;
            }

            //if exists a resident that has the fewest cases from all Retinal Conditions, then assign case to him
            //Assign the case to the resident with the lowest number of cases from all the 19 retinal conditions
            List<Resident> residentsThatMeetCondition = residentThatHasTheFewestCases(residentsWithNumberOfCases);
            if (residentsThatMeetCondition.size() == 1) {
                System.out.println("Assigned resident due to the fact that there exists a resident that has the fewest cases from all Retinal Conditions");
                return residentsThatMeetCondition.get(0);
            }
            System.out.println("Randomly assigned resident");
            return residentsThatMeetCondition.get(random.nextInt(residentsThatMeetCondition.size()));
        } else {
            // Diagnosis not contained in the 19 RCs

            //if exists a resident that has the fewest cases that day, then assign case to him
            //Assign the case to the resident which has seen fewer cases overall that day and at the same time virtual case to each resident
            List<Resident> residentsThatMeetCondition = residentThatHasTheFewestCases(residentService.getAllResidentsWithTheNumberOfCasesAllocatedIn(dateService.now()).entrySet());
            if (residentsThatMeetCondition.size() == 1) {
                virtualCaseRepository.save(virtualCaseMapper.toVirtualCase(medicalCase));
                System.out.println("Assigned resident due to the fact that there exists a resident that has the fewest cases that day");
                return residentsThatMeetCondition.get(0);
            }

            //if exists a resident that has the fewest cases from all Retinal Conditions, then assign case to him
            //Assign the case to the resident with the lowest number of cases from all retinal conditions
            // The case will also be assigned as a virtual case to all the other residents in the program, since we consider it a rarer case.
            // The case will not be evaluated and it won’t have a difficulty score, neither for real nor for virtual assignments.
            residentsThatMeetCondition = residentThatHasTheFewestCases(residentService.getAllResidentsWithTheNumberOfCases().entrySet());
            if (residentsThatMeetCondition.size() == 1) {
                System.out.println("Assigned resident due to the fact that there exists a resident that has the fewest cases from all Retinal Conditions");
                return residentsThatMeetCondition.get(0);
            }
            return residentsThatMeetCondition.get(random.nextInt(residentsThatMeetCondition.size()));
        }
    }

    @Scheduled(cron = "0 0 14 * * *", zone = "Europe/Istanbul")
    public void assignEducationalCases() {
        List<Resident> residentsWithZeroCases = residentService.getAllResidentsWithTheNumberOfCasesAllocatedIn(dateService.now()).entrySet().stream().filter(residentLongEntry -> residentLongEntry.getValue() == 0).map(Map.Entry::getKey).toList();
        List<String> retinalCases = diseaseService.getAllRetinalCases().stream().map(Disease::getName).toList();
        long now = dateService.now().toEpochSecond(ZoneOffset.UTC);

        for (Resident resident : residentsWithZeroCases) {
            Map<String, Long> retinalCasesWithNumber = new HashMap<>();
            retinalCases.forEach(s -> retinalCasesWithNumber.put(s, 0L));

            resident.getMedicalCases().forEach(
                    medicalCase -> {
                        String diagnosis = medicalCase.getPresumptiveDiagnosis();
                        retinalCasesWithNumber.put(diagnosis, retinalCasesWithNumber.getOrDefault(diagnosis, 0L) + 1);
                    }
            );

            long minimumNumberOfRetinalCases = Long.MAX_VALUE;
            double lowestGrade = Double.MAX_VALUE;
            long longestEncounterTime = 0;
            List<String> fewestRetinalConditions = new ArrayList<>();
            List<String> lowestGradeForRetinalConditions = new ArrayList<>();
            List<String> longestEncounteredTForRetinalCondition = new ArrayList<>();
            boolean hasSeenLessThanOneCaseFromEveryRetinalCondition = true;
            boolean hasSeenLessThanThreeCasesFromEveryRetinalCondition = true;
            boolean hasGradeLessThanSevenForEveryRetinalCondition = true;

            for (Map.Entry<String, Long> retinalCaseWithNumber : retinalCasesWithNumber.entrySet()) {
                long numberOfCases = retinalCaseWithNumber.getValue();
                if (numberOfCases != 0) {
                    hasSeenLessThanOneCaseFromEveryRetinalCondition = false;
                }
                if (numberOfCases >= 3) {
                    hasSeenLessThanThreeCasesFromEveryRetinalCondition = false;
                }
                if (numberOfCases < minimumNumberOfRetinalCases) {
                    minimumNumberOfRetinalCases = numberOfCases;
                }
                for (MedicalCase medicalCase : resident.getMedicalCases()) {
                    double grade = medicalCase.getGrade();
                    long encounteredTime = medicalCase.getAllocationDate().toEpochSecond(ZoneOffset.UTC);
                    if (grade >= 7) {
                        hasGradeLessThanSevenForEveryRetinalCondition = false;
                    }
                    if (grade < lowestGrade) {
                        lowestGrade = grade;
                    }
                    if (now - encounteredTime > longestEncounterTime) {
                        longestEncounterTime = now - encounteredTime;
                    }
                }
            }

            //if resident has seen <1 from every Retinal Condition then randomly assign a case
            if (hasSeenLessThanOneCaseFromEveryRetinalCondition) {
                //randomly assign a case between them
                assignOneCaseFromVirtualCases(resident, virtualCaseRepository.findAll());
            }

            for (Map.Entry<String, Long> retinalCaseWithNumber : retinalCasesWithNumber.entrySet()) {
                long numberOfCases = retinalCaseWithNumber.getValue();
                if (numberOfCases == minimumNumberOfRetinalCases) {
                    fewestRetinalConditions.add(retinalCaseWithNumber.getKey());
                }
            }
            for (MedicalCase medicalCase : resident.getMedicalCases()) {
                if (lowestGrade == medicalCase.getGrade()) {
                    lowestGradeForRetinalConditions.add(medicalCase.getPresumptiveDiagnosis());
                }
                if (longestEncounterTime == now - medicalCase.getAllocationDate().toEpochSecond(ZoneOffset.UTC)) {
                    longestEncounteredTForRetinalCondition.add(medicalCase.getPresumptiveDiagnosis());
                }
            }

            //if resident has seen <3 from every Retinal Condition then if there is one RC seen the least, assign a case, else randomly assign the case
            if (hasSeenLessThanThreeCasesFromEveryRetinalCondition) {
                assignFewestRetinalConditionsToResident(fewestRetinalConditions, resident);
            }

            //if resident has grade <7 from every Retinal Condition then if there is one RC with the lowest grade, assign a case, else randomly assign the case
            if (hasGradeLessThanSevenForEveryRetinalCondition) {
                if (lowestGradeForRetinalConditions.size() == 1) {
                    //assign case to the resident
                    assignVirtualCaseToResident(lowestGradeForRetinalConditions.get(0), resident);
                } else {
                    //randomly assign a case between them
                    assignVirtualCaseToResident(lowestGradeForRetinalConditions.get(random.nextInt(lowestGradeForRetinalConditions.size())), resident);
                }
            }

            //if resident has seen <=1 Retinal Case with the longest encounter t, then assign a case
            if (longestEncounteredTForRetinalCondition.size() == 1) {
                //assign case to the resident
                assignVirtualCaseToResident(longestEncounteredTForRetinalCondition.get(0), resident);
            }

            //if resident has seen <=1 Retinal Case with fewest seen cases, then assign a case, else randomly assign a case
            assignFewestRetinalConditionsToResident(fewestRetinalConditions, resident);
        }
    }

    private void assignOneCaseFromVirtualCases(Resident resident, List<VirtualCase> virtualCases) {
        VirtualCase virtualCase;
        if (virtualCases.size() >= 1) {
            virtualCase = virtualCases.get(random.nextInt(virtualCases.size()));
        } else {
            List<VirtualCase> newVirtualCases = virtualCaseRepository.findAll();
            virtualCase = newVirtualCases.get(random.nextInt(newVirtualCases.size()));
        }

        MedicalCase medicalCase = virtualCaseMapper.toMedicalCase(virtualCase);
        medicalCase.setResident(resident);
        medicalCase.setAllocationDate(dateService.now());
        medicalCaseRepository.save(medicalCase);
        LOGGER.info("Virtual case with diagnosis {} was assigned to {}", virtualCase.getPresumptiveDiagnosis(), resident.getAccount().getUsername());
    }

    private Resident residentThatHasNotSeenOneCase(Set<Map.Entry<Resident, Long>> residents) {
        List<Resident> residentsThatHaveLessThanOneCase = residents.stream().filter(residentLongEntry -> residentLongEntry.getValue() < 1).map(Map.Entry::getKey).toList();
        if (!residentsThatHaveLessThanOneCase.isEmpty()) {
            return residentsThatHaveLessThanOneCase.get(random.nextInt(residentsThatHaveLessThanOneCase.size()));
        }
        return null;
    }

    private Resident residentThatHasNotSeenThreeCases(Set<Map.Entry<Resident, Long>> residents) {
        if (residents.stream().anyMatch(residentLongEntry -> residentLongEntry.getValue() <= 3)) {
            Optional<Map.Entry<Resident, Long>> minValue = residents.stream().min(Comparator.comparingLong(Map.Entry::getValue));
            List<Map.Entry<Resident, Long>> residentsWithTheFewestCases = residents.stream().filter(residentLongEntry -> residentLongEntry.getValue().equals(minValue.get().getValue())).toList();
            if (residentsWithTheFewestCases.size() == 1) {
                return residentsWithTheFewestCases.get(0).getKey();
            }
            return residentsWithTheFewestCases.get(random.nextInt(residentsWithTheFewestCases.size())).getKey();
        }
        return null;
    }

    private Resident residentThatHasGradeLessThanSeven(List<Resident> residents, String diagnosis) {
        double minValue = Double.MAX_VALUE;
        Map<Resident, Double> residentsWithGrades = new HashMap<>();
        for (Resident resident : residents) {
            double gradeForDiagnosis = 0;
            int numberOfCases = 0;
            for (MedicalCase medicalCase : resident.getMedicalCases()) {
                if (diagnosis.equals(medicalCase.getPresumptiveDiagnosis()) && medicalCase.isCompletedByExpert()) {
                    gradeForDiagnosis += medicalCase.getGrade();
                    numberOfCases++;
                }
            }
            if (numberOfCases != 0) {
                gradeForDiagnosis = Math.round(gradeForDiagnosis * 100 / numberOfCases) / 100.0;
                residentsWithGrades.put(resident, gradeForDiagnosis);
                if (minValue > gradeForDiagnosis) {
                    minValue = gradeForDiagnosis;
                }
            }
        }
        if (minValue < 7) {
            double finalMinValue = minValue;
            List<Map.Entry<Resident, Double>> residentsWithTheFewestGrade = residentsWithGrades.entrySet().stream().filter(residentDoubleEntry -> finalMinValue == residentDoubleEntry.getValue()).toList();
            if (residentsWithTheFewestGrade.size() == 1) {
                return residentsWithTheFewestGrade.get(0).getKey();
            }
            return residentsWithTheFewestGrade.get(random.nextInt(residentsWithTheFewestGrade.size())).getKey();
        }
        return null;
    }

    private Resident residentThatHasTheLongestTimeSinceEncounterTheDiagnosis(List<Resident> residents, String diagnosis) {
        List<Resident> residentsWithLongestTSinceEncounterDiagnosis = new ArrayList<>();
        long longestTime = 0;
        long now = dateService.now().toEpochSecond(ZoneOffset.UTC);

        for (Resident resident : residents) {
            Optional<MedicalCase> medicalCaseOptional = resident.getMedicalCases()
                    .stream().filter(medicalCase -> diagnosis.equals(medicalCase.getPresumptiveDiagnosis())).max(Comparator.comparing(MedicalCase::getAllocationDate));
            if (medicalCaseOptional.isPresent()) {
                long time = now - medicalCaseOptional.get().getAllocationDate().toEpochSecond(ZoneOffset.UTC);
                if (time > longestTime) {
                    longestTime = time;
                }
            }
        }

        for (Resident resident : residents) {
            if (now - resident.getMedicalCases().last().getAllocationDate().toEpochSecond(ZoneOffset.UTC) == longestTime) {
                residentsWithLongestTSinceEncounterDiagnosis.add(resident);
            }
        }

        if (residentsWithLongestTSinceEncounterDiagnosis.size() == 1) {
            return residentsWithLongestTSinceEncounterDiagnosis.get(0);
        }

        return null;
    }

    private Resident residentThatHasTheFewestCasesFromEducationalTopic(List<Resident> residents, String diagnosis) {
        Map<Resident, Long> residentsThatHaveTheFewestCases = new HashMap<>();
        long fewestCases = Long.MAX_VALUE;

        for (Resident resident : residents) {
            long numberOfCases = 0;
            for (MedicalCase medicalCase : resident.getMedicalCases()) {
                Disease disease = diseaseService.getDiseaseByName(medicalCase.getPresumptiveDiagnosis());
                if (disease != null) {
                    if (disease.getEducationalTopic().equals(diseaseService.getDiseaseByName(diagnosis).getEducationalTopic())) {
                        numberOfCases++;
                    }
                }
            }
            residentsThatHaveTheFewestCases.put(resident, numberOfCases);
            if (fewestCases > numberOfCases) {
                fewestCases = numberOfCases;
            }
        }

        List<Resident> residentsThatHaveTheFewestCasesThatSatisfyCondition = new ArrayList<>();
        for (Map.Entry<Resident, Long> entry : residentsThatHaveTheFewestCases.entrySet()) {
            if (entry.getValue() == fewestCases) {
                residentsThatHaveTheFewestCasesThatSatisfyCondition.add(entry.getKey());
            }
        }

        if (residentsThatHaveTheFewestCasesThatSatisfyCondition.size() == 1) {
            return residentsThatHaveTheFewestCasesThatSatisfyCondition.get(0);
        }

        return null;
    }

    private Resident residentThatHasTheFewestCasesFromRetinalCondition(List<Resident> residents, String diagnosis) {
        Map<Resident, Long> residentsThatHaveTheFewestCases = new HashMap<>();
        long fewestCases = Long.MAX_VALUE;

        for (Resident resident : residents) {
            long numberOfCases = 0;
            for (MedicalCase medicalCase : resident.getMedicalCases()) {
                Disease disease = diseaseService.getDiseaseByName(medicalCase.getPresumptiveDiagnosis());
                if (disease != null) {
                    if (disease.getName().equals(diseaseService.getDiseaseByName(diagnosis).getName())) {
                        numberOfCases++;
                    }
                }
            }
            residentsThatHaveTheFewestCases.put(resident, numberOfCases);
            if (fewestCases > numberOfCases) {
                fewestCases = numberOfCases;
            }
        }

        List<Resident> residentsThatHaveTheFewestCasesThatSatisfyCondition = new ArrayList<>();
        for (Map.Entry<Resident, Long> entry : residentsThatHaveTheFewestCases.entrySet()) {
            if (entry.getValue() == fewestCases) {
                residentsThatHaveTheFewestCasesThatSatisfyCondition.add(entry.getKey());
            }
        }

        if (residentsThatHaveTheFewestCasesThatSatisfyCondition.size() == 1) {
            return residentsThatHaveTheFewestCasesThatSatisfyCondition.get(0);
        }

        return null;
    }

    private List<Resident> residentThatHasTheFewestCases(Set<Map.Entry<Resident, Long>> residents) {
        Optional<Map.Entry<Resident, Long>> residentOptional = residents.stream().min(Comparator.comparingDouble(Map.Entry::getValue));
        if (residentOptional.isPresent()) {
            long fewestCases = residentOptional.get().getValue();

            return residents.stream().filter(residentLongEntry -> residentLongEntry.getValue() == fewestCases).map(Map.Entry::getKey).toList();
        }

        return Collections.emptyList();
    }

    private void assignVirtualCaseToResident(String retinalCondition, Resident resident) {
        List<VirtualCase> virtualCases = virtualCaseRepository.findAllByPresumptiveDiagnosis(retinalCondition);
        assignOneCaseFromVirtualCases(resident, virtualCases);
    }

    private void assignFewestRetinalConditionsToResident(List<String> fewestRetinalConditions, Resident resident) {
        if (fewestRetinalConditions.size() == 1) {
            //assign case to the resident
            assignVirtualCaseToResident(fewestRetinalConditions.get(0), resident);
        } else {
            //randomly assign a case between them
            assignVirtualCaseToResident(fewestRetinalConditions.get(random.nextInt(fewestRetinalConditions.size())), resident);
        }
    }

}
