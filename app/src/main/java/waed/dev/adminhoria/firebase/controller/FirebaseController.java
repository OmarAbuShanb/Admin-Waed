package waed.dev.adminhoria.firebase.controller;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseController {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore database;
    private static volatile FirebaseController Instance;

    private FirebaseController() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

    }

    public synchronized static FirebaseController getInstance() {
        if (Instance == null) {
            Instance = new FirebaseController();
        }
        return Instance;
    }


    // authentic
    private FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    private FirebaseAuth getAuth() {
        return firebaseAuth;
    }
    // fire-store

    public FirebaseFirestore getDatabase() {
        return database;
    }


    // Auth

    public void login(String email, String password, Activity activity, AuthCallback authCallback) {
        getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                authCallback.onSuccess();
            } else {
                var taskException= task.getException();
                if (taskException instanceof FirebaseAuthException){
                    var errorCode = ((FirebaseAuthException) taskException).getErrorCode();
                    var errorMessage = getFirebaseErrorMessage(errorCode);
                    authCallback.onFailure(errorMessage);
                }else {
                    Log.e("FC", "login: taskEx went wrong");
                }
            }
        });
    }

    private String getFirebaseErrorMessage(String errorCode) {
        String errorMessage;
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL" -> errorMessage = "Invalid email address.";
            case "ERROR_WRONG_PASSWORD" -> errorMessage = "Incorrect password.";
            case "ERROR_USER_NOT_FOUND" -> errorMessage = "User not found.";
            case "ERROR_EMAIL_ALREADY_IN_USE" -> errorMessage = "Email already in use.";
            default -> errorMessage = "Authentication failed.";
        }
        return errorMessage;
    }

    public interface AuthCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}
