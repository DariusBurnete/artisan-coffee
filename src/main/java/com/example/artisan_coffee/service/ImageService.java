package com.example.artisan_coffee.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private final Path uploadsDir = Paths.get("C:/Users/EBURNED41/OneDrive - NTT DATA EMEAL/Escritorio/coffee/uploads/");


    public void renameImageToDeleted(String imageFileName) throws IOException {
        if (imageFileName == null || imageFileName.isBlank()) return;

        Path source = uploadsDir.resolve(imageFileName);
        if (Files.exists(source)) {
            Path target = uploadsDir.resolve("_____deleted_____" + imageFileName);
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

