package com.example.user.inventory_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.user.inventory_app.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summary1TextView = view.findViewById(R.id.summary1);
        TextView summary2TextView = view.findViewById(R.id.summary2);

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        final int prodQuant= cursor.getInt(quantityColumnIndex);
        nameTextView.setText(productName);
        summary1TextView.setText(productPrice);
        summary2TextView.setText(productQuantity);

        final long id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        view.findViewById(R.id.bsale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogActivity catalogActivity = (CatalogActivity) context;
                catalogActivity.subOnefromQuantity(id, prodQuant);
            }
        });
    }
}