package com.example.nhom10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student_MD> studentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Student_MD student);
    }

    public StudentAdapter(List<Student_MD> studentList) {
        this.studentList = studentList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student_MD student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvId.setText(student.getId());
        holder.tvClass.setText(student.getStudentClass());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvClass;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_name);
            tvId = itemView.findViewById(R.id.tv_student_id);
            tvClass = itemView.findViewById(R.id.tv_student_class);
        }
    }
}
