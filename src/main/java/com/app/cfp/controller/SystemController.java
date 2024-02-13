package com.app.cfp.controller;

import com.app.cfp.dto.ImageDTO;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.entity.VirtualCase;
import com.app.cfp.service.MedicalCaseService;
import com.app.cfp.service.SystemService;
import com.app.cfp.utils.UploadClient;
import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/system")
@AllArgsConstructor
@CrossOrigin
public class SystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    SystemService systemService;

    @Autowired
    MedicalCaseService medicalCaseService;

    @Autowired
    UploadClient uploadClient;

    @GetMapping("/file")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ImageDTO> getFiles() {
//        final String pathh = "images";
//        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
//
//        if(jarFile.isFile()) {  // Run with JAR file
//            String path = this.getClass().getClassLoader().getResource("image").toExternalForm();
//            System.out.println("Run with JAR file");
//            InputStream in = SystemController.class.getClassLoader().getResourceAsStream("/data.sav");
//            final JarFile jar = new JarFile(jarFile);
//            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
//            while(entries.hasMoreElements()) {
//                final String name = entries.nextElement().getName();
//                if (name.startsWith(pathh + "/")) { //filter according to the path
//                    System.out.println(name);
//                }
//            }
//            jar.close();
//        } else { // Run with IDE
//            System.out.println("Run with IDE");
//            String path = this.getClass().getClassLoader().getResource("images").toExternalForm();
//            System.out.println(path);
////            final URL url = SystemController.class.getResource("/" + pathh);
//            if (path != null) {
//                try {
//                    final File apps = new File(path);
//                    System.out.println(apps);
//                    for (File app : apps.listFiles()) {
//                        System.out.println(app);
//                        if (!app.isFile()){
//                            for (File file : app.listFiles()) {
//                                System.out.println(file);
//                                try (FileInputStream imageInFile = new FileInputStream(file)) {
//                                    // Reading a file from file system
//                                    byte[] fileData = new byte[(int) file.length()];
//                                    imageInFile.read(fileData);
//                                    //add case into tempMedicalCaseRepo
//                                    systemService.addTempMedicalCase(TempMedicalCase.builder().CFPImage(ImageUtility.compressImage(fileData)).presumptiveDiagnosis(file.toString().substring(file.toString().lastIndexOf('\\') + 1)).CFPImageName(file.toString().substring(file.toString().lastIndexOf('\\') + 1)).build());
//                                } catch (FileNotFoundException e) {
//                                    System.out.println("File not found" + e);
//                                } catch (IOException ioe) {
//                                    System.out.println("Exception while reading the file " + ioe);
//                                }
//                            }
//                        }
//
//                    }
//                } catch (Exception ex) {
//                    // never happens
//                }
//            }
//        }


//        String mainDirectory = "https://cfp-images.s3.eu-central-1.amazonaws.com/images";
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mainDirectory))) {
//            for (Path path : stream) {
//                if (Files.isDirectory(path)) {
//                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(Paths.get(path.toString()))) {
//                        for (Path subPath : subStream) {
//                            if (!Files.isDirectory(subPath)) {
//                                File file = new File(subPath.toString());
//                                try (FileInputStream imageInFile = new FileInputStream(file)) {
//                                    // Reading a file from file system
////                                    byte[] fileData = new byte[(int) file.length()];
////                                    imageInFile.read(fileData);
////                                    byte[] fileData =resize(file); // TODO
//                                    uploadClient.uploadFile(file);
//                                    //add case into tempMedicalCaseRepo
//                                    systemService.addTempMedicalCase(TempMedicalCase.builder().presumptiveDiagnosis(path.toString().substring(path.toString().lastIndexOf('\\') + 1)).CFPImageName(subPath.toString().substring(subPath.toString().lastIndexOf('\\') + 1)).build());
//                                } catch (FileNotFoundException e) {
//                                    System.out.println("File not found" + e);
//                                } catch (IOException ioe) {
//                                    System.out.println("Exception while reading the file " + ioe);
//                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        List<String> objects = uploadClient.listObjects();
        for(String object: objects){
//            systemService.addTempMedicalCase(TempMedicalCase.builder().presumptiveDiagnosis(object.substring(object.indexOf('/')+1, object.lastIndexOf('/'))).CFPImageName(object.substring(object.lastIndexOf('/') + 1)).build());
            systemService.addVirtualCase(createVirtualCase(object.substring(object.indexOf('/')+1, object.lastIndexOf('/')), object.substring(object.lastIndexOf('/') + 1)));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    VirtualCase createVirtualCase(String presumtiveDiagnosis, String CFPImageName){
        VirtualCase virtualCase = new VirtualCase();
        String randomName = java.util.UUID.randomUUID() + java.util.UUID.randomUUID().toString();

        virtualCase.setEncodedInfo("$2a$12$" + randomName.substring(0, 52));
        virtualCase.setAdditionalInformation("No additional information");
        virtualCase.setAutomaticCase(true);
        virtualCase.setPresumptiveDiagnosis(presumtiveDiagnosis);
        virtualCase.setCFPImageName(CFPImageName);

        return virtualCase;
    }

    public static byte[] resize(File icon) {
        try {
            BufferedImage originalImage = ImageIO.read(icon);

            originalImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 1350, 900);
            //To save with original ratio uncomment next line and comment the above.
            //originalImage= Scalr.resize(originalImage, 153, 128);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/file/number")
    public ResponseEntity<Integer> getNumberOfFiles() {
        return new ResponseEntity<>((int) systemService.countAllVirtualCases(), HttpStatus.OK);
    }

    @DeleteMapping("/file")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Integer> deleteTempFiles() {
        LOGGER.info("Deleting files...");
        systemService.deleteAllVirtualCases();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
