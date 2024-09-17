package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    public List<File> getFilesByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return fileMapper.findByUserId(user.getUserId());
    }

    public File getFileById(int fileId) {
        return fileMapper.findById(fileId);
    }

    public boolean isFileOwner(int fileId, String username) {
        User user = userMapper.findByUserName(username);
        File file = fileMapper.findById(fileId);
        return file.getUserId().intValue() == user.getUserId().intValue();
    }

    public boolean isFileNameExist(String username, String fileName) {
        return getFilesByUserName(username).stream().anyMatch(file -> file.getFileName().equals(fileName));
    }

    public int uploadFile(MultipartFile file, String username) {
        int rowsEffected = 0;
        if (file.isEmpty()) {
            return rowsEffected;
        }
        if (isFileNameExist(username, file.getOriginalFilename())) {
            return rowsEffected;
        }
        User user = userMapper.findByUserName(username);

        try {
            File saveFile = new File(null,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    String.valueOf(file.getSize()),
                    user.getUserId(),
                    file.getBytes());

            rowsEffected = fileMapper.insert(saveFile);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        return rowsEffected;
    }

    public int removeFile(int fileId, String username) {
        if (isFileOwner(fileId, username)) {
            return fileMapper.delete(fileId);
        }
        return 0;
    }
}
