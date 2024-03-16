package we.nstu.registration.Event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> implements ItemTouchHelperAdapterEvent{

    private List<Event> eventList;
    private OnItemClickListener listener;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getEventTime());
        holder.eventDescription.setText(event.getEventDescription());

        String email = MainActivity.getEmail(holder.itemView.getContext());

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child("Schools").child(String.valueOf(user.getSchoolID())).child("Events").child(event.getEventID()).child("Event_logo.jpg");

                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .into(holder.eventImage)).addOnFailureListener(e -> {
                            // Handle any errors
                        });
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventTime, eventDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventDescription = itemView.findViewById(R.id.eventDescription);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void removeItem(int position) {
        eventList.remove(position);
        notifyItemRemoved(position);
        // Здесь вам нужно будет также удалить элемент из базы данных
        // Например, если у вас есть ссылка на DocumentReference для элемента, вы можете его удалить так:
        // newsList.get(position).getDocumentReference().delete();
    }

    public Event getItem(int position) {
        return eventList.get(position);
    }
    @Override
    public void onItemDismiss(int position) {
        eventList.remove(position);
        notifyItemRemoved(position);
    }
}