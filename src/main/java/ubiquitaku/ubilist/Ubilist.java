package ubiquitaku.ubilist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Ubilist extends JavaPlugin implements Listener {
    String prefix = "§a§l[UbiList]§r";
    FileConfiguration config;
    List<UUID> ulw = new ArrayList<>();
    List<UUID> ulb = new ArrayList<>();
    String msg;
    boolean b;
    boolean w;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        ulw = (List<UUID>) config.getList("ulw",new ArrayList<>());
        ulb = (List<UUID>) config.getList("ulb",new ArrayList<>());
        msg = config.getString("bmsg"," ");
        b = config.getBoolean("b",false);
        w = config.getBoolean("w",false);
        saveConfig();
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ul")) {
            if (!sender.isOp()) {
                sender.sendMessage("§c§lあなたはそのコマンドを実行する権限を持っていません");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(prefix);
                sender.sendMessage("========================================");
                sender.sendMessage("/ul <w or b> add <MCID> : ブラックリストorホワイトリストに登録します");
                sender.sendMessage("/ul <w or b> rem <MCID> : ブラックリストorホワイトリストから削除します");
                sender.sendMessage("/ul <w or b> off : このプラグインのリストを停止します");
                sender.sendMessage("/ul <w or b> on : このプラグインのリストを動かします");
                sender.sendMessage("========================================");
                return true;
            }
            if (args[0].equals("w")) {
                if (args[1].equals("on")) {
                    if (w) {
                        sender.sendMessage(prefix+"ホワイトリストは既に起動しています");
                        return true;
                    }
                    w = true;
                    config.set("w",true);
                    saveConfig();
                    sender.sendMessage(prefix+"ホワイトリストを起動しました");
                    return true;
                }
                if (args[1].equals("off")) {
                    if (!w) {
                        sender.sendMessage(prefix+"ホワイトリストは既に停止しています");
                        return true;
                    }
                    w = false;
                    config.set("w",false);
                    saveConfig();
                    sender.sendMessage(prefix+"ホワイトリストを停止しました");
                    return true;
                }
                if (args.length != 3) {
                    sender.sendMessage(prefix+"argsの数が間違っている可能性があります");
                    return true;
                }
                Player p = getServer().getPlayer(args[2]);
                if (p.getName().equals("")) {
                    sender.sendMessage(prefix+"§c§lそのプレイヤーは存在しません");
                    return true;
                }
                if (args[1].equals("add")) {
                    if (ulw.contains(p.getUniqueId())) {
                        sender.sendMessage(prefix+"そのプレイヤーは既に登録されています");
                        return true;
                    }
                    ulw.add(p.getUniqueId());
                    config.set("ulw",ulw);
                    saveConfig();
                    sender.sendMessage(prefix+"登録しました");
                    return true;
                }
                if (args[1].equals("rem")) {
                    if (!ulw.contains(p.getUniqueId())) {
                        sender.sendMessage(prefix+"そのプレイヤーは登録されていません");
                        return true;
                    }
                    ulw.remove(p.getUniqueId());
                    config.set("ulw",ulw);
                    saveConfig();
                    sender.sendMessage(prefix+"削除しました");
                    return true;
                }
                return true;
            }
            if (args[0].equals("b")) {
                if (args[1].equals("on")) {
                    if (b) {
                        sender.sendMessage(prefix+"ブラックリストは既に起動しています");
                        return true;
                    }
                    b = true;
                    config.set("b",true);
                    saveConfig();
                    sender.sendMessage(prefix+"ブラックリストを起動しました");
                    return true;
                }
                if (args[1].equals("off")) {
                    if (!b) {
                        sender.sendMessage(prefix+"ブラックリストは既に停止しています");
                        return true;
                    }
                    b = false;
                    config.set("b",false);
                    saveConfig();
                    sender.sendMessage(prefix+"ブラックリストを停止しました");
                    return true;
                }
                if (args.length != 3) {
                    sender.sendMessage(prefix+"argsの数が間違っている可能性があります");
                    return true;
                }
                Player p = getServer().getPlayer(args[2]);
                if (p.getName().equals("")) {
                    sender.sendMessage(prefix+"§c§lそのプレイヤーは存在しません");
                    return true;
                }
                if (args[1].equals("add")) {
                    if (ulb.contains(p.getUniqueId())) {
                        sender.sendMessage(prefix+"そのプレイヤーは既に登録されています");
                        return true;
                    }
                    ulb.add(p.getUniqueId());
                    config.set("ulb",ulb);
                    saveConfig();
                    sender.sendMessage(prefix+"登録しました");
                    return true;
                }
                if (args[1].equals("rem")) {
                    if (!ulb.contains(p.getUniqueId())) {
                        sender.sendMessage(prefix+"そのプレイヤーは登録されていません");
                        return true;
                    }
                    ulb.remove(p.getUniqueId());
                    config.set("ulb",ulb);
                    saveConfig();
                    sender.sendMessage(prefix+"削除しました");
                    return true;
                }
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        if (w) {
            if (ulw.contains(e.getPlayer().getUniqueId())) {
                return;
            }
            e.getPlayer().kickPlayer(msg);
            return;
        }
        if (b) {
            if (!ulb.contains(e.getPlayer().getUniqueId())) {
                return;
            }
            e.getPlayer().kickPlayer(msg);
            return;
        }
    }
}
