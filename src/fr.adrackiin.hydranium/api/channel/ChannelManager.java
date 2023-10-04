package fr.adrackiin.hydranium.api.channel;

import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

public class ChannelManager {

    private final Hashtable<String, Channel> channels = new Hashtable<>();

    public void newChannel(String channel, String displayName){
        channels.put(channel, new Channel(channel, displayName));
    }

    public Channel getChannel(String channel) throws ChannelNotFoundException {
        if(!this.channels.containsKey(channel)) throw new ChannelNotFoundException("No channel found: " + channel);
        return this.channels.get(channel);
    }

    public Set<String> channelList(){
        return channels.keySet();
    }

    public Collection<Channel> getChannels() {
        return channels.values();
    }
}
