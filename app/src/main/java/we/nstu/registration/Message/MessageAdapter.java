package we.nstu.registration.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import we.nstu.registration.Event.Event;
import we.nstu.registration.Event.EventAdapter;
import we.nstu.registration.R;

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
        holder.imageView.setImageResource(message.getImageResource());
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
}

