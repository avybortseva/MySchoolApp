package we.nstu.registration.News;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.DialogAddNewsBinding;

public class AddNewsDialog extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private DialogAddNewsBinding binding;
    private List<News> newsList;
    private String email;
    private List<Uri> uriList;

    public AddNewsDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddNewsBinding.inflate(inflater, container, false);

        binding.addNewsButton.setOnClickListener(v -> {

            email = MainActivity.getEmail(getContext());

            DocumentReference usersReference = MainActivity.database.collection("users").document(email);

            usersReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);

                            MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                    .addOnSuccessListener(ds -> {

                                        String newsJson = ds.get("newsJson").toString();

                                        if(newsJson != "")
                                        {
                                            SchoolNews schoolNews = SchoolNews.newsFromJson(newsJson);
                                            newsList = schoolNews.getNewsList();
                                        }
                                        else
                                        {
                                            newsList = new ArrayList<>();
                                        }

                                        LocalDateTime currentDateTime = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                                        String formattedDateTime = currentDateTime.format(formatter);

                                        newsList.add(new News(binding.newsTitleEditText.getText().toString(),binding.newsDescriptionEditText.getText().toString(),formattedDateTime, new ArrayList<String>(),String.valueOf(newsList.size())));

                                        SchoolNews newSchoolNews = new SchoolNews(newsList);
                                        String newNewsJson = newSchoolNews.newsToJson();

                                        MainActivity.database.collection("schools")
                                                .document(String.valueOf(user.getSchoolID()))
                                                .update("newsJson", newNewsJson);

                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference newsRef = storageRef.child("Schools").child(String.valueOf(user.getSchoolID())).child("News").child(String.valueOf(newsList.size() - 1));

                                        if (uriList!=null)
                                        {

                                            for (int i = 0; i < uriList.size(); i++) {
                                                StorageReference imageRef = newsRef.child(String.valueOf(i) + ".jpg");
                                                imageRef.putFile(uriList.get(i));
                                            }
                                        }
                                    });

                        }
                    });

            dismiss();
        });

        binding.imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });



        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getClipData() != null)
        {
            int count = data.getClipData().getItemCount();

            if (count > 5) {
                Toast.makeText(getActivity(), "Вы можете выбрать до 5 изображений", Toast.LENGTH_SHORT).show();
                return;
            }

            if (count < 1) {
                Toast.makeText(getActivity(), "Вы должны выбрать хотя бы одно изображение", Toast.LENGTH_SHORT).show();
                return;
            }

            uriList = new ArrayList<>();


            for (int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                uriList.add(imageUri);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        params.width = (int) (displayMetrics.widthPixels * 0.9);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        return dialog;
    }
}