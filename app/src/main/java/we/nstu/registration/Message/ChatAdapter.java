package we.nstu.registration.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{

    private static final int VIEW_TYPE_SENT = R.layout.item_message;
    private static final int VIEW_TYPE_RECEIVED = R.layout.item_message_2;

    private List<ChatMessage> chatMessages;
    private FirebaseFirestore database;
    private Context context;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        if (chatMessages.get(position).getMessageSender().equals(MainActivity.getEmail(context)))
        {
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_2, parent, false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        }
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {

        database = FirebaseFirestore.getInstance();

        ChatMessage chatMessage = chatMessages.get(position);



        holder.messageTextView.setText(chatMessage.getMessageText());
        holder.timeTextView.setText(chatMessage.getMessageTime().substring(0,11));

        //Установка аватарки
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference usersRef = storageRef.child("Users").child(chatMessage.getMessageSender());
        StorageReference imageRef = usersRef.child("profile_image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView)
                    .load(uri)
                    .into(holder.avatar);
        });
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, messageTextView, timeTextView;
        ImageView avatar;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            messageTextView = itemView.findViewById(R.id.message);
            timeTextView = itemView.findViewById(R.id.time);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
