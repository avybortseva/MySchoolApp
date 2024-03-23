package we.nstu.registration.Event;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.News.News;
import we.nstu.registration.News.SchoolNews;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.DialogAddEventBinding;
import we.nstu.registration.databinding.FragmentNewsBinding;

public class AddEventDialog extends DialogFragment {

    private DialogAddEventBinding binding;
    private final int GALLERY_REQUEST = 1;
    private Uri uri;
    private String email;
    private List<Event> eventList;
    private String eventDate;
    private String eventTime;
    private OnEventAddedListener listener;
    private Context context;
    private FirebaseFirestore database;

    public void setOnEventsAddedListener(OnEventAddedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddEventBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();

        binding.addEventButton.setOnClickListener(v -> {

            if (eventDate == null || eventTime == null || binding.eventTitleEditText.getText().toString() == "" || binding.eventDescriptionEditText.getText().toString() == "")
            {
                return;
            }

            email = MainActivity.getEmail(getContext());

            DocumentReference usersReference = database.collection("users").document(email);

            usersReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);

                            database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                    .addOnSuccessListener(ds -> {

                                        String eventsJson = ds.get("eventsJson").toString();

                                        if(eventsJson != "")
                                        {
                                            SchoolEvent schoolEvent = SchoolEvent.eventFromJson(eventsJson);
                                            eventList = schoolEvent.getEventList();
                                        }
                                        else
                                        {
                                            eventList = new ArrayList<>();
                                        }

                                        eventList.add(new Event(binding.eventTitleEditText.getText().toString(),binding.eventDescriptionEditText.getText().toString(), eventTime + " | " + eventDate, String.valueOf(eventList.size())));

                                        SchoolEvent newSchoolEvent = new SchoolEvent(eventList);
                                        String newEventsJson = newSchoolEvent.eventToJson();

                                        database.collection("schools")
                                                .document(String.valueOf(user.getSchoolID()))
                                                .collection("classrooms")
                                                .document(String.valueOf(user.getClassroomID()))
                                                .update("eventsJson", newEventsJson)
                                                .addOnSuccessListener(runnable -> {

                                                    Toast.makeText(context, "Событие успешно создано", Toast.LENGTH_SHORT).show();

                                                });

                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference newsRef = storageRef.child("Schools").child(String.valueOf(user.getSchoolID())).child(String.valueOf(user.getClassroomID())).child("Events").child(String.valueOf(eventList.size() - 1));

                                        if (uri!=null)
                                        {

                                            StorageReference imageRef = newsRef.child( "Event_logo.jpg");
                                            imageRef.putFile(uri);

                                        }

                                        if (listener != null) {
                                            listener.onEventAdded();
                                        }

                                    });

                        }
                    });


            dismiss();
        });

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                eventDate =  String.format("%02d", dayOfMonth) + "." + String.format("%02d", monthOfYear + 1) + "." + year;
            }
        };

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                eventTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            }
        };


        // Получаем текущую дату
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        int initialHour = calendar.get(Calendar.HOUR);
        int initialMinute = calendar.get(Calendar.MINUTE);

        binding.dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    dateSetListener,
                    initialYear,
                    initialMonth,
                    initialDay
            );
            datePickerDialog.show();
        });

        binding.timeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
                    timeSetListener,
                    initialHour,
                    initialMinute,
                    true
            );
            timePickerDialog.show();
        });

        binding.imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            uri = selectedImage;
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

    public interface OnEventAddedListener {
        void onEventAdded();
    }
}
