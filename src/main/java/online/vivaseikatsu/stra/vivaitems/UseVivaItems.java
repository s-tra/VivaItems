package online.vivaseikatsu.stra.vivaitems;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.annotation.ParametersAreNullableByDefault;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Random;


public class UseVivaItems implements Listener {

    private  final VivaItems plg;

    public UseVivaItems(VivaItems plg_){
        plg = plg_;
    }


    // ==== Eventをきっかけに動く部分 ====

    // プレイヤーがブロックにクリックした時
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){
        // プレイヤーの取得
        Player p = e.getPlayer();
        // 権限がない場合なにもせず終了
        if(!p.hasPermission("vivaitems.use")) return;
        // 手に持ってるアイテムを取得
        ItemStack i = e.getPlayer().getInventory().getItemInMainHand();

        // 行動がブロックに対する右クリックの場合
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){

            // 手に持ってるアイテムに説明がない場合なにもせず終了
            if(i.getItemMeta() == null) return;
            if(i.getItemMeta().getLore() == null ) return;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()){

                // テスト
                // vivaitems.rightclick.test
                if(s.contains("vivaitems.rightclick.test")){
                    // playerがスニークじゃない場合はなにもせず終了
                    if(!p.isSneaking()) return;
                    // クールタイム500ms
                    if(plg.isCooldown(p, 500, true)) break;
                    p.sendMessage("[VivaItems]ブロック右クリックを検知");
                    // スニークを解除
                    p.setSneaking(false);
                    break;
                }

                // ブロックをクリックで天気を変更
                // vivaitems.rightclick.weather.*
                if(s.contains("vivaitems.rightclick.weather.")){
                    // playerがスニークじゃない場合はなにもせず終了
                    if(!p.isSneaking()) return;
                    // クールタイム1000ms
                    if(plg.isCooldown(p, 1000, false)) break;

                    // クリックしたブロックを取得
                    Block b = e.getClickedBlock();
                    if(b == null) break;

                    // 天気変更イベントを起こす
                    WeatherChangeEvent(p,b,s);
                    // 手に持っているアイテムを1つ減らす
                    plg.DecreaseMainHandItem(p,1);

                    // スニークを解除
                    p.setSneaking(false);
                    break;
                // 天気を変更ここまで
                }


                // 無限バケツ
                if(s.contains("vivaitems.bucket.reuseable")) {

                    // クールタイム300ms
                    if(plg.isCooldown(p, 100, false)) break;
                    reuseableBucket(p,e.getClickedBlock());

                }


            // 説明欄の走査ここまで
            }

        // RIGHT_CLICK_BLOCKの処理ここまで
        }

        // ブロックまたは空気を右クリックの場合
        if((e.getAction() == Action.RIGHT_CLICK_AIR)||
                (e.getAction() == Action.RIGHT_CLICK_BLOCK)){

            // 手に持ってるアイテムに説明がない場合なにもせず終了
            if(i.getItemMeta() == null) return;
            if(i.getItemMeta().getLore() == null ) return;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()) {

                // テスト
                // vivaitems.rightclick.test
                if(s.contains("vivaitems.rightclick.air.test")){
                    // playerがスニークじゃない場合はなにもせず終了
                    if(!p.isSneaking()) return;
                    // クールタイム1000ms
                    if(plg.isCooldown(p, 1000, true)) break;
                    p.sendMessage("[VivaItems]右クリックを検知");
                    // スニークを解除
                    p.setSneaking(false);
                    break;
                }

                // 見てるブロックまでテレポート
                // vivaitems.rightclick.teleport.see_block
                if(s.contains("vivaitems.rightclick.teleport.see_block")){
                    // テレポートの処理を実行
                    if(TeleportToSeeBlock(p,s)) {
                        if (s.contains(".once")) {
                            // 手に持っているアイテムを1つ減らす
                            plg.DecreaseMainHandItem(p, 1);
                        }
                    }
                // テレポートここまで
                }


            // 説明欄の走査ここまで
            }

        // ブロックまたは空気を右クリックの処理ここまで
        }

    // PlayerInteractEventここまで
    }


    // エンティティに対して右クリックした時
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        // プレイヤーの取得
        Player p = e.getPlayer();
        // 権限がない場合なにもせず終了
        if(!p.hasPermission("vivaitems.use")) return;

        // 手に持ってるアイテムを取得
        ItemStack i = e.getPlayer().getInventory().getItemInMainHand();


        // 手に持ってるアイテムに説明がない場合なにもせず終了
        if(i.getItemMeta() == null) return;
        if(i.getItemMeta().getLore() == null ) return;

        // itemの説明欄の文字列を走査
        for(String s : i.getItemMeta().getLore()){

            // テスト
            // vivaitems.rightclick.test
            if(s.contains("vivaitems.mob_rightclick.test")){
                // playerがスニークじゃない場合はなにもせず終了
                if(!p.isSneaking()) return;
                p.sendMessage("[VivaItems]右クリックを検知");
                // スニークを解除
                p.setSneaking(false);
                break;
            }

            // アーマースタンド用ツールたち
            // vivaitems.rightclick.armor_stand.*
            if(s.contains("vivaitems.rightclick.armor_stand.")){
                // クリックしたエンティティを取得
                // ArmorStandにinstanceできないときは終了
                if(!(e.getRightClicked() instanceof ArmorStand)) return;
                // アーマースタンドを取得
                ArmorStand armorStand = (ArmorStand) e.getRightClicked();
                // アーマースタンド用ツールの処理を実行
                ArmorStandTools(p,s,armorStand);

            }

            // 遠隔ダメージ武器
            // vivaitems.rightclick.weapon
            if(s.contains("vivaitems.rightclick.weapon")){

                // ダメージを与えられるエンティティだった場合処理を続行
                if(e.getRightClicked() instanceof Damageable){
                    rightClickWeapon(p,e.getRightClicked());
                }
            }

            // モブおんぶひも
            // vivaitems.rightclick.mobstrap
            if(s.contains("vivaitems.rightclick.mobstrap")){

                // 対象がプレイヤーだった場合は終了
                if(e.getRightClicked().getType() == EntityType.PLAYER) break;

                // ダメージを与えられるエンティティだった場合処理を続行
                if(e.getRightClicked() instanceof Damageable){
                    // クールタイム1000ms
                    if(plg.isCooldown(p, 1000, false)) break;
                    mobStrap(p,e.getRightClicked());
                }

            }





            // 説明欄の走査ここまで
        }

    // PlayerInteractAtEntityここまで
    }


    // アーマーになにか持たせたり、剥ぎ取ったりする時
    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e){
        // プレイヤーの取得
        Player p = e.getPlayer();
        // 権限がない場合なにもせず終了
        if(!p.hasPermission("vivaitems.use")) return;

        // 手に持ってるアイテムを取得
        ItemStack i = e.getPlayer().getInventory().getItemInMainHand();

        // 手に持ってるアイテムに説明がない場合なにもせず終了
        if(i.getItemMeta() == null) return;
        if(i.getItemMeta().getLore() == null ) return;

        // itemの説明欄の文字列を走査
        for(String s : i.getItemMeta().getLore()){

            // アーマースタンド用アイテムを持たせないようにする
            if(s.contains("vivaitems.rightclick.armor_stand.")){
                e.setCancelled(true);
            }

        // 説明欄の走査ここまで
        }

    // ArmorStandManipulateEventここまで
    }


    // ブロックを設置する時
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        // 設置しようとしているブロックのメタデータを取得
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;
        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsを置けないようにする動作
            if(s.contains("vivaitems.")) e.setCancelled(true);

            // おいたとき動くアイテム
            if(s.contains("vivaitems.place.")){
                // キャンセルをやめる
                e.setCancelled(false);

                // 無限スポンジ
                if(s.contains(".sponge")) {
                    // 水中に置かれたとき
                    if(e.getBlockReplacedState().getType() == Material.WATER){
                        // スポンジイベントを発生させる
                        VivaSponge(e.getBlockPlaced().getLocation());
                    }

                    // スポンジを置いた場所にドロップさせる
                    // 持ってるスポンジを取得
                    ItemStack inHand = new ItemStack(e.getItemInHand());
                    inHand.setAmount(1);

                    World world = e.getBlockPlaced().getWorld();

                    world.dropItemNaturally(e.getBlockPlaced().getLocation(),inHand);

                    e.getBlockPlaced().setType(Material.AIR);

                }

                // マグマスポンジ
                if(s.contains(".magmasponge")) {
                    // 溶岩の中に置かれたとき
                    if(e.getBlockReplacedState().getType() == Material.LAVA){
                        // スポンジイベントを発生させる
                        MagmaSponge(e.getBlockPlaced().getLocation());
                        Location l = e.getBlockPlaced().getLocation();
                        l.getWorld().spawnParticle(Particle.CLOUD,l,1000);
                        l.getWorld().playSound(l,Sound.BLOCK_FIRE_EXTINGUISH,1,1);
                        e.getBlockPlaced().setType(Material.MAGMA_BLOCK);
                    }else {
                        e.setCancelled(true);
                    }





                }


            }



        // 説明欄の走査ここまで
        }

    // BlockCanBuildEventここまで
    }


    // playerがなにかを食べ終わった瞬間
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
        // 食べようとしているブロックのメタデータを取得
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;

        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsを食べれないようにする動作
            if(s.contains("vivaitems.")) e.setCancelled(true);

            // 食べた時動作するアイテムたち
            if(s.contains("vivaitems.eat.")){

                // 食べキャンセルを無効に
                e.setCancelled(false);

                // ガレット・デ・ロワ
                if(s.contains(".galette_des_rois")) {
                    GaletteDesRois(e.getPlayer());
                }

                // フォーチュンクッキー
                // 減らない食べ物





            // 食べる用アイテムここまで
            }

        // 説明欄の走査ここまで
        }
    // PlayerItemConsumeEventここまで
    }


    // エンティティがダメージを受けた時
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){

        // Playerが絡む場合、プレイヤーを取得
        Player p = null;
        if(e.getDamager().getType().equals(EntityType.PLAYER)) p = (Player) e.getDamager();
        if(e.getEntity().getType().equals(EntityType.PLAYER)) p = (Player) e.getEntity();

        // Playerがnullなら終了
        if(p == null)return;
        // 権限がない場合終了
        if(!p.hasPermission("vivaitems.use")) return;

        // インベントリ内全スロットを走査
        for(ItemStack i : p.getInventory().getContents()){
            // 説明欄の取得ができない場合、終了
            if(i == null) continue;
            if(i.getItemMeta() == null) continue;
            if(i.getItemMeta().getLore() == null ) continue;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()) {

                // ファントム・ガード
                // vivaitems.inventory.phantomguard
                if(s.contains("vivaitems.inventory.phantomguard")){
                    // ファントムがプレイヤーに攻撃した場合のみ無効化
                    if(e.getDamager().getType().equals(EntityType.PHANTOM)&&
                            e.getEntity().getType().equals(EntityType.PLAYER)){
                        e.setCancelled(true);
                    }
                }



            // 説明欄の走査ここまで
            }
        // 全スロットの走査ここまで
        }


        // メインハンドの走査
        ItemStack i = p.getInventory().getItemInMainHand();
        // 説明がない場合、なにもせず終了
        if (i.getItemMeta() == null) return;
        if (i.getItemMeta().getLore() == null) return;

        // itemの説明欄の文字列を走査
        for (String s : i.getItemMeta().getLore()) {

            // なぐるたびに経験値
            // vivaitems.punch.experience
            if(s.contains("vivaitems.punch.experience")){
                // プレイヤーがMobを攻撃するたびに経験値オーブを発生させる
                if(e.getDamager().getType().equals(EntityType.PLAYER)){
                    punchExperience(e.getEntity());
                }
            }



        // 説明欄(メインハンド)の走査ここまで
        }

    // EntityDamageByEntityEventここまで
    }


    // playerが死亡したとき
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){

        // Playerを取得
        Player p = e.getEntity();

        // 権限がない場合終了
        if(!p.hasPermission("vivaitems.use")) return;

        // インベントリ内全スロットを走査
        for(ItemStack i : p.getInventory().getContents()){
            // 説明欄の取得ができない場合、終了
            if(i == null) continue;
            if(i.getItemMeta() == null) continue;
            if(i.getItemMeta().getLore() == null ) continue;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()) {

                // ゆめの半券
                if(s.contains("vivaitems.inventory.dreamstub")){

                    // Playerのインベントリを保持させる
                    e.setKeepInventory(true);
                    // Playerのレベルを保持させる
                    e.setKeepLevel(true);

                    // Playerのアイテムドロップを無効に
                    e.getDrops().clear();
                    e.setDroppedExp(0);

                    // 死亡メッセージを編集
                    e.setDeathMessage(p.getDisplayName() + "は死んでしまう夢を見た。");

                    // アイテムを1つ減らす
                    i.setAmount(i.getAmount() - 1);

                    continue;

                // ゆめの半券ここまで
                }



                // 説明欄の走査ここまで
            }
            // 全スロットの走査ここまで
        }



    }

    // playerがバケツでmobを掬うとき
    @EventHandler
    public void onPlayerBucketEntityEvent(PlayerBucketEntityEvent e){

        // メインハンドに持っているバケツのメタデータを取得
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;

        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsなバケツを使えないようにする
            if(s.contains("vivaitems.")) e.setCancelled(true);




            // 説明欄の走査ここまで
        }


        // PlayerBucketEmptyEventここまで
    }


    // playerがバケツを空にしたとき
    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){

        // メインハンドに持っているバケツのメタデータを取得
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;

        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsなバケツを使えないようにする
            if(s.contains("vivaitems.")) e.setCancelled(true);

            // 動作するバケツたち
            if(s.contains("vivaitems.bucket.")){


                // 無限バケツ
                if(s.contains(".reuseable")) {

                    // 大釜が対象だった場合
                    if(e.getBlockClicked().getType() == Material.CAULDRON) return;

                    // バケツキャンセルを無効に
                    e.setCancelled(false);

                    // バケツの準備
                    ItemStack old_bucket = e.getPlayer().getInventory().getItemInMainHand();

                    // playerのメインハンドのアイテムを新しいバケツに置き換え
                    e.getItemStack().setType(old_bucket.getType());
                    e.getItemStack().setItemMeta(old_bucket.getItemMeta());

                }






            // 使えるバケツここまで
            }

        // 説明欄の走査ここまで
        }


    // PlayerBucketEmptyEventここまで
    }


    // playerがバケツを満たしたとき
    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent e){

        // メインハンドに持っているバケツのメタデータを取得
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;

        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsなバケツを使えないようにする
            if(s.contains("vivaitems.")) e.setCancelled(true);

            // 動作するバケツたち
            if(s.contains("vivaitems.bucket.")){

                // 無限バケツ
                if(s.contains(".reuseable")) {

                    // 大釜が対象だった場合、終了
                    if(e.getBlockClicked().getType() == Material.CAULDRON) return;

                    // バケツキャンセルを無効に
                    e.setCancelled(false);

                    // バケツの準備
                    ItemStack old_bucket = e.getPlayer().getInventory().getItemInMainHand();

                    // playerのメインハンドのアイテムを新しいバケツに置き換え
                    e.getItemStack().setType(old_bucket.getType());
                    e.getItemStack().setItemMeta(old_bucket.getItemMeta());

                }






                // 使えるバケツここまで
            }

            // 説明欄の走査ここまで
        }


        // PlayerBucketFillEventここまで
    }


    // 大釜のレベルを変更したとき
    @EventHandler
    public void onCauldronLevelChangeEvent(CauldronLevelChangeEvent e){

        // エンティティがいない場合、終了
        if(e.getEntity() == null) return;
        // 操作者がプレイヤーじゃない場合、終了
        if(e.getEntity().getType() != EntityType.PLAYER) return;

        // プレイヤーを取得
        Player p = (Player) e.getEntity();

        // メインハンドに持っているバケツのメタデータを取得
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        // nullをける
        if(meta == null) return;
        if(meta.getLore() == null) return;

        // 説明欄の走査
        for(String s : meta.getLore()){

            // vivaitemsなアイテムを使えないようにする
            if(s.contains("vivaitems.")) e.setCancelled(true);

        // 説明欄の走査ここまで
        }



    }


    // playerが移動したとき
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){

        //移動先がnullの場合(首を回したとか)は、終了
        if(e.getTo() == null) return;

        // 同じブロック内での移動だった場合、終了
        if(e.getFrom().getBlockX() == e.getTo().getBlockX()
                && e.getFrom().getBlockY() == e.getTo().getBlockY()
                && e.getFrom().getBlockZ() == e.getTo().getBlockZ()
        ) return;

        // 足に装備してるアイテムを取得
        ItemStack i = e.getPlayer().getInventory().getBoots();

        // ブーツの走査
        if(i != null){

            // 足に装備してるアイテムに説明がない場合なにもせず終了
            if(i.getItemMeta() == null) return;
            if(i.getItemMeta().getLore() == null ) return;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()){

                // カタパルトシューズ
                if(s.contains("vivaitems.boots.catapult")){

                    // 1つ上のブロックに移動した場合のみ発動
                    if(e.getFrom().getBlockY() + 1 == e.getTo().getBlockY()){
                        CatapultShoes(e.getPlayer(),e.getFrom());
                    }


                }



            // 説明欄の走査ここまで
            }

        // ブーツの走査ここまで
        }







    // PlayerMoveEventここまで
    }



    // ==== 以下、個々のアイテムの処理たち ====

    // ブロックをクリックで天気変更
    private void WeatherChangeEvent(Player player,Block block,String lore){
        // ブロックのワールドを取得
        World w = block.getWorld();

        // 表示制御用変数
        String weather = null;

        // 天気変更処理
        if(lore.contains(".thunder")){
            w.setStorm(true);
            w.setThundering(true);
            weather = "雷雨";
        }
        if(lore.contains(".rain")){
            w.setStorm(true);
            w.setThundering(false);
            weather = "雨";
        }
        if(lore.contains(".clear")){
            w.setStorm(false);
            w.setThundering(false);
            weather = "晴れ";
        }


        // クリックした場所で音を鳴らす
        w.playSound(block.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE,50,-1);
        w.playSound(block.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,0);
        // パーティクルの発生
        w.spawnParticle(Particle.CLOUD,block.getLocation(),100,1,1,1,0.1);
        w.spawnParticle(Particle.TOTEM,block.getLocation(),800,3,5,3,0.1);


        // 天気変更しなかった場合の処理
        if(weather == null){
            player.sendMessage(ChatColor.GRAY+"[VivaItems] "+ChatColor.GOLD+"しかし、なにも起こらなかった…");
            return;
        }

        // 天気の操作を全体チャットで通知
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY+"[VivaItems] "+ChatColor.GOLD+player.getDisplayName()+"が"+w.getName()+"の天気を"+weather+"にしました。");

    }


    // 見てるブロックにテレポート
    private boolean TeleportToSeeBlock(Player p,String lore){

        // playerがスペクテイターモードのとき、終了
        if(p.getGameMode() == GameMode.SPECTATOR) return false;

        // 対象となるブロックの最大距離を設定
        int distance = 50;
        if(lore.contains(".5")) distance = 5;
        if(lore.contains(".10")) distance = 10;
        if(lore.contains(".20")) distance = 20;
        if(lore.contains(".30")) distance = 30;
        if(lore.contains(".40")) distance = 40;
        if(lore.contains(".50")) distance = 50;
        if(lore.contains(".60")) distance = 60;
        if(lore.contains(".70")) distance = 70;
        if(lore.contains(".80")) distance = 80;
        if(lore.contains(".90")) distance = 90;
        if(lore.contains(".100")) distance = 100;

        // ターゲットのブロックを取得
        Block b = p.getTargetBlockExact(distance);
        // 距離の範囲内で取得できなかった場合
        if(b == null){
            p.sendMessage(ChatColor.GRAY+"[VivaItems] " + distance + "ブロック以内にターゲットがありません！");
            return false;
        }

        // 見てるブロックが岩盤だった時はワープしない
        if(b.getType() == Material.BEDROCK){
            p.sendMessage(ChatColor.GRAY+"[VivaItems] 岩盤の上にはワープできません！");
            return false;
        }


        // 見てる方角を取得
        Vector v = p.getLocation().getDirection();

        // 取得したブロックの上2ブロックを取得
        int x = b.getX();
        int y = b.getY();
        int z = b.getZ();
        Location l_1 = new Location(p.getWorld(),x,y+1,z);
        Location l_2 = new Location(p.getWorld(),x,y+2,z);
        if(l_1.getBlock().isPassable() &&
               l_2.getBlock().isPassable()){

            // 見てる方向を指定
            l_1.setDirection(v);
            // 座標をブロックの真ん中に
            l_1.setX(l_1.getX() + 0.5);
            l_1.setY(l_1.getY() + 0.5);
            l_1.setZ(l_1.getZ() + 0.5);

            // クールタイム1500ms
            if(plg.isCooldown(p, 1500, true)) return false;

            // 取得した場所にワープ、できなかったらfalseを返し終了
            if(p.teleport(l_1, PlayerTeleportEvent.TeleportCause.ENDER_PEARL)){
                // テレポート成功の場合サウンドを再生
                p.playSound(p.getLocation(),Sound.ITEM_CHORUS_FRUIT_TELEPORT,1,1);
                p.spawnParticle(Particle.PORTAL,p.getLocation(),100,1,1,1,0.1);
                return true;
            }

        }
        else{
            p.sendMessage(ChatColor.GRAY+"[VivaItems] ブロックの上に十分な空間がありません！");
        }
        return false;

    }


    // アーマースタンド用ツールたち
    private void ArmorStandTools(Player player,String lore,ArmorStand armorStand){

        // アーマースタンドのサイズを変える
        // vivaitems.rightclick.armor_stand.switch_size
        if(lore.contains("vivaitems.rightclick.armor_stand.switch_size")){

            // アーマースタンドが透明なときはなにもしない
            if(!armorStand.isVisible()) return;
            // アーマースタンドが”小さいかどうか”を反転させる
            armorStand.setSmall(!armorStand.isSmall());
            // サウンドを再生
            player.playSound(armorStand.getLocation(),Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,-1);
            // パーティクルを発生させる
            player.spawnParticle(Particle.COMPOSTER,armorStand.getLocation(),20,0.5,0.5,0.5);

        // アーマースタンドサイズ変更ここまで
        }


        // アーマースタンドに腕を生やす
        // vivaitems.rightclick.armor_stand.switch_arms
        if(lore.contains("vivaitems.rightclick.armor_stand.switch_arms")){

            // アーマースタンドが透明なときはなにもしない
            if(!armorStand.isVisible()) return;
            // アーマースタンドに”腕があるかどうか”を反転させる
            armorStand.setArms(!armorStand.hasArms());
            // サウンドを再生
            player.playSound(armorStand.getLocation(),Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,-1);
            // パーティクルを発生させる
            player.spawnParticle(Particle.COMPOSTER,armorStand.getLocation(),20,0.5,0.5,0.5);

        // アーマースタンド腕生やしここまで
        }

        // アーマースタンドを透明にする
        // vivaitems.rightclick.armor_stand.switch_visible
        if(lore.contains("vivaitems.rightclick.armor_stand.switch_visible")){

            // アーマースタンドに”透明かかどうか”を反転させる
            armorStand.setVisible(!armorStand.isVisible());
            // サウンドを再生
            player.playSound(armorStand.getLocation(),Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,-1);
            // パーティクルを発生させる
            player.spawnParticle(Particle.COMPOSTER,armorStand.getLocation(),20,0.5,0.5,0.5);

        // アーマースタンド透明ここまで
        }

        // アーマースタンドのポーズを変える
        // vivaitems.rightclick.armor_stand.switch_pose
        if(lore.contains("vivaitems.rightclick.armor_stand.switch_pose")){
            // アーマースタンドが透明なときはなにもしない
            if(!armorStand.isVisible()) return;
            // 角度指定用のデータを生成
            EulerAngle angle = new EulerAngle(0,0,0);
            Random random = new Random();
            // 数値をランダムに
            // -0.4 to 0.4 (0.2刻み)
            double Head = (double)  (random.nextInt(4) - 2) / 5;
            // -1.6 to 1.6 (0.4刻み)
            double LeftArm = (double)  (random.nextInt(8) - 4) / 2.5;
            double RightArm = (double)  (random.nextInt(8) - 4) / 2.5;
            // -1.0 to 1.0 (0.2刻み)
            double LeftLeg = (double)  (random.nextInt(10) - 5) / 5;
            double RightLeg = (double)  (random.nextInt(10) - 5) / 5;

            // アーマースタンドのポーズを変える
            armorStand.setHeadPose(angle.setX(Head));
            armorStand.setLeftArmPose(angle.setX(LeftArm));
            armorStand.setRightArmPose(angle.setX(RightArm));
            armorStand.setLeftLegPose(angle.setX(LeftLeg));
            armorStand.setRightLegPose(angle.setX(RightLeg));

            // デバッグ用出力
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] -------------");
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] Head = " + Head);
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] LeftArm = " + LeftArm);
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] RightArm = " + RightArm);
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] LeftLeg = " + LeftLeg);
            //player.sendMessage(ChatColor.GRAY+"[VivaDebug] RightLeg = " + RightLeg);

            // サウンドを再生
            player.playSound(armorStand.getLocation(),Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,-1);
            // パーティクルを発生させる
            player.spawnParticle(Particle.COMPOSTER,armorStand.getLocation(),20,0.5,0.5,0.5);

        // アーマースタンドポーズ変更ここまで
        }


    }


    // ガレット・デ・ロワ
    private void GaletteDesRois(Player p){

        // 乱数生成の準備
        Random random = new Random();
        // 乱数を生成 (0-4)
        int r = random.nextInt(5);

        // 乱数が0の時(確率20％)
        if(r == 0){

            // フェーヴの生成
            ItemStack item = new ItemStack(Material.SUNFLOWER,1);
            // メタデータの取得
            ItemMeta meta = item.getItemMeta();
            // 説明欄用の文字列リストを作成
            ArrayList<String> lore = new ArrayList<String>();

            // フェーヴの種類をランダムに
            int fave = random.nextInt(100);

            // 50%
            if(fave >= 0){
                // アイテムの種類をセット
                item.setType(Material.SUNFLOWER);
                // アイテム名をセット
                meta.setDisplayName(ChatColor.GOLD+""+ChatColor.ITALIC+"太陽のフェーヴ");
                // 説明欄の記入
                lore.clear();
                lore.add(ChatColor.DARK_PURPLE+"もってるだけで幸せになれそう");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY+""+ChatColor.MAGIC+"vivaitems.hotbar.luck");
            }
            // 25%
            if(fave >= 50){
                // アイテムの種類をセット
                item.setType(Material.SCUTE);
                // アイテム名をセット
                meta.setDisplayName(ChatColor.GOLD+""+ChatColor.ITALIC+"カメのフェーヴ");
                // 説明欄の記入
                lore.clear();
                lore.add(ChatColor.DARK_PURPLE+"もってるだけでゆっくりになりそう");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY+""+ChatColor.MAGIC+"vivaitems.hotbar.slowness.3");
            }
            // 15%
            if(fave >= 75){
                // アイテムの種類をセット
                item.setType(Material.FEATHER);
                // アイテム名をセット
                meta.setDisplayName(ChatColor.GOLD+""+ChatColor.ITALIC+"羽のフェーヴ");
                // 説明欄の記入
                lore.clear();
                lore.add(ChatColor.DARK_PURPLE+"もってるだけでフワフワしそう");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY+""+ChatColor.MAGIC+"vivaitems.hotbar.slow_falling");
            }
            // 8%
            if(fave >= 90){
                // アイテムの種類をセット
                item.setType(Material.RABBIT_HIDE);
                // アイテム名をセット
                meta.setDisplayName(ChatColor.GOLD+""+ChatColor.ITALIC+"うさぎのフェーヴ");
                // 説明欄の記入
                lore.clear();
                lore.add(ChatColor.DARK_PURPLE+"もってるだけでぴょんぴょんできそう");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY+""+ChatColor.MAGIC+"vivaitems.hotbar.jump_boost.1");
            }
            // 2%
            if(fave >= 98){
                // アイテムの種類をセット
                item.setType(Material.BLAZE_POWDER);
                // アイテム名をセット
                meta.setDisplayName(ChatColor.GOLD+""+ChatColor.ITALIC+"炎のフェーヴ");
                // 説明欄の記入
                lore.clear();
                lore.add(ChatColor.DARK_PURPLE+"もってるだけで天までのぼれそう");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY+""+ChatColor.MAGIC+"vivaitems.hotbar.levitation");
            }

            // 説明欄をセット
            meta.setLore(lore);
            // アイテムにメタデータをセット
            item.setItemMeta(meta);

            // 効果音とパーティクルの再生
            p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1,2);
            p.spawnParticle(Particle.VILLAGER_HAPPY,p.getLocation(),50,2,2,2,20);
            // playerのインベントリにアイテムを追加
            p.getInventory().addItem(item);

        }


    }


    // なぐるたび経験値
    private void punchExperience(Entity entity){

            ExperienceOrb Orb = (ExperienceOrb) entity.getWorld().spawnEntity(entity.getLocation(),EntityType.EXPERIENCE_ORB);
            Orb.setExperience(1);

    }


    // 無限スポンジ
    private void VivaSponge(Location l){

        // ワールドを取得
        World w = l.getWorld();
        if(w == null) return;

        // blockから縦横高さそれぞれ4ブロックの距離を走査、置き換え
        for(int x = -4 ; x <= 4 ; x++){
            for(int y = -4 ; y <= 4 ; y++){
                for(int z = -4 ; z <= 4 ; z++){

                    // 対象のBlockstateを取得
                    Block block = w.getBlockState((int)l.getX() + x,(int)l.getY() + y,(int)l.getZ() + z).getBlock();


                    // WATERだったら置き換え
                    if(block.getType() == Material.WATER){
                        block.setType(Material.AIR);
                    }

                }
            }
        }



    // 無限スポンジここまで
    }


    // マグマスポンジ
    private void MagmaSponge(Location l){

        // ワールドを取得
        World w = l.getWorld();
        if(w == null) return;

        // blockから縦横高さそれぞれ4ブロックの距離を走査、置き換え
        for(int x = -4 ; x <= 4 ; x++){
            for(int y = -4 ; y <= 4 ; y++){
                for(int z = -4 ; z <= 4 ; z++){

                    // 対象のBlockstateを取得
                    Block block = w.getBlockState((int)l.getX() + x,(int)l.getY() + y,(int)l.getZ() + z).getBlock();


                    // WATERだったら置き換え
                    if(block.getType() == Material.LAVA){
                        block.setType(Material.AIR);
                    }

                }
            }
        }



        // 無限スポンジここまで
    }


    // カタパルトシューズ
    private void CatapultShoes(Player p,Location from){

        // playerがスペクテイターモードのとき、終了
        if(p.getGameMode() == GameMode.SPECTATOR) return;

        // playerが水中にいる場合、終了
        if(from.getBlock().isLiquid()) return;
        // 水に浸かったブロックにいるとき、終了
        if(from.getBlock().getBlockData() instanceof Waterlogged) {
            Waterlogged waterlogged = (Waterlogged) from.getBlock().getBlockData();
            if (waterlogged.isWaterlogged()) return;
        }

        // playerの足元が固体ブロックじゃない場合は、終了
        Location ashimoto = new Location(from.getWorld(),from.getX(),from.getY() - 1,from.getZ());
        if(ashimoto.getBlock().isPassable()) return;

        // はしごでの暴発を防ぐ処理
        if(from.getBlock().getType() == Material.LADDER) return;

        // 階段、半ブロックを登ったときの暴発を防ぐ処理
        // 判定時のY座標を文字列に変換
        String str = String.valueOf(p.getLocation().getY() - p.getLocation().getBlockY());
        // 0.0001よりも小さい位が存在しない場合、終了
        if(str.length() < 7) return;



        // クールタイム2500ms
        if(plg.isCooldown(p,2500,false)) return;

        // サウンドとパーティクルの再生
        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_BLAST,10,1);
        p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,from,200,0,3,0,1);
        // playerに運動を付与
        p.setVelocity( new Vector(0,10,0));


    }

    // 無限バケツ
    private void reuseableBucket(Player p,Block b){

        // 空バケツを大釜に使う場合
        if(p.getInventory().getItemInMainHand().getType() == Material.BUCKET){
            if(b.getType() == Material.WATER_CAULDRON
                    || b.getType() == Material.LAVA_CAULDRON
                    || b.getType() == Material.POWDER_SNOW_CAULDRON) {

                // 効果音を鳴らす
                World w = p.getWorld();

                Location soundLocation = b.getLocation().clone();
                soundLocation.setX(soundLocation.getX() + 0.5);
                soundLocation.setY(soundLocation.getY() + 0.5);
                soundLocation.setZ(soundLocation.getZ() + 0.5);

                if(b.getType() == Material.WATER_CAULDRON) w.playSound(soundLocation, Sound.ITEM_BUCKET_FILL, 100, 1);
                if(b.getType() == Material.LAVA_CAULDRON) w.playSound(soundLocation, Sound.ITEM_BUCKET_FILL_LAVA, 100, 1);
                if(b.getType() == Material.POWDER_SNOW_CAULDRON) w.playSound(soundLocation, Sound.ITEM_BUCKET_FILL_POWDER_SNOW, 100, 1);

                // 空の大釜に置き換え
                b.setType(Material.CAULDRON);

                return;
            }
        }

        // 水バケツを大釜に使う場合
        if(p.getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
            if (b.getType() == Material.CAULDRON
                    || b.getType() == Material.WATER_CAULDRON) {

                // 空の大釜をWATER_CAULDRONに置き換え
                if (b.getType() == Material.CAULDRON) {
                    b.setType(Material.WATER_CAULDRON);
                }

                // ブロックデータの取得
                Levelled cauldron = (Levelled) b.getBlockData();
                // ブロックのレベルを最大値にセット
                cauldron.setLevel(cauldron.getMaximumLevel());
                // ブロックデータの反映
                b.setBlockData(cauldron);

                // 効果音を鳴らす
                Location soundLocation = b.getLocation().clone();
                soundLocation.setX(soundLocation.getX() + 0.5);
                soundLocation.setY(soundLocation.getY() + 0.5);
                soundLocation.setZ(soundLocation.getZ() + 0.5);
                p.getWorld().playSound(soundLocation, Sound.ITEM_BUCKET_EMPTY, 100, 1);

                return;
            }
        }

        // 溶岩バケツを大釜に使う場合
        if(p.getInventory().getItemInMainHand().getType() == Material.LAVA_BUCKET) {
            if (b.getType() == Material.CAULDRON
                    || b.getType() == Material.LAVA_CAULDRON) {

                // 空の大釜をLAVA_CAULDRONに置き換え
                if (b.getType() == Material.CAULDRON) {
                    b.setType(Material.LAVA_CAULDRON);
                }

                // 効果音を鳴らす
                Location soundLocation = b.getLocation().clone();
                soundLocation.setX(soundLocation.getX() + 0.5);
                soundLocation.setY(soundLocation.getY() + 0.5);
                soundLocation.setZ(soundLocation.getZ() + 0.5);
                p.getWorld().playSound(soundLocation, Sound.ITEM_BUCKET_EMPTY_LAVA, 100, 1);

                return;
            }
        }

        // 粉雪バケツを大釜に使う場合
        if(p.getInventory().getItemInMainHand().getType() == Material.POWDER_SNOW_BUCKET) {
            if (b.getType() == Material.CAULDRON
                    || b.getType() == Material.POWDER_SNOW_CAULDRON) {

                // 空の大釜をPOWDER_SNOW_CAULDRONに置き換え
                if (b.getType() == Material.CAULDRON) {
                    b.setType(Material.POWDER_SNOW_CAULDRON);
                }

                // ブロックデータの取得
                Levelled cauldron = (Levelled) b.getBlockData();
                // ブロックのレベルを最大値にセット
                cauldron.setLevel(cauldron.getMaximumLevel());
                // ブロックデータの反映
                b.setBlockData(cauldron);

                // 効果音を鳴らす
                Location soundLocation = b.getLocation().clone();
                soundLocation.setX(soundLocation.getX() + 0.5);
                soundLocation.setY(soundLocation.getY() + 0.5);
                soundLocation.setZ(soundLocation.getZ() + 0.5);
                p.getWorld().playSound(soundLocation, Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW, 100, 1);

                return;
            }
        }


    }

    // 右クリック武器
    private void rightClickWeapon(Player p,Entity e){

        // クールタイム200ms
        if(plg.isCooldown(p, 200, false)) return;

        Damageable d = (Damageable) e;
        World w = e.getWorld();

        // 対象の体力を0に
        d.setHealth(0);

        // ダメージを与える演出
        w.playSound(e.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE,50,-1);
        w.spawnParticle(Particle.SOUL,e.getLocation(),10,1,1,1,0.1);
        w.spawnParticle(Particle.SOUL_FIRE_FLAME,e.getLocation(),10,1,1,1,0.1);



    }

    // モブおんぶひも
    private void mobStrap(Player player,Entity entity){

        // 頭上になにかいるかどうか
        if(player.isEmpty()){

            //　エンティティを乗せる
            player.addPassenger(entity);
            //　サウンドを再生
            player.getWorld().playSound(player.getLocation(),Sound.ITEM_ARMOR_EQUIP_LEATHER,1,0);

        }else{

            // Passengersを順番に下ろす
            for(Entity e:player.getPassengers()){

                // Entityを下ろす
                player.removePassenger(e);

                // プレイヤーの見てる方向を取得
                Vector vec = player.getLocation().getDirection();
                // 数値を処理
                vec.normalize().multiply(0.3);

                // エンティティに速度を付与
                e.setVelocity(vec);
                // サウンドを再生
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_ARROW_SHOOT,1,-5);
            }

        }



    }











// おしまい
}
