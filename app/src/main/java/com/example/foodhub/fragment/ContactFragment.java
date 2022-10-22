package com.example.foodhub.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodhub.databinding.ContactsupportBinding;

public class ContactFragment extends Fragment {

    ContactsupportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ContactsupportBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        return view;
    }
}
