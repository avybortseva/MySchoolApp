package we.nstu.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import we.nstu.registration.User.User;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList)
    {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textNameUser.setText(user.getFirstName() + " " + user.getSecondName() + " " + user.getSurname());
        holder.textStatusUser.setText(user.accessLevelToText());

        //Установка имени школьного класса
        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                .addOnSuccessListener(ds -> {
                    holder.textClassUser.setText(ds.get("classroomName").toString());
                });

        //Установка аватарки
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference usersRef = storageRef.child("Users").child(user.getEmail());
        StorageReference imageRef = usersRef.child("profile_image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView)
                    .load(uri)
                    .into(holder.photoUser);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textNameUser;
        public TextView textStatusUser;
        public TextView textClassUser;
        public ImageView photoUser;

        public UserViewHolder(View itemView) {
            super(itemView);
            textNameUser = itemView.findViewById(R.id.textNameUser);
            textStatusUser = itemView.findViewById(R.id.textStatusUser);
            textClassUser = itemView.findViewById(R.id.textClassUser);
            photoUser = itemView.findViewById(R.id.photoUser);
        }
    }
}
