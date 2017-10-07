package com.doohaa.chat.volley.multipart;

import java.io.OutputStream;

public interface BodyWritable {
	void writeBody(OutputStream out);

	int getFixedLength();
}
