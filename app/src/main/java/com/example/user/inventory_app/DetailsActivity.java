package com.example.user.inventory_app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.inventory_app.ProductContract.ProductEntry;


public class DetailsActivity extends AppCompatActivity {

    Uri currentProductUri;
    Cursor cursor;
    int quantity;
    String supplierPhone;
    private Button button;
    private Button del_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        currentProductUri = intent.getData();

        TextView nameTextView = findViewById(R.id.prod_name);
        TextView priceTextView = findViewById(R.id.price);
        TextView quantityTextView = findViewById(R.id.quantity);
        TextView supplierTextView = findViewById(R.id.supplier);
        TextView phoneTextView = findViewById(R.id.supplier_phone);

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_SUPPLIER,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_PHONE};
        cursor = getContentResolver().query(currentProductUri, projection, null, null,
                null);
        while (cursor.moveToNext()) {
            DatabaseUtils.dumpCursor(cursor);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER);
            int supplierphoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE);
            String productName = cursor.getString(nameColumnIndex);
            String productPrice = cursor.getString(priceColumnIndex);
            String productQuantity = cursor.getString(quantityColumnIndex);
            quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            supplierPhone = cursor.getString(supplierphoneColumnIndex);
            nameTextView.setText(productName);
            priceTextView.setText(productPrice);
            quantityTextView.setText(productQuantity);
            supplierTextView.setText(supplier);
            phoneTextView.setText(supplierPhone);
        }
        button = findViewById(R.id.call_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        del_button = findViewById(R.id.deleteItem);
        del_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage(R.string.delete_dialog_msg);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteItem();
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    public void decrease_quantity(View view) {
        int n_quantity = quantity - 1;
        quantity = n_quantity;
        if (n_quantity < 0) {
            n_quantity = 0;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_QUANTITY, n_quantity);
        TextView quantityTextView = findViewById(R.id.quantity);
        quantityTextView.setText(String.valueOf(n_quantity));
        getContentResolver().update(currentProductUri, values, null, null);

    }

    public void increase_quantity(View view) {
        int n_quantity = quantity + 1;
        quantity = n_quantity;
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_QUANTITY, n_quantity);
        TextView quantityTextView = findViewById(R.id.quantity);
        quantityTextView.setText(String.valueOf(n_quantity));
        getContentResolver().update(currentProductUri, values, null, null);

    }

    public void deleteItem() {
        getContentResolver().delete(currentProductUri, null, null);

    }

    public void editItem(View view) {
        Intent intent = new Intent(DetailsActivity.this, EditorActivity.class);
        intent.setData(currentProductUri);
        startActivity(intent);
    }
}