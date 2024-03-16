package we.nstu.registration.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import we.nstu.registration.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.nameTextView.setText(chatMessage.getMessageSender());
        holder.messageTextView.setText(chatMessage.getMessageText());
        holder.timeTextView.setText(chatMessage.getMessageTime());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, messageTextView, timeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            messageTextView = itemView.findViewById(R.id.message);
            timeTextView = itemView.findViewById(R.id.time);
        }
    }
}
