package com.snowback.tilemapandcharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;

/**
 * Created by bback99 on 2017-05-30.
 */

public class AssetManager {

    private static AssetManager mInstance = null;

    protected AssetManager() {
        // Exists only to defeat instantiation.
    }

    public static AssetManager getInstance() {
        if(mInstance == null) {
            mInstance = new AssetManager();
        }
        return mInstance;
    }

    private com.badlogic.gdx.assets.AssetManager mAssetManager = new com.badlogic.gdx.assets.AssetManager();
    private Texture mTxtZombie, mTxtBullet, mTxtHPBar;
    private TextureAtlas mAtlasWalking;
    private Animation mAniCharRight, mAniCharRightDown, mAniCharDown, mAniCharDownLeft, mAniCharLeft, mAniCharLeftUp, mAniCharUp, mAniCharUpRight;
    private boolean bIsLoaded = false;

    public void Init() {
        mAssetManager.load("resources/zombie.png", Texture.class);
        mAssetManager.load("resources/bullet.png", Texture.class);
        mAssetManager.load("uiAssets/green.jpg", Texture.class);
        mAssetManager.load("players/character10/right/walkingRight.atlas", TextureAtlas.class);
        for(int i=1; i<=22; i++) {
            String strName = "players/character10/" + String.format("%02d", i) + ".png";
            mAssetManager.load(strName, Texture.class);
        }
    }

    public void makeResource() {
        mTxtZombie = mAssetManager.get("resources/zombie.png", Texture.class);
        mTxtBullet = mAssetManager.get("resources/bullet.png", Texture.class);
        mTxtHPBar = mAssetManager.get("uiAssets/green.jpg", Texture.class);

        // make animation from atlas
        mAtlasWalking = mAssetManager.get("players/character10/right/walkingRight.atlas", TextureAtlas.class);
        mAniCharRight = new Animation(1/10f, mAtlasWalking.getRegions());

        // make animation from texture
        ArrayList<String> lstTexture = new ArrayList<String>();
        lstTexture.add("players/character10/09.png");
        lstTexture.add("players/character10/10.png");
        lstTexture.add("players/character10/11.png");
        lstTexture.add("players/character10/12.png");
        mAniCharRightDown = makeCharacterAnimation(4, lstTexture);

        lstTexture.clear();
        lstTexture.add("players/character10/20.png");
        lstTexture.add("players/character10/21.png");
        lstTexture.add("players/character10/22.png");
        mAniCharDown = makeCharacterAnimation(3, lstTexture);

        lstTexture.clear();
        lstTexture.add("players/character10/13.png");
        lstTexture.add("players/character10/14.png");
        lstTexture.add("players/character10/15.png");
        lstTexture.add("players/character10/16.png");
        mAniCharDownLeft = makeCharacterAnimation(4, lstTexture);

        lstTexture.clear();
        lstTexture.add("players/character10/05.png");
        lstTexture.add("players/character10/06.png");
        lstTexture.add("players/character10/07.png");
        lstTexture.add("players/character10/08.png");
        mAniCharLeft = makeCharacterAnimation(4, lstTexture);
        mAniCharLeftUp = makeCharacterAnimation(4, lstTexture);

        lstTexture.clear();
        lstTexture.add("players/character10/17.png");
        lstTexture.add("players/character10/18.png");
        lstTexture.add("players/character10/19.png");
        mAniCharUp = makeCharacterAnimation(3, lstTexture);

        lstTexture.clear();
        lstTexture.add("players/character10/01.png");
        lstTexture.add("players/character10/02.png");
        lstTexture.add("players/character10/03.png");
        lstTexture.add("players/character10/04.png");
        mAniCharUpRight = makeCharacterAnimation(4, lstTexture);

        bIsLoaded = true;
    }

    public Animation makeCharacterAnimation(int regionCount, ArrayList<String> lstTexture) {
        int nCount = 0;
        TextureRegion[] frames = new TextureRegion[regionCount];
        for(String textureName: lstTexture) {
            if (mAssetManager.isLoaded(textureName)) {
                Texture texture = mAssetManager.get(textureName, Texture.class);
                frames[nCount++] = new TextureRegion(texture);
            }
        }
        return new Animation(1/10f, frames);
    }

    public com.badlogic.gdx.assets.AssetManager getAssetManager() { return mAssetManager; }
    public Texture getZombie() { return mTxtZombie; }
    public Texture getBullet() { return mTxtBullet; }
    public Texture getHPBar() { return mTxtHPBar; }
    public Animation getAniCharRight() { return mAniCharRight; }
    public Animation getAniCharRightDown() { return mAniCharRightDown; }
    public Animation getAniCharDown() { return mAniCharDown; }
    public Animation getAniCharDownLeft() { return mAniCharDownLeft; }
    public Animation getAniCharLeft() { return mAniCharLeft; }
    public Animation getAniCharLeftUp() { return mAniCharLeftUp; }
    public Animation getAniCharUp() { return mAniCharUp; }
    public Animation getAniCharUpRight() { return mAniCharUpRight; }

    public boolean isbIsLoaded() {
        return bIsLoaded;
    }
}
