package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.channel.Channel;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import org.bukkit.Bukkit;

public class ChannelCmd implements APCommandListener {

    private final APCommand command;

    public ChannelCmd(){
        this.command = new APCommand(
                "channel",
                new String[]{},
                "Gérer les channels",
                new String[]{"get <joueur> - Afficher les cannaux du joueur",
                        "add <joueur> <cannal> - Ajouter un joueur au cannal",
                        "remove <joueur> <cannal> - Enlever un joueur du cannal"},
                "hydranium.api.command.channel"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length < 2){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "get":
                if(args.length > 2){
                    return -1;
                }
                return 1;
            case "add":
                if(args.length > 3){
                    return -1;
                }
                return 2;
            case "remove":
                if(args.length > 3){
                    return -1;
                }
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(!APICore.getPlugin().doesAPPlayerExists(args[1]) || Bukkit.getPlayer(args[1]) == null){
            return "Ce joueur n'est pas connecté";
        }
        APPlayer target = APICore.getPlugin().getAPPlayer(args[1]);
        Channel channel;
        switch(syntax){
            case 1:
                StringBuilder message = new StringBuilder("§a");
                for(Channel c : APICore.getPlugin().getChannelManager().getChannels()){
                    if(c.isMember(target.getUUID())){
                        message.append(c.getName().split("\\.")[1]).append(", ");
                    }
                }
                if(message.length() == 0){
                    player.sendMessage(Prefix.hydranium + "§7Cannaux de §c" + player.getName() + "§7: §cAucun");
                    return null;
                }
                player.sendMessage(Prefix.hydranium + "§7Cannaux de §c" + player.getName() + "§7: " + message.substring(0, message.length() - 2));
                break;
            case 2:
                try {
                    channel = APICore.getPlugin().getChannelManager().getChannel(args[2]);
                } catch(ChannelNotFoundException e) {
                    return "Cannal inexistant: §c" + args[2];
                }
                channel.addPlayer(player.getUUID());
                player.sendMessage(Prefix.hydranium + "§c" + player.getName() + "§7 a été ajouté au cannal §c" + channel.getName().split("\\.")[1]);
                break;
            case 3:
                try {
                    channel = APICore.getPlugin().getChannelManager().getChannel(args[2]);
                } catch(ChannelNotFoundException e) {
                    return "Cannal inexistant: §c" + args[2];
                }
                channel.removePlayer(player.getUUID());
                player.sendMessage(Prefix.hydranium + "§c" + player.getName() + "§7 a été enlevé du cannal §c" + channel.getName().split("\\.")[1]);
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
