package fr.adrackiin.hydranium.api.commands.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGUniqueOpenEvent;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MuteCmd implements APCommandListener, Listener {

    private final APCommand command;
    private String nameForGui;

    public MuteCmd(){
        this.command = new APCommand(
                "mute",
                new String[]{},
                "Mettre au silence un joueur",
                new String[]{"<joueur> - Mute automatique", "<joueur> <raison> - Mute définitif", "<joueur> <durée> <raison> - Mute temporaire"},
                "hydranium.api.command.mute"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return -1;
        }
        if(args.length == 1){
            return 1;
        }
        try {
            Integer.parseInt(args[1].substring(0, args[1].length() - 1));
            return 2;
        } catch(NumberFormatException e){
            return 3;
        }
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        OfflinePlayer target = Players.getOfflinePlayer(args[0]);
        if(APICore.getPlugin().getPlayerMuted().contains(target.getUniqueId())){
            return "§7Ce joueur est déjà mute";
        }
        StringBuilder reason = new StringBuilder();
        switch(syntax){
            case 1:
                try {
                    this.nameForGui = "§d" + target.getName();
                    APICore.getPlugin().getAPGuiManager().get("§cMute").getAPGui().open(player);
                    this.nameForGui = "";
                } catch(APGuiNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                char timeType = args[1].toLowerCase().charAt(args[1].length() - 1);
                int time = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                if(timeType == 'h' && time > 23){
                    return "Pour plus de 23 heures, veuillez spécifier la durée en jours (ex 2 jours: 2d)";
                }
                if(timeType == 'd'){
                    time *= 24;
                }
                for(int i = 2; i < args.length; i++){
                    reason.append(args[i]).append(" ");
                }
                APICore.getPlugin().getPlayerManager().mutePlayer(reason.substring(0, reason.length() - 1), time, target, player.getName()); //Ban
                break;
            case 3:
                for(int i = 1; i < args.length; i++){
                    reason.append(args[i]).append(" ");
                }
                APICore.getPlugin().getPlayerManager().mutePlayer(reason.substring(0, reason.length() - 1), -1, target, player.getName()); //Ban
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }

    @EventHandler
    public void onAskName(APGUniqueOpenEvent e){
        if(e.getName().equals("§cMute")){
            e.setObject(nameForGui);
        }
    }
}
