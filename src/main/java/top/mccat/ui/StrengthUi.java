package top.mccat.ui;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.exception.ItemStrengthException;
import top.mccat.factory.ThreadPoolFactory;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.StrengthMenu;
import top.mccat.service.StrengthService;
import top.mccat.service.impl.StrengthServiceImpl;
import top.mccat.utils.ColorParseUtils;
import top.mccat.utils.ItemStackCheckUtils;
import top.mccat.utils.MsgUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Distance
 * @date 2022/9/8
 */
public class StrengthUi implements Listener {
    private StrengthMenu strengthMenu;
    private final ItemStack displayBar = new ItemStack(Material.PAINTING);
    private final ItemStack air = new ItemStack(Material.AIR);
    private final ItemStack progressBar = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    private final ItemStack runningBar = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
    private final ItemStack successProgressBar = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
    private final ItemStack failProgressBar = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
    private final ItemStack strengthDividerGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    private final ItemStack enchantingTable = new ItemStack(Material.ENCHANTING_TABLE);
    private final ItemStack ironBars = new ItemStack(Material.IRON_BARS);
    private final ItemStack fire = new ItemStack(Material.SOUL_CAMPFIRE);
    private final ItemStack startButton = new ItemStack(Material.END_CRYSTAL);
    private final ItemStack extraTable = new ItemStack(Material.END_PORTAL_FRAME);
    private final ItemStack closeMenu = new ItemStack(Material.BARRIER);
    private final JavaPlugin plugin;
    private StrengthService strengthService;
    private MsgUtils msgUtils;
    private final ThreadPoolExecutor threadPool = ThreadPoolFactory.getThreadPool();
    private Map<String ,StrengthStone> stoneExtraMap = new HashMap<>();
    private final int inventorySize = 54;
    private final Map<Player,Boolean> playerInStrengthActionMap = new HashMap<>();
    /**
     * 整体ui数组，特殊按钮等用air itemstack填充，强化物品放置栏用
     */
    private final ItemStack[] STRENGTH_UI = new ItemStack[]
            {strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,ironBars,ironBars,ironBars,ironBars,strengthDividerGlass,displayBar,
                    strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,ironBars,air,air,ironBars,strengthDividerGlass,strengthDividerGlass,
                    strengthDividerGlass,air,strengthDividerGlass,ironBars,ironBars,ironBars,ironBars,strengthDividerGlass,air,
                    strengthDividerGlass,enchantingTable,strengthDividerGlass,ironBars,fire,fire,ironBars,strengthDividerGlass,extraTable,
                    strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,strengthDividerGlass,startButton,strengthDividerGlass,
                    progressBar,progressBar,progressBar,progressBar,progressBar,progressBar,progressBar,strengthDividerGlass,closeMenu};

    public StrengthUi(JavaPlugin plugin) {
        this.plugin = plugin;
        msgUtils = MsgUtils.newInstance();
        strengthMenu = StrengthMenu.newInstance();
        //赋值强化券Map代码
        Map<String, StrengthStone> stoneMap = StrengthStone.newInstance();
        Set<String> keys = stoneMap.keySet();
        for(String key : keys) {
            StrengthStone strengthStone = stoneMap.get(key);
            if(strengthStone.getExtraStone()){
                stoneExtraMap.put(strengthStone.getName(), strengthStone);
            }
        }
        //赋值强化卷代码结束
        setItemName(enchantingTable,"&b强化物品台");
        setItemName(fire,"&5灵火锻造");
        setItemName(progressBar,"&a进度条");
        setItemName(ironBars,"&8分割线");
        setItemName(strengthDividerGlass, "&8分隔板");
        setItemName(startButton,"&c开始强化");
        setItemName(extraTable,"&b附加物品台");
        setItemName(displayBar, "&a强化信息");
        setItemName(closeMenu, "&c关闭菜单");
        strengthService = new StrengthServiceImpl();
    }

    public Inventory getStrengthInventory(){
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, strengthMenu.getMenuTitle());
        inventory.setContents(STRENGTH_UI);
        return inventory;
    }

    /**
     * 进行强化ui操作监听
     * @param clickEvent 点击强化事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent clickEvent){
//        如果被取消则直接return
        if(clickEvent.isCancelled()){
            return;
        }
//        如果不为玩家直接return
        if(!(clickEvent.getWhoClicked() instanceof Player)){
            return;
        }
        InventoryView inventoryView = clickEvent.getWhoClicked().getOpenInventory();
        Inventory inventory = clickEvent.getInventory();
//        如果其强化菜单名不为玩家设定的参数，且箱子大小不为54直接return
        String parseTitle = ColorParseUtils.parseColorStr(strengthMenu.getMenuTitle());
        if(!inventoryView.getTitle().equals(parseTitle) || inventory.getSize() != inventorySize){
            return;
        }
//        0-53为上层物品栏
        int location = clickEvent.getRawSlot();
        if (location >= 0 && location < inventorySize){
            //msgUtils.sendToConsole("on click event , location: " + location);
//            如果存在玩家正在强化则取消强化事件
            Player player = (Player) clickEvent.getWhoClicked();
            if(playerInStrengthActionMap.containsKey(player) && playerInStrengthActionMap.get(player)){
                clickEvent.setCancelled(true);
                msgUtils.sendToPlayer("您正在执行强化操作，禁止再次强化！",player);
                return;
            }
            switch (clickEvent.getRawSlot()){
                //强化石左槽位
                case 13:
                    break;
                //强化石右槽位
                case 14:
                    break;
                //强化武器放置槽位
                case 19:
                    break;
                //强化券放置槽位
                case 26:
                    break;
                //强化点击槽位
                case 43:
                    clickEvent.setCancelled(true);
                    playerInStrengthActionMap.put(player,true);
                    //获取强化物品，并进行判断
                    ItemStack strengthItem = inventory.getItem(19);
                    if(!ItemStackCheckUtils.notNullAndAir(strengthItem)){
                        msgUtils.sendToPlayer("&c强化栏物品不能为空", player);
                        playerInStrengthActionMap.remove(player);
                        return;
                    }
                    //左右强化石槽位
                    ItemStack leftStone = inventory.getItem(13);
                    ItemStack rightStone = inventory.getItem(14);
                    if(!ItemStackCheckUtils.notNullAndAir(leftStone) && !ItemStackCheckUtils.notNullAndAir(rightStone)) {
                        msgUtils.sendToPlayer("&c强化石槽位不能全部为空", player);
                        playerInStrengthActionMap.remove(player);
                        return;
                    }
                    //强化保护券槽位
                    ItemStack stoneExtra = inventory.getItem(26);
                    StrengthStone strengthExtraStone = null;
                    //进行保护强化卷校验
                    if(ItemStackCheckUtils.notNullAndAir(stoneExtra)){
                        ItemMeta itemMeta = stoneExtra.getItemMeta();
                        //如果强化map中包含该强化券，获取该对象
                        if(stoneExtraMap.containsKey(itemMeta.getDisplayName())){
                            StrengthStone strengthStone = stoneExtraMap.get(stoneExtra.getItemMeta().getDisplayName());
                            //比对lore，物品material
                            strengthStone.getLore().equals(itemMeta.getLore());
                            strengthStone.getItem().equals(stoneExtra.getType().toString());
                            strengthExtraStone = strengthStone;
                        }
                    }
                    StrengthServiceImpl.StrengthResult result;
                    try {
                        result = strengthService.getLevel(strengthItem, new ItemStack[]{leftStone,rightStone}, strengthExtraStone, stoneExtra);
                    } catch (ItemStrengthException e) {
                        msgUtils.sendToPlayer(e.getMessage(),player);
                        playerInStrengthActionMap.remove(player);
                        break;
                    }
                    strengthAction(inventory, strengthItem, player,result);
                    break;
                //按键关闭本菜单
                case 53:
                    player.closeInventory();
                    break;
                //其他方式关闭本菜单
                case -999:
                    break;
                default:
                    clickEvent.setCancelled(true);
            }
        }
    }

    /**
     * 当强化ui进行关闭的事件
     * @param closeEvent 关闭事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryCloseEvent(InventoryCloseEvent closeEvent){
        InventoryView inventoryView = closeEvent.getView();
        Inventory inventory = closeEvent.getInventory();
//        如果不为54格或者不为箱子则直接return
        String parseTitle = strengthMenu.getMenuTitle();
        if(!inventoryView.getTitle().equals(parseTitle) || inventory.getSize() != inventorySize){
            return;
        }
        Player player = (Player)closeEvent.getPlayer();
//        如果不存在则直接return
        if(playerInStrengthActionMap.getOrDefault(player,false)){
            playerInStrengthActionMap.remove(player);
            msgUtils.sendToPlayer("&c强化强行终止，请等待强化冷却后重新强化，已扣除强化石并不返还！",player);
        }
        PlayerInventory playerInventory = player.getInventory();
//        获取左右强化石槽位
        ItemStack leftStrengthStone = inventory.getItem(13);
        setPlayerItem(leftStrengthStone,playerInventory);
        ItemStack rightStrengthStone = inventory.getItem(14);
        setPlayerItem(rightStrengthStone,playerInventory);
//        获取强化物品和附加强化石
        ItemStack strengthItem = inventory.getItem(19);
        setPlayerItem(strengthItem,playerInventory);
        ItemStack stoneExtra = inventory.getItem(26);
        setPlayerItem(stoneExtra,playerInventory);
        msgUtils.sendToPlayer("&c强化ui关闭 ，已返还强化物品",player);
    }

    /**
     * 强化动画及其使用，使用线程池方法
     * @param inventory 背包参数
     * @param strengthItem 强化物品
     * @param player 玩家对象
     * @param strength 强化信息对象
     */
    private void strengthAction(Inventory inventory,ItemStack strengthItem, Player player, StrengthServiceImpl.StrengthResult strength){
        /*Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.STAR).withFlicker().build());
        fireworkMeta.setPower(1);*/
        //如果线程池满应当停止强化操作，并进行提示，回头补上
        threadPool.execute(()->{
            //如果玩家关闭强化菜单则取消事件
            if(!playerInStrengthActionMap.get(player)){
                return;
            }
            //铁砧维修声音
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1F,0F);
            //            循环开始和闪烁次数
            int i = 45;
            int time = 0;
            while(i < 52){
                try {
                    inventory.setItem(i,runningBar);
                    i++;
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    msgUtils.sendToConsole("&c警告，线程阻塞，可能是并发导致的问题！");
                    break;
                }
            }
            //26为额外强化卷
            boolean result = strengthService.strengthItemInUi(strengthItem, player, strength);
            if(result){
                //成功声音
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1F,0F);
                //发射烟花
                //firework.setFireworkMeta(fireworkMeta);
            }else {
                //失败声音，需要进行变更
                player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH,1F,0F);
            }
            while(time < 6){
                try {
                    strengthFinishAnimation(inventory, time, result);
                    time++;
                } catch (InterruptedException e) {
                    msgUtils.sendToConsole("&c警告，线程阻塞，可能是并发导致的问题！");
                    break;
                }
            }
            playerInStrengthActionMap.put(player,false);
        });
    }

    /**
     * 进行强化结束动画展示
     * @param inventory 强化ui
     * @param time 闪烁次数
     * @param success 是否强化成功
     */
    private void strengthFinishAnimation(Inventory inventory,int time, boolean success) throws InterruptedException {
        int start = 0;
        if(success){
            while(start < time){
                for(int i = 45; i < 52; i++){
                    inventory.setItem(i,modifyProgressBarStatus(time));
                }
                Thread.sleep(200);
                start++;
            }
        }else {
            for (int i = 45; i < 52; i++){
                inventory.setItem(i,modifyProgressBarStatus(time, failProgressBar));
            }
        }
    }

    /**
     * 切换动画状态
     * @param time 次数
     * @return ItemStack 对象
     */
    private ItemStack modifyProgressBarStatus(int time){
        if(time%2==0){
            return successProgressBar;
        }
        return progressBar;
    }

    /**
     * 切换动画状态
     * @param time 次数
     * @param otherBar 第二个bar
     * @return ItemStack 对象
     */
    private ItemStack modifyProgressBarStatus(int time, ItemStack otherBar){
        if(time % 2==0){
            return successProgressBar;
        }
        return otherBar;
    }

    /**
     * 设置物品名
     * @param stack itemStack对象
     * @param name 物品名
     */
    private void setItemName(ItemStack stack, String name){
        ItemMeta itemMeta = stack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ColorParseUtils.parseColorStr(name));
        stack.setItemMeta(itemMeta);
    }

    /**
     * 设置物品lore参数
     * @param stack itemStack对象
     * @param lore 物品lore
     */
    private void setItemLore(ItemStack stack, List<String> lore){
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    /**
     * 进行物品设置
     * @param stack 物品堆
     * @param playerInventory 玩家菜单
     */
    private void setPlayerItem(ItemStack stack, PlayerInventory playerInventory){
        if(!ItemStackCheckUtils.notNullAndAir(stack)){
            return;
        }
        int emptyIndex = playerInventory.firstEmpty();
        playerInventory.setItem(emptyIndex,stack);
    }

    public void reloadConfig(){
        msgUtils = MsgUtils.newInstance();
        strengthMenu = StrengthMenu.newInstance();
        //赋值强化券Map代码
        Map<String, StrengthStone> stoneMap = StrengthStone.newInstance();
        Set<String> keys = stoneMap.keySet();
        for(String key : keys) {
            StrengthStone strengthStone = stoneMap.get(key);
            if(strengthStone.getExtraStone()){
                stoneExtraMap.put(strengthStone.getName(), strengthStone);
            }
        }
        strengthService = new StrengthServiceImpl();
        strengthService.reloadConfig();
    }
}
