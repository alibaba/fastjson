package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Bug_for_uin57 extends TestCase {

    public void test_multiArray() throws Exception {
        String jsonString = "{\"block\":{\"boxList\":[{\"dx\":1,\"dy\":1},{\"dx\":0,\"dy\":0},{\"dx\":0,\"dy\":2},{\"dx\":2,\"dy\":0},{\"dx\":2,\"dy\":2}],\"centerBox\":{\"dx\":1,\"dy\":1},\"offsetX\":0,\"offsetY\":0},\"boxs\":[[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null]]}";

        GameSnapShot gs = JSON.parseObject(jsonString, GameSnapShot.class);

        Block block = gs.getBlock();
        Assert.assertEquals(5, block.getBoxList().size());
        Assert.assertEquals(1, block.getBoxList().get(0).getX());
        Assert.assertEquals(1, block.getBoxList().get(0).getY());
        Assert.assertEquals(0, block.getBoxList().get(2).getX());
        Assert.assertEquals(2, block.getBoxList().get(2).getY());
        
        Box[][] boxs = gs.getBoxs();
        Assert.assertEquals(20, boxs.length);
        Assert.assertEquals(12, boxs[0].length);
    }

    public static class GameSnapShot implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 8755961532274905269L;
        protected Box[][]         boxs             = null;
        private Block             block;

        public GameSnapShot(){
            super();
        }

        public GameSnapShot(Box[][] boxs, Block block){
            super();
            this.boxs = boxs;
            this.block = block;
        }

        public Box[][] getBoxs() {
            return boxs;
        }

        public void setBoxs(Box[][] boxs) {
            this.boxs = boxs;
        }

        public Block getBlock() {
            return block;
        }

        public void setBlock(Block block) {
            this.block = block;
        }

    }

    public static class Box {

        @JSONField(name = "dx")
        private int x;

        @JSONField(name = "dy")
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    }

    public static class Block {

        private List<Box> boxList = new ArrayList<Box>();

        private Box       centerBox;

        private int       offsetX;

        private int       offsetY;

        public int getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(int offsetX) {
            this.offsetX = offsetX;
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(int offsetY) {
            this.offsetY = offsetY;
        }

        public Box getCenterBox() {
            return centerBox;
        }

        public void setCenterBox(Box centerBox) {
            this.centerBox = centerBox;
        }

        public List<Box> getBoxList() {
            return boxList;
        }

        public void setBoxList(List<Box> boxList) {
            this.boxList = boxList;
        }

    }
}
