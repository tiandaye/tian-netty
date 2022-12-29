package com.tianwangchong.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 数据传输载体 ByteBuf
 *
 * @author: tianwangchong
 * @date: 2020/11/2 5:25 下午
 */
public class ByteBufTest {

	public static void main(String[] args) {
		// 分配内存 capacity:9, maxCapacity:100
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
		print("allocate ByteBuf(9, 100)", buffer);
		// bytebuf是一块内存区域，存储读写内容，由bytebufallocator分配，可以指定堆内分配或堆外分配，堆外分配不受垃圾回收器控制，需要手动释放。bytebuf采用引用计数法标记是否可以回收，如果为0，则可以被回收。分配一个bytebuf，引用计数就加1，调用retain相关的方法，引用计数会加1，release相关方法，引用计数会减1。
		// 分配堆内存使用:ByteBufAllocator.heapBuffer()

		// write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
		// 每个byte占用1个字节, 4个字节(0->4)
		buffer.writeBytes(new byte[]{1, 2, 3, 4});
		print("writeBytes(1,2,3,4)", buffer);

		// write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4
		// 1个int占用4个字节(4->8)
		buffer.writeInt(12);
		print("writeInt(12)", buffer);

		// write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
		// 每个byte占用1个字节, 1个字节(8->9)
		buffer.writeBytes(new byte[]{5});
		print("writeBytes(5)", buffer);

		// write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
		// 每个byte占用1个字节, 1个字节(9->10)
		buffer.writeBytes(new byte[]{6});
		print("writeBytes(6)", buffer);

		// get 方法不改变读写指针【这一块没看明白】
		// 执行了buffer.writeBytes(new byte[]{1, 2, 3, 4});后，往buffer里写了4个byte，再执行buffer.writeInt(12);后，因为int长度为4 bytes，所以又往buffer里写了4个byte，总共写入8个byte。而getshort(3)是从第4个byte开始，一共读取2个byte（即第4和第5个），其二进制表示为 0000 0100 0000 0000，变成十进制就是1024
		// 表示从第3个字节开始读取
		System.out.println("getByte(3) return: " + buffer.getByte(3));
		System.out.println("getShort(3) return: " + buffer.getShort(3));
		System.out.println("getInt(3) return: " + buffer.getInt(3));
		print("getByte()", buffer);


		// set 方法不改变读写指针
		buffer.setByte(buffer.readableBytes() + 1, 0);
		print("setByte()", buffer);

		// read 方法改变读指针
		byte[] dst = new byte[buffer.readableBytes()];
		buffer.readBytes(dst);
		print("readBytes(" + dst.length + ")", buffer);

	}

	private static void print(String action, ByteBuf buffer) {
		System.out.println("after ===========" + action + "============");
		System.out.println("capacity(): " + buffer.capacity());
		System.out.println("maxCapacity(): " + buffer.maxCapacity());
		System.out.println("readerIndex(): " + buffer.readerIndex());
		System.out.println("readableBytes(): " + buffer.readableBytes());
		System.out.println("isReadable(): " + buffer.isReadable());
		System.out.println("writerIndex(): " + buffer.writerIndex());
		System.out.println("writableBytes(): " + buffer.writableBytes());
		System.out.println("isWritable(): " + buffer.isWritable());
		System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
		System.out.println();
	}
}
