package online.vivaseikatsu.stra.vivaitems;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class RuntaskVivaItems extends BukkitRunnable {

    private  final VivaItems plg;

    public RuntaskVivaItems(VivaItems plg_){
        plg = plg_;
    }

    // ここから定期実行処理
    @Override
    public void run(){

        // オンラインなプレイヤー全員を走査
        for(Player p : Bukkit.getOnlinePlayers()){
            //権限のないプレイヤーは対象外に
            if(!p.hasPermission("vivaitems.use")) continue;

            // 装備チェック
            checkArmor(p);
            // ホットバーチェック
            checkHotbar(p);
            // インベントリチェック
            checkInventory(p);

        // 全プレイヤーの走査ここまで
        }

    // 定期実行処理ここまで
    }


    // ==== インベントリ内をチェックする処理たち ====

    // 装備してある時用処理
    public void checkArmor(Player p){
        // 装備してあるアイテムを走査
        for(ItemStack i : p.getInventory().getArmorContents()) {
            // 説明がない場合、なにもせず終了
            if (i == null) continue;
            if (i.getItemMeta() == null) continue;
            if (i.getItemMeta().getLore() == null) continue;

            // itemの説明欄の文字列を走査
            for (String s : i.getItemMeta().getLore()) {
                if (s.contains("vivaitems.armor.")){

                    // テスト
                    // vivaitems.armor.test
                    if (s.contains("vivaitems.armor.test")) {
                        p.sendMessage("[VivaItems]そうびを検出");
                    }

                    // ポーションエフェクトが書かれていたら効果付与
                    plg.sendPotionEffectByLore(p,s,400,true);

                    // パーティクル発生の処理
                    if(s.contains(".particle")){
                        double x = p.getLocation().getX();
                        double y = p.getLocation().getY();
                        double z = p.getLocation().getZ();
                        Location l_1 = new Location(p.getWorld(),x,y+1,z);
                        plg.spawnParticleByLore(s,p.getLocation(),3,0.5,1,0.5,0.01);
                        plg.spawnParticleByLore(s,l_1,3,0.5,1,0.5,0.01);
                    }


                }
            // 説明欄の走査ここまで
            }
            //装備の走査ここまで
        }
    // 装備の処理ここまで
    }


    // ホットバーにある時用処理
    public void checkHotbar(Player p){
        // スロット番号0-8を走査
        for(int i = 0; i <= 8; i++){
            // 対象スロットのアイテムを取得
            ItemStack item = p.getInventory().getItem(i);
            // 説明欄の取得ができない場合、終了
            if(item == null) continue;
            if(item.getItemMeta() == null) continue;
            if(item.getItemMeta().getLore() == null) continue;

            // 説明欄の走査
            for(String s : item.getItemMeta().getLore()){
                if (s.contains("vivaitems.hotbar.")) {

                    // テスト
                    // vivaitems.hotbar.test
                    if (s.contains("vivaitems.hotbar.test")) {
                        p.sendMessage("[VivaItems]ホットバー所持を検出");
                    }

                    // ポーションエフェクトが書かれていたら効果付与
                    plg.sendPotionEffectByLore(p, s, 300,true);

                    // パーティクル発生の処理
                    if(s.contains(".particle")){
                        double x = p.getLocation().getX();
                        double y = p.getLocation().getY();
                        double z = p.getLocation().getZ();
                        Location l_1 = new Location(p.getWorld(),x,y+1,z);
                        plg.spawnParticleByLore(s,p.getLocation(),3,0.5,1,0.5,0.01);
                        plg.spawnParticleByLore(s,l_1,3,0.5,1,0.5,0.01);
                    }


                }
            //説明欄の走査ここまで
            }
        // ホットバー走査ここまで
        }
    // ホットバー処理ここまで
    }


    // インベントリにある時用処理
    public void checkInventory(Player p){
        // インベントリ内全スロットを走査
        for(ItemStack i : p.getInventory().getContents()){
            // 説明欄の取得ができない場合、終了
            if(i == null) continue;
            if(i.getItemMeta() == null) continue;
            if(i.getItemMeta().getLore() == null ) continue;

            // itemの説明欄の文字列を走査
            for(String s : i.getItemMeta().getLore()) {
                if (s.contains("vivaitems.inventory.")) {
                    // テスト
                    // vivaitems.inventory.test
                    if (s.contains("vivaitems.inventory.test")) {
                        p.sendMessage("[VivaItems]インベントリ内所持を検出");
                    }

                    // ポーションエフェクトが書かれていたら効果付与
                    plg.sendPotionEffectByLore(p, s, 300,true);

                    // ファントム・ガード
                    if (s.contains(".phantomguard")) {
                        PhantomGuard(p, 20);
                    }

                }
            // 説明欄の走査ここまで
            }
        // 全スロットの走査ここまで
        }
    // インベントリ処理ここまで
    }


    // ==== 以下、個別の処理たち ====

    // ファントムガードの処理
    private void PhantomGuard(Player player,int radius){
        // 周囲のエンティティを走査
        for(Entity e : player.getNearbyEntities((double) radius,(double)radius,(double)radius)){
            // 生きているファントムにのみ処理を実行
            if(e.getType() == EntityType.PHANTOM){
                if (e instanceof LivingEntity) {
                    ((LivingEntity) e).damage(20);
                }
            }
            // エンティティ走査ここまで
        }
        // ファントムガードここまで
    }





// おしまい
}
