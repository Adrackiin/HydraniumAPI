package fr.adrackiin.hydranium.api.core;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.accounts.Account;
import fr.adrackiin.hydranium.api.core.sanctions.BannedPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.HistoryPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.MutedPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.RecidivistPlayer;
import fr.adrackiin.hydranium.api.events.APGuiCloseEvent;
import fr.adrackiin.hydranium.api.events.APGuiInteractEvent;
import fr.adrackiin.hydranium.api.events.APPlayerJoinEvent;
import fr.adrackiin.hydranium.api.events.AccountLoadedEvent;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGMultiplePlayer;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.api.management.data.Data;
import fr.adrackiin.hydranium.api.management.data.DataManager;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.pluginmessage.PluginMessageManager;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class  PlayerManager implements Listener {

    public PlayerManager(){
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void checkBan(AsyncPlayerPreLoginEvent e){
        UUID uuid = e.getUniqueId();
        if(APICore.getPlugin().getPlayerBanned().contains(uuid)){
            BannedPlayer bannedPlayer = APICore.getPlugin().getPlayerBanned().get(uuid);
            if((int)bannedPlayer.get("expire") != -1 && (int)bannedPlayer.get("expire") < (int)(System.currentTimeMillis() / 1000)){
                PubSub.unbanPlayer(uuid);
                return;
            }
            String[] date = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date((int)bannedPlayer.get("expire") * 1000L)).split("-");
            if((int)bannedPlayer.get("expire") == -1){
                e.setKickMessage(Prefix.odin + "§cVous avez été banni pour §6" + bannedPlayer.get("reason") + " §cdéfinitivement");
            } else {
                e.setKickMessage(Prefix.odin + "§cVous avez été banni pour §6" + bannedPlayer.get("reason") + "§c\nVous serez débanni le " + "§6" + date[0] + "/" + date[1] + "/" + date[2] + " à " + date[3] + "h" + date[4]);
            }
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);

        APPlayer player = APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId());
        vanishOnJoin(player);
        if(APICore.getPlugin().getPlayerMuted().contains(player.getUUID())){
            player.setMute(true);
        }
        APICore.getPlugin().getServer().getPluginManager().callEvent(new APPlayerJoinEvent(player));
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            Account account = null;
            try {
                account = APICore.getPlugin().getAccountManager().getAccount(player.getUUID());
            } catch(CanTakeTimeException ignored){}
            Account finalAccount = account;
            Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> APICore.getPlugin().getServer().getPluginManager().callEvent(new AccountLoadedEvent(player, finalAccount)));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAccountLoaded(AccountLoadedEvent e){
        if(e.getAccount() == null){
            PluginMessageManager.kickPlayer(e.getPlayer().getName(), ": Erreur critique, veuillez essayer de vous reconnectez ou contactez @Adrackiin sur Twitter si le problème persiste");
            return;
        }
        APPlayer player = e.getPlayer();
        APICore.getPlugin().getAccountManager().setDataRedis("server", APICore.getPlugin().getServerType().getName(), player.getUUID());
        if(e.getAccount().getSettings().equals("")){
            e.getAccount().setSettings(APICore.getPlugin().getAPDataManager().getDefaultData("players-settings"));
        }
        if(e.getAccount().getStats().equals("")){
            e.getAccount().setStats(APICore.getPlugin().getAPDataManager().getDefaultData("players-stats"));
        }
        if(e.getAccount().getRecidivism().equals("")){
            Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> APICore.getPlugin().getDataManager().set(player.getUUID(), "recidivism", APICore.getPlugin().getAPDataManager().getDefaultData("recidivism")));
        }
        player.getSettings().deserialize(e.getAccount().getSettings());
        player.getStats().deserialize(e.getAccount().getStats());
        player.setTeam(e.getAccount().getRank());
        APICore.getPlugin().getPermissionsManager().addPermissions(player, e.getAccount().getRank());
        if(player.hasPermission("hydranium.api.channel.staff")){
            try {
                APICore.getPlugin().getChannelManager().getChannel("channel.staff").addPlayer(player.getUUID());
            } catch(ChannelNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        if(APICore.getPlugin().getServerType() == APICore.Type.LOBBY) {
            if(player.hasPermission("hydranium.lobby.announce") && player.isVisible()) {
                Bukkit.broadcastMessage(e.getAccount().getRank().getPrefix() + player.getName() + " §6a rejoint le lobby");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
        APPlayer player = APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId());
        APICore.getPlugin().getDataManager().set(player.getUUID(), new Data("settings", player.getSettings().serialize()), new Data("stats", player.getStats().serialize()));

        player.deletePermissions();
        for(APGuiListener apgl : APICore.getPlugin().getAPGuiManager().getGuis().values()){
            if(apgl.getAPGui().getSource() instanceof APGMultiplePlayer){
                ((APGMultiplePlayer) apgl).getAPGuis().remove(player.getUUID());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e){
        e.setMessage(e.getMessage().replaceAll("%", "%%"));
        UUID uuid = e.getPlayer().getUniqueId();
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        if(player.isMute()){
            MutedPlayer mutedPlayer = APICore.getPlugin().getPlayerMuted().get(uuid);
            if((int)mutedPlayer.get("expire") != -1 && (int)(System.currentTimeMillis() / 1_000L) > (int)mutedPlayer.get("expire")){
                PubSub.unmutePlayer(uuid);
            } else {
                if((int)mutedPlayer.get("expire") == -1){
                    player.getPlayer().sendMessage(Prefix.odin + "§7Vous êtes mute pour §c" + mutedPlayer.get("reason") + " §ldéfinitivement");
                } else {
                    String[] date = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date((int)mutedPlayer.get("expire") * 1000L)).split("-");
                    player.getPlayer().sendMessage(Prefix.odin + "§7Vous êtes mute pour §c" + mutedPlayer.get("reason") + " §7jusqu'au §c" + date[0] + "/" + date[1] + "/" + date[2] + " à " + date[3] + "h" + date[4]);
                }
                e.setCancelled(true);
            }
        }
        String message = e.getMessage();
        String firstCharacter = message.substring(0, 1);
        if (firstCharacter.equals("$") && player.hasPermission("hydranium.api.channel.staff")){
            e.setCancelled(true);
            if(message.contains("¤")){
                player.getPlayer().sendMessage("§c" + "Caractère interdit");
                return;
            }
            String messageStaff = message.substring(1);
            PubSub.chatStaff(player.getPlayer().getName() + "¤" + messageStaff);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventory(InventoryClickEvent e){
        if(e.getCurrentItem() == null){
            return;
        }
        ItemStack item = e.getCurrentItem();
        byte slot = (byte)e.getSlot();
        if(e.getClick() == ClickType.NUMBER_KEY){
            if(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) == null){
                return;
            }
            item = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
            slot = (byte) e.getHotbarButton();
        }
        if(item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS) &&
                item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) &&
                item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)){
            if(item.getItemMeta().getDisplayName().equals(" ")){
                e.setCancelled(true);
                return;
            }
            try {
                APGuiListener apg = APICore.getPlugin().getAPGuiManager().get(APICore.getPlugin().getAPGuiManager().getPAGuiName(e.getClickedInventory().getName()));
                if(e.getSlot() == (apg.getAPGui().getLines() - 1) * 9 && item.getItemMeta().getDisplayName().equals("§cRetour")){
                    e.setCancelled(true);
                    APICore.getPlugin().getServer().getPluginManager().callEvent(new APGuiCloseEvent(APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()), apg));
                    return;
                }
                APICore.getPlugin().getServer().getPluginManager().callEvent(new APGuiInteractEvent(apg, APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()), e.getClick(), (byte)e.getSlot(), apg.getAPGui().getItem(slot)));
            } catch(APGuiNotFoundException e1) {
                e1.printStackTrace();
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent e){
        if(e.getItem() == null){
            return;
        }
        if(e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() &&
                e.getItem().getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS) &&
                e.getItem().getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) &&
                e.getItem().getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)){
            byte slot = 0;
            byte i = 0;
            boolean stop = false;
            while(i < e.getPlayer().getInventory().getContents().length && !stop){
                if(e.getPlayer().getInventory().getContents()[i] != null) {
                    if(e.getPlayer().getInventory().getContents()[i].getType() == e.getItem().getType() && e.getPlayer().getInventory().getContents()[i].getItemMeta().getDisplayName().equals(e.getItem().getItemMeta().getDisplayName())) {
                        stop = true;
                        slot = i;
                    }
                }
                i ++;
            }
            try {
                APGuiListener apg = APICore.getPlugin().getAPGuiManager().get(APICore.getPlugin().getAPGuiManager().getPAGuiName(e.getPlayer().getInventory().getName()));
                APICore.getPlugin().getServer().getPluginManager().callEvent(new APGuiInteractEvent(apg, APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()), ClickType.LEFT, slot, apg.getAPGui().getItem(slot)));
            } catch(APGuiNotFoundException e1) {
                e1.printStackTrace();
            }
            e.setCancelled(true);
        }
    }

    public void vanishOnJoin(APPlayer player){
        for(APPlayer p : APICore.getPlugin().getAPPlayers()){
            if(p.isConnected() && !p.isVisible()){
                player.hidePlayer(p.getPlayer());
            }
        }
        if(!player.isVisible()){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.hidePlayer(player.getPlayer());
            }
        }
    }

    //Temps en heures
    public void banPlayer(String reason, int time, OfflinePlayer player, String sender) {
        UUID uuid = player.getUniqueId();
        String pseudo = player.getName();
        DataManager dataManager = APICore.getPlugin().getDataManager();
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            final HistoryPlayer history = new HistoryPlayer();
            try {
                history.deserialize((String) dataManager.get(uuid, "history"));
            } catch(CanTakeTimeException ignored){}
            history.set("n", (int) history.get("n") + 1);
            history.set("history" + history.get("n"), Arrays.asList("ban", pseudo, sender, (System.currentTimeMillis() / 1_000L), reason, time, ""));
            dataManager.set(uuid, "history", history.serialize());
            BannedPlayer bannedPlayer = new BannedPlayer();
            bannedPlayer.set("uuid", uuid.toString());
            bannedPlayer.set("name", pseudo);
            bannedPlayer.set("reason", reason);
            bannedPlayer.set("expire", (time == -1 ? -1 : ((int)(System.currentTimeMillis() / 1000) + time * 3600)));
            APICore.getPlugin().getPlayerBanned().put(uuid, bannedPlayer);
            APICore.getPlugin().getAccountManager().banPlayer(bannedPlayer);
            try {
                if(APICore.getPlugin().getAccountManager().isAccountOnRedis(uuid) && !((String)APICore.getPlugin().getAccountManager().getDataRedis("server", uuid)).equalsIgnoreCase("not-connected")) { //Si joueur co --> kick
                    PluginMessageManager.kickBanPlayer(pseudo, reason);
                }
            } catch(CanTakeTimeException ignored){}
        });
        PubSub.broadcast(Prefix.odin + "§3" + pseudo + "§7" + " a été ban pour " + "§b" + reason + (time == -1 ? " §cdéfinitivement" : "§7 durant §b" + (time > 23 ? (time / 24) + " §7jours" : time + " §7heures")));
    }

    public void mutePlayer(String reason, int time, OfflinePlayer target, String sender){
        String pseudo = target.getName();
        UUID uuid = target.getUniqueId();
        DataManager dataManager = APICore.getPlugin().getDataManager();
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            String newReason = reason;
            int newTime = time;
            RecidivistPlayer recidivistPlayer = new RecidivistPlayer();
            try {
                recidivistPlayer.deserialize((String)dataManager.get(uuid, "recidivism"));
            } catch(CanTakeTimeException ignored){}
            if(recidivistPlayer.getTexts().contains(reason.toLowerCase())){
                newReason = recidivistPlayer.getRecidivismReason(reason);
                newTime = recidivistPlayer.getRecidivismTime(reason);
                recidivistPlayer.set(reason.toLowerCase(), (int)recidivistPlayer.get(reason.toLowerCase()) + 1);
            }
            if(newTime < -1){
                APICore.getPlugin().getPlayerManager().banPlayer(newReason, -newTime, target, sender);
                return;
            }
            final HistoryPlayer history = new HistoryPlayer();
            try {
                history.deserialize((String) dataManager.get(uuid, "history"));
            } catch(CanTakeTimeException ignored){}
            history.set("n", (int) history.get("n") + 1);
            history.set("history" + history.get("n"), Arrays.asList("mute", pseudo, sender, (System.currentTimeMillis() / 1_000L), newReason, newTime, ""));
            dataManager.set(uuid, new Data("history", history.serialize()), new Data("recidivism", recidivistPlayer.serialize()));
            MutedPlayer mutedPlayer = new MutedPlayer();
            mutedPlayer.set("uuid", uuid.toString());
            mutedPlayer.set("name", pseudo);
            mutedPlayer.set("reason", newReason);
            mutedPlayer.set("expire", (newTime == -1 ? -1 : ((int)(System.currentTimeMillis() / 1000) + newTime * 3600)));
            APICore.getPlugin().getPlayerMuted().put(uuid, mutedPlayer);
            APICore.getPlugin().getAccountManager().mutePlayer(mutedPlayer);
            PubSub.broadcast(Prefix.odin + "§3" + pseudo + "§7" + " a été mute pour " + "§b" + newReason +  (newTime == -1 ? " §cdéfinitivement" : "§7 durant §b" + (newTime > 23 ? (newTime / 24) + " §7jours" : newTime + " §7heures")));
        });
    }

    public void sendMessageAll(APText message){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.sendMessage(message);
        }
    }

    public void sendMessageAll(String message){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.sendMessage(message);
        }
    }

    public void sendTitleAll(String title, String subTitle){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.sendTitle(title, subTitle);
        }
    }

    public void sendTitleAll(String title, String subTitle, int timeShow, int stay, int timeHide){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.sendTitle(title, subTitle, timeShow, stay, timeHide);
        }
    }

    public void sendMessageBarAll(String message){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.sendMessageBar(message);
        }
    }

    public void executeCommandAll(String command){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            player.performCommand(command);
        }
    }

}
