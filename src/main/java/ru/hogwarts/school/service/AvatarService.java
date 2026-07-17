package ru.hogwarts.school.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    private final Path storageLocation = Paths.get("uploads");

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для хранения файлов", e);
        }
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));

        String fileName = studentId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path savePath = storageLocation.resolve(fileName);

        Files.write(savePath, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(savePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        student.setAvatar(avatar);

        return avatarRepository.save(avatar);
    }

    public ByteArrayResource getAvatarFromDb(Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new IllegalArgumentException("Аватар не найден"));

        return new ByteArrayResource(avatar.getData());
    }

    public Page<Avatar> getAllAvatarsPage(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }

    public UrlResource getAvatarFromDisk(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Файл не найден по пути: " + filePath);
        }
        return new UrlResource(path.toUri());
    }
}
