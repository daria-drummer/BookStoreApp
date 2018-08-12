package com.example.android.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.Data.BookContract;
import com.example.android.bookstoreapp.Data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public int quantity;
    public TextView nameTextView;
    public TextView priceTextView;
    public TextView quantityTextView;
    public int nameColumnIndex;
    public int priceColumnIndex;
    public int quantityColumnIndex;
    public String bookName;
    public String bookPrice;
    public String bookQty;
    public Button sellButton;


    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        nameTextView = (TextView) view.findViewById(R.id.name);
        priceTextView = (TextView) view.findViewById(R.id.price);
        quantityTextView = (TextView) view.findViewById(R.id.quantity);

        nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
        final int bookId = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry._ID));

        bookName = cursor.getString(nameColumnIndex);
        bookPrice = cursor.getString(priceColumnIndex);
        bookQty = cursor.getString(quantityColumnIndex);

        nameTextView.setText(bookName);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQty);

        sellButton = (Button) view.findViewById(R.id.sale_button);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityTextView.getText().toString());
                if (quantity == 0) {
                    Toast.makeText(view.getContext(), R.string.quantity_decrement_error, Toast.LENGTH_SHORT).show();
                } else {
                    quantity -= 1;
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
                    String selection = BookEntry._ID + "=?";
                    Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, bookId);
                    String[] selectionArgs = new String[]{String.valueOf(bookId)};
                    context.getContentResolver().update(currentBookUri, values, selection, selectionArgs);
                }
            }
        });
    }
}
