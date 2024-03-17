package we.nstu.registration.Profile;

import static android.app.Activity.RESULT_OK;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import we.nstu.registration.Login.Login;
import we.nstu.registration.MainActivity;
import we.nstu.registration.Registration.Registration;
import we.nstu.registration.Settings.Settings;
import we.nstu.registration.User.User;
import we.nstu.registration.UsersActivity;
import we.nstu.registration.databinding.FragmentProfileBinding;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private final int GALLERY_REQUEST = 1;
    private String email;
    private StorageReference usersRef;
    private StorageReference storageRef;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.progressBar.setVisibility(View.VISIBLE);

        email = MainActivity.getEmail(getContext());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        usersRef = storageRef.child("Users").child(email);
        StorageReference imageRef = usersRef.child("profile_image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (isAdded()) {
                Glide.with(requireActivity())
                        .load(uri)
                        .into(binding.imageView);
            }
        });

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        binding.fullName.setText(user.getFirstName() + " " + user.getSecondName() + " " + user.getSurname());
                        binding.email.setText(email);
                        binding.accessLevel.setText(user.accessLevelToText());


                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {
                                    binding.schoolName.setText(ds.get("schoolName").toString());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                });

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                .addOnSuccessListener(ds -> {
                                    binding.classNumber.setText(ds.get("classroomName").toString());
                                    binding.teatcherFullName.setText(ds.get("teacher").toString());
                                    binding.progressBar.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                });

                    }
                }).addOnFailureListener(e->{
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });


        binding.logoutButton.setOnClickListener(v -> {
            MainActivity.clearEmail(getContext());
            Toast.makeText(getContext(), "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(requireActivity(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            requireActivity().finishAffinity();
        });

        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        });

        binding.deleteAccount.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Подтвердите удаление аккаунта");
            builder.setMessage("Вы уверены, что хотите удалить аккаунт?");
            builder.setPositiveButton("Да", (dialog, which) -> {

                DocumentReference usersReference2 = MainActivity.database.collection("users").document(email);
                usersReference2.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                deleteAccount(user);
                            }
                        });
            });
            builder.setNegativeButton("Нет", (dialog, which) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        });

        binding.createNewInvitation.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), UsersActivity.class));
        });

        return view;
    }

    public void deleteAccount(User user)
    {

        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                .addOnSuccessListener(ds -> {
                    String studentsID = ds.get("studentsID").toString();
                    // Удаляем учетную запись пользователя из списка
                    String updatedStudentsID = studentsID.replace(user.getEmail() + " ", "").replace(" " + user.getEmail(), "");
                    // Обновляем список почт в базе данных
                    MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).update("studentsID", updatedStudentsID);
                });


        MainActivity.database.collection("users").document(email).delete()
                .addOnSuccessListener(runnable -> {
                    MainActivity.clearEmail(getContext());
                    Intent i = new Intent(requireActivity(), Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    requireActivity().finishAffinity();
                    Toast.makeText(getContext(), "Вы успешно удалили аккаунт", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Не удалось удалить аккаунт. Попробуйте позже", Toast.LENGTH_SHORT).show();
                });

        //Удаление картинки
        usersRef = storageRef.child("Users").child(email);
        StorageReference imageRef2 = usersRef.child("profile_image.jpg");
        imageRef2.delete();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                bitmap = cropToSquare(bitmap);
                binding.imageView.setImageBitmap(bitmap);

                // Сохранение в Firebase Storage
                StorageReference imageRef = usersRef.child("profile_image.jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data2 = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data2);
                uploadTask.addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(getContext(), "Картинка успешно сохранена!", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = Math.min(width, height);
        int newHeight = newWidth;

        int cropW = (width - height) / 2;
        cropW = Math.max(cropW, 0);
        int cropH = (height - width) / 2;
        cropH = Math.max(cropH, 0);

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

}