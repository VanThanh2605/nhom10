package com.example.nhom10;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    private EditText etName, etId, etClass;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student_MD> studentList;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        etName = view.findViewById(R.id.et_student_name);
        etId = view.findViewById(R.id.et_student_id);
        etClass = view.findViewById(R.id.et_course);
        recyclerView = view.findViewById(R.id.rv_students);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");

        view.findViewById(R.id.btn_add).setOnClickListener(v -> addStudent());
        view.findViewById(R.id.btn_update).setOnClickListener(v -> updateStudent());
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> deleteStudent());

        fetchStudents();

        // Lắng nghe sự kiện click vào item
        adapter.setOnItemClickListener(student -> {
            // Đưa dữ liệu lên các EditText
            etId.setText(student.getId());
            etName.setText(student.getName());
            etClass.setText(student.getStudentClass());
        });

        return view;
    }

    private void addStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String studentClass = etClass.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(studentClass)) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student_MD student = new Student_MD(id, name, studentClass);
        databaseReference.child(id).setValue(student)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String studentClass = etClass.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(getContext(), "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student_MD student = new Student_MD(id, name, studentClass);
        databaseReference.child(id).setValue(student)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(getContext(), "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchStudents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student_MD student = dataSnapshot.getValue(Student_MD.class);
                    studentList.add(student);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
