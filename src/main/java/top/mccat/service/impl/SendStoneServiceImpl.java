package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.dao.SendStoneDao;
import top.mccat.dao.impl.SendStoneDaoImpl;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.service.SendStoneService;

import java.util.Map;
import java.util.Objects;

/**
 * @author Kevin Li
 * @date 2022/9/28
 * @description
 */
public class SendStoneServiceImpl implements SendStoneService {
    private SendStoneDao sendStoneDao;
    private final Map<String, StrengthStone> stringStrengthStoneMap;
    private static final int MAX_SIZE = 64;
    public SendStoneServiceImpl(Map<String, StrengthStone> stringStrengthStoneMap) {
        this.sendStoneDao = new SendStoneDaoImpl();
        this.stringStrengthStoneMap = stringStrengthStoneMap;
    }

    @Override
    public void sendStoneToPlayer(int count, Player player, String key) throws ItemStrengthException {
        PlayerInventory inventory = player.getInventory();
        StrengthStone strengthStone = stringStrengthStoneMap.get(key);
        //对物品堆进行设置
        Material material = Material.getMaterial(strengthStone.getItem());
        if(material==null){
            throw new ItemStrengthException("&c 物品不存在，请确定您指定的强化石存在！");
        }
        ItemStack stack = new ItemStack(Objects.requireNonNull(material));
        ItemMeta itemMeta = stack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setLore(strengthStone.getLore());
        itemMeta.setDisplayName(strengthStone.getName());
        stack.setItemMeta(itemMeta);
        //计数单位
        int amount ;
        while (count > 0) {
            int firstIndex = inventory.firstEmpty();
            if(count > 64){
                count -= 64;
                amount = 64;
            }else {
                amount = count;
                count = 0;
            }
            if(firstIndex != -1){
                stack.setAmount(amount);
                sendStoneDao.send(firstIndex, stack, player);
                continue;
            }
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if(itemStack == null){
                    continue;
                }
                if(itemStack.getAmount() == MAX_SIZE){
                    continue;
                }
                ItemMeta firstItemMeta = itemStack.getItemMeta();
                assert firstItemMeta != null;
                //下方方法仍有问题，需要bug更改
                if(!firstItemMeta.hasLore()){
                    continue;
                }
                System.out.println(Objects.requireNonNull(firstItemMeta.getLore()).equals(itemMeta.getLore()));
                System.out.println(firstItemMeta.getDisplayName().equals(itemMeta.getDisplayName()));
                if(Objects.requireNonNull(firstItemMeta.getLore()).equals(itemMeta.getLore()) && firstItemMeta.getDisplayName().equals(itemMeta.getDisplayName())) {
                    //第一个相同的数据
                    int firstAmount = itemStack.getAmount();
                    if (amount > 64 - firstAmount) {
                        amount = 64;
                        count -= 64 - firstAmount;
                        itemStack.setAmount(amount);
                        sendStoneDao.send(i, itemStack, player);
                    } else {
                        amount = firstAmount + firstAmount;
                        itemStack.setAmount(amount);
                        sendStoneDao.send(i, itemStack, player);
                    }
                }
            }
            if(amount > 0) {
                throw new ItemStrengthException("&c 当前背包已满，漏发强化石" + count + "个，请凭借截图询问服务器管理员！");
            }
        }
    }
}
