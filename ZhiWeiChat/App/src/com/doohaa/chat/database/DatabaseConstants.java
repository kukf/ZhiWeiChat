package com.doohaa.chat.database;

public class DatabaseConstants {
	public static final String DATABASE_NAME = "note.db";
	public static final int DATABASE_VERSION = 1;

	private interface NoteColumns {
		String NOTE_ID = "id";
		String CONTENT = "content";
		String UPDATE_TIME="update_time";
	}


	public static final class NOTE implements NoteColumns {
		public static final String TABLE_NAME = "note";
		public static final String[] COLUMNS = new String[]{
				NOTE_ID,
				CONTENT,
				UPDATE_TIME,
		};
	}
}
