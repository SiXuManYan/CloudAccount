package com.fatcloud.account.feature.product.detail.spinners;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fatcloud.account.R;
import com.fatcloud.account.entity.product.Price;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw on 2020/6/10 0010 13:36.
 * </br>
 */
public class ProductSpinnerAdapter implements SpinnerAdapter {


    public ArrayList<Price> mList = new ArrayList<>();
    public Context mContext;

    public ProductSpinnerAdapter(ArrayList<Price> mList, Context mContext) {
        this.mList .clear();
        this.mList.addAll(mList);
        this.mContext = mContext;
    }




    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return  mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner_view, null);
        TextView spinner_text = convertView.findViewById(R.id.spinner_text);
        spinner_text.setText(mList.get(position).getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner_view, null);
        TextView spinner_text = convertView.findViewById(R.id.spinner_text);
        spinner_text.setText(mList.get(position).getName());

        return convertView;
    }



}
