package com.ias.quickfix.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseService {
    private Firestore firestore;

    @Autowired
    public FirebaseService(
            Firestore firestore
    ) {
        this.firestore = firestore;
    }

    public void saveObject(String collectionName, String documentId, Object o) {
        DocumentReference documentReference = this.firestore.collection(collectionName).document(documentId);
        documentReference.set(o);
    }

    public <T> List<T> getAllObjects(String collection, Class<T> clazz) {
        List<T> objects = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(collection).get();
        try {
            QuerySnapshot querySnapshot = future.get();
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                objects.add(doc.toObject(clazz));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve objects from Firestore", e);
        }
        return objects;
    }

    public <T> T getObjectById(String collection, String documentId, Class<T> clazz) {
        ApiFuture<DocumentSnapshot> future = firestore.collection(collection).document(documentId).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(clazz);
            } else {
                throw new RuntimeException("Document not found: " + documentId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve object from Firestore", e);
        }
    }

    public boolean documentExists(String collection, String documentId) {
        try {
            DocumentSnapshot document = firestore.collection(collection).document(documentId).get().get();
            return document.exists();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if document exists", e);
        }
    }

    public void deleteObject(String collection, String documentId) {
        try {
            firestore.collection(collection).document(documentId).delete().get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete document with ID: " + documentId, e);
        }
    }

}
