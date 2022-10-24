package top.mccat.listener;

import javafx.scene.layout.Priority;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthExtra;
import top.mccat.utils.RomaMathGenerateUtil;

import java.util.List;

/**
 * @author Raven
 * @date 2022/10/24 15:48
 */
public class AttackEventListener implements Listener {

    private final StrengthAttribute strengthAttribute;
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    private final StrengthExtra strengthExtra;
    /**
     * 三属性伤害参数
     */
    private final String meleeDamage;
    private final String remotelyDamage;
    private final String armorDefence;
    public AttackEventListener() {
        strengthAttribute = StrengthAttribute.newInstance();
        romaMathGenerateUtil = new RomaMathGenerateUtil();
        strengthExtra = StrengthExtra.newInstance();
        meleeDamage = strengthAttribute.getMeleeDamage();
        remotelyDamage = strengthAttribute.getRemotelyDamage();
        armorDefence = strengthAttribute.getArmorDefence();
    }

    /**
     * 玩家伤害监听事件
     * @param event 事件
     * @return
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void damageEvent(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        //判断物品
        if(!(damager instanceof Player)){
            return;
        }
        Player player = (Player)damager;
        ItemStack weapon = player.getItemInUse();
        if(weapon == null || weapon.getType() == Material.AIR ){
            return;
        }
        ItemMeta itemMeta = weapon.getItemMeta();
        if(itemMeta == null){
            return;
        }
        List<String> lore = itemMeta.getLore();
        if(!itemMeta.hasLore() || lore == null){
            return;
        }
        if(lore.size() == 0){
            return;
        }
        for(String subLore : lore){
            //远程伤害
            if(subLore.contains(remotelyDamage)){
                int levelDamage = romaMathGenerateUtil.romanToInt(subLore.substring(remotelyDamage.length()));
                levelDamage *= strengthExtra.getBowDamage();
                System.out.println("bowDamage:"+levelDamage);
                event.setDamage(levelDamage);
            }
            //近战伤害
            if(subLore.contains(meleeDamage)){
                int levelDamage = romaMathGenerateUtil.romanToInt(subLore.substring(meleeDamage.length()));
                levelDamage *= strengthExtra.getSwordDamage();
                System.out.println("swordDamage:"+levelDamage);
                event.setDamage(levelDamage);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void defenceEvent(EntityDamageByEntityEvent event) {
        Entity defencer = event.getEntity();
    }

}
