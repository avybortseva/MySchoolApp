package we.nstu.registration.Event;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.EventActivityBinding;

public class EventFull extends AppCompatActivity {
    private EventActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EventActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String eventId = getIntent().getStringExtra("eventID");

        String email = MainActivity.getEmail(getApplicationContext());

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {

                                    // Assuming the correct field name for the events JSON is "eventsJson"
                                    Object eventsJsonObject = ds.get("eventsJson");
                                    if (eventsJsonObject != null) {
                                        String eventsJson = eventsJsonObject.toString();

                                        // Assuming SchoolEvent.eventFromJson is a method that parses the JSON string
                                        SchoolEvent schoolEvent = SchoolEvent.eventFromJson(eventsJson);

                                        List<Event> eventList = schoolEvent.getEventList();

                                        for (Event event : eventList) {
                                            if (event.getEventID().equals(eventId)) {
                                                binding.titleText.setText(event.getEventName()); // Assuming eventName is the title
                                                binding.textBody.setText(event.getEventDescription()); // Assuming eventDescription is the body
                                                // Format the event time if needed
                                                binding.date.setText(event.getEventTime());
                                                break;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(EventFull.this, "Events JSON is null", Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(EventFull.this, "Ошибка при загрузке событий", Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(EventFull.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EventFull.this, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });

    }
}