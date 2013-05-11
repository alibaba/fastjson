package com.alibaba.json.bvt;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

import data.media.MediaContent;

public class ListFieldTest3 extends TestCase {

	public void test_typeRef() throws Exception {
		String text = "{\"images\":[],\"media\":{\"width\":640}}";

		MediaContent object = JSON.parseObject(text, MediaContent.class);
	}

	public static class Root {
		private List<Image> images = new ArrayList<Image>();
		private Entity media;

		public List<Image> getImages() {
			return images;
		}

		public void setImages(List<Image> images) {
			this.images = images;
		}

		public Entity getMedia() {
			return media;
		}

		public void setMedia(Entity media) {
			this.media = media;
		}

	}

	public static class Image {
		public int width;
	}

	public static class Entity {
		public String title; // Can be null
		public int width;
		public int height;
		public Size size;
	}

	public enum Size {
		SMALL, LARGE
	}
}
