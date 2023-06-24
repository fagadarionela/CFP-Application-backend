package com.app.cfp.controller;

import com.app.cfp.dto.ImageDTO;
import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.service.MedicalCaseService;
import com.app.cfp.service.SystemService;
import com.app.cfp.utils.ImageUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/system")
@AllArgsConstructor
@CrossOrigin
public class SystemController {


    @Autowired
    SystemService systemService;

    @Autowired
    MedicalCaseService medicalCaseService;

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
                                    systemService.addTempMedicalCase(TempMedicalCase.builder().CFPImage(ImageUtility.compressImage(fileData)).presumptiveDiagnosis(path.toString().substring(path.toString().lastIndexOf('\\') + 1)).CFPImageName(subPath.toString().substring(subPath.toString().lastIndexOf('\\') + 1)).build());
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
