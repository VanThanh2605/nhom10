package com.example.nhom10;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_AC extends AppCompatActivity {
    private EditText etName, etId, etClass;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student_MD> studentList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);

        etName = findViewById(R.id.et_student_name);
        etId = findViewById(R.id.et_student_id);
        etClass = findViewById(R.id.et_course);
        recyclerView = findViewById(R.id.rv_students);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");

        findViewById(R.id.btn_add).setOnClickListener(view -> addStudent());
        findViewById(R.id.btn_update).setOnClickListener(view -> updateStudent());
        findViewById(R.id.btn_delete).setOnClickListener(view -> deleteStudent());

        fetchStudents();
    }

    private void addStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String studentClass = etClass.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(studentClass)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student_MD student = new Student_MD(id, name, studentClass);
        databaseReference.child(id).setValue(student)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String studentClass = etClass.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student_MD student = new Student_MD(id, name, studentClass);
        databaseReference.child(id).setValue(student)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                Toast.makeText(Student_AC.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
