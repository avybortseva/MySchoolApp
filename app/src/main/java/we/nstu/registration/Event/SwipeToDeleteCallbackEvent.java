package we.nstu.registration.Event;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import we.nstu.registration.MainActivity;

public class SwipeToDeleteCallbackEvent extends ItemTouchHelper.SimpleCallback {

    private EventAdapter adapter;

    public SwipeToDeleteCallbackEvent(EventAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
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
        builder.setMessage("Вы уверены, что хотите удалить это событие?");
        builder.setPositiveButton("Да", (dialog, which) -> {
            // Удаление события из Firestore
            deleteEventFromFirestore(itemView.getContext(), position);
            adapter.notifyItemChanged(position);
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

    private void deleteEventFromFirestore(Context context, int position) {
        Event event = adapter.getItem(position);
        // Получить ссылку на документ события из Firestore
        DocumentReference documentReference = event.getDocumentReference();
        if (documentReference != null) {
            documentReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        adapter.removeItem(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(context, "Событие удалено", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Обработка ошибки удаления
                        Toast.makeText(context, "Ошибка при удалении события: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}