## StrengthPlus

### 2.0版本开发中
目前已拥有Beta先行版，有需要的人可以进行下载，你的支持是我最大的开发进度，如有bug请反馈，谢谢！
由于是Beta版所以我就不写太过正式的使用文档了~（目前仅在1.16.5和 1.18.2上测试，理论上支持1.16.5以上版本，1.16.5以下我没进行测试，1.12.2-1.14.4个人感觉不支持）
支持所有的强化石，无上限强化等级，强化等级指定不同强化石的自定义，支持ui强化功能，支持指定强化石额外几率等操作。支持各种消息通知，插件信息提示等等的完全自定义化。具体配置文档如下所示：
     
**相对于StrengthPlus1.4，StrengthPlus2.0强在哪里？**

随着StrengthPlus的开发后，我陆续收到了许多人的想要不限制等级上限，武器，强化属性扩展等的建议，主要总结为想要完全自定义的信息，想要不计的等级上限，想要自定义强化石，所以2.0版的特性主要是包含了一个定制ui（目前开发已经定型，后期如果有空可能会进行动态适配），包含了强化石的完全自定义（配置文件会自动扫描您自定义的强化石），包含了不同等级使用不同强化石的强化需求（即完全的实现每一级都用不同的强化石，区别等级差距），还有一部分额外属性（暂时由于工作问题搁置，仅实现了吸血），相对而言的，使得其自定义度更加强大，下面我会进行更深层次的讲解。

### 配置文件即自定义教学：
#### config.yml
对于config.yml，主要支持对其的debug操作，用于进行调试和ui开启，但是暂时不支持指令强化，所以这里目前仅支持debug功能和插件名字配置：
```yml
strengthPlus:
  pluginName: '&e[&b&lStrengthPlus&e]'
  # 是否允许调试模式
  debug: true
  # 是否允许强化menu（目前不支持指令强化，请无视）
  enableMenu: true
```
#### strength-attribute.yml
strength-addtibute对应强化的属性，支持强化lore的生成自定义，比如强化的伤害信息，强化的防御，等等信息的配置。目前仅支持罗马字符串，实例效果如下所示：
```yml
strength-attribute:
  # 强化lore标题
  title: '&b[强化等级]:'
  # 强化lore分割线长度
  divider: '----------------'
  # 强化lore等级标识（未使用）
  levelIcon: '✡'
  # 每几个等级标识一换行
  nextLineCount: 10
  # 特殊属性，在允许的属性后增加额外数据
  especialAttribute:
    # 近战类型允许的特殊附魔
    melee:
```
#### strength-level.yml
strength-level对应动态等级配置操作，具体对应无上限等级配置，支持不同等级使用不同的强化石，是否破碎，是否降级等等，这里配置过长，仅展示两个为范例
```yml
# 对应等级
strength-level:
  # 默认配置1-10 等级
  levels:
    -
      # 是否会丢失等级，即强化失败默认掉级（如不写该条目默认为false）
      loseLevel: false
      # 是否会破碎，如果会破碎则一定会进行全服强化广播（如不写该条目默认为false）
      canBreak: false
      #最多指定为两个，ui目前仅支持两个强化石指定
      costStone:
        - 'normal-stone'
      # 对应强化几率，对应上面的等级
      chance: 100
      # 特殊attribute参数
      especialAttribute: false
    -
      loseLevel: true
      canBreak: false
      costStone:
        - 'normal-stone'
      chance: 90
      especialAttribute: false
```
#### strength-stone.yml
对应强化石配置文件，支持各种自定义的强化石，在获取的时候可以使用 /sp givestone 键名（比如普通强化石下的normalstone，即为键名），支持必定成功强化石，保护，管理员等强化石信息，这里仅提供部分范例作为添加模板
```yml
# 强化石类别
strength-stone:
  # 普通强化石
  normal-stone:
    name: '&b普通强化石'
    item: 'SPONGE'
    lore:
      - '&b仅用于普通强化的强化石'
      - '&bdemo for test'
    # 是否为保护强化，必定成功强化，管理员强化
    safe: false
    # 是否为必定成功强化
    success: false
    # 是否为管理员强化，一键满级（
    admin: false
    # 额外强化几率
    chanceExtra: 0
    # 是否为强化券
    extraStone: false
  # 普通强化石
  rare-stone:
    name: '&b高级强化石'
    item: 'SPONGE'
    lore:
      - '&b用于高级强化的强化石'
      - '&c拥有额外的5%的几率'
    # 是否为保护强化，必定成功强化，管理员强化
    safe: false
    # 是否为必定成功强化
    success: false
    # 是否为管理员强化，一键满级（
    admin: false
    # 额外强化几率
    chanceExtra: 5
    # 是否为强化券
    extraStone: false
  safe-stone:
    name: '保护强化卷'
    item: 'PAPER'
    lore:
      - '用于使强化物品不破碎的强化'
    # 是否为保护强化，必定成功强化，管理员强化
    safe: true
    # 是否为必定成功强化
    success: false
    # 是否为管理员强化，一键满级
    admin: false
    # 额外强化几率
    chanceExtra: 0
    # 是否为强化券
    extraStone: true
```

#### strength-msg.yml
强化信息通知的自定义代码，支持玩家变量%player%和等级%level%的自定义了，支持各种信息通知的配置
```yml
strength-msg:
  # 通知
  notify:
    #成功时通知
    success: '强化成功'
    #失败时通知
    fail: '强化失败'
  broadcast:
    success: '&2恭喜%player%将他的装备强化到%level%级'
    safe: '&5很遗憾，%player%在将他的装备强化到%level%级的时候强化炉发生了爆炸，好在装备保护强化券保护了装备没有被摧毁！'
    fail: '&4很遗憾，%player%在将他的装备强化到%level%级的时候强化炉发生了爆炸，装备被摧毁了！'
    maxLevel: '&e只见强化器中一抹金光闪烁，%player%在将他的装备强化到了%level%级！真是让人惊叹！'
```

#### strength-extra.yml
强化参数，用于对标伤害参数和护甲参数。这次通过不同配置重塑了伤害监听：
```yml
strength-extra:
  # 伤害参数
  damage:
    # 单手剑伤害强化倍率
    sword: 1.5
    # 弓箭伤害强化倍率
    bow: 1.5
    # 弩箭伤害强化倍率
    crossbow: 1.2
  # 防御参数
  defence:
    # 一级护甲带来的伤害减免
    armorDefence: 0.5
    # 最小造成伤害
    minDamage: 1
```
#### strength-item.yml
对标强化物品
```yml
#对应可被强化的物品名
strength-item:
  # 近战
  melee:
    - 'WOODEN_SWORD'
    - 'DIAMOND_SWORD'
  # 远程
  remote:
    - 'BOW'
    - 'CROSS_BOW'
  # 防御
  defence:
    - 'DIAMOND_BOOTS'
    - 'DIAMOND_CHESTPLATE'
    - 'DIAMOND_LEGGINGS'
    - 'DIAMOND_HELMET'
```

### 权限配置：
strength.admin 管理员权限，用于给强化石，重载插件配置
### 操作详情：
<img src="https://attachment.mcbbs.net/data/myattachment/forum/202210/02/200952kd8a2kkk572b6yzm.png">
左边附魔台对应强化武器，篝火对应强化石（最多两种），强化信息下对应强化券（即配置文件中的extraStone配置），最后，点击水晶进行强化，结果为黄色玻璃闪烁且播放等级提升声音为强化成功，反之为失败。

### 指令详情：
- /sp menu 或 /qh menu 进入强化菜单
- /sp help 进入指令帮助界面

#### 管理员指令：
- /sp givestone 石头名（支持联想提示） 玩家名（支持联想提示，不填默认为自己） 数量
- /sp reload 重载配置文件
