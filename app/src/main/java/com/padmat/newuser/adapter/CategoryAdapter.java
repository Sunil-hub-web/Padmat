package com.padmat.newuser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.padmat.newuser.databinding.CategorypageBinding;
import com.padmat.newuser.extra.Constants;

import com.padmat.newuser.R;

import com.padmat.newuser.fragment.SubCategoryProduct;
import com.padmat.newuser.model.Category_Model;
import com.padmat.newuser.model.SubCatGetSet;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder>  {

    ArrayList<Category_Model> categoryModels;
    Context context;

    public CategoryAdapter(ArrayList<Category_Model> category_models, FragmentActivity activity) {

        this.categoryModels = category_models;
        this.context = activity;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(CategorypageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        Category_Model catmodel = categoryModels.get(position);

        holder.binding.categoryName.setText(catmodel.getName());

        Glide.with(context).load(catmodel.getImage()).placeholder(R.drawable.food_delivery_banner).into(holder.binding.imageCategory);

        holder.binding.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.itemfilters = new ArrayList<SubCatGetSet>();

                /*Intent in = new Intent(context, SubCategoryProduct.class);
                in.putExtra("title", categoryModels.get(position).getName());
                in.putExtra("id", categoryModels.get(position).getId());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);*/

                SubCategoryProduct fragmentB = new SubCategoryProduct();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragmentB);
                Bundle bundle=new Bundle();
                bundle.putString("title", categoryModels.get(position).getName());
                bundle.putString("id", categoryModels.get(position).getId());
                fragmentB.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        CategorypageBinding binding;

        public Viewholder(CategorypageBinding categorypageBinding){
            super(categorypageBinding.getRoot());
            binding = categorypageBinding;
        }

    }
}
