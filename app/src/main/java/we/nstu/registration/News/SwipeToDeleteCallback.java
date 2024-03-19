package we.nstu.registration.News;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import we.nstu.registration.R;
import we.nstu.registration.User.User;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final NewsAdapter adapter;
    private final Context context;
    private FirebaseFirestore database;

    public SwipeToDeleteCallback(NewsAdapter adapter, Context context) {
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
        builder.setMessage("Вы уверены, что хотите удалить эту новость?");
        builder.setPositiveButton("Да", (dialog, which) -> {
            deleteNewsFromFirestore(position);
            adapter.notifyItemRemoved(position);
        });
        builder.setNegativeButton("Нет", (dialog, which) -> {
            adapter.notifyItemChanged(position);
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteNewsFromFirestore(int position) {

        if (database == null)
        {
            database = FirebaseFirestore.getInstance();
        }

        List<News> newsList = adapter.getNewsList();
        String newsID = newsList.get(position).getNewsID();
        newsList.remove(position);

        Collections.reverse(newsList);

        String email = MainActivity.getEmail(context);

        DocumentReference usersReference = database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        SchoolNews schoolNews = new SchoolNews(newsList);
                        if (newsList.size() == 0)
                        {
                            String newsJson = "";
                            updateNewsJson(user, newsJson, newsID);
                        }
                        else
                        {
                            String newsJson = schoolNews.newsToJson();
                            updateNewsJson(user, newsJson, newsID);
                        }
                        Toast.makeText(context, "Новость успешно удалена", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Не удалось удалить новость", Toast.LENGTH_SHORT).show();
                });

    }

    private void updateNewsJson(User user, String newNewsJson, String newsID) {
        DocumentReference schoolDocument = database.collection("schools").document(String.valueOf(user.getSchoolID()));
        schoolDocument.update("newsJson", newNewsJson);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference newsRef = storageRef.child("Schools").child(String.valueOf(user.getSchoolID())).child("News").child(newsID);

        newsRef.listAll().addOnSuccessListener(listResult -> {
            List<StorageReference> items = listResult.getItems();
            List<Task<Void>> deleteTasks = new ArrayList<>();

            for (StorageReference item : items) {
                deleteTasks.add(item.delete());
            }
        });
    }

}