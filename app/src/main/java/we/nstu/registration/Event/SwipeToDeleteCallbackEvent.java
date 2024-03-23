package we.nstu.registration.Event;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import we.nstu.registration.MainActivity;
import we.nstu.registration.News.News;
import we.nstu.registration.News.SchoolNews;
import we.nstu.registration.R;
import we.nstu.registration.User.User;

public class SwipeToDeleteCallbackEvent extends ItemTouchHelper.SimpleCallback {

    private final EventAdapter adapter;
    private final Context context;
    private FirebaseFirestore database;

    public SwipeToDeleteCallbackEvent(EventAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
            showConfirmationDialog(viewHolder.itemView, position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftActionIcon(R.drawable.delete)
                .addSwipeRightActionIcon(R.drawable.delete)
                .addBackgroundColor(Color.RED)
                .addCornerRadius(5,5)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void showConfirmationDialog(View itemView, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Подтвердите удаление");
        builder.setMessage("Вы уверены, что хотите удалить это событие?");
        builder.setPositiveButton("Да", (dialog, which) -> {
            // Удаление события из Firestore
            deleteNewsFromFirestore(position);
            adapter.notifyItemRemoved(position);
        });
        builder.setNegativeButton("Нет", (dialog, which) -> {
            // Отмена удаления
            adapter.notifyItemChanged(position);
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true); // Разрешить закрытие диалога при нажатии за пределами диалога
        dialog.setCanceledOnTouchOutside(false); // Запретить закрытие диалога при свайпе вне диалога
        dialog.show();
    }

    private void deleteNewsFromFirestore(int position)
    {

        if (database == null)
        {
            database = FirebaseFirestore.getInstance();
        }

        List<Event> eventList = adapter.getEventList();
        String eventID = eventList.get(position).getEventID();
        eventList.remove(position);

        Collections.reverse(eventList);

        String email = MainActivity.getEmail(context);

        DocumentReference usersReference = database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        SchoolEvent schoolEvent = new SchoolEvent(eventList);
                        if (eventList.size() == 0)
                        {
                            String eventsJson = "";
                            updateEventsJson(user, eventsJson, eventID);
                        }
                        else
                        {
                            String eventsJson = schoolEvent.eventToJson();
                            updateEventsJson(user, eventsJson, eventID);
                        }
                        Toast.makeText(context, "Событие успешно удалено",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Не удалось удалить событие", Toast.LENGTH_SHORT).show();
                });

    }

    private void updateEventsJson(User user, String newEventsJson, String eventID) {

        if (database == null)
        {
            database = FirebaseFirestore.getInstance();
        }

        DocumentReference schoolDocument = database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID()));
        schoolDocument.update("eventsJson", newEventsJson);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference eventsRef = storageRef.child("Schools").child(String.valueOf(user.getSchoolID())).child(String.valueOf(user.getClassroomID())).child("Events").child(eventID);

        eventsRef.listAll().addOnSuccessListener(listResult -> {

            List<StorageReference> items = listResult.getItems();
            List<Task<Void>> deleteTasks = new ArrayList<>();

            for (StorageReference item : items) {
                deleteTasks.add(item.delete());
            }
        });
    }
}