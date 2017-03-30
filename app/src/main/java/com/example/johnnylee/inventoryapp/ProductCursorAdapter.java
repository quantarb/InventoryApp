package com.example.johnnylee.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnnylee.inventoryapp.data.ProductContract;
import com.example.johnnylee.inventoryapp.data.ProductContract.ProductEntry;

import static android.R.attr.id;

public class ProductCursorAdapter extends CursorAdapter {


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);
        Button sellButton = (Button) view.findViewById(R.id.sell_button);


        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        final String productName = cursor.getString(nameColumnIndex);
        final Double productPrice = cursor.getDouble(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(Double.toString(productPrice));
        quantityTextView.setText(Integer.toString(productQuantity));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productQuantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity - 1);
                    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
                    Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                    int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);

                    if (rowsAffected == 0) {
                        Toast.makeText(context, R.string.update_product_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.update_product_successful,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
