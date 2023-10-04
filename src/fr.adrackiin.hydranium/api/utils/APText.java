package fr.adrackiin.hydranium.api.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

import java.util.ArrayList;
import java.util.List;

public class APText {

    private final TextComponent text;

    public APText(String brutText){
        TextComponent text = new TextComponent("");
        if(!brutText.contains("§")){
            text.addExtra(brutText);
            text.setColor(ChatColor.WHITE);
            this.text = text;
            return;
        }
        String[] partTemp = brutText.split("§");
        List<String> part = new ArrayList<>();
        for(short i = 0; i < partTemp.length; i ++){
            if(!partTemp[i].equals("")) {
                if(partTemp[i].length() == 1 && ChatColor.getByChar(partTemp[i].charAt(0)) != null) {
                    if(i + 1 < partTemp.length) {
                        part.add("§" + partTemp[i] + "§" + partTemp[i + 1]);
                        i++;
                    }
                } else {
                    part.add("§" + partTemp[i]);
                }
            }
        }
        for(String s : part) {
            byte change = 0;
            TextComponent temp = new TextComponent(s);
            for(Character c : s.toCharArray()){
                if(c == '§'){
                    change ++;
                }
            }
            temp.setText(s.substring(change * 2));
            if(change != 0) {
                for(short i = 0; i < s.toCharArray().length; i++) {
                    if(s.toCharArray()[i] == '§') {
                        switch(s.toCharArray()[i + 1]) {
                            case 'k': temp.setObfuscated(true);
                                break;
                            case 'l': temp.setBold(true);
                                break;
                            case 'm': temp.setStrikethrough(true);
                                break;
                            case 'n': temp.setUnderlined(true);
                                break;
                            case 'o': temp.setItalic(true);
                                break;
                            case 'r':
                                temp.setObfuscated(false);
                                temp.setBold(false);
                                temp.setStrikethrough(false);
                                temp.setUnderlined(false);
                                temp.setItalic(false);
                                break;
                            default:
                                temp.setColor(ChatColor.getByChar(s.toCharArray()[i + 1]));
                                break;
                        }
                        i++;
                    }
                }
            }
            text.addExtra(temp);
        }
        this.text = text;
    }

    public APText showText(String show){
        return showText(getBaseComponent(show));
    }

    public APText showText(BaseComponent[] show){
        this.text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, show));
        return this;
    }

    public APText runCommand(String command){
        this.text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    public APText suggestCommand(String command){
        this.text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    public APText add(APText add){
        this.text.addExtra(add.send());
        return this;
    }

    public APText add(BaseComponent[] add){
        for(BaseComponent b : add){
            this.text.addExtra(b);
        }
        return this;
    }

    public TextComponent send(){
        return text;
    }

    public BaseComponent[] getShowedText(){
        return this.text.getHoverEvent().getValue();
    }

    private BaseComponent[] getBaseComponent(String message){
        return new ComponentBuilder(message).create();
    }
}
