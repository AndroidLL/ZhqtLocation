package zhqt.lmw.zhqtlocationTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool 
{

	public static byte[]getBytes (InputStream is ) throws IOException
	{
		byte[] buffer = new byte[1024]; 
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		
		int len = 0;
		while((len = is.read(buffer))!= -1 )
		{
			b.write(buffer,0,len);
		}
		is.close();
		
		return b.toByteArray();
	}
	
}
