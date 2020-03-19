package com.hpcc.kursovaya.ui.subjects;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hpcc.kursovaya.R;
import com.hpcc.kursovaya.dao.entity.Speciality;
import com.hpcc.kursovaya.dao.entity.Subject;
import com.hpcc.kursovaya.dao.entity.constant.ConstantEntity;
import com.hpcc.kursovaya.dao.entity.query.DBManager;

import java.util.LinkedHashMap;
import java.util.Map;

import io.realm.RealmList;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditSubjectActivity extends AppCompatActivity {
    private static final String TAG = EditSubjectActivity.class.getSimpleName();

    private int pickDefaultColor;
    private Button colorPickButton;
    private EditText subjectEditText;

    private Map<Speciality, EditText> map = new LinkedHashMap<>();
    private Subject subject = new Subject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_path_150));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);

        TextView textCont = (TextView)findViewById(R.id.toolbar_title);
        textCont.setText("Редагування предмету");

        Intent intent = getIntent();
        subject = intent.getParcelableExtra("editSubject");

        colorPickButton = (Button) findViewById(R.id.pickColorBtn);
        colorPickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        pickDefaultColor = getResources().getColor(R.color.sideBar);

        LinearLayout parent = findViewById(R.id.spinnerSpeciality);

        final RealmList<Speciality> specialityList = DBManager.readAll(Speciality.class);
        for(int i = 0 ; i< specialityList.size();i++){
            LinearLayout specLayout = new LinearLayout(this);
            specLayout.setOrientation(LinearLayout.HORIZONTAL);
            specLayout.setWeightSum(10);

            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            specLayout.setLayoutParams(LLParams);
            final EditText hourEditTxt = new EditText(this);
            CheckBox checkSpecHour = new CheckBox(this);

            final Speciality speciality = specialityList.get(i);
            if (subject.containsKey(speciality)){
                checkSpecHour.setChecked(true);
                hourEditTxt.setText(String.valueOf(subject.get(speciality)));
                subject.getSpecialityCountHourMap().remove(speciality);
            }
            checkSpecHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        map.put(speciality, hourEditTxt);
                        hourEditTxt.setEnabled(true);
                    } else{
                        map.remove(speciality);
                        hourEditTxt.setEnabled(false);
                    }
                    Log.d(TAG, map.toString());
                }
            });

            LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,2);
            checkSpecHour.setLayoutParams(checkBoxParams);
            checkSpecHour.setWidth(0);
            checkSpecHour.setButtonTintList(getResources().getColorStateList(R.color.sideBar));
            TextView specUI = new TextView(this);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,6);
            specUI.setWidth(0);
            specUI.setLayoutParams(textViewParams);
            specUI.setText(specialityList.get(i).getName());
            specUI.setTextColor(getResources().getColor(R.color.appDefaultBlack));
            specUI.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

            LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,2);
            hourEditTxt.setWidth(0);
            hourEditTxt.setLayoutParams(etParams);
            hourEditTxt.setHint("Введіть кількість годин");
            hourEditTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            hourEditTxt.setEnabled(false);

            specLayout.addView(checkSpecHour);
            specLayout.addView(specUI);
            specLayout.addView(hourEditTxt);

            parent.addView(specLayout);

            Spinner spinnerCourse = (Spinner) findViewById(R.id.spinnerCourse);
            listenerSpinnerCourse(spinnerCourse);
            spinnerCourse.setSelection(subject.getNumberCourse() - ConstantEntity.ONE);

            subjectEditText = findViewById(R.id.editTextSubjectName);
            subjectEditText.setText(subject.getName());

            ImageButton editButton = findViewById(R.id.edit_subject);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSubject();
                }
            });
        }
    }

    private void editSubject(){
        subject.setName(subjectEditText.getText().toString())
                .putAll(ConstantEntity.convertMapEditTextToMapInt(map))
                .setColor(pickDefaultColor)
                .newEntity();

        if(DBManager.write(subject) > ConstantEntity.ZERO) {
            Intent intent = getIntent();
            intent.putExtra("editSubject", subject);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, pickDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                color = (subject.getColor() != ConstantEntity.ZERO)? subject.getColor() : color;

                pickDefaultColor = color;
                colorPickButton = (Button) findViewById(R.id.pickColorBtn);
                GradientDrawable background = (GradientDrawable) colorPickButton.getBackground();
                background.setColor(color);
                colorPickButton.setBackground(background);
            }
        });
        colorPicker.show();
    }

    private void listenerSpinnerCourse(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String item = (String) parent.getItemAtPosition(selectedItemPosition);
                int course = Integer.parseInt(item);
                subject.setNumberCourse(course);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
