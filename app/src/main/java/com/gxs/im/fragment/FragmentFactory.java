package com.gxs.im.fragment;

import com.gxs.im.base.BaseFragment;
import com.gxs.im.fragment.conversationtask.ConversationFragment;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class FragmentFactory {

    private static ConversationFragment conversationFragment;
    private static ContactFragment contactFragment;
    private static PluginFragment pluginFragment;

    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment = null;
        switch (position){
            case 0:
                if(conversationFragment==null) {
                    conversationFragment = new ConversationFragment();
                }
                baseFragment = conversationFragment;
                break;
            case 1:
                if(contactFragment==null) {
                    contactFragment = new ContactFragment();
                }
                baseFragment = contactFragment;
                break;
            case 2:
                if(pluginFragment==null) {
                    pluginFragment = new PluginFragment();
                }
                baseFragment = pluginFragment;
                break;
        }
        return baseFragment;
    }
}
