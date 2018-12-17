package io.rong.imkit;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by zxy on 2018/12/13 11:17.
 */

public class DefExtensionModule extends DefaultExtensionModule
{
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType)
    {
//        return super.getPluginModules(conversationType);
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        pluginModuleList.add(new DefPicturePlugin());
        pluginModuleList.add(new DefCameraPlugin());
        pluginModuleList.add(new DefLocationPlugin());
        pluginModuleList.add(new DefFilePlugin());
        pluginModuleList.add(new DefRedPacketPlugin());
        pluginModuleList.add(new DefTransferAccountsPlugin());
        pluginModuleList.add(new DefVoiceInputPlugin());
        pluginModuleList.add(new DefPersonalCardPlugin());
        pluginModuleList.add(new DefVoiceCallPlugin());
        pluginModuleList.add(new DefVideoCallPlugin());
        return pluginModuleList;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs()
    {
        return super.getEmoticonTabs();
    }
}
