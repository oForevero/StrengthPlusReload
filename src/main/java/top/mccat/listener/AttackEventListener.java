package top.mccat.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.pojo.bean.Attribute;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthExtra;
import top.mccat.utils.ItemStackCheckUtils;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.RomaMathGenerateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Raven
 * @date 2022/10/24 15:48
 */
public class AttackEventListener implements Listener {

    private StrengthAttribute strengthAttribute;
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    private StrengthExtra strengthExtra;
    private final MsgUtils msgUtils;
    private Map<String,Attribute> especialMeleeAttribute;
    private Map<String,Attribute> especialRemoteAttribute;
    private Map<String,Attribute> especialDefenceAttribute;
    private List<Attribute> enableMeleeList;
    private List<Attribute> enableRemoteList;
    private List<Attribute> enableDefenceList;
    private ItemStack bowWeapon = null;
    private Player bowDamager = null;
    public AttackEventListener() {
        romaMathGenerateUtil = new RomaMathGenerateUtil();
        msgUtils = MsgUtils.newInstance();
        strengthExtra = StrengthExtra.newInstance();
        strengthAttribute = StrengthAttribute.newInstance();
        initEspecialAttributes();
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
        bowDamager = (Player) event.getEntity();
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
        Player player = null;
        ItemStack weapon = null;
        if(damager.getType() == EntityType.ARROW){
            if(bowWeapon == null){
                return;
            }
            player = bowDamager;
            weapon = bowWeapon;
        }else if(!(damager instanceof Player)){
            return;
        }
        if(bowWeapon == null && bowDamager == null){
            player = (Player) damager;
            weapon = player.getInventory().getItemInMainHand();
        }
        if(!ItemStackCheckUtils.notNullAndAir(weapon)){
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
        //进行近战额外参数判定
        for(String subLore : lore){
            //当长度大于4则有额外属性
            if(lore.size() > 4){
                String subStr = subLore.substring(0, subLore.length() - 4);
                List<Attribute> collect = enableMeleeList.stream().filter(attr -> attr.getName().equals(subStr)).collect(Collectors.toList());
                for(Attribute attr : collect){
                    switch(attr.getTag()){
                        case "bloodSuck":
                            //执行吸血
                            if(especialMeleeAttribute.containsKey("bloodSuck")){
                                double health = player.getHealth();
                                double maxHealth = player.getMaxHealth();
                                if(maxHealth - health < 2){
                                    player.setHealth(player.getMaxHealth());
                                }else {
                                    player.setHealth(player.getHealth() + 2);
                                }
                                msgUtils.sendDebugMsgToConsole("&b吸血玩家为：&a[&b"+player.getName()+"&a] 吸血血量为："+2);
                            }
                            break;
                        /*case "absoluteDamage":
                            //绝对伤害
                            if(especialMeleeAttribute.containsKey("absoluteDamage")){
                                damageByEntityEvent.getEntity().set
                            }*/
                        default:
                            break;
                    }
                }
            }
            //远程伤害
            if(subLore.contains(strengthAttribute.getRemotelyDamage())){
                int levelDamage = romaMathGenerateUtil.romanToInt(subLore.substring(strengthAttribute.getRemotelyDamage().length()));
                levelDamage *= strengthExtra.getBowDamage();
                double damage = levelDamage + damageByEntityEvent.getDamage();
                msgUtils.sendDebugMsgToConsole("&b远程伤害参数：&a[&c"+damage+"&a]"+" &a基础伤害："+"&b[&c"+ levelDamage +"&b]"+ "&e 等级附加伤害："+"&b[&c"+ levelDamage +"&b]");
                damageByEntityEvent.setDamage(damage);
                //return;
            }
            //近战伤害
            if(subLore.contains(strengthAttribute.getMeleeDamage())){
                int levelDamage = romaMathGenerateUtil.romanToInt(subLore.substring(strengthAttribute.getMeleeDamage().length()));
                levelDamage *= strengthExtra.getSwordDamage();
                double baseDamage = damageByEntityEvent.getDamage();
                double damage = levelDamage + baseDamage;
                damageByEntityEvent.setDamage(damage);
                msgUtils.sendDebugMsgToConsole("&b近战伤害参数：&a[&c"+damage+"&a]"+" &a基础伤害："+"&b[&c"+ baseDamage +"&b]"+ "&e 等级附加伤害："+"&b[&c"+ levelDamage +"&b]");
                damageByEntityEvent.setDamage(levelDamage + baseDamage);
                //return;
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
                if (subLore.contains(strengthAttribute.getArmorDefence())){
                    int levelDefence = romaMathGenerateUtil.romanToInt(subLore.substring(strengthAttribute.getArmorDefence().length()));
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

    /**
     * 初始化允许的map参数
     */
    private void initEspecialAttributes() {
        this.especialMeleeAttribute = strengthAttribute.getMeleeAttribute().entrySet().stream().filter(entry -> entry.getValue().isEnable()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k2));
        this.especialDefenceAttribute = strengthAttribute.getDefenceAttribute().entrySet().stream().filter(entry -> entry.getValue().isEnable()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k2));
        this.especialRemoteAttribute = strengthAttribute.getRemoteAttribute().entrySet().stream().filter(entry -> entry.getValue().isEnable()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k2));
        this.enableDefenceList = new ArrayList<>(especialDefenceAttribute.values());
        this.enableMeleeList = new ArrayList<>(especialMeleeAttribute.values());
        this.enableRemoteList = new ArrayList<>(especialRemoteAttribute.values());
    }

    public void reloadConfig() {
        msgUtils.reloadMsgConfig();
        strengthExtra = strengthExtra.reloadConfigFile();
        strengthAttribute = strengthAttribute.reloadConfigFile();
        initEspecialAttributes();
    }
}
