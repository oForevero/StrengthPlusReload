package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.mccat.dao.StoneDao;
import top.mccat.dao.impl.StoneDaoImpl;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.service.StoneService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Distance
 * @date 2022/10/2
 */
public class StoneServiceImpl implements StoneService {
    private StoneDao stoneDao;
    private Map<String, StrengthStone> strengthStoneMap;
    public StoneServiceImpl(){
        stoneDao = new StoneDaoImpl();
        strengthStoneMap = StrengthStone.newInstance();
    }
    @Override
    public void sendStoneToPlayer(String stoneName, PlayerInventory playerInventory, int amount) throws ItemStrengthException {
        boolean hasStone = strengthStoneMap.containsKey(stoneName);
        if(!hasStone){
            throw new ItemStrengthException("&c 强化石不存在，请确保您请求的强化石存在");
        }
        StrengthStone strengthStone = strengthStoneMap.get(stoneName);
        ItemStack stoneStack = stoneDao.giveStoneMeta(strengthStone);
        Material material = Material.getMaterial(strengthStone.getItem());
        assert material != null;
        //第一个相同material的物品
        HashMap<Integer, ? extends ItemStack> allSameItemMap = playerInventory.all(material);
        Set<Integer> sameItemIndexSet = allSameItemMap.keySet();
        while(amount > 0){
            //第一个空的物品
            int firstEmptyIndex = playerInventory.firstEmpty();
            if(firstEmptyIndex > 0 ){
                if(amount > 64){
                    stoneStack.setAmount(64);
                    playerInventory.setItem(firstEmptyIndex, stoneStack);
                    amount -= 64;
                }else {
                    stoneStack.setAmount(amount);
                    playerInventory.setItem(firstEmptyIndex,stoneStack);
                    amount = 0;
                }
                continue;
            }
            for (Integer index : sameItemIndexSet) {
                int sameItemAmount = allSameItemMap.get(index).getAmount();
                if(sameItemAmount == 64){
                    continue;
                }
                if(amount + sameItemAmount > 64 ){
                    stoneStack.setAmount(64);
                    playerInventory.setItem(index, stoneStack);
                    amount -= sameItemAmount;
                }else {
                    stoneStack.setAmount(amount + sameItemAmount);
                    playerInventory.setItem(index, stoneStack);
                    amount = 0;
                }
                break;
            }
            if(amount > 0){
                throw new ItemStrengthException("&c 强化石给予溢出，您的背包已满，溢出数量&a[&b"+amount+"&a]&c个，请通过截图联系管理员进行补发");
            }
        }
    }
}
