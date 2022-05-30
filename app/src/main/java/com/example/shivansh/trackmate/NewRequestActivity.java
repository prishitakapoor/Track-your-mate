package com.example.shivansh.trackmate;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NewRequestActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        final EditText professor = findViewById(R.id.edit_professor);
        final EditText regarding = findViewById(R.id.edit_regarding);
        final EditText desc = findViewById(R.id.edit_desc);

        Button submit = findViewById(R.id.submt);
        auth = FirebaseAuth.getInstance();
        final String t = auth.getCurrentUser().getUid();
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                int off=0;
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("request");
                final String userId = mDatabase.push().getKey();
                final String reg = regarding.getText().toString();
                final String udesc = desc.getText().toString();
                ArrayList<String> bad = new ArrayList<>();
                bad.add("Shit");
                bad.add("Fuck");
                bad.add("Hit");
                bad.add("Hell");
                bad.add("Lol");
                bad.add("Ass");
                bad.add("Dick");
                bad.add("Pussy");
                bad.add("Boobs");
                bad.add("Sexual");
                bad.add("Penis");
                bad.add("Jerk");
                bad.add("Nigger");
                bad.add("Slut");
                bad.add("Whore");
                bad.add("Cunt");
                bad.add("Crap");
                bad.add("Shit");
                bad.add("Vagina");
                bad.add("Wank");
                bad.add("Aroused");
                String[] desc = udesc.split(" ");
                for (int i=0;i<desc.length;i++) {
                    for (int j=0;j<bad.size();j++) {
                        if (bad.get(j).equalsIgnoreCase(desc[i])) {
                            off = 1;
                            Toast.makeText(NewRequestActivity.this, "This Description Contains Offensive Words such as : " + desc[i], Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if(off==0) {
                    final String prof = professor.getText().toString();
                    final String[] profRoll = new String[1];
                    final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("professor");
                    mDatabase2.orderByChild("name").equalTo(prof).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.e("size", String.valueOf(snapshot.getChildrenCount()));
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Professor trans = postSnapshot.getValue(Professor.class);
                                profRoll[0] = trans.getEmail();
                            }
                            makeRequest(udesc,profRoll[0],mDatabase,userId,reg);
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError) {
                            /*
                             * You may print the error message.
                             **/
                        }
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeRequest(final String udesc, final String prof, DatabaseReference mDatabase, final String userId, final String reg) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String[] data = dtf.format(now).split(" ");
        Requests r = new Requests(udesc,"Still Not Received",prof,auth.getCurrentUser().getEmail(),"roll number","pending",data[0],data[1],"Not Received","Not Received",reg);
        mDatabase.child(userId).setValue(r);
        Toast.makeText(NewRequestActivity.this,"Request Made",Toast.LENGTH_LONG).show();
        finish();
    }

}