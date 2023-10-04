package fr.adrackiin.hydranium.api.utils.statics;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.management.redis.RedisAccess;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.UUID;

public class PubSub {

    private static final APICore apiCore = APICore.getPlugin();
    private static final RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();

    public static void broadcast(String message){
        RTopic<String> broadcast = redissonClient.getTopic("Broadcast");
        long clientsReceivedMessage = broadcast.publish(message);
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: Broadcast");
        }
    }

    public static void refreshBan(){
        RTopic<String> refreshBan = redissonClient.getTopic("RefreshBan");
        long clientsReceivedMessage = refreshBan.publish("");
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: RefreshBan");
        }
    }

    public static void refreshMute(){
        RTopic<String> refreshMute = redissonClient.getTopic("RefreshMute");
        long clientsReceivedMessage = refreshMute.publish("");
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: RefreshMute");
        }
    }

    public static void unbanPlayer(UUID uuid){
        RTopic<String> unbanPlayer = redissonClient.getTopic("UnbanPlayer");
        long clientsReceivedMessage = unbanPlayer.publish(uuid.toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: UnbanPlayer");
        }
    }

    public static void unmutePlayer(UUID uuid){
        RTopic<String> unmutePlayer = redissonClient.getTopic("UnmutePlayer");
        long clientsReceivedMessage = unmutePlayer.publish(uuid.toString());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: UnmutePlayer");
        }
    }

    public static void chatStaff(String message){
        RTopic<String> staffChat = redissonClient.getTopic("StaffChat");
        long clientsReceivedMessage = staffChat.publish(message);
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: StaffChat");
        }
    }

    public static void vanish(APPlayer player){
        RTopic<String> vanishPlayer = redissonClient.getTopic("VanishPlayer");
        long clientsReceivedMessage = vanishPlayer.publish(player.getUUID().toString() + "²" + !player.isVisible());
        if(clientsReceivedMessage == 0){
            APICore.getPlugin().logServer("No receivers: VanishPlayer");
        }
    }

    public static void subscribe(){ //Reçoit un message

        /*RTopic<String> accountUpdated = redissonClient.getTopic("AccountUpdated");
        accountUpdated.addListener((channel, message) -> {
            Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
                String[] part = message.split("²");
                final APPlayer player = APICore.getPlugin().getAPPlayer(UUID.fromString(part[0]));
                if(player == null){
                    return;
                }
                final DataManager dataManager = APICore.getPlugin().getDataManager();
                Bukkit.getScheduler().runTaskLaterAsynchronously(APICore.getPlugin(), ()-> {
                    try {
                        switch(DataManager.Data.valueOf(part[1])){
                            case RANK: player.setRank((Rank)dataManager.get(player.getUUID(), DataManager.Data.RANK)); break;
                            case HYDRAS: player.setHydras((int)dataManager.get(player.getUUID(), DataManager.Data.HYDRAS)); break;
                            case WHITELIST: player.setWhitelist((int)dataManager.get(player.getUUID(), DataManager.Data.WHITELIST)); break;
                            case STATS: player.getStats().set((String)dataManager.get(player.getUUID(), DataManager.Data.STATS)); break;
                            case SETTINGS: APICore.getPlugin().getSettingsManager().deserialize((String) dataManager.get(player.getUUID(), DataManager.Data.SETTINGS), player.getSettings()); break;
                        }
                    } catch(CanTakeTimeException ignored){}
                }, 20L);
            });
        });*/

        RTopic broadcast = redissonClient.getTopic("Broadcast");
        broadcast.addListener((channel, message) -> APICore.getPlugin().getPlayerManager().sendMessageAll(message));

        RTopic<String> refreshBan = redissonClient.getTopic("RefreshBan");
        refreshBan.addListener((channel, message) -> apiCore.getAccountManager().loadBannedPlayers());

        RTopic<String> refreshMute = redissonClient.getTopic("RefreshMute");
        refreshMute.addListener((channel, message) -> apiCore.getAccountManager().loadMutedPlayers());

        RTopic<String> unbanPlayer = redissonClient.getTopic("UnbanPlayer");
        unbanPlayer.addListener((channel, message) -> apiCore.getAccountManager().unbanPlayer(UUID.fromString("")));

        RTopic<String> unmutePlayer = redissonClient.getTopic("UnmutePlayer");
        unmutePlayer.addListener((channel, message) -> apiCore.getAccountManager().unmutePlayer(UUID.fromString(message)));

        RTopic<String> staffChat = redissonClient.getTopic("StaffChat");
        staffChat.addListener((channel, message) -> {
            String[] playerName = message.split("¤");
            try {
                APICore.getPlugin().getChannelManager().getChannel("channel.staff").sendMessage("§c" + playerName[0] + "§7 » §b" + playerName[1]);
            } catch(ChannelNotFoundException e) {
                e.printStackTrace();
            }
        });

        RTopic<String> vanishPlayer = redissonClient.getTopic("VanishPlayer");
        vanishPlayer.addListener((channel, message) -> {
            String[] strings = message.split("²");
            APPlayer player = APICore.getPlugin().createAPPlayer(UUID.fromString(strings[0]));
            player.setVisible(Boolean.getBoolean(strings[1]));
        });
    }
}