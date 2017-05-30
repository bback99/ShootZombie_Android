package com.snowback.tilemapandcharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by shoong on 2017-05-16.
 */

public class World {
    //size of the Tiles in pixels, required for drawing
    public static final int TILESIZE = 16;
    public static final int DRAW_TILESIZE = 16;

    public static int width = 58; //amount of blocks per row
    public static int height = 40; //amount of blocks per column


    //(col, row) => (row * width + col)
    //(x, y)     => (y * width + row)
    public Tile[] mapData;

    //textures and regions
    Texture texture;
    TextureRegion road, tree, bigTree, stump, glass, flower;
    SpriteCache cache;
    int cacheID = 0;

    /**
     * Constructor
     */
    public World()
    {
        int worldMap[][] = {
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,4,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,3,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1 },
                { 1,1,1,1,5,5,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,5,5,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,5,4,0,0,0,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,1,4,0,0,0,5,5,1,6,6,6,6,6,6,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1 },
                { 5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,5,5,5,5,5,5,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1 },
                { 5,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,5,5,5,5,5,1,1,1,1,1,1,1,1 },
                { 4,0,0,0,5,5,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 0,0,0,0,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,4,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,5,5,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,3,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,3,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,5,5,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,5,5,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,0,0,0,0,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 5,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1 },
                { 4,0,0,0,5,5,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1 },
                { 0,0,0,0,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,5,5,1,1,1,1,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,0,0,0,5,5,1,6,6,6,6,6,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1 },
                { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,1 },
        };

        //load textures and regions
        this.texture = new Texture(Gdx.files.internal("maps/tileset.png"));
        this.road = new TextureRegion(texture, 4*TILESIZE, 2*TILESIZE, TILESIZE, TILESIZE);
        this.tree = new TextureRegion(texture, 2*TILESIZE, 0*TILESIZE, TILESIZE, TILESIZE);
        this.glass = new TextureRegion(texture, 1*TILESIZE, 0*TILESIZE, TILESIZE, TILESIZE);
        this.flower = new TextureRegion(texture, 2*TILESIZE, 1*TILESIZE, TILESIZE, TILESIZE);
        this.bigTree = new TextureRegion(texture, 3*TILESIZE, 3*TILESIZE, TILESIZE*4, TILESIZE*5);
        this.stump = new TextureRegion(texture, 13*TILESIZE, 15*TILESIZE, TILESIZE*4, TILESIZE*4);

        //create the entire map and put it in our cache
        cache = new SpriteCache(height*width, true);
        cache.beginCache();

        for(int col = 0; col < height; col++) {
            for(int row = 0; row < width; row++) {
                if (worldMap[col][row] == World.TileType.ROAD.ordinal()) {
                    cache.add(road, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE, DRAW_TILESIZE);
                }
                else if (worldMap[col][row] == World.TileType.TREE.ordinal()) {
                    cache.add(tree, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE, DRAW_TILESIZE);
                }
                else if (worldMap[col][row] == World.TileType.GLASS.ordinal()) {
                    cache.add(glass, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE, DRAW_TILESIZE);
                }
                else if (worldMap[col][row] == World.TileType.FLOWER.ordinal()) {
                    cache.add(flower, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE, DRAW_TILESIZE);
                }
                else if (worldMap[col][row] == World.TileType.BIGTREE.ordinal()) {
                    cache.add(bigTree, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE*4, DRAW_TILESIZE*5);
                }
                else if (worldMap[col][row] == World.TileType.STUMP.ordinal()) {
                    cache.add(stump, TILESIZE*row, TILESIZE*col, DRAW_TILESIZE*4, DRAW_TILESIZE*4);
                }
            }
        }
        cacheID = cache.endCache();
    }

    public void dispose() {
        texture.dispose();
        cache.dispose();
    }

    /**
     * Draws our tiles one by one
     * @param camera
     */
    public void render(OrthographicCamera camera) {
        cache.setProjectionMatrix(camera.combined);
        cache.begin();
        cache.draw(cacheID); //call our cache with cache ID and draw it
        cache.end();
    }

    enum TileType {
        BLACNK, ROAD, TREE, BIGTREE, STUMP, GLASS, FLOWER;

        public static TileType fromInteger(int id)
        {
            switch(id)
            {
                case 0: return BLACNK;
                case 1: return ROAD;
                case 2: return TREE;
                case 3: return BIGTREE;
                case 4: return STUMP;
                default: return BLACNK;
            }
        }
    }

    public class Tile {
        TileType type;
        Color color;
        int x, y;

        public Tile(TileType type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;

            if (type == TileType.TREE) {
                this.color = Color.GREEN;
            } //trees - solid
            else if (type == TileType.ROAD) {
                this.color = Color.DARK_GRAY;
            } //floor
            else if (type == TileType.BLACNK) {
                this.color = Color.RED;
            } //target area
            else {
                this.color = Color.WHITE;
            }
        }


        /*** Getters ***/
        public TileType getType() {
            return this.type;
        }

        public Color getColor() {
            return color;
        }

        /**
         * Returns the position of this tile in the world
         *
         * @return
         */
        public float getX() {
            return this.x * World.TILESIZE;
        }

        public float getY() {
            return this.y * World.TILESIZE;
        }

        /**
         * Returns whether or not a Tile is passable
         *
         * @param type
         * @return
         */
        public boolean isPassable(TileType type) {
            if (type != TileType.TREE)
                return true;
            return false;
        }
    }
}
