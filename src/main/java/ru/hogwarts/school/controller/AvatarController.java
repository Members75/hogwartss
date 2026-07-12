package ru.hogwarts.school.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Avatar> uploadAvatar(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok(avatar);
    }

    @GetMapping("/db/{avatarId}")
    public ResponseEntity<Resource> getAvatarFromDb(@PathVariable Long avatarId) {
        Resource resource = avatarService.getAvatarFromDb(avatarId);
        String contentType = "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/disk")
    public ResponseEntity<Resource> getAvatarFromDisk(@RequestParam String filePath) throws IOException {
        Resource resource = avatarService.getAvatarFromDisk(filePath);
        String contentType = "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}