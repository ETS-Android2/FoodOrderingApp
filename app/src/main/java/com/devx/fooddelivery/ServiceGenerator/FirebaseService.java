package com.devx.fooddelivery.ServiceGenerator;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseService {
    private static FirebaseDatabase  firebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public static FirebaseDatabase getDbInstance(){
        return firebaseDatabase;
    }

    public static FirebaseStorage getStorageRef(){
        return firebaseStorage;
    }
}
