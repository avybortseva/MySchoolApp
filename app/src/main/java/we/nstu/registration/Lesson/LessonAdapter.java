package we.nstu.registration.Lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import we.nstu.registration.R;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;

    public LessonAdapter(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lessons_card, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.lessonNumber.setText(lesson.getLessonNumber());
        if(lesson.getTeacher() == "" || lesson.getClassroom() == "" || lesson.getLessonStartTime()=="" || lesson.getLessonEndTime() == "")
        {
            holder.lessonInfo.setText("Расписание на эту дату еще не составленно!");
        }
        else
        {
            holder.lessonInfo.setText(lesson.getLessonName() + " " + lesson.getLessonStartTime() + " - " + lesson.getLessonEndTime() + "\n" + lesson.getTeacher() + " " + lesson.getClassroom() + " кабинет");
        }
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonNumber, lessonInfo;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonNumber = itemView.findViewById(R.id.lessonNumber);
            lessonInfo = itemView.findViewById(R.id.lessonName);
        }
    }
}