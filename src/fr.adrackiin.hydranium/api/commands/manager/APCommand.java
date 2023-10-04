package fr.adrackiin.hydranium.api.commands.manager;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APSet;
import fr.adrackiin.hydranium.api.utils.APText;
import org.bukkit.command.TabCompleter;

public class APCommand {

    private final String command;
    private final APSet<String> syntax = new APSet<>();
    private final APSet<String> aliases = new APSet<>();
    private final String description;
    private final String permission;
    private boolean isVisible;

    public APCommand(String command, String[] aliases, String description, String[] syntax, String permission) {
        this.command = command;
        this.syntax.add(syntax);
        this.aliases.add(aliases);
        this.description = description;
        this.permission = permission;
        this.isVisible = true;
    }

    public APCommand setInvisible(){
        this.isVisible = false;
        return this;
    }

    public boolean hasPermission(APPlayer sender){
        if(this.getPermission().equals("hydranium.api.command.help")){
            return true;
        }
        return sender.hasPermission(this.getPermission());
    }

    public String getCommand() {
        return command;
    }

    public APSet<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public APSet<String> getSyntaxs(){
        return this.syntax;
    }

    public APText getSyntax(){
        APText description = new APText("   §6» §e/" + this.getCommand() + " §f- §7" + this.getDescription());
        StringBuilder desc = new StringBuilder();
        for(String str : this.getSyntaxs().copy()){
            desc.append("§e» §b/").append(this.getCommand()).append(" §a").append(colorDescription(str)).append("\n");
        }
        if(desc.length() != 0){
            description.showText(desc.substring(0, desc.length() - 1));
        }
        return description;
    }

    public APCommand setTabCompleter(TabCompleter completer){
        APICore.getPlugin().getCommand(command).setTabCompleter(completer);
        return this;
    }

    private String colorDescription(String description){
        if(!description.contains("- ")){
            return description;
        }
        String[] split = description.split("- ");
        StringBuilder newDesc = new StringBuilder(split[0]).append("§f- §e").append(split[1]);
        for(int i = 2, splitLength = split.length; i < splitLength; i ++){
            newDesc.append(" - ").append(split[i]);
        }
        return newDesc.toString();
    }



}
