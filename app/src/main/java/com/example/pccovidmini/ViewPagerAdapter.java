package com.example.pccovidmini;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pccovidmini.fragment.EditFragment;
import com.example.pccovidmini.fragment.FindF0Fragment;
import com.example.pccovidmini.fragment.ManagementFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ManagementFragment();
            case 1:
                return new FindF0Fragment();
            case 2:
                return new EditFragment();
            default:
                return new ManagementFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
