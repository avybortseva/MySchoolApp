package we.nstu.registration.News;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private NewsAdapter adapter;
    private RecyclerView recyclerView;

    public SwipeToDeleteCallback(NewsAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.recyclerView = recyclerView;
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

    private void showConfirmationDialog(View itemView, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Подтвердите удаление");
        builder.setMessage("Вы уверены, что хотите удалить эту новость?");
        builder.setPositiveButton("Да", (dialog, which) -> {
            // Удаление новости из Firestore
            deleteNewsFromFirestore(position);
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

    private void deleteNewsFromFirestore(int position) {
        News news = adapter.getItem(position);
        DocumentReference documentReference = news.getDocumentReference();
        if (documentReference != null) {
            documentReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        adapter.removeItem(position);
                        adapter.notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }
}