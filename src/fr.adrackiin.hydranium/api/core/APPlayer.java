package fr.adrackiin.hydranium.api.core;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.channel.Channel;
import fr.adrackiin.hydranium.api.core.playersinfo.APPStatistics;
import fr.adrackiin.hydranium.api.core.playersinfo.settings.APPSettings;
import fr.adrackiin.hydranium.api.events.APPermissionChangedEvent;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGMultiplePlayer;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.api.utils.PacketUtils;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.statics.ItemCreator;
import fr.adrackiin.hydranium.api.utils.statics.StringUtils;
import fr.adrackiin.hydranium.commons.Rank;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class APPlayer {

    private final APPStatistics stats;
    private final APPSettings settings;
    private final String name;
    private String nickname;
    private boolean isMute = false;
    private String muteReason;
    private String muteTime;
    private String recidivism;
    private boolean isMuteDef = false;
    private long muteUnixTme;
    private final HashSet<String> permissions = new HashSet<>();
    private final UUID uuid;
    private UUID lastPlayerMsg = null;
    private boolean visible = true;

    public APPlayer(UUID uuid){
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.stats = new APPStatistics();
        this.settings = new APPSettings();
    }

    public boolean hasPermission(String permission){
        return permissions.contains(permission);
    }

    public void addPermission(String permission){
        if(!this.hasPermission(permission)){
            this.permissions.add(permission);
            APICore.getPlugin().getServer().getPluginManager().callEvent(new APPermissionChangedEvent(permission, this, true));
        }
    }

    public void removePermission(String permission){
        if(this.hasPermission(permission)){
            this.permissions.remove(permission);
            APICore.getPlugin().getServer().getPluginManager().callEvent(new APPermissionChangedEvent(permission, this, false));
        }
    }
    
    public void deletePermissions(){
        this.permissions.clear();
    }

    public void setTeam(){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                final Rank rank = Rank.valueOf((String)APICore.getPlugin().getDataManager().get(this.getUUID(), "rank"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
                    this.setTeam(rank);
                });
            } catch(CanTakeTimeException ignored){}
        });
    }

    public void sendMessage(String message) {
        if(this.isConnected()) {
            getPlayer().spigot().sendMessage(new APText(message).send());
        }
    }

    public void sendMessage(APText message) {
        if(this.isConnected()) {
            getPlayer().spigot().sendMessage(message.send());
        }
    }

    public void sendMessageBar(String message) {
        if(this.isConnected()){
            this.sendPacket(PacketUtils.packetActionBar(StringUtils.getIChatBaseComponent(message)));
        }
    }

    public void sendTitle(String title, String subTitle){
        this.sendTitle(title, subTitle, 10, 40, 10);
    }

    public void sendTitle(String title, String subTitle, int timeShow, int stay, int timeHide){
        if(this.isConnected()) {
            this.sendPacket(PacketUtils.packetTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, StringUtils.getIChatBaseComponent(title), timeShow, stay, timeHide));
            this.sendPacket(PacketUtils.packetTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, StringUtils.getIChatBaseComponent(subTitle), timeShow, stay, timeHide));
        }
    }

    public void sendPrivateMessage(APPlayer target, String message){
        if(!this.isConnected() || !target.isConnected()){
            return;
        }
        if(!target.hasPrivateMessage() && !this.hasPermission("hydranium.api.msg.bypass")){
            this.sendMessage(Prefix.msg + "§cCe joueur a désactivé ses messages privés");
            return;
        }
        if(!this.hasPrivateMessage()){
            this.sendMessage(Prefix.msg + "§aVos MPs ont été activés");
            this.setPrivateMessage(true);
        }
        sendMessage(Prefix.msg + "§3Envoyé à §d" + target.getName() + "§8 » §f" + message);
        target.sendMessage(Prefix.msg + "§3Reçu de §d" + getName() + "§8 » §f" + message);
        try {
            APICore.getPlugin().getChannelManager().getChannel("channel.socialspy").sendMessage(Prefix.socialspy + "§3" + this.getName() + "§7 » §3" + target.getName() + "§7: " + message, this.getUUID(), target.getUUID());
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
        }
        this.setLastPlayerMsg(target.getUUID());
        target.setLastPlayerMsg(this.getUUID());
    }

    public boolean hasPrivateMessage(){
        return (boolean) this.getSettings().get("private-message");
    }

    public void sendMessageFromConsole(String message){
        sendMessage(Prefix.msg + "§3Reçu de §dConsole" + "§8 » §f" + message);
    }

    public void performCommand(String command){
        if(this.isConnected()) {
            getPlayer().chat("/" + command);
        }
    }

    public void showHelpCommand(){
        this.performCommand("help cmd");
    }

    public void updateInventory(){
        if(this.isConnected()) {
            getPlayer().updateInventory();
        }
    }

    public void clearInventory(){
        if(this.isConnected()) {
            getPlayer().getInventory().clear();
            getPlayer().getInventory().setArmorContents(null);
        }
    }

    public void openInventory(Inventory inventory) {
        if(this.isConnected()) {
            getPlayer().openInventory(inventory);
        }
    }

    public void closeInventory(){
        if(this.isConnected()) {
            getPlayer().closeInventory();
        }
    }

    public void teleport(Location location) {
        if(this.isConnected()) {
            getPlayer().teleport(location);
        }
    }

    public void teleport(int x, int y, int z) {
        if(!this.isConnected()){
            return;
        }
        getPlayer().teleport(new Location(getPlayer().getWorld(), x, y, z));
    }

    public void give(List<ItemStack> items) {
        if(!this.isConnected()){
            return;
        }
        for(ItemStack i : items){
            getPlayer().getInventory().setItem(getPlayer().getInventory().firstEmpty(), i);
        }
    }

    public void addPotionEffect(PotionEffectType effect, int time, int amplifier, boolean hide){
        if(!this.isConnected()){
            return;
        }
        getPlayer().addPotionEffect(new PotionEffect(effect,time, amplifier, false, !hide));
    }

    public void removePotionEffect(PotionEffectType effect) {
        if(!this.isConnected()){
            return;
        }
        if(getPlayer().hasPotionEffect(effect)) {
            getPlayer().removePotionEffect(effect);
        }
    }

    public void reset(){
        if(!this.isConnected()){
            return;
        }
        this.setGameMode(GameMode.ADVENTURE);
        if(this.getMaxHealth() != 20){
            this.setMaxHealth(20);
        }
        this.clearInventory();
        this.setHealth(20);
        this.setFoodLevel(20);
        this.setSaturation(20);
        this.setLevel(0);
        this.setBarLevel((byte)0);
        if(!this.getActivePoions().isEmpty()){
            for(PotionEffect p : getActivePoions()){
                this.removePotionEffect(p.getType());
            }
        }
    }

    public void reset(Location toTp){
        if(!this.isConnected()){
            return;
        }
        this.teleport(toTp);
        this.setGameMode(GameMode.ADVENTURE);
        if(this.getMaxHealth() != 20){
            this.setMaxHealth(20);
        }
        this.clearInventory();
        this.setHealth(20);
        this.setFoodLevel(20);
        this.setSaturation(20);
        this.setLevel(0);
        this.setBarLevel((byte)0);
        if(!this.getActivePoions().isEmpty()){
            for(PotionEffect p : getActivePoions()){
                this.removePotionEffect(p.getType());
            }
        }
    }

    public boolean isGameMode(GameMode... gameMode){
        boolean result = false;
        for(GameMode gm : gameMode){
            if(this.getGameMode() == gm){
                result = true;
            }
        }
        return result;
    }

    public void hidePlayer(Player player){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().hidePlayer(player);
    }

    public void showPlayer(Player player){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().showPlayer(player);
    }

    public void addPotionsEffect(PotionEffect... effects){
        if(!this.isConnected()){
            return;
        }
        for(PotionEffect effect : effects){
            this.addPotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier(), !effect.hasParticles());
        }
    }

    public void addPotionsEffect(List<PotionEffect> effects){
        if(!this.isConnected()){
            return;
        }
        for(PotionEffect effect : effects){
            this.addPotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier(), !effect.hasParticles());
        }
    }

    public void hidePlayers(List<UUID> toHide){
        if(!this.isConnected()){
            return;
        }
        for(UUID uuid : toHide){
            this.hidePlayer(Bukkit.getPlayer(uuid));
        }
    }

    public void showPlayers(List<UUID> toShow){
        if(!this.isConnected()){
            return;
        }
        for(UUID uuid : toShow){
            this.showPlayer(Bukkit.getPlayer(uuid));
        }
    }

    public void hideForOther(List<UUID> hideThis){
        if(!this.isConnected()){
            return;
        }
        Player p;
        for(UUID uuid : hideThis){
            p = Bukkit.getPlayer(uuid);
            if(p != null) {
                p.hidePlayer(this.getPlayer());
            }
        }
    }

    public void showForOther(List<UUID> showThis){
        if(!this.isConnected()){
            return;
        }
        Player p;
        for(UUID uuid : showThis){
            p = Bukkit.getPlayer(uuid);
            if(p != null) {
                p.showPlayer(this.getPlayer());
            }
        }
    }

    public void hideForOther(Collection<? extends Player> hideThis){
        if(!this.isConnected()){
            return;
        }
        for(Player p : hideThis){
            p.hidePlayer(this.getPlayer());
        }
    }

    public void showForOther(Collection<? extends Player> showThis){
        if(!this.isConnected()){
            return;
        }
        for(Player p : showThis){
            p.showPlayer(this.getPlayer());
        }
    }

    public void fixVisibility(){
        if(!this.isConnected()){
            return;
        }
        Bukkit.getScheduler().runTaskLater(APICore.getPlugin(), ()-> {
            for(Player p : Bukkit.getOnlinePlayers()){
                p.hidePlayer(this.getPlayer());
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                p.showPlayer(this.getPlayer());
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                this.getPlayer().hidePlayer(p);
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                this.getPlayer().showPlayer(p);
            }
        }, 10L);
    }

    public void sendPacket(Packet packet) {
        ((CraftPlayer)this.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getName() {
        return name;
    }

    public APPStatistics getStats(){
        return this.stats;
    }

    public APPSettings getSettings(){
        return settings;
    }

    public Rank getRank() throws CanTakeTimeException{
        return Rank.valueOf((String)APICore.getPlugin().getDataManager().get(this.getUUID(), "rank"));
    }

    public void setRank(Rank rank) {
        APICore.getPlugin().getDataManager().set(this.getUUID(), "rank", rank.toString());
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public String getMuteReason() {
        return muteReason;
    }

    public void setMuteReason(String muteReason) {
        this.muteReason = muteReason;
    }

    public String getMuteTime() {
        return muteTime;
    }

    public void setMuteTime(String muteTime) {
        this.muteTime = muteTime;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getRecidivism() {
        return recidivism;
    }

    public void setRecidivism(String recidivism) {
        this.recidivism = recidivism;
    }

    public boolean isMuteDef() {
        return isMuteDef;
    }

    public void setMuteDef(boolean muteDef) {
        isMuteDef = muteDef;
    }

    public long getMuteUnixTme() {
        return muteUnixTme;
    }

    public void setMuteUnixTme(long muteUnixTme) {
        this.muteUnixTme = muteUnixTme;
    }

    public boolean isSocialSpyer(){
        try {
            return APICore.getPlugin().getChannelManager().getChannel("channel.socialspy").isMember(this.getUUID());
        } catch(ChannelNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public UUID getLastPlayerMsg() {
        return lastPlayerMsg;
    }

    public void setLastPlayerMsg(UUID lastPlayerMsg) {
        this.lastPlayerMsg = lastPlayerMsg;
    }

    public boolean isConnected() {
        return getPlayer() != null;
    }

    public PlayerInventory getInventory(){
        if(!this.isConnected()){
            return null;
        }
        return getPlayer().getInventory();
    }

    public GameMode getGameMode(){
        if(!this.isConnected()){
            return null;
        }
        return getPlayer().getGameMode();
    }

    public void setGameMode(GameMode gameMode){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setGameMode(gameMode);
    }

    public String getDisplayName(){
        if(!this.isConnected()){
            return null;
        }
        return getPlayer().getDisplayName();
    }

    public void setDisplayName(String name){
        if(this.isConnected()) {
            this.getPlayer().setDisplayName(name);
        }
    }

    public InventoryView getOpenInventory(){
        if(!this.isConnected()){
            return null;
        }
        return getPlayer().getOpenInventory();
    }

    public int getPing(){
        if(!this.isConnected()){
            return -1;
        }
        return ((CraftPlayer)getPlayer()).getHandle().ping;
    }

    public Location getLocation() {
        if(this.isConnected()) {
            return getPlayer().getLocation();
        }
        return null;
    }

    public double getHealth() {
        if(!this.isConnected()){
            return 0;
        }
        return getPlayer().getHealth();
    }

    public void setHealth(double health){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setHealth(health);
    }

    public double getMaxHealth(){
        if(!this.isConnected()){
            return 0;
        }
        return this.getPlayer().getMaxHealth();
    }

    public void setMaxHealth(double health) {
        if(!this.isConnected()){
            return;
        }
        getPlayer().setMaxHealth(health);
    }

    public Collection<PotionEffect> getActivePoions(){
        if(!this.isConnected()){
            return Collections.emptyList();
        }
        return this.getPlayer().getActivePotionEffects();
    }

    public int getLevel(){
        if(!this.isConnected()){
            return 0;
        }
        return this.getPlayer().getLevel();
    }

    public void setLevel(int level){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setLevel(level);
    }

    public int getFoodLevel(){
        if(!this.isConnected()){
            return 0;
        }
        return this.getPlayer().getFoodLevel();
    }

    public void setFoodLevel(int food){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setFoodLevel(food);
    }

    public float getSaturation(){
        if(!this.isConnected()){
            return 0;
        }
        return this.getPlayer().getSaturation();
    }

    public void setSaturation(float saturation){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setSaturation(saturation);
    }

    public Collection<PotionEffect> getActivePotionEffects(){
        if(!this.isConnected()){
            return Collections.emptyList();
        }
        return this.getPlayer().getActivePotionEffects();
    }

    public ItemStack getItemInHand(){
        if(this.isConnected()){
            return this.getPlayer().getItemInHand();
        }
        return ItemCreator.simpleItem(Material.AIR);
    }

    public int getHydras() throws CanTakeTimeException{
        return (int) APICore.getPlugin().getDataManager().get(this.getUUID(), "hydras");
    }

    public void setHydras(int hydras) {
        APICore.getPlugin().getDataManager().set(this.getUUID(), "hydras", hydras);
    }

    public int getWhitelist() throws CanTakeTimeException{
        return (int) APICore.getPlugin().getDataManager().get(this.getUUID(), "whitelist");
    }

    public void setWhitelist(int whitelist) {
        APICore.getPlugin().getDataManager().set(this.getUUID(), "whitelist", whitelist);
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public void setTeam(Rank rank){
        if(this.isConnected()){
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(rank.toString().toLowerCase()).addEntry(getName());
            this.getPlayer().setDisplayName(rank.getPrefix() + getName());
        }
    }

    public void setTeam(Team team){
        if(this.isConnected()) {
            team.addEntry(this.getName());
            this.getPlayer().setDisplayName(team.getPrefix() + this.getName());
        }
    }

    public void setSocialSpy(boolean socialSpy){
        Channel spy;
        try {
            spy = APICore.getPlugin().getChannelManager().getChannel("channel.socialspy");
        } catch(ChannelNotFoundException e){
            e.printStackTrace();
            return;
        }
        if(socialSpy){
            this.sendMessage(spy.getDisplayName() + "§aActivé: Vous voyez les messages privés");
            spy.addPlayer(this.getUUID());
            spy.sendMessage("§6" + this.getName() + "§a a activé son socialspy", this.getUUID());
        } else {
            this.sendMessage(spy.getDisplayName() + "§cDésactivé: Vous ne voyez plus les messages privés");
            spy.removePlayer(this.getUUID());
            spy.sendMessage("§6" + this.getName() + "§c a désactivé son socialspy");
        }
    }

    public void setPrivateMessage(boolean privateMessage){
        this.getSettings().set("private-message", privateMessage);
        try {
            APGMultiplePlayer settings = (APGMultiplePlayer)APICore.getPlugin().getAPGuiManager().get("§6Options").getAPGui().getSource();
            APGui gui = settings.getAPGui(this);
            gui.getItem((byte)13).setName((privateMessage ? "§a" : "§c") + "Messages privés").setDescription(Arrays.asList("",
                    "§7Activer / Désactiver","§7ses messages privés", "", "§6État: " + (privateMessage ? "§aActivé" : "§cDésactivé")));
            gui.update((byte)13);
        } catch(APGuiNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setBarLevel(byte percentage){
        if(!this.isConnected()){
            return;
        }
        this.getPlayer().setExp((float)percentage / 100);
    }

    public void setPlayerListName(String name){
        if(this.isConnected()) {
            this.getPlayer().setPlayerListName(name);
        }
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
        PacketUtils.setGameProfile(((CraftPlayer)getPlayer()).getProfile(), nickname);
    }
}
