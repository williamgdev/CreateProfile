package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText inputText;
    static final int REQUEST_IMAGE_CAPTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = ((EditText) findViewById(R.id.textView));

        DataBaseHandler db = new DataBaseHandler(this);
        db.addContact(new Contact("Jhon", "510234567"));
        db.addContact(new Contact("Bond", "530510234567"));
        /* Check the data is created and write in console
        List<Contact> contacts = db.getAllContacts();
        for (Contact c :
                contacts) {
            String logname = c.get_name();
            Log.d("Name: ", logname);
        }*/
    }

    public void onGo(View view) {
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("textInput", inputText.getText().toString());
        startActivity(i);

    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences setting = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("Name", inputText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        String nameSaved = settings.getString("Name", "No Name");
        inputText.setText(nameSaved);
    }
}
