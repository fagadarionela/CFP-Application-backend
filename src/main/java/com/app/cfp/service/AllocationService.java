package com.app.cfp.service;

import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.MedicalCaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AllocationService {

    private final MedicalCaseRepository medicalCaseRepository;
    private final ResidentService residentService;

    public Resident allocateMedicalCase(MedicalCase medicalCase) {
        Random rand = new Random();
        Map<Resident, Long> residentsMap = residentService.getAllResidentsThatDoNotHaveAnAllocatedCaseIn(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        Set<Map.Entry<Resident, Long>> set = residentsMap.entrySet();

        //if not every resident has seen 1 case then randomly assign the case to a resident
        List<Resident> residentsThatHaveLessThanOneCase = set.stream().filter(residentLongEntry -> residentLongEntry.getValue() < 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!residentsThatHaveLessThanOneCase.isEmpty()) {
            return residentsThatHaveLessThanOneCase.get(rand.nextInt(residentsThatHaveLessThanOneCase.size()));
        }

        //if not every resident has seen more than 3 cases then assign the case to the resident that has seen the fewest cases, else random
        if (set.stream().anyMatch(residentLongEntry -> residentLongEntry.getValue() <= 3)) {
            Optional<Map.Entry<Resident, Long>> minValue = set.stream().min(Comparator.comparingLong(Map.Entry::getValue));
            List<Map.Entry<Resident, Long>> residentsWithTheFewestCases = set.stream().filter(residentLongEntry -> residentLongEntry.getValue().equals(minValue.get().getValue())).collect(Collectors.toList());
            if (residentsWithTheFewestCases.size() == 1) {
                return residentsWithTheFewestCases.get(0).getKey();
            }
            return residentsWithTheFewestCases.get(rand.nextInt(residentsWithTheFewestCases.size())).getKey();
        }

        //if not every resident has grade >=7 then assign the case to the resident that has the lowest grade, else random
        if (set.stream().anyMatch(residentLongEntry -> residentLongEntry.getKey().getGrade() < 7)) {
            Optional<Map.Entry<Resident, Long>> minValue = set.stream().min(Comparator.comparingDouble(residentLongEntry -> residentLongEntry.getKey().getGrade()));
            List<Map.Entry<Resident, Long>> residentsWithTheFewestGrade = set.stream().filter(residentLongEntry -> residentLongEntry.getKey().getGrade().equals(minValue.get().getKey().getGrade())).collect(Collectors.toList());

            if (residentsWithTheFewestGrade.size() == 1) {
                return residentsWithTheFewestGrade.get(0).getKey();
            }
            return residentsWithTheFewestGrade.get(rand.nextInt(residentsWithTheFewestGrade.size())).getKey();
        }
        //if exists a resident that has the longest t since encounter the diagnosis, then assign case to him
        List<Resident> residentsWithLongestTSinceEncounterDiagnosis = new ArrayList<>();
        long logestTime = 0;

        for (Map.Entry<Resident, Long> residentLongEntry : set) {
            long time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - residentLongEntry.getKey().getMedicalCases().last().getAllocationDate().toEpochSecond(ZoneOffset.UTC);
            if (time >= logestTime) {
                logestTime = time;
                residentsWithLongestTSinceEncounterDiagnosis.add(residentLongEntry.getKey());
            }
        }

        if (residentsWithLongestTSinceEncounterDiagnosis.size() == 1) {
            return residentsWithLongestTSinceEncounterDiagnosis.get(0);
        }

        //if exists a resident that has the fewest cases from Educational Topic, then assign case to him

        //if exists a resident that has the fewest cases from Retinal Conditions, then assign case to him

        //if exists a resident that has the fewest cases from all Retinal Conditions, then assign case to him

        //else assign a virtual case
        return null;
    }
}
