package we.nstu.registration.Message;

import static we.nstu.registration.Message.ChatActivity.chatMessageFromJson;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import we.nstu.registration.Event.Event;
import we.nstu.registration.Event.EventAdapter;
import we.nstu.registration.Login.Login;
import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private MessageAdapter.OnItemClickListener listener;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_card, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        //Установка ФИО собеседника
        DocumentReference reference = MainActivity.database.collection("users").document(message.getCompanionEmail());
        reference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists())
                    {
                        User user = documentSnapshot.toObject(User.class);

                        holder.titleTextView.setText(user.getSecondName() + " " + user.getFirstName() + " " + user.getSurname());

                    }
                    else
                    {

                    }
                });

        // Установка последнего сообщения
        String chatIndex = getChatIndex(message.getCompanionEmail(), message.getEmail()).replace(".", "_dot_");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef = database.getReference("chats").child(chatIndex);
        messageRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String messageJson =  ds.getValue(String.class);
                    ChatMessage chatMessage = chatMessageFromJson(messageJson);
                    holder.textTextView.setText(chatMessage.getMessageText());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки
            }
        });

        //Установка аватарки
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference usersRef = storageRef.child("Users").child(message.getCompanionEmail());
        StorageReference imageRef = usersRef.child("profile_image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView)
                    .load(uri)
                    .into(holder.imageView);
        });

        holder.titleTextView.setText(message.getTitle());
        holder.textTextView.setText(message.getText());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, textTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
        }
    }

    public String getChatIndex(String companionEmail, String email)
    {
        List<String> interlocutors = new ArrayList<>();
        interlocutors.add(companionEmail);
        interlocutors.add(email);
        Collections.sort(interlocutors);
        return interlocutors.get(0) + " to " + interlocutors.get(1);
    }

}

