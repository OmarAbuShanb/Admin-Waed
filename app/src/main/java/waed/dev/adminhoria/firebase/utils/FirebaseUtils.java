package waed.dev.adminhoria.firebase.utils;

public class FirebaseUtils {
    private static volatile FirebaseUtils Instance;

    private FirebaseUtils() {
    }

    public static synchronized FirebaseUtils getInstance() {
        if (Instance == null) {
            Instance = new FirebaseUtils();
        }
        return Instance;
    }

    public String getFirebaseErrorMessage(String errorCode) {
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

//    public void userExitCase(){
//        AppSharedPreferences.getInstance().clear();
//    }
}
