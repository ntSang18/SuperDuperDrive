package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public List<Credential> getCredentialsByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return credentialMapper
                .findByUserId(user.getUserId())
                .stream()
                .peek(credential -> credential.setPassword(encryptionService.decryptValue(
                                                credential.getPassword(),
                                                credential.getKey())))
                .collect(Collectors.toList());
    }

    public boolean isCredentialOwner(Integer credentialId, String username) {
        User user = userMapper.findByUserName(username);
        Credential credential = credentialMapper.findById(credentialId);

        return credential.getUserId().intValue() == user.getUserId().intValue();
    }

    public int createCredential(Credential credential) {

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptPassword);
        return credentialMapper.insert(credential);
    }

    public int editCredential(Credential credential) {

        Credential dbCredential = credentialMapper.findById(credential.getCredentialId());
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), dbCredential.getKey());

        credential.setPassword(encryptPassword);
        return credentialMapper.update(credential);
    }

    public int saveCredential(Credential credential, String username) {
        User user = userMapper.findByUserName(username);
        int rowsEffected = 0;

        if (credential.getCredentialId() == null) {
            credential.setUserId(user.getUserId());
            rowsEffected = createCredential(credential);
        } else {
            // Check credential owner
            Credential dbCredential = credentialMapper.findById(credential.getCredentialId());
            if (dbCredential.getUserId().intValue() == user.getUserId().intValue()) {
                rowsEffected = editCredential(credential);
            }
        }
        return rowsEffected;
    }

    public int removeCredential(int credentialId, String username) {
        User user = userMapper.findByUserName(username);
        Credential credential = credentialMapper.findById(credentialId);
        if (credential.getUserId().intValue() == user.getUserId().intValue()) {
            return credentialMapper.delete(credentialId);
        }
        return 0;
    }
}
