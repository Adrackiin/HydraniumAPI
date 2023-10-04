package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.api.utils.APTreeSet;
import fr.adrackiin.hydranium.commons.Rank;

public class HelpCmd implements APCommandListener {

    private final APCommand command;

    private final APText show = new APText("   §7(Vous pouvez survolez / cliquez)");

    private final APText first = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§3 §aAide §3§m-§e§m--§c§m---§e§m--§3§m-");
    private final APText server = new APText("   §6» §eServeur §f– §7Informations générales");
    private final APText commands = new APText("   §6» §eCommandes §f– §7Liste des commandes");
    private final APText end = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§a§m----§3§m-§e§m--§c§m---§e§m--§3§m-");

    private final APText firstS = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§3 §aServeur §3§m-§e§m--§c§m---§e§m--§3§m-");
    private final APText info = new APText("   §6» §eLe serveur §f– §7Informations sur le serveur");
    private final APText ts = new APText("   §6» §eTeamspeak §f– §7Le Teamspeak du serveur");
    private final APText staff = new APText("   §6» §eStaff §f– §7Liste des membres du staff");
    private final APText endS = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§a§m--------§3§m-§e§m--§c§m---§e§m--§3§m-");

    private final APText previous = new APText("                          §b« Précédent");
    private final APText next = new APText("                          §bSuivant »");
    private final APText endC = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§a§m---------------§3§m-§e§m--§c§m---§e§m--§3§m-");

    public HelpCmd(){
        this.command = new APCommand(
                "help",
                new String[]{"aide", "sos"},
                "Afficher l'aide",
                new String[]{"- Menu de l'aide", "server - Informations sur le serveur", "command - Commandes disponibles", "command <page> - Commandes disponibles"},
                "hydranium.api.command.help"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
        server.showText("Afficher les informations générales");
        server.runCommand( "/help serveur");
        commands.showText("Afficher les commandes");
        commands.runCommand( "/help commandes");

        info.showText("Hydranium est un serveur proposant\ndes UHCs dont le but est de se préparer\npuis de combattre les différents joueurs\nLes UHCs sont annoncés sur le Twitter du serveur\n§b@HydraniumUHC");
        ts.showText("Hydranium dispose d'un Teamspeak public\nafin de discuter entre joueurs ou\ns'il y a besoin de parler à\nun membre du staff\nIP: §bts.hydranium.fr");
        staff.showText("Membres du staff: \n §6» " + Rank.ADMIN.getPrefix() + "§fAdrackiin - CosmoSs_");
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return 1;
        }
        switch(args[0].toLowerCase()){
            case "server":
            case "serveur":
            case "srv":
                return 2;
            case "command":
            case "commands":
            case "commande":
            case "commandes":
            case "cmd":
                if(args.length == 1){
                    return 3;
                }
                return 4;
            default:
                return 1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        switch(syntax){
            case 1:
                player.sendMessage("");
                player.sendMessage(first);
                player.sendMessage("");
                player.sendMessage(show);
                player.sendMessage(server);
                player.sendMessage(commands);
                player.sendMessage("");
                player.sendMessage(end);
                break;
            case 2:
                player.sendMessage("");
                player.sendMessage(firstS);
                player.sendMessage("");
                player.sendMessage(info);
                player.sendMessage(ts);
                player.sendMessage(staff);
                player.sendMessage("");
                player.sendMessage(endS);
                break;
            case 3:
            case 4:
                APCommand commandTemp;
                APTreeSet<String> commandsShowed = new APTreeSet<>();
                for(APCommandListener cmd : APICore.getPlugin().getCommandManager().getCommands().getValues()){
                    if(player.hasPermission(cmd.getCommand().getPermission()) && cmd.getCommand().isVisible()){
                        commandsShowed.add(cmd.getCommand().getCommand());
                    }
                }
                if(commandsShowed.getSize() <= 0){
                    if(player.isConnected()){
                        APICore.getPlugin().logServer(player.getDisplayName() + " n'a pas pu faire /help cmd");
                        return "§7Une erreur est survenue";
                    } else {
                        return null;
                    }
                }
                int page;
                if(syntax == 3){
                    page = 1;
                } else {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e){
                        return "§7Veuillez spécifier un nombre";
                    }
                }
                int pages;
                float nbCmd = commandsShowed.getSize();
                if((nbCmd / 5) > (int)(nbCmd / 5)){
                    pages = (int)(nbCmd / 5) + 1;
                } else {
                    pages = (int)(nbCmd / 5);
                }
                if(page > pages){
                    page = pages;
                }
                APText tmp = new APText("    §3§m-§e§m--§c§m---§e§m--§3§m-§a Commandes [" + page + "/" + pages + "] §3§m-§e§m--§c§m---§e§m--§3§m-");
                player.sendMessage("");
                player.sendMessage(tmp);
                if(page != 1){
                    previous.runCommand( "/help cmd " + (page - 1));
                    player.sendMessage(previous);
                } else {
                    player.sendMessage("");
                }
                APText description;
                for(int i = (page - 1) * 5; i < page * 5; i ++){
                    if(i < commandsShowed.getSize()) {
                        commandTemp = APICore.getPlugin().getCommandManager().getCommandListener(commandsShowed.get(i)).getCommand();
                        description = commandTemp.getSyntax();
                        description.suggestCommand("/" + commandTemp.getCommand() + " ");
                        player.sendMessage(description);
                    }
                }
                if(page != pages){
                    next.runCommand( "/help cmd " + (page + 1));
                    player.sendMessage(next);
                } else {
                    player.sendMessage("");
                }
                player.sendMessage(endC);
                break;
        }
        return null;
    }


    @Override
    public APCommand getCommand(){
        return command;
    }
}
