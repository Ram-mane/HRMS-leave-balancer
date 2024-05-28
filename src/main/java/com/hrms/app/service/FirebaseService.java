package com.hrms.app.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.hrms.app.config.FirebaseInitialization;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    private Firestore firestore = FirebaseInitialization.firestore;

    public Integer getPageSizeEmp() throws Exception {
        DocumentReference docRef = firestore.collection("hrms_db").document("z2Xu9PIXZbNlIUQItbW0");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getLong("page_size_emp").intValue();
        } else {
            return 1;
        }
    }

    public Integer getPageSizeLeave() throws Exception {
        DocumentReference docRef = firestore.collection("hrms_db").document("z2Xu9PIXZbNlIUQItbW0");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getLong("page_size_leave").intValue();
        } else {
            return 1;
        }
    }

    public Integer getOptionalLeavesAllowed() throws Exception {
        DocumentReference docRef = firestore.collection("hrms_db").document("LW04wsVC7dQm1gChWIiq");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getLong("optional_leave").intValue();
        } else {
            return 1;
        }
    }

    public Integer getNationalLeavesAllowed() throws Exception {
        DocumentReference docRef = firestore.collection("hrms_db").document("LW04wsVC7dQm1gChWIiq");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getLong("national_leave").intValue();
        } else {
            return 1;
        }
    }


}
