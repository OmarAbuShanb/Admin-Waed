package waed.dev.adminhoria.firebase.controller;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import waed.dev.adminhoria.firebase.utils.FirebaseUtils;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.models.PrisonerCard;

public class FirebaseController {
    private static final String TAG = "FirebaseController";

    private final FirebaseUtils firebaseUtils;

    private final FirebaseAuth firebaseAuth;
    private final FirebaseDatabase realTimeDatabase;
    private final FirebaseFirestore fireStoreDatabase;
    private final FirebaseStorage firebaseStorage;

    private static volatile FirebaseController Instance;

    private FirebaseController() {
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        realTimeDatabase = FirebaseDatabase.getInstance();
        fireStoreDatabase = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public synchronized static FirebaseController getInstance() {
        if (Instance == null) {
            Instance = new FirebaseController();
        }
        return Instance;
    }

    // Authentic
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void login(String email, String password, Activity activity, FirebaseAuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            callback.onSuccess(user.getUid());
                        }
                    } else {
                        var taskException = task.getException();
                        if (taskException instanceof FirebaseAuthException) {
                            var errorCode = ((FirebaseAuthException) taskException).getErrorCode();
                            var errorMessage = firebaseUtils.getFirebaseErrorMessage(errorCode);
                            callback.onFailure(errorMessage);
                        } else {
                            Log.e("FC", "login: taskEx went wrong");
                        }
                    }
                });
    }

    /* ****************************************************************************************** */

    public void registerNewAdmin(String email, String password, FirebaseAuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            callback.onSuccess(user.getUid());
                        }
                    } else {
                        var taskException = task.getException();
                        if (taskException instanceof FirebaseAuthException) {
                            var errorCode = ((FirebaseAuthException) taskException).getErrorCode();
                            var errorMessage = firebaseUtils.getFirebaseErrorMessage(errorCode);
                            callback.onFailure(errorMessage);
                        } else {
                            Log.e("FC", "login: taskEx went wrong");
                        }
                    }
                });
    }

    // Realtime
    public void updateUrgentNews(String newsText, FirebaseCallback callback) {
        realTimeDatabase.getReference("Urgent")
                .child("newsText")
                .setValue(newsText)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to send Urgent News: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void updateStatistics(String key, String newValue, FirebaseCallback callback) {
        realTimeDatabase.getReference("Statistics")
                .child(key)
                .setValue(newValue)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateStatistics: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getStatistics(GetStatisticsCallback callback) {
        realTimeDatabase.getReference("Statistics")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String,String> mapData = new HashMap<>();
                        for (DataSnapshot snapshot: task.getResult().getChildren()) {
                            mapData.put(snapshot.getKey(), String.valueOf(snapshot.getValue()));
                        }
                        callback.onSuccess(mapData);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getStatistics: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    // FireStore
    public void addAdmin(String userUid, FirebaseCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userUid);

        fireStoreDatabase.collection("Admins")
                .document(userUid)
                .set(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "addAdmin: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void setBook(Book book, String bookId, FirebaseCallback callback) {
        fireStoreDatabase.collection("Books")
                .document(bookId)
                .set(book)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "addBook: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getBooks(GetBooksCallback callback) {
        fireStoreDatabase.collection("Books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Book> books = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            books.add(documentSnapshot.toObject(Book.class));
                        }
                        callback.onSuccess(books);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "getBooks: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deleteBook(String bookId, FirebaseCallback callback) {
        fireStoreDatabase.collection("Books")
                .document(bookId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "deleteBook: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void setNews(News news, String newsId, FirebaseCallback callback) {
        fireStoreDatabase.collection("News")
                .document(newsId)
                .set(news)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "addBook: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getNews(GetNewsCallback callback) {
        fireStoreDatabase.collection("News")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<News> news = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            news.add(documentSnapshot.toObject(News.class));
                        }
                        callback.onSuccess(news);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "getNews: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deleteNews(String newsId, FirebaseCallback callback) {
        fireStoreDatabase.collection("News")
                .document(newsId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "deleteBook: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void setPrisonerCard(PrisonerCard prisonerCard, String prisonerCardId, FirebaseCallback callback) {
        fireStoreDatabase.collection("PrisonersCards")
                .document(prisonerCardId)
                .set(prisonerCard)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "addPrisonerCard: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getPrisonersCards(GetPrisonerCardsCallback callback) {
        fireStoreDatabase.collection("PrisonersCards")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<PrisonerCard> prisonerCards = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            prisonerCards.add(documentSnapshot.toObject(PrisonerCard.class));
                        }
                        callback.onSuccess(prisonerCards);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "getPrisonersCards: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deletePrisonerCard(String prisonerCardId, FirebaseCallback callback) {
        fireStoreDatabase.collection("PrisonersCards")
                .document(prisonerCardId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "deletePrisonerCard: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void setPoster(Poster poster, String posterId, FirebaseCallback callback) {
        fireStoreDatabase.collection("Posters")
                .document(posterId)
                .set(poster)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "addPoster: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getPosters(GetPostersCallback callback) {
        fireStoreDatabase.collection("Posters")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Poster> posters = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            posters.add(documentSnapshot.toObject(Poster.class));
                        }
                        callback.onSuccess(posters);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "getPosters: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deletePoster(String posterId, FirebaseCallback callback) {
        fireStoreDatabase.collection("Posters")
                .document(posterId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "deletePoster: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    // Storage
    public void uploadPdfFile(Uri pdfFileUri, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage.getReference("PDF-Books")
                .child(UUID.randomUUID().toString() + ".pdf");

        uploadFile(pdfFileUri, reference, callback);
    }

    public void uploadImage(Uri imageUri, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage.getReference("Images")
                .child(UUID.randomUUID().toString() + ".jpg");

        uploadFile(imageUri, reference, callback);
    }

    private void uploadFile(Uri fileUri, StorageReference reference, UploadFileCallback callback) {
        reference.putFile(fileUri)
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.d(TAG, "Upload progress: " + progress);
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful() && task.getException() != null) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    callback.onSuccess(uri.toString());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.e(TAG, "Failed to upload image: " + e.getMessage());
                });
    }

    /* Interfaces------------------- */
    public interface FirebaseCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface FirebaseAuthCallback {
        void onSuccess(String userUid);

        void onFailure(String errorMessage);
    }

    public interface GetBooksCallback {
        void onSuccess(ArrayList<Book> books);

        void onFailure(String errorMessage);
    }

    public interface GetNewsCallback {
        void onSuccess(ArrayList<News> news);

        void onFailure(String errorMessage);
    }

    public interface GetPrisonerCardsCallback {
        void onSuccess(ArrayList<PrisonerCard> prisonerCards);

        void onFailure(String errorMessage);
    }

    public interface GetPostersCallback {
        void onSuccess(ArrayList<Poster> posters);

        void onFailure(String errorMessage);
    }

    public interface GetStatisticsCallback {
        void onSuccess(Map<String, String> statistics);

        void onFailure(String errorMessage);
    }

    public interface UploadFileCallback {
        void onSuccess(String pdfUrl);

        void onFailure(String errorMessage);
    }
}
