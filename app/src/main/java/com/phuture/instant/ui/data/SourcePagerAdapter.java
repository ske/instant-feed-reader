package com.phuture.instant.ui.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.phuture.instant.db.Client;
import com.phuture.instant.model.Source;
import com.phuture.instant.ui.fragments.ArticleListFragment;

import java.util.ArrayList;
import java.util.List;

public class SourcePagerAdapter extends FragmentPagerAdapter {

    protected Context ctx;
    protected List<SourceDescriptor> pages;

    public SourcePagerAdapter(Context ctx, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.ctx = ctx;
        pages = new ArrayList<>();
        pages.add(SourceDescriptor.createAll());
        List<Source> sources = Client.instance(ctx).getDb().sourceDao().getAll();
        for (Source src: sources) {
            pages.add(SourceDescriptor.createSingle(src));
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ArticleListFragment.newInstance(pages.get(position).sourceId);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
