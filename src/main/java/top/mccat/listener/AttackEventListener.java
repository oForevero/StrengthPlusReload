package top.mccat.listener;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthExtra;
import top.mccat.utils.ItemStackCheckUtils;
import top.mccat.utils.MsgUtils;
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
    private final MsgUtils msgUtils;
    /**
     * 三属性伤害参数
     */
    private final String meleeDamage;
    private final String remotelyDamage;
    private final String armorDefence;
    private ItemStack bowWeapon = null;
    public AttackEventListener() {
        romaMathGenerateUtil = new RomaMathGenerateUtil();
        msgUtils = MsgUtils.newInstance();
        strengthExtra = StrengthExtra.newInstance();
        strengthAttribute = StrengthAttribute.newInstance();
        meleeDamage = strengthAttribute.getMeleeDamage();
        remotelyDamage = strengthAttribute.getRemotelyDamage();
        armorDefence = strengthAttribute.getArmorDefence();
    }

    /**
     * 获取弓监听
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void remoteDamageEvent(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        bowWeapon = event.getBow();
    }

    /**
     * 玩家伤害监听事件
     * @param damageByEntityEvent 近战事件
     * @return
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void damageEvent(EntityDamageByEntityEvent damageByEntityEvent){
        Entity damager = damageByEntityEvent.getDamager();
        ItemStack weapon;
        if(damager.getType() == EntityType.ARROW){
            if(bowWeapon == null){
                return;
            }
            weapon = bowWeapon;
        }else if(!(damager instanceof Player)){
            return;
        }else {
            Player player = (Player) damager;
            weapon = player.getInventory().getItemInMainHand();
        }
        if(ItemStackCheckUtils.notNullAndAir(weapon)){
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
                double damage = levelDamage + damageByEntityEvent.getDamage();
                msgUtils.sendDebugMsgToConsole("&b远程伤害参数：&a[&c"+damage+"&a]"+" &a基础伤害："+"&b[&c"+ levelDamage +"&b]"+ "&e 等级附加伤害："+"&b[&c"+ levelDamage +"&b]");
                damageByEntityEvent.setDamage(damage);
                return;
            }
            //近战伤害
            if(subLore.contains(meleeDamage)){
                int levelDamage = romaMathGenerateUtil.romanToInt(subLore.substring(meleeDamage.length()));
                levelDamage *= strengthExtra.getSwordDamage();
                double baseDamage = damageByEntityEvent.getDamage();
                double damage = levelDamage + baseDamage;
                damageByEntityEvent.setDamage(damage);
                msgUtils.sendDebugMsgToConsole("&b近战伤害参数：&a[&c"+damage+"&a]"+" &a基础伤害："+"&b[&c"+ baseDamage +"&b]"+ "&e 等级附加伤害："+"&b[&c"+ levelDamage +"&b]");
                damageByEntityEvent.setDamage(levelDamage + baseDamage);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void defenceEvent(EntityDamageByEntityEvent event) {
        Entity defencer = event.getEntity();
        if(!(defencer instanceof Player)){
            return;
        }
        Player player = (Player)defencer;
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack armorContent : armorContents) {
            if(!ItemStackCheckUtils.notNullMeta(armorContent)){
                continue;
            }
            ItemMeta itemMeta = armorContent.getItemMeta();
            assert itemMeta != null;
            if(!itemMeta.hasLore()){
                continue;
            }
            List<String> lore = itemMeta.getLore();
            for (String subLore : lore) {
                if (subLore.contains(armorDefence)){
                    int levelDefence = romaMathGenerateUtil.romanToInt(subLore.substring(armorDefence.length()));
                    levelDefence *= strengthExtra.getArmorDefence();
                    double eventDamage = event.getDamage();
                    double damage = levelDefence - eventDamage;
                    msgUtils.sendDebugMsgToConsole("&b伤害参数：&a[&c"+eventDamage+"&a]"+" &a抵御伤害："+"&b[&c"+ damage +"&b]"+ "&e 等级附加防御："+"&b[&c"+ levelDefence +"&b]");
                    //防御值是否超过伤害
                    if (damage > 0) {
                        double minDamage = strengthExtra.getMinDamage();
                        msgUtils.sendDebugMsgToConsole("&c伤害未超过防御值，设置最小伤害：&b[&a"+ minDamage+"&b]");
                        event.setDamage(minDamage);
                    } else {
                        event.setDamage(damage);
                    }
                    return;
                }
            }
        }
    }

}
