package top.mccat.handler;

import com.sun.istack.internal.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Distance
 * @date 2022/9/6
 */
public class CommanderHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, @NotNull Command command, @NotNull String mainCommand, String[] commandArray) {
        if(commandSender instanceof Player){
            if (mainCommand.equalsIgnoreCase("")){

            }
        }
        return true;
    }
}
