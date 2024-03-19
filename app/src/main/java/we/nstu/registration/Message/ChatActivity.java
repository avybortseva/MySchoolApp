package we.nstu.registration.Message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import we.nstu.registration.MainActivity;
import we.nstu.registration.News.NewsFull;
import we.nstu.registration.News.SchoolNews;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ChatActivityBinding;
import we.nstu.registration.databinding.FragmentMessageBinding;

public class ChatActivity extends AppCompatActivity
{

    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private ChatActivityBinding binding;
    private FirebaseDatabase databaseRealtime;
    private FirebaseFirestore database;
    private String chatIndex;
    private String companionEmail;
    private StorageReference usersRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        databaseRealtime = FirebaseDatabase.getInstance();

        companionEmail = getIntent().getStringExtra("companionEmail");
        chatIndex = getChatIndex(companionEmail).replace(".", "_dot_");

        DocumentReference usersReference = database.collection("users").document(companionEmail);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    binding.name.setText(user.getFirstName() + " " + user.getSecondName() + " " + user.getSurname());
                });


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        binding.recyclerView.setAdapter(chatAdapter);

        DatabaseReference reference = databaseRealtime.getReference("chats").child(chatIndex);
        reference.get()
                .addOnSuccessListener(dataSnapshot -> {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String chatMessageJson = ds.getValue(String.class);

                        ChatMessage chatMessage = chatMessageFromJson(chatMessageJson);

                        chatMessages.add(chatMessage);
                    }

                    if (chatMessages.size() != 0)
                    {
                        chatMessages.remove(0);

                        if(chatMessages.size() > 5)
                        {
                            binding.recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                        }

                    }

                    chatAdapter.notifyDataSetChanged();
                });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        usersRef = storageRef.child("Users").child(companionEmail);
        StorageReference imageRef = usersRef.child("profile_image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(binding.avatar);
        });

        DatabaseReference chatRef = databaseRealtime.getReference("chats").child(chatIndex);
        Query lastItemQuery = chatRef.limitToLast(1);
        lastItemQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String messageJson =  ds.getValue(String.class);
                    ChatMessage chatMessage = chatMessageFromJson(messageJson);
                    chatMessages.add(chatMessage);
                }
                chatAdapter.notifyDataSetChanged();

                if (chatMessages.size() > 5)
                {
                    binding.recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибки
            }
        });

        binding.messageSendButton.setOnClickListener(v -> {
            String messageText = binding.messageField.getText().toString();
            String email = MainActivity.getEmail(getApplicationContext());

            ChatMessage chatMessage = new ChatMessage(email, companionEmail, messageText, getCurrentTime(), false);

            if (!messageText.isEmpty()) {
                DatabaseReference myRef = databaseRealtime.getReference("chats").child(chatIndex).push();
                myRef.setValue(chatMessageToJson(chatMessage));

                binding.messageField.setText("");

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.messageField.getWindowToken(), 0);

                database.collection("users").document(email).get()
                        .addOnSuccessListener(ds -> {

                            String dialogs = ds.get("dialogs").toString();
                            if (dialogs.isEmpty()) {
                                dialogs = companionEmail;
                            } else {
                                dialogs = companionEmail + " " + dialogs;
                                String[] dialogsArray = dialogs.split(" ");
                                Set<String> dialogsSet = new HashSet<>(Arrays.asList(dialogsArray));
                                dialogs = String.join(" ", dialogsSet);
                            }

                            database.collection("users").document(email).update("dialogs", dialogs);
                        });

                database.collection("users").document(companionEmail).get()
                        .addOnSuccessListener(ds -> {

                            String dialogs = ds.get("dialogs").toString();
                            if (dialogs.isEmpty()) {
                                dialogs = email;
                            } else {
                                dialogs = email + " " + dialogs;
                                String[] dialogsArray = dialogs.split(" ");
                                Set<String> dialogsSet = new HashSet<>(Arrays.asList(dialogsArray));
                                dialogs = String.join(" ", dialogsSet);
                            }

                            database.collection("users").document(companionEmail).update("dialogs", dialogs);
                        });

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Введите сообщение", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backToDialogs.setOnClickListener(v -> {
            finish();
        });



    }

    public String getChatIndex(String companionEmail)
    {
        List<String> interlocutors = new ArrayList<>();
        interlocutors.add(companionEmail);
        interlocutors.add(MainActivity.getEmail(getApplicationContext()));
        Collections.sort(interlocutors);
        return interlocutors.get(0) + " to " + interlocutors.get(1);
    }

    public static String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        ZonedDateTime moscowDateTime = localDateTime.atZone(moscowZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return moscowDateTime.format(formatter);
    }

    public String chatMessageToJson(ChatMessage chatMessage)
    {
        Gson gson = new Gson();
        return gson.toJson(chatMessage);
    }

    public static ChatMessage chatMessageFromJson(String json) {
        Gson gson = new Gson();
        Type chatMessageType = new TypeToken<ChatMessage>(){}.getType();
        return gson.fromJson(json, chatMessageType);
    }
}
