package fr.adrackiin.hydranium.api;

import fr.adrackiin.hydranium.api.channel.ChannelManager;
import fr.adrackiin.hydranium.api.commands.manager.APCommandManager;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.PermissionsManager;
import fr.adrackiin.hydranium.api.core.PlayerManager;
import fr.adrackiin.hydranium.api.core.dbwrapper.APDataManager;
import fr.adrackiin.hydranium.api.core.playersinfo.APPStatistics;
import fr.adrackiin.hydranium.api.core.sanctions.BannedPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.MutedPlayer;
import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.api.gui.APGuiManager;
import fr.adrackiin.hydranium.api.management.data.AccountManager;
import fr.adrackiin.hydranium.api.management.data.DataManager;
import fr.adrackiin.hydranium.api.management.redis.RedisAccess;
import fr.adrackiin.hydranium.api.management.sql.DatabaseManager;
import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.api.utils.packet.APPacket;
import fr.adrackiin.hydranium.api.utils.pluginmessage.PluginMessageManager;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;
import fr.adrackiin.hydranium.commons.Rank;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class APICore
        extends JavaPlugin {

    private static final Random random = new Random();
    private static APICore plugin;
    private final APHash<UUID, BannedPlayer> playerBanned = new APHash<>();
    private final APHash<UUID, MutedPlayer> muted = new APHash<>();
    private final APHash<UUID, APPlayer> players = new APHash<>();

    private Type server;

    private AccountManager accountManager;
    private DataManager dataManager;
    private ChannelManager channelManager;
    private APCommandManager commandManager;
    private PermissionsManager permissionsManager;
    private PlayerManager playerManager;
    private APGuiManager apGuiManager;
    private APPacket packetManager;
    private APDataManager apDataManager;

    @Override
    public void onEnable(){

        plugin = this;

        logServer("========== HydraniumAPI ==========");
        logServer("           Par Adrackiin");
        logServer("           Pour Hydranium");
        logServer("========== HydraniumAPI ==========");

        RedisAccess.init();
        logServer("Redis connected");

        PubSub.subscribe();
        pluginMessage();
        this.saveDefaultConfig();
        switch(this.getConfig().getString("type")){
            case "uhc":
                this.server = Type.UHC;
                break;
            case "lobby":
                this.server = Type.LOBBY;
                break;
        }
        logServer("Server: " + server);

        initScoreboard();

        accountManager = new AccountManager();
        dataManager = new DataManager();
        channelManager = new ChannelManager();
        permissionsManager = new PermissionsManager();
        playerManager = new PlayerManager();
        commandManager = new APCommandManager();
        apGuiManager = new APGuiManager();
        packetManager = new APPacket();
        apDataManager = new APDataManager();

        accountManager.loadBannedPlayers();
        accountManager.loadMutedPlayers();

        channelManager.newChannel("channel.staff", "§8[§cStaff§8] ");

        getAPDataManager().addDefaultData("players-settings", "private-message:true");
        getAPDataManager().addDefaultData("players-stats", "game-played:0,play-time:0,win:0,death:0,killed-players:0,inflicted-damage:0,taken-damage:0,mined-block:0,placed-block:0,diamond-mined:0,gold-mined:0,iron-mined:0,lapis-mined:0,redstone-mined:0,quartz-mined:0,coal-mined:0,emerald-mined:0,mob-killed:0,monster-killed:0,venimous-killed:0,items-crafted:0,potion-crafted:0,item-cooked:0,item-enchanted:0,tnt-crafted:0,arrow-fired:0,enderpearl-launched:0,food-eaten:0,gapple-eaten:0,ghead-eaten:0");
        getAPDataManager().addDefaultData("recidivism", "insultes:0,provocation:0,spam:0,dox:0,spoil:0");

        logServer("Enabled");

        Bukkit.getScheduler().runTaskLater(this, ()-> this.getServer().getPluginManager().callEvent(new PostInitEvent()), 1L);
    }

    @Override
    public void onDisable(){

        DatabaseManager.closeAllDatabaseConnection();
        logServer("Database disconnected");

        RedisAccess.close();
        logServer("Redis disconnected");

        logServer("Disabled");

    }

    public boolean doesAPPlayerExists(String pseudo){
        return doesAPPlayerExists(Players.getOfflinePlayer(pseudo).getUniqueId());
    }

    public boolean doesAPPlayerExists(UUID uuid){
        return players.contains(uuid);
    }

    public APPlayer getAPPlayer(UUID uuid){
        if(this.doesAPPlayerExists(uuid)) {
            return players.get(uuid);
        } else {
            if(Bukkit.getPlayer(uuid) == null){
                return null;
            }
            players.put(uuid, new APPlayer(uuid));
            return players.get(uuid);
        }
    }

    public APPlayer createAPPlayer(UUID uuid){
        if(doesAPPlayerExists(uuid)){
            return getAPPlayer(uuid);
        } else {
            players.put(uuid, new APPlayer(uuid));
            return players.get(uuid);
        }
    }

    public APPlayer getAPPlayer(String player) {
        return getAPPlayer(Players.getOfflinePlayer(player).getUniqueId());
    }

    public void logServer(String message){
        System.out.println("[HydraniumAPI] " + message);
    }

    public List<APPlayer> getAPPlayers(){
        return players.getValues();
    }

    public APHash<UUID, BannedPlayer> getPlayerBanned() {
        return playerBanned;
    }

    public APHash<UUID, MutedPlayer> getPlayerMuted() {
        return muted;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public APCommandManager getCommandManager() {
        return commandManager;
    }

    public PermissionsManager getPermissionsManager(){
        return permissionsManager;
    }

    public Type getServerType() {
        return server;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public APGuiManager getAPGuiManager() {
        return apGuiManager;
    }

    public APPacket getPacketManager(){
        return packetManager;
    }

    public APDataManager getAPDataManager(){
        return apDataManager;
    }

    private void initScoreboard(){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

        if(scoreboard.getTeam("admin") != null){
            scoreboard.getTeam("admin").unregister();
        }
        Team admin = scoreboard.registerNewTeam("admin");
        admin.setPrefix(Rank.ADMIN.getPrefix());

        if(scoreboard.getTeam("leadhost") != null){
            scoreboard.getTeam("leadhost").unregister();
        }
        Team leadhost = scoreboard.registerNewTeam("leadhost");
        leadhost.setPrefix(Rank.LEADHOST.getPrefix());

        if(scoreboard.getTeam("host") != null){
            scoreboard.getTeam("host").unregister();
        }
        Team host = scoreboard.registerNewTeam("host");
        host.setPrefix(Rank.HOST.getPrefix());

        if(scoreboard.getTeam("hosttest") != null){
            scoreboard.getTeam("hosttest").unregister();
        }
        Team hosttest = scoreboard.registerNewTeam("hosttest");
        hosttest.setPrefix(Rank.HOSTTEST.getPrefix());

        if(scoreboard.getTeam("builder") != null){
            scoreboard.getTeam("builder").unregister();
        }
        Team builder = scoreboard.registerNewTeam("builder");
        builder.setPrefix(Rank.BUILDER.getPrefix());

        if(scoreboard.getTeam("friend") != null){
            scoreboard.getTeam("friend").unregister();
        }
        Team friend = scoreboard.registerNewTeam("friend");
        friend.setPrefix(Rank.FRIEND.getPrefix());

        if(scoreboard.getTeam("player") != null){
            scoreboard.getTeam("player").unregister();
        }
        Team joueur = scoreboard.registerNewTeam("player");
        joueur.setPrefix(Rank.PLAYER.getPrefix());
    }

    private void pluginMessage(){
        PluginMessageManager.registerChannels();
    }

    public static APICore getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }

    public enum Type {

        LOBBY,
        UHC;

        public String getName() {
            return this.toString().toLowerCase();
        }
    }
}
