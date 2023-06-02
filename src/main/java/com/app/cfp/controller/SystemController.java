package com.app.cfp.controller;

import com.app.cfp.dto.ImageDTO;
import com.app.cfp.dto.SystemDateDTO;
import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.service.DateService;
import com.app.cfp.service.MedicalCaseService;
import com.app.cfp.service.SystemService;
import com.app.cfp.utils.ImageUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/system")
@AllArgsConstructor
@CrossOrigin
public class SystemController {

    @Autowired
    DateService dateService;

    @Autowired
    SystemService systemService;

    @Autowired
    MedicalCaseService medicalCaseService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocalDateTime> getTime() {
        return new ResponseEntity<>(dateService.now(), HttpStatus.OK);
    }

    @GetMapping("/plusOneDay")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocalDateTime> addDay() {
        LocalDateTime dateTime = dateService.now().plusDays(1L).withHour(0).withMinute(0).withSecond(0);
        dateService.setDate(dateTime);
        return new ResponseEntity<>(dateTime, HttpStatus.OK);
    }

    @GetMapping("/set14HourOfDay")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocalDateTime> set14HourOfDay() {
        LocalDateTime dateTime = dateService.now().withHour(14).withMinute(0).withSecond(0);
        dateService.setDate(dateTime);
        return new ResponseEntity<>(dateTime, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setTime(@RequestBody SystemDateDTO systemDateDTO) {
        dateService.setDate(LocalDateTime.of(systemDateDTO.getYear(), systemDateDTO.getMonth(), systemDateDTO.getDay(), systemDateDTO.getHour(), systemDateDTO.getMinute(), systemDateDTO.getSecond()));

        System.out.println(dateService.now());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTime() {
        dateService.deleteDate();
        System.out.println(dateService.now());
    }

    @GetMapping("/file")
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<ImageDTO> getFiles() {
        String mainDirectory = "src/main/resources/images";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mainDirectory))) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(Paths.get(path.toString()))) {
                        for (Path subPath : subStream) {
                            if (!Files.isDirectory(subPath)) {
                                File file = new File(subPath.toString());
                                try (FileInputStream imageInFile = new FileInputStream(file)) {
                                    // Reading a file from file system
                                    byte[] fileData = new byte[(int) file.length()];
                                    imageInFile.read(fileData);
                                    //add case into tempMedicalCaseRepo
                                    systemService.addTempMedicalCase(TempMedicalCase.builder().CFPImage(ImageUtility.compressImage(fileData)).presumptiveDiagnosis(path.toString().substring(path.toString().lastIndexOf('\\') + 1)).build());
                                } catch (FileNotFoundException e) {
                                    System.out.println("File not found" + e);
                                } catch (IOException ioe) {
                                    System.out.println("Exception while reading the file " + ioe);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
