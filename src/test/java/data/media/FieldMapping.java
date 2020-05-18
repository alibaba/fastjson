package data.media;

import java.util.HashMap;

public final class FieldMapping
{
	public final static int FIELD_IX_MEDIA = 1;
	public final static String FULL_FIELD_NAME_MEDIA = "media";
	public final static int FIELD_IX_IMAGES = 2;
	public final static String FULL_FIELD_NAME_IMAGES = "images";
	public final static int FIELD_IX_PLAYER = 3;
	public final static String FULL_FIELD_NAME_PLAYER = "player";
	public final static int FIELD_IX_URI = 4;
	public final static String FULL_FIELD_NAME_URI = "uri";
	public final static int FIELD_IX_TITLE = 5;
	public final static String FULL_FIELD_NAME_TITLE = "title";
	public final static int FIELD_IX_WIDTH = 6;
	public final static String FULL_FIELD_NAME_WIDTH = "width";
	public final static int FIELD_IX_HEIGHT = 7;
	public final static String FULL_FIELD_NAME_HEIGHT = "height";
	public final static int FIELD_IX_FORMAT = 8;
	public final static String FULL_FIELD_NAME_FORMAT = "format";
	public final static int FIELD_IX_DURATION = 9;
	public final static String FULL_FIELD_NAME_DURATION = "duration";
	public final static int FIELD_IX_SIZE = 10;
	public final static String FULL_FIELD_NAME_SIZE = "size";
	public final static int FIELD_IX_BITRATE = 11;
	public final static String FULL_FIELD_NAME_BITRATE = "bitrate";
	public final static int FIELD_IX_PERSONS = 12;
	public final static String FULL_FIELD_NAME_PERSONS = "persons";
	public final static int FIELD_IX_COPYRIGHT = 13;
	public final static String FULL_FIELD_NAME_COPYRIGHT = "copyright";

        // 25-Jun-2011, tatu: Some earlier tests used minimal names; not in use any more
        /*
	
//      public final static String FIELD_NAME_MEDIA = "md";
//      public final static String FIELD_NAME_IMAGES = "im";
//      public final static String FIELD_NAME_PLAYER = "pl";
//      public final static String FIELD_NAME_URI = "ul";
//      public final static String FIELD_NAME_TITLE = "tl";
//      public final static String FIELD_NAME_WIDTH = "wd";
//      public final static String FIELD_NAME_HEIGHT = "hg";
//      public final static String FIELD_NAME_FORMAT = "fr";
//      public final static String FIELD_NAME_DURATION = "dr";
//      public final static String FIELD_NAME_SIZE = "sz";
//      public final static String FIELD_NAME_BITRATE = "br";
//      public final static String FIELD_NAME_PERSONS = "pr";
//      public final static String FIELD_NAME_COPYRIGHT = "cp";
	
	public static final HashMap<String,Integer> fieldToIndex = new HashMap<String,Integer>();
	static {
		fieldToIndex.put(FIELD_NAME_MEDIA, FIELD_IX_MEDIA);
		fieldToIndex.put(FIELD_NAME_IMAGES, FIELD_IX_IMAGES);
		fieldToIndex.put(FIELD_NAME_PLAYER, FIELD_IX_PLAYER);
		fieldToIndex.put(FIELD_NAME_URI, FIELD_IX_URI);
		fieldToIndex.put(FIELD_NAME_TITLE, FIELD_IX_TITLE);
		fieldToIndex.put(FIELD_NAME_WIDTH, FIELD_IX_WIDTH);
		fieldToIndex.put(FIELD_NAME_HEIGHT, FIELD_IX_HEIGHT);
		fieldToIndex.put(FIELD_NAME_FORMAT, FIELD_IX_FORMAT);
		fieldToIndex.put(FIELD_NAME_DURATION, FIELD_IX_DURATION);
		fieldToIndex.put(FIELD_NAME_SIZE, FIELD_IX_SIZE);
		fieldToIndex.put(FIELD_NAME_BITRATE, FIELD_IX_BITRATE);
		fieldToIndex.put(FIELD_NAME_PERSONS, FIELD_IX_PERSONS);
		fieldToIndex.put(FIELD_NAME_COPYRIGHT, FIELD_IX_COPYRIGHT);
	}
	*/

	public static final HashMap<String,Integer> fullFieldToIndex = new HashMap<String,Integer>();
	static {
		fullFieldToIndex.put(FULL_FIELD_NAME_MEDIA, FIELD_IX_MEDIA);
		fullFieldToIndex.put(FULL_FIELD_NAME_IMAGES, FIELD_IX_IMAGES);
		fullFieldToIndex.put(FULL_FIELD_NAME_PLAYER, FIELD_IX_PLAYER);
		fullFieldToIndex.put(FULL_FIELD_NAME_URI, FIELD_IX_URI);
		fullFieldToIndex.put(FULL_FIELD_NAME_TITLE, FIELD_IX_TITLE);
		fullFieldToIndex.put(FULL_FIELD_NAME_WIDTH, FIELD_IX_WIDTH);
		fullFieldToIndex.put(FULL_FIELD_NAME_HEIGHT, FIELD_IX_HEIGHT);
		fullFieldToIndex.put(FULL_FIELD_NAME_FORMAT, FIELD_IX_FORMAT);
		fullFieldToIndex.put(FULL_FIELD_NAME_DURATION, FIELD_IX_DURATION);
		fullFieldToIndex.put(FULL_FIELD_NAME_SIZE, FIELD_IX_SIZE);
		fullFieldToIndex.put(FULL_FIELD_NAME_BITRATE, FIELD_IX_BITRATE);
		fullFieldToIndex.put(FULL_FIELD_NAME_PERSONS, FIELD_IX_PERSONS);
		fullFieldToIndex.put(FULL_FIELD_NAME_COPYRIGHT, FIELD_IX_COPYRIGHT);
	}
}
