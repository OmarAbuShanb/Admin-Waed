package waed.dev.adminhoria.firebase.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import waed.dev.adminhoria.firebase.utils.FirebaseUtils;
import waed.dev.adminhoria.models.Album;
import waed.dev.adminhoria.models.AlbumImage;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.models.PrisonerCard;
import waed.dev.adminhoria.models.Statistic;
import waed.dev.adminhoria.models.Video;
import waed.dev.adminhoria.models.WhatsAppTweet;

public class FirebaseController {
    private static final String TAG = "FirebaseController";

    private enum FireStoreCollection {
        Admins,
        Statistics,
        Books,
        News,
        PrisonersCards,
        Posters,
        Albums,
        Images,
        Videos,
        WhatsAppTweets
    }

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
    public void updateUrgentNews(String urgentNewsText, FirebaseCallback callback) {
        realTimeDatabase.getReference("Urgent")
                .child("newsText")
                .setValue(urgentNewsText)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateUrgentNews: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void updateWhatsAppGroupUrl(String newUrl, FirebaseCallback callback) {
        realTimeDatabase.getReference("WhatsAppGroupUrl")
                .child("url")
                .setValue(newUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateWhatsAppGroupUrl: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    // FireStore
    public void addAdmin(String userUid, FirebaseCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userUid);

        fireStoreDatabase.collection(FireStoreCollection.Admins.name())
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

    public <T> void getData(String collectionPath, Class<T> modelType, GetDataCallback<T> callback) {
        fireStoreDatabase.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<T> dataList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            dataList.add(documentSnapshot.toObject(modelType));
                        }
                        callback.onSuccess(dataList);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "get" + modelType.getName() + ": " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public <T> void setData(String collectionPath, T model, String uuid, FirebaseCallback callback) {
        fireStoreDatabase.collection(collectionPath)
                .document(uuid)
                .set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "set" + model.getClass().getName() + ": " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deleteData(String collectionPath, String uuid, FirebaseCallback callback) {
        fireStoreDatabase.collection(collectionPath)
                .document(uuid)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "delete" + collectionPath + ": " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    /* ****************************************************************************************** */

    public void setStatistic(Statistic statistic, FirebaseCallback callback) {
        setData(FireStoreCollection.Statistics.name(), statistic, statistic.getUuid(), callback);
    }

    public void getStatistics(GetDataCallback<Statistic> callback) {
        getData(FireStoreCollection.Statistics.name(), Statistic.class, callback);
    }

    public void deleteStatistic(String statisticsUuid, FirebaseCallback callback) {
        deleteData(FireStoreCollection.Statistics.name(), statisticsUuid, callback);
    }

    public void uploadStatisticFile(String uuid, Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Statistics.name())
                .child(uuid)
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    public void deleteStatisticFiles(String uuid, FirebaseCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Statistics.name())
                .child(uuid);

        deleteFileUsingReference(reference, callback);
    }

    /* ****************************************************************************************** */

    public void setBook(Book book, FirebaseCallback callback) {
        setData(FireStoreCollection.Books.name(), book, book.getUuid(), callback);
    }

    public void getBooks(GetDataCallback<Book> callback) {
        getData(FireStoreCollection.Books.name(), Book.class, callback);
    }

    public void deleteBook(String bookUuid, FirebaseCallback callback) {
        deleteData(FireStoreCollection.Books.name(), bookUuid, callback);
    }

    public void uploadBookFile(String uuid, Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Books.name())
                .child(uuid)
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    public void deleteBookFiles(String uuid, FirebaseCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Books.name())
                .child(uuid);

        deleteFileUsingReference(reference, callback);
    }

    /* ****************************************************************************************** */

    public void setNews(News news, FirebaseCallback callback) {
        setData(FireStoreCollection.News.name(), news, news.getUuid(), callback);
    }

    public void getNews(GetDataCallback<News> callback) {
        getData(FireStoreCollection.News.name(), News.class, callback);
    }

    public void deleteNews(String newsId, FirebaseCallback callback) {
        deleteData(FireStoreCollection.News.name(), newsId, callback);
    }

    public void uploadNewsImage(Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.News.name())
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    /* ****************************************************************************************** */

    public void setPrisonerCard(PrisonerCard prisonerCard, FirebaseCallback callback) {
        setData(FireStoreCollection.PrisonersCards.name(), prisonerCard, prisonerCard.getUuid(), callback);
    }

    public void getPrisonersCards(GetDataCallback<PrisonerCard> callback) {
        getData(FireStoreCollection.PrisonersCards.name(), PrisonerCard.class, callback);
    }

    public void deletePrisonerCard(String prisonerCardId, FirebaseCallback callback) {
        deleteData(FireStoreCollection.PrisonersCards.name(), prisonerCardId, callback);
    }

    public void uploadPrisonerImage(Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.PrisonersCards.name())
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    /* ****************************************************************************************** */

    public void setPoster(Poster poster, FirebaseCallback callback) {
        setData(FireStoreCollection.Posters.name(), poster, poster.getUuid(), callback);
    }

    public void getPosters(GetDataCallback<Poster> callback) {
        getData(FireStoreCollection.Posters.name(), Poster.class, callback);
    }

    public void deletePoster(String posterId, FirebaseCallback callback) {
        deleteData(FireStoreCollection.Posters.name(), posterId, callback);
    }

    public void uploadPosterImage(Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Posters.name())
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    /* ****************************************************************************************** */

    public void setAlbum(Album album, FirebaseCallback callback) {
        setData(FireStoreCollection.Albums.name(), album, album.getUuid(), callback);
    }

    public void getAlbums(GetDataCallback<Album> callback) {
        getData(FireStoreCollection.Albums.name(), Album.class, callback);
    }

    public void deleteAlbum(String uuid, FirebaseCallback callback) {
        deleteData(FireStoreCollection.Albums.name(), uuid, callback);
    }

    public void uploadAlbumImage(String uuid, Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Albums.name())
                .child(uuid)
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    /* ****************************************************************************************** */

    public void setAlbumImage(AlbumImage albumImage, String albumUuid, FirebaseCallback callback) {
        fireStoreDatabase
                .collection(FireStoreCollection.Albums.name())
                .document(albumUuid)
                .collection(FireStoreCollection.Images.name())
                .document(albumImage.getUuid())
                .set(albumImage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "setAlbumImage: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void getAlbumImages(String albumUuid, GetDataCallback<AlbumImage> callback) {
        fireStoreDatabase
                .collection(FireStoreCollection.Albums.name())
                .document(albumUuid)
                .collection(FireStoreCollection.Images.name())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<AlbumImage> albumImages = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            albumImages.add(documentSnapshot.toObject(AlbumImage.class));
                        }
                        callback.onSuccess(albumImages);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "getAlbum: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void deleteAlbumImage(String albumImageUuid, String albumUuid, FirebaseCallback callback) {
        fireStoreDatabase
                .collection(FireStoreCollection.Albums.name())
                .document(albumUuid)
                .collection(FireStoreCollection.Images.name())
                .document(albumImageUuid)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "deleteAlbumImage: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }

    public void uploadAlbumImages(String uuid, Uri fileUri, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Albums.name())
                .child(uuid)
                .child(FireStoreCollection.Images.name())
                .child(UUID.randomUUID().toString() + ".jpg");

        uploadFileUri(fileUri, reference, callback);
    }

    /* ****************************************************************************************** */

    public void setVideo(Video video, FirebaseCallback callback) {
        setData(FireStoreCollection.Videos.name(), video, video.getUuid(), callback);
    }

    public void getVideos(GetDataCallback<Video> callback) {
        getData(FireStoreCollection.Videos.name(), Video.class, callback);
    }

    public void deleteVideo(String videoUuid, FirebaseCallback callback) {
        deleteData(FireStoreCollection.Videos.name(), videoUuid, callback);
    }

    public void uploadVideoFile(String uuid, Uri fileUri, String fileName, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Videos.name())
                .child(uuid)
                .child(fileName);

        uploadFileUri(fileUri, reference, callback);
    }

    public void uploadVideoImageBitmap(String uuid, Bitmap bitmapImage, UploadFileCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Videos.name())
                .child(uuid)
                .child(uuid + ".jpg");

        uploadImageByte(bitmapImage, reference, callback);
    }

    public void deleteVideoFiles(String uuid, FirebaseCallback callback) {
        StorageReference reference = firebaseStorage
                .getReference(FireStoreCollection.Videos.name())
                .child(uuid);

        deleteFileUsingReference(reference, callback);
    }

    /* ****************************************************************************************** */
//                .collection(FireStoreCollection.WhatsAppTweets.name())
//                .orderBy("postDate")
//                .limit(3)
//                .get()

    public void setWhatsAppTweets(WhatsAppTweet whatsAppTweet, FirebaseCallback callback) {
        setData(FireStoreCollection.WhatsAppTweets.name(), whatsAppTweet, whatsAppTweet.getUuid(), callback);
    }

    public void getWhatsAppTweets(GetDataCallback<WhatsAppTweet> callback) {
        getData(FireStoreCollection.WhatsAppTweets.name(), WhatsAppTweet.class, callback);
    }

    public void deleteWhatsAppTweets(String whatsAppTweetUuid, FirebaseCallback callback) {
        deleteData(FireStoreCollection.WhatsAppTweets.name(), whatsAppTweetUuid, callback);
    }

    /* ****************************************************************************************** */

    // Storage
//    public void uploadPdfFile(Uri pdfFileUri, UploadFileCallback callback) {
//        StorageReference reference = firebaseStorage.getReference("PDF-Books")
//                .child(UUID.randomUUID().toString() + ".pdf");
//
//        uploadFile(pdfFileUri, reference, callback);
//    }
//
//    public void uploadVideo(Uri videoUri, UploadFileCallback callback) {
//        StorageReference reference = firebaseStorage.getReference("Videos")
//                .child(UUID.randomUUID().toString() + ".mp4");
//
//        uploadFile(videoUri, reference, callback);
//    }
//
//    public void uploadImage(Uri imageUri, UploadFileCallback callback) {
//        StorageReference reference = firebaseStorage.getReference("Images")
//                .child(UUID.randomUUID().toString() + ".jpg");
//
//        uploadFile(imageUri, reference, callback);
//    }


    private void uploadFileUri(Uri fileUri, StorageReference reference, UploadFileCallback callback) {
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
                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.e(TAG, "uploadFile: " + e.getMessage());
                });
    }

    private void uploadImageByte(Bitmap bitmapImage, StorageReference reference, UploadFileCallback callback) {
        byte[] imageByte = getArrayByteFromBitmapImage(bitmapImage);

        reference.putBytes(imageByte)
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
                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.e(TAG, "uploadFile: " + e.getMessage());
                });
    }

    private byte[] getArrayByteFromBitmapImage(Bitmap bitmapImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void deleteFileUsingReference(StorageReference reference, FirebaseCallback callback) {
        reference.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        item.delete();
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.e(TAG, "deleteFileUsingReference: " + e.getMessage());
                });
    }

    public void deleteFileUsingUrl(String fileUrl, FirebaseCallback callback) {
        firebaseStorage
                .getReferenceFromUrl(fileUrl)
                .delete()
                .addOnSuccessListener(uri -> callback.onSuccess())
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                    Log.e(TAG, "deleteFileUsingUrl: " + e.getMessage());
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

    public interface GetDataCallback<T> {
        void onSuccess(ArrayList<T> data);

        void onFailure(String errorMessage);
    }

    public interface UploadFileCallback {
        void onSuccess(String fileUrl);

        void onFailure(String errorMessage);
    }
}
