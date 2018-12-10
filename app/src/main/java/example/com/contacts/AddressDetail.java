package example.com.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddressDetail extends AppCompatActivity {

    private Spinner designation;
    private EditText firstName;
    private EditText lastName;
    private EditText address;
    private Spinner province;
    private EditText country;
    private EditText postalCode;
    private Button submit;

    private Uri addressUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        setWidgets();

        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        addressUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(AddressContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            addressUri = extras.getParcelable(AddressContentProvider.CONTENT_ITEM_TYPE);
            fillData(addressUri);
        }

        setSubmit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    // Create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailmenu, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detailaboutID:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMsg)
                        .show();
                return true;
            case R.id.clearID:
                clearField();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(AddressContentProvider.CONTENT_ITEM_TYPE, addressUri);
    }

    private void setWidgets() {
        designation = (Spinner) findViewById(R.id.designationID);
        firstName = (EditText) findViewById(R.id.firstNameID);
        lastName = (EditText) findViewById(R.id.lastNameID);
        address = (EditText) findViewById(R.id.addressID);
        province  = (Spinner) findViewById(R.id.provinceID);
        country = (EditText) findViewById(R.id.countryID);
        postalCode = (EditText) findViewById(R.id.postalcodeID);
        submit = (Button) findViewById(R.id.submitID);
    }

    private void setSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Address adr = new Address(designation.getSelectedItem().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        address.getText().toString(),
                        province.getSelectedItem().toString(),
                        country.getText().toString(),
                        postalCode.getText().toString());

                if (adr.isEmpty()) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void makeToast() {
        Toast.makeText(AddressDetail.this, "Please fill all fields.",Toast.LENGTH_LONG).show();
    }

    private void clearField() {
        designation.setSelection(0);
        firstName.setText("");
        lastName.setText("");
        address.setText("");
        province.setSelection(0);
        country.setText("");
        postalCode.setText("");
    }

    private void fillData(Uri uri) {
        String[] projection = {AddressTableHandler.COLUMN_DESIGNATION, AddressTableHandler.COLUMN_FIRST_NAME, AddressTableHandler.COLUMN_LAST_NAME, AddressTableHandler.COLUMN_ADDRESS, AddressTableHandler.COLUMN_PROVINCE, AddressTableHandler.COLUMN_COUNTRY, AddressTableHandler.COLUMN_POSTAL_CODE};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            String desg = cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_DESIGNATION));
            for(int i=0; i<designation.getCount(); i++) {
                String temp = (String) designation.getItemAtPosition(i);
                if(temp.equalsIgnoreCase(desg)) {
                    designation.setSelection(i);
                }
            }

            String prov = cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_PROVINCE));
            for(int i=0; i<province.getCount(); i++) {
                String temp = (String) province.getItemAtPosition(i);
                if(temp.equalsIgnoreCase(prov)) {
                    province.setSelection(i);
                }
            }

            firstName.setText(cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_FIRST_NAME)));
            lastName.setText(cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_LAST_NAME)));
            address.setText(cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_ADDRESS)));
            country.setText(cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_COUNTRY)));
            postalCode.setText(cursor.getString(cursor.getColumnIndex(AddressTableHandler.COLUMN_POSTAL_CODE)));


            // Always close the cursor
            cursor.close();
        }
    }

    private void saveState() {
        String desg = designation.getSelectedItem().toString();
        String fname = firstName.getText().toString();
        String lname = lastName.getText().toString();
        String adr =  address.getText().toString();
        String prov = province.getSelectedItem().toString();
        String co = country.getText().toString();
        String pos = postalCode.getText().toString();

        Address objA = new Address(desg,fname,lname,adr,prov,co,pos);

        if (objA.isEmpty()) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(AddressTableHandler.COLUMN_DESIGNATION, desg);
        values.put(AddressTableHandler.COLUMN_FIRST_NAME, fname);
        values.put(AddressTableHandler.COLUMN_LAST_NAME,lname);
        values.put(AddressTableHandler.COLUMN_ADDRESS,adr);
        values.put(AddressTableHandler.COLUMN_PROVINCE,prov);
        values.put(AddressTableHandler.COLUMN_COUNTRY,co);
        values.put(AddressTableHandler.COLUMN_POSTAL_CODE,pos);

        if (addressUri == null) {
            // New ToDo
            addressUri = getContentResolver().insert(AddressContentProvider.CONTENT_URI, values);
        } else {
            // Update ToDo
            getContentResolver().update(addressUri, values, null, null);
        }
    }
}
