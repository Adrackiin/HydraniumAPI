package fr.adrackiin.hydranium.api.management.data;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import org.bukkit.Bukkit;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private static final APICore apiCore = APICore.getPlugin();
    private static final AccountManager accountManager = apiCore.getAccountManager();

    public void set(UUID uuid, String type, Object value){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                if(accountManager.isAccountOnRedis(uuid)){
                    accountManager.setDataRedis(type, value, uuid);
                    return;
                }
                if(accountManager.isAccountOnDB(uuid)){
                    accountManager.setDataDB(type, value, uuid);
                }
            } catch(CanTakeTimeException ignored){}
        });
    }

    public void set(UUID uuid, Data... datas){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            Map<String, Object> account = new Hashtable<>();
            for(Data data : datas){
                account.put(data.getType(), data.getValue());
            }
            try {
                if(accountManager.isAccountOnRedis(uuid)){
                    accountManager.setDataRedis(account, uuid);
                    return;
                }
                if(accountManager.isAccountOnDB(uuid)){
                    for(Data data : datas){
                        accountManager.setDataDB(data.getType(), data.getValue(), uuid);
                    }
                }
            } catch(CanTakeTimeException ignored){}
        });
    }

    public Object get(UUID uuid, String type) throws CanTakeTimeException{
        if(accountManager.isAccountOnRedis(uuid)){
            return accountManager.getDataRedis(type, uuid);
        }
        return accountManager.getDataDB(type, uuid);
    }
}
