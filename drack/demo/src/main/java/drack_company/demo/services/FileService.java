package drack_company.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    private final Path fileStorageLocation = Paths.get(System.getProperty("user.dir"), "uploads")
            .toAbsolutePath().normalize();

    public FileService(){
        try {
            Files.createDirectories(this.fileStorageLocation);

        }catch (Exception e){
            throw new RuntimeException("Failed create directories for upload files.  ",e);
        }
    }

    public String saveFile(MultipartFile file){
        if (file.isEmpty()){
            throw new IllegalArgumentException("Impossible to save empty file ");
        }
        try {
            String originalFilesNames = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilesNames;

            Path filePath = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        }catch (IOException e){
            throw new RuntimeException("Error during save file: " + e.getMessage());
        }
    }
}
