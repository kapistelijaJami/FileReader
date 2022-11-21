package filereader;

import binaryutil.BinaryUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Optional;

public class FileReader {
	protected int readerHEAD = 0;
	
	public String readChars(byte[] bytes, int offset, int length) {
		String res = "";
		for (int i = offset; i < offset + length; i++) {
			char c = (char) bytes[i];
			res += c;
		}
		readerHEAD += length;
		return res;
	}
	
	public int readInt(byte[] bytes, int offset, ByteOrder order) {
		ByteBuffer buf = ByteBuffer.wrap(bytes, offset, 4);
		buf.order(order);
		readerHEAD += 4;
		return buf.getInt();
	}
	
	public short readShort(byte[] bytes, int offset, ByteOrder order) {
		ByteBuffer buf = ByteBuffer.wrap(bytes, offset, 2);
		buf.order(order);
		readerHEAD += 2;
		return buf.getShort();
	}
	
	public byte readByte(byte[] bytes, int offset) {
		readerHEAD++;
		return bytes[offset];
	}
	
	public static void print(String s) {
		System.out.println(s);
	}
	
	public static void print(byte b) {
		System.out.println(b + " ; " + (char) b);
	}
	
	public static void print(int i) {
		System.out.println(i);
	}
	
	public static void print(short s) {
		System.out.println(s);
	}
	
	public static Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
		  .filter(f -> f.contains("."))
		  .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	public long readBytesAsLong(byte[] bytes, int offset, int amount, ByteOrder byteOrder) {
		readerHEAD += amount;
		return BinaryUtil.bytesToNumber(byteOrder, true, Arrays.copyOfRange(bytes, offset, offset + amount));
	}
	
	public void resetReader() {
		readerHEAD = 0;
	}
	
	public int readVariableLengthQty(byte[] bytes, int offset) {
		long mask = BinaryUtil.getMask(7);
		
		int res = 0;
		
		int i = 0;
		while (true) {
			int b = readByte(bytes, offset + i);
			
			res = res << 7;
			res += b & mask;
			
			if (BinaryUtil.firstBit(b) == 0) {
				break;
			}
			i++;
		}
		
		return res;
	}
	
	public String readHexByte(byte[] bytes, int offset, boolean prefix) {
		return BinaryUtil.toHexString(readByte(bytes, offset), 2, prefix, false);
	}
}
