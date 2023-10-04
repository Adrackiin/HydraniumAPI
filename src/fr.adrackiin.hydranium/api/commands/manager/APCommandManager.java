package fr.adrackiin.hydranium.api.commands.manager;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.BroadcastAllCmd;
import fr.adrackiin.hydranium.api.commands.BroadcastCmd;
import fr.adrackiin.hydranium.api.commands.BugCmd;
import fr.adrackiin.hydranium.api.commands.ChannelCmd;
import fr.adrackiin.hydranium.api.commands.Compte;
import fr.adrackiin.hydranium.api.commands.ConnectedCmd;
import fr.adrackiin.hydranium.api.commands.EasterEggsCmd;
import fr.adrackiin.hydranium.api.commands.FlyCmd;
import fr.adrackiin.hydranium.api.commands.GCCmd;
import fr.adrackiin.hydranium.api.commands.HelpCmd;
import fr.adrackiin.hydranium.api.commands.LagCmd;
import fr.adrackiin.hydranium.api.commands.LobbyCmd;
import fr.adrackiin.hydranium.api.commands.MsgCmd;
import fr.adrackiin.hydranium.api.commands.NickCmd;
import fr.adrackiin.hydranium.api.commands.PermissionCmd;
import fr.adrackiin.hydranium.api.commands.ReplyCmd;
import fr.adrackiin.hydranium.api.commands.SettingsCmd;
import fr.adrackiin.hydranium.api.commands.SocialspyCmd;
import fr.adrackiin.hydranium.api.commands.TestCmd;
import fr.adrackiin.hydranium.api.commands.VanishCmd;
import fr.adrackiin.hydranium.api.commands.VanishlistCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.BanCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.HistoryCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.KickCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.MuteCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.UnbanCmd;
import fr.adrackiin.hydranium.api.commands.sanctions.UnmuteCmd;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.api.utils.PacketUtils;
import fr.adrackiin.hydranium.api.utils.packet.APPacket;
import fr.adrackiin.hydranium.api.utils.packet.APPacketListener;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class APCommandManager implements Listener {

    private final APHash<String, APCommandListener> commands = new APHash<>();
    private final APHash<String, APServerCommandListener> serverCommands = new APHash<>();

    public APCommandManager() {
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommandPlayer(PlayerCommandPreprocessEvent e){
        String[] preArgs = e.getMessage().split(" ");
        String commandStr = preArgs[0].substring(1).toLowerCase();
        APPlayer player = APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId());
        if(commands.contains(commandStr)){
            APCommandListener listener = commands.get(commandStr);
            APCommand command = listener.getCommand();
            if(command.hasPermission(player)){
                String[] args = new String[preArgs.length - 1];
                System.arraycopy(preArgs, 1, args, 0, preArgs.length - 1);
                int syntax = listener.getSyntax(args);
                if(syntax > -1){
                    String error = listener.onCommand(commandStr, player, args, (short)syntax);
                    if(error != null){
                        player.sendMessage("§c§lErreur: §7" + error);
                    }
                } else {
                    APText error = new APText("§c§lSyntaxe: ");
                    if(command.getSyntaxs().getSize() == 1){
                        error.add(command.getSyntax().getShowedText());
                    } else {
                        error.add(new APText("§7Veuillez survoler ce texte").showText(command.getSyntax().getShowedText()));
                    }
                    player.sendMessage(error);
                }
            } else {
                player.showHelpCommand();
            }
        } else {
            player.showHelpCommand();
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommandServer(ServerCommandEvent e){

    }

    public APCommandListener getCommandListener(String command){
        return this.getCommands().get(command);
    }

    public void addCommand(APCommandListener listener){
        APCommand command = listener.getCommand();
        this.getCommands().put(command.getCommand(), listener);
        for(String alias : command.getAliases().copy()){
            this.getCommands().put(alias, listener);
        }
    }

    public void addServerCommand(APCommand command, APServerCommandListener listener){
        this.getServerCommands().put(command.getCommand(), listener);
        for(String alias : command.getAliases().copy()){
            this.getServerCommands().put(alias, listener);
        }
    }

    public void removeCommand(String command){
        this.getCommands().remove(command);
    }

    @EventHandler
    public void onPostInit(PostInitEvent e){
        new BanCmd();
        new BugCmd();
        new BroadcastCmd();
        new BroadcastAllCmd();
        new ConnectedCmd();
        new Compte();
        new FlyCmd();
        new HelpCmd();
        new HistoryCmd();
        new KickCmd();
        new LagCmd();
        new LobbyCmd();
        new NickCmd();
        new MsgCmd();
        new ReplyCmd();
        new MuteCmd();
        new SettingsCmd();
        new SocialspyCmd();
        new UnbanCmd();
        new UnmuteCmd();
        new VanishCmd();
        new VanishlistCmd();
        new ChannelCmd();
        new PermissionCmd();
        new EasterEggsCmd();
        new TestCmd();
        new GCCmd();
        tabManager();
    }

    public void tabManager() {
        APICore.getPlugin().getPacketManager().addPacketListener(APPacket.Context.READ, new APPacketListener() {
            @Override
            public Object onPacket(Player pl, Packet packet){
                if(packet instanceof PacketPlayInTabComplete){
                    try {
                        Field commandTab = packet.getClass().getDeclaredField("a");
                        commandTab.setAccessible(true);
                        String tab = (String)commandTab.get(packet);
                        String[] tabArray = tab.split(" ");
                        String toCheck = tabArray[0];
                        if(!toCheck.startsWith("/")){
                            return packet;
                        }
                        toCheck = toCheck.substring(1);
                        APPlayer player = APICore.getPlugin().getAPPlayer(pl.getUniqueId());
                        APICore.getPlugin().logServer(player.getName() + " tried tab " + tab);
                        switch(toCheck){
                            case "pl":
                            case "plugins":
                                ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(PacketUtils.packetTabComplete(new String[]{"Qu'est", "ce", "qui", "est", "jaune", "et", "qui", "attend", "?"}));
                                return null;
                            case "ver":
                            case "version":
                                ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(PacketUtils.packetTabComplete(new String[]{"D", "E", "S", "P", "A", "C", "I", "T", "O"}));
                                return null;
                            case "server":
                                ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(PacketUtils.packetTabComplete(new String[]{"C'est", "pas", "un", "café", "ici", "!"}));
                                return null;
                        }
                        if(!getCommands().contains(toCheck)){
                            return null;
                        }
                        if(player.hasPermission(getCommands().get(toCheck).getCommand().getPermission())){
                            String last = tab.substring(tab.length() - 1);
                            if(last.equalsIgnoreCase(" ")){
                                commandTab.set(packet, "");
                                return packet;
                            }
                            List<String> players = new ArrayList<>();
                            for(Player p : Bukkit.getOnlinePlayers()){
                                if(p.getName().toLowerCase().startsWith(tabArray[tabArray.length - 1].toLowerCase())){
                                    players.add(p.getName());
                                }
                            }
                            ((CraftPlayer)player.getPlayer()).getHandle().playerConnection.sendPacket(PacketUtils.packetTabComplete(players.toArray(new String[0])));
                            return null;
                        }
                        return null;
                    } catch(NoSuchFieldException | IllegalAccessException e){
                        e.printStackTrace();
                    }
                }
                return packet;
            }
        });
    }

    public void remove(String command){
        this.commands.remove(command);
    }

    public APHash<String, APCommandListener> getCommands(){
        return commands;
    }

    public APHash<String, APServerCommandListener> getServerCommands(){
        return serverCommands;
    }
}