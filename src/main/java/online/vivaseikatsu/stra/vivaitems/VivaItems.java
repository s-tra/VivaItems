package online.vivaseikatsu.stra.vivaitems;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;


public final class VivaItems extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // listenerの処理を行うクラスを宣言
        getServer().getPluginManager().registerEvents(new UseVivaItems(this),this);

        // 定期実行するクラスを設定
        new RuntaskVivaItems(this).runTaskTimer(this,0,100);

        getLogger().info("プラグインが有効になりました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが無効になりました。");
    }

    // ==== Commonな処理たち ====

    // ポーションエフェクトをかける処理
    // Entity,効果名,効果時間(tick),強さ
    public Boolean sendPotionEffect(Entity entity, String effectname, int tick, int amplifier,boolean isAmbient){
        // エフェクト名が無効な場合は終了
        if(PotionEffectType.getByName(effectname) == null) return false;

        // エンティティが生きている場合のみ効果をかける
        if(entity instanceof LivingEntity){
            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.getByName(effectname),tick,amplifier,isAmbient));
        }
        return true;
    // ポーション効果ここまで
    }


    // 説明文からポーションエフェクトをかける処理
    // Entity,説明文,tick
    public void sendPotionEffectByLore(Entity entity, String lore, int tick, boolean isAmbient){

        String effectname = null;
        int amp = 0;

        // 移動速度上昇
        if(lore.contains(".speed")) effectname = "SPEED";
        // 移動速度低下
        if(lore.contains(".slowness")) effectname = "SLOW";
        // 採掘速度上昇
        if(lore.contains(".haste")) effectname = "FAST_DIGGING";
        // 採掘速度低下
        if(lore.contains(".mining_fatigue")) effectname = "SLOW_DIGGING";
        // 攻撃力上昇
        if(lore.contains(".strength")) effectname = "INCREASE_DAMAGE";
        // 即時回復
        if(lore.contains(".instant_health")) effectname = "HEAL";
        // 即時ダメージ
        if(lore.contains(".instant_damage")) effectname = "HARM";
        // 跳躍力上昇
        if(lore.contains(".jump_boost")) effectname = "JUMP";
        // 吐き気
        if(lore.contains(".nausea")) effectname = "CONFUSION";
        // 再生能力
        if(lore.contains(".regeneration")) effectname = "REGENERATION";
        // 耐性
        if(lore.contains(".resistance")) effectname = "DAMAGE_RESISTANCE";
        // 火炎耐性
        if(lore.contains(".fire_resistance")) effectname = "FIRE_RESISTANCE";
        // 水中呼吸
        if(lore.contains(".water_breathing")) effectname = "WATER_BREATHING";
        // 透明化
        if(lore.contains(".invisibility")) effectname = "INVISIBILITY";
        // 盲目
        if(lore.contains(".blindness")) effectname = "BLINDNESS";
        // 暗視
        if(lore.contains(".night_vision")) effectname = "NIGHT_VISION";
        // 空腹
        if(lore.contains(".hunger")) effectname = "HUNGER";
        // 弱体化
        if(lore.contains(".weakness")) effectname = "WEAKNESS";
        // 毒
        if(lore.contains(".poison")) effectname = "POISON";
        // 衰弱
        if(lore.contains(".wither")) effectname = "WITHER";
        // 体力増強
        if(lore.contains(".health_boost")) effectname = "HEALTH_BOOST";
        // 衝撃吸収
        if(lore.contains(".absorption")) effectname = "ABSORPTION";
        // 満腹度回復
        if(lore.contains(".saturation")) effectname = "SATURATION";
        // 発光
        if(lore.contains(".glowing")) effectname = "GLOWING";
        // 浮遊
        if(lore.contains(".levitation")) effectname = "LEVITATION";
        // 幸運
        if(lore.contains(".luck")) effectname = "LUCK";
        // 不運
        if(lore.contains(".unluck")) effectname = "UNLUCK";
        // 落下速度低下
        if(lore.contains(".slow_falling")) effectname = "SLOW_FALLING";
        // コンジットパワー
        if(lore.contains(".conduit_power")) effectname = "CONDUIT_POWER";
        // イルカの好意
        if(lore.contains(".dolphins_grace")) effectname = "DOLPHINS_GRACE";
        // 不吉な予感
        if(lore.contains(".bad_omen")) effectname = "BAD_OMEN";
        // 村の英雄
        if(lore.contains(".hero_of_the_village")) effectname = "HERO_OF_THE_VILLAGE";

        // 効果レベルを取得
        if(lore.contains(".0")) amp = 0;
        if(lore.contains(".1")) amp = 1;
        if(lore.contains(".2")) amp = 2;
        if(lore.contains(".3")) amp = 3;
        if(lore.contains(".4")) amp = 4;

        // effectnameがnullじゃない時は実際に効果付与
        if(!(effectname == null)){
            sendPotionEffect(entity,effectname,tick,amp,isAmbient);
        }

    // 説明欄からの効果付与ここまで
    }

    // 説明文からパーティクルを発生させる処理
    // Location,
    public void spawnParticleByLore(String lore, Location location,int count,double x,double y,double z,double extra){

        // 念の為nullをける
        if(location.getWorld() == null) return;

        // パーティクルの初期化
        Particle particle = null;

        // 環境エンティティ効果
        if(lore.contains(".ambient_entity_effect"))particle = Particle.SPELL_MOB_AMBIENT;
        // 怒っている村人
        if(lore.contains(".angry_villager"))particle = Particle.VILLAGER_ANGRY;
        // 灰
        if(lore.contains(".ash"))particle = Particle.ASH;
        // バブル
        if(lore.contains(".bubble"))particle = Particle.WATER_BUBBLE;
        // バブルコラムアップ
        if(lore.contains(".bubble_column_up"))particle = Particle.BUBBLE_COLUMN_UP;
        // バブルポップ
        if(lore.contains(".bubble_pop"))particle = Particle.BUBBLE_POP;
        // キャンプファイヤー居心地の良い煙
        if(lore.contains(".campfire_cosy_smoke"))particle = Particle.CAMPFIRE_COSY_SMOKE;
        // キャンプファイヤー信号煙
        if(lore.contains(".campfire_signal_smoke"))particle = Particle.CAMPFIRE_SIGNAL_SMOKE;
        // 雲
        if(lore.contains(".cloud"))particle = Particle.CLOUD;
        // コンポスター
        if(lore.contains(".composter"))particle = Particle.COMPOSTER;
        // 深紅色の胞子
        if(lore.contains(".crimson_spore"))particle = Particle.CRIMSON_SPORE;
        // クリティカル
        if(lore.contains(".crit"))particle = Particle.CRIT;
        // 現在のダウン
        if(lore.contains(".current_down"))particle = Particle.CURRENT_DOWN;
        // 損傷インジケータ
        if(lore.contains(".damage_indicator"))particle = Particle.DAMAGE_INDICATOR;
        // イルカ
        if(lore.contains(".dolphin"))particle = Particle.DOLPHIN;
        // ドラゴンブレス
        if(lore.contains(".dragon_breath"))particle = Particle.DRAGON_BREATH;
        // 滴る蜂蜜
        if(lore.contains(".dripping_honey"))particle = Particle.DRIPPING_HONEY;
        // 滴る溶岩
        if(lore.contains(".dripping_lava"))particle = Particle.DRIP_LAVA;
        // 黒曜石の涙が滴る
        if(lore.contains(".dripping_obsidian_tear"))particle = Particle.DRIPPING_OBSIDIAN_TEAR;
        // 滴る水
        if(lore.contains(".dripping_water"))particle = Particle.DRIP_WATER;
        // エンチャント
        if(lore.contains(".enchant"))particle = Particle.ENCHANTMENT_TABLE;
        // エンドロッド
        if(lore.contains(".end_rod"))particle = Particle.END_ROD;
        // 爆発
        if(lore.contains(".explosion"))particle = Particle.EXPLOSION_NORMAL;
        // 爆発エミッタ
        if(lore.contains(".explosion_emitter"))particle = Particle.EXPLOSION_HUGE;
        // 落下蜂蜜
        if(lore.contains(".falling_honey"))particle = Particle.FALLING_HONEY;
        // 溶岩の落下
        if(lore.contains(".falling_lava"))particle = Particle.FALLING_LAVA;
        // 落下する蜜
        if(lore.contains(".falling_nectar"))particle = Particle.FALLING_NECTAR;
        // 落ちる黒曜石の涙
        if(lore.contains(".falling_obsidian_tear"))particle = Particle.FALLING_OBSIDIAN_TEAR;
        // 流れ落ちる水
        if(lore.contains(".falling_water"))particle = Particle.FALLING_WATER;
        // 花火
        if(lore.contains(".firework"))particle = Particle.FIREWORKS_SPARK;
        // 火炎
        if(lore.contains(".flame"))particle = Particle.FLAME;
        // 閃光
        if(lore.contains(".flash"))particle = Particle.FLASH;
        // 幸せな村人
        if(lore.contains(".happy_villager"))particle = Particle.VILLAGER_HAPPY;
        // 心臓
        if(lore.contains(".heart"))particle = Particle.HEART;
        // アイテムスライム
        if(lore.contains(".item_slime"))particle = Particle.SLIME;
        // アイテム雪だるま
        if(lore.contains(".item_snowball"))particle = Particle.SNOWBALL;
        // 着陸蜂蜜
        if(lore.contains(".landing_honey"))particle = Particle.LANDING_HONEY;
        // 着陸溶岩
        if(lore.contains(".landing_lava"))particle = Particle.LANDING_LAVA;
        // 黒曜石の涙の着陸
        if(lore.contains(".landing_obsidian_tear"))particle = Particle.LANDING_OBSIDIAN_TEAR;
        // 大きな煙
        if(lore.contains(".large_smoke"))particle = Particle.SMOKE_LARGE;
        // 溶岩
        if(lore.contains(".lava"))particle = Particle.LAVA;
        // ノーチラス
        if(lore.contains(".nautilus"))particle = Particle.NAUTILUS;
        // 音符
        if(lore.contains(".note"))particle = Particle.NOTE;
        // ポータル
        if(lore.contains(".portal"))particle = Particle.PORTAL;
        // 煙
        if(lore.contains(".smoke"))particle = Particle.SMOKE_NORMAL;
        // くしゃみ
        if(lore.contains(".sneeze"))particle = Particle.SNEEZE;
        // 魂の炎
        if(lore.contains(".soul_fire_flame"))particle = Particle.SOUL_FIRE_FLAME;
        // ツバ吐き
        if(lore.contains(".spit"))particle = Particle.SPIT;
        // スプラッシュ
        if(lore.contains(".splash"))particle = Particle.WATER_SPLASH;
        // イカ墨
        if(lore.contains(".squid_ink"))particle = Particle.SQUID_INK;
        // スイープアタック
        if(lore.contains(".sweep_attack"))particle = Particle.SWEEP_ATTACK;
        // 不滅のトーテム
        if(lore.contains(".totem_of_undying"))particle = Particle.TOTEM;
        // 歪んだ胞子
        if(lore.contains(".warped_spore"))particle = Particle.WARPED_SPORE;
        // ワックス・オン
        if(lore.contains(".wax_on"))particle = Particle.WAX_ON;
        // ワックス・オフ
        if(lore.contains(".wax_off"))particle = Particle.WAX_OFF;
        // 魔女
        if(lore.contains(".witch"))particle = Particle.SPELL_WITCH;

        // 有効なパーティクルが指定されなかった場合終了
        if(particle == null) return;

        // パーティクルを発生させる
        location.getWorld().spawnParticle(particle,location,count,x,y,z,extra);

    }

    // プレイヤーのメインハンドにあるアイテムを任意の数減らす処理
    // Player,減らす数
    public Boolean DecreaseMainHandItem(Player player,int value){
        // 手に持っているアイテムを取得
        ItemStack i = player.getInventory().getItemInMainHand();

        // 減らした後用のアイテムスタックを作成(アイテムが減らす数以上の時)
        if(i.getAmount() >= value){
            ItemStack after = new ItemStack(i.getType(),i.getAmount()-value);
            // メタデータがある場合は内容をコピー
            if(!(i.getItemMeta() == null)) after.setItemMeta(i.getItemMeta());
            // 手に持ってるアイテムを減らした後ものに変更
            player.getInventory().setItemInMainHand(after);
        }
        else{
            return false;
        }
        return true;
    // アイテム減らしここまで
    }

    // 実行のクールタイムを判定、クールダウン中ならtrueを返す
    // Player,クールタイム(ミリ秒),メッセージを表示するかどうか
    public HashMap<String, Long> cooldownMap = new HashMap<String, Long>();
    public boolean isCooldown(Player player, int cooldownTime, boolean sendMessage){

        // プレイヤーがハッシュマップに含まれている場合
        if(cooldownMap.containsKey(player.getName())){

            // 前回の時間からクールダウンタイム満了までの時間(秒)を算出
            long waitTime = cooldownMap.get(player.getName()) - System.currentTimeMillis() + cooldownTime;

            // 待ち時間が残り0秒以上だった場合
            if(waitTime>0){
                // メッセージが必要な場合
                if(sendMessage){
                    if(waitTime >= 1000){
                        player.sendMessage(ChatColor.GRAY+"[VivaItems] クールダウン中 (あと"+ waitTime/1000 +"秒)");
                    } else {
                        player.sendMessage(ChatColor.GRAY+"[VivaItems] クールダウン中 (あと"+ (double)waitTime/1000 +"秒)");
                    }
                }

                return true;
            }

        }

        //　クールダウンで蹴られなかった場合は、今回の実行時間を記録
        cooldownMap.put(player.getName(), System.currentTimeMillis());

        return false;

        // クールダウン用の処理ここまで
    }



// おしまい
}
