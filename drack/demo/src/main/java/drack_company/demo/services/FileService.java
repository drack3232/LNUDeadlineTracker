package drack_company.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file){
        if (file.isEmpty()){
            throw new IllegalArgumentException("Impossible to save empty file ");
        }
        try {
            File directory = new File(UPLOAD_DIR);
            if (directory.exists()){
                directory.mkdirs();
            }
            String originalFilesNames = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilesNames;

            Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);

//her I'm use method transferTo from spring
            file.transferTo(filePath.toFile());

            return uniqueFileName;
        }catch (IOException e){
            throw new RuntimeException("Error during save file: " + e.getMessage());
        }
    }
}
