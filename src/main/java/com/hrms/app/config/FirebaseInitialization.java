package com.hrms.app.config;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseInitialization {
    public static Firestore firestore;

    @PostConstruct
    public void initializeFirestore() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("./hrms-serviceAccountKey.json");

        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId("hrms-768af")
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        firestore = firestoreOptions.getService();
    }




}
