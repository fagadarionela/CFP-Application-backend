package com.app.cfp.service;

import com.app.cfp.entity.MyDate;
import com.app.cfp.repository.MyDateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DateService {

    private final MyDateRepository dateRepository;

    public void setDate(LocalDateTime date) {
        MyDate myDate = new MyDate();
        myDate.setName("date");
        myDate.setDate(date);
        dateRepository.save(myDate);
    }

    public void deleteDate() {
        dateRepository.deleteById("date");
    }

    public LocalDateTime now() {
        Optional<MyDate> myDateOptional = dateRepository.findById("date");
        if (myDateOptional.isPresent()) {
            return myDateOptional.get().getDate();
        } else {
            return LocalDateTime.now(ZoneOffset.UTC);
        }
    }
}
