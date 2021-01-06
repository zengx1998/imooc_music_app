package com.imooc.imooc_voice.view;

import androidx.fragment.app.Fragment;

import com.imooc.imooc_voice.view.friend.FriendFragment;

public class VideoFragment extends Fragment {

    public static Fragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }
}
