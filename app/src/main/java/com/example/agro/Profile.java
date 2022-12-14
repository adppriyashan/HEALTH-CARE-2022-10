package com.example.agro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agro.Adapters.DeviceListAdapter;
import com.example.agro.Models.Device;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private ImageView profile_back;
    private LinearLayout profilelayout;
    private DatabaseReference mDatabase;
    private ArrayList<EditText> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_back=findViewById(R.id.profile_back_btn);
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fields=new ArrayList<>();

        profilelayout=findViewById(R.id.profilelayout);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("prediction_input").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fields.clear();

                for(DataSnapshot rec:snapshot.getChildren()){

                    TextView label=new TextView(Profile.this);
                    label.setPadding(16,50,16,20);
                    label.setText(getLabel(rec.getKey()));

                    EditText editText=new EditText(Profile.this);
                    editText.setPadding(25,5,25,5);
                    editText.setHeight(180);
                    editText.setBackground(getDrawable(R.drawable.textfieldborder));
                    editText.setText(rec.getValue().toString());
                    profilelayout.addView(label);
                    profilelayout.addView(editText);

                    fields.add(editText);
                }

                Button updateButton=new Button(Profile.this);
                updateButton.setBackground(getDrawable(R.drawable.btn_design));
                updateButton.setHeight(200);
                updateButton.setText("Update Profile");
                updateButton.setTextColor(getResources().getColor(R.color.white));
                updateButton.setPadding(50,20,50,20);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 50, 0, 50);
                updateButton.setLayoutParams(params);


                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] fieldsKeys = {"Age", "BMI", "Calories", "Fruits","Gender","HeartDiseaseorArrack","Heartrate","Hight_cm","Humidity","SitandBend","Situps","Smoker","Spo2","StepCount","Temp","Veggies","Weight_kg","WorkoutTime","WorkoutType","body_temp"};
                        Map<String,String> updateObject=new HashMap<>();
                        int x=0;
                        for(EditText fie:fields){
                            updateObject.put(fieldsKeys[x],fie.getText().toString());
                            x++;
                        }
                        mDatabase.child("prediction_input").setValue(updateObject);
                        Toast.makeText(Profile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                profilelayout.addView(updateButton);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Data fetching error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getLabel(String key){
        switch (key){
            case "HeartDiseaseorArrack":
                return "Heart Disease";
            case "Heartrate":
                return "Heart Rate";
            case "Hight_cm":
                return "Height (cm)";
            case "StepCount":
                return "Step Count";
            case "Weight_kg":
                return "Weight (kg)";
            case "WorkoutTime":
                return "Workout Time";
            case "WorkoutType":
                return "Workout Type";
            case "body_temp":
                return "Body Temprature";
            default:return key;
        }

    }
}