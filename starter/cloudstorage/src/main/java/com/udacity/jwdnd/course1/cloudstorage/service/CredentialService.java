package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int addCredential(Credential credential){
        return credentialMapper.addCredential(credential);
    }
    public List<Credential> getCredentials(int userId){
        return this.credentialMapper.getUserCredentials(userId);
    }

    public Credential getCredentialsById(int credentialId){
        return this.credentialMapper.getCredential(credentialId);
    }

    public int editCredential(Credential credential){
        return this.credentialMapper.editCredential(credential);
    }

    public int deleteCredential(int credentialId){
        return this.credentialMapper.deleteCredential(credentialId);
    }

    public Credential getLastElement(){
        return this.credentialMapper.getLastCredential();
    }
}
