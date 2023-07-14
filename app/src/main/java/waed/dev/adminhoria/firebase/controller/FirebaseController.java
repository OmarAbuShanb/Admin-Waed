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
    public FirebaseUser getCurrentUser() {
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

    public void login(String email, String password, Activity activity, LoginCallback loginCallback) {
        getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                loginCallback.onSuccess();
            } else {
                var taskException = task.getException();
                if (taskException instanceof FirebaseAuthException) {
                    var errorCode = ((FirebaseAuthException) taskException).getErrorCode();
                    var errorMessage = getFirebaseErrorMessage(errorCode);
                    loginCallback.onFailure(errorMessage);
                } else {
                    Log.e("FC", "login: taskEx went wrong");
                }
            }
        });
    }

    public void register(String email, String password,
            RegisterCallback registerCallback) {
        getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                var taskResult =task.getResult().getUser();
                if (taskResult != null) {
                    var uid = taskResult.getUid();
                    registerCallback.onSuccess(uid);
                }else {
                    registerCallback.onSuccess("");
                }
            } else {
                var taskException = task.getException();
                if (taskException instanceof FirebaseAuthException) {
                    var errorCode = ((FirebaseAuthException) taskException).getErrorCode();
                    var errorMessage = getFirebaseErrorMessage(errorCode);
                    registerCallback.onFailure(errorMessage);
                } else {
                    Log.e("FC", "Register: taskEx went wrong");
                }
            }
        });
    }


    private void signOut() {
        getAuth().signOut();
//        AppSharedPreferences.getInstance(context).clear()
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

    public interface LoginCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface RegisterCallback{
        void onSuccess(String uid);
        void onFailure(String errorMessage);
    }
}
