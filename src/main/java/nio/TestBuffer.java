package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 描述:
 * TestBuffer
 * 根据数据类型不同，提供了对应类型的缓冲区
 * @outhor sren
 * @create 2017-10-27 13:08
 */
public class TestBuffer {



    @Test
    public void test01() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String s = "abcdefgh";
        byteBuffer.put(s.getBytes());
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        System.out.println("flip()......................");
        Buffer flip = byteBuffer.flip();
        System.out.println(flip.capacity());
        System.out.println(flip.position());
        System.out.println(flip.limit());

        System.out.println("get().................");
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);
        System.out.println(new String(dst));

        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        byteBuffer.rewind();

        System.out.println("rewind()..............");
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        byteBuffer.clear();
        System.out.println("clear()....................");
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
    }

    @Test
    public void test2() throws Exception {
        FileInputStream fis = new FileInputStream("1.jpg");
        FileOutputStream fos = new FileOutputStream("2.jpg");

        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取数据
        while (inChannel.read(buffer) != -1) {
            buffer.flip(); //切换为读模式
            outChannel.write(buffer);
            buffer.clear();
        }

        inChannel.close();
        outChannel.close();
        fos.close();
        fis.close();

    }

    @Test
    public void test03() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE);

        MappedByteBuffer inMapBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        byte[] dst = new byte[inMapBuffer.limit()];
        inMapBuffer.get(dst);
        outMapBuffer.put(dst);

        inChannel.close();
        outChannel.close();

    }

    @Test
    public void test04() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE);

        inChannel.transferTo(0, inChannel.size(), outChannel);

        inChannel.close();
        outChannel.close();

    }

    @Test
    public void test05() throws Exception {
        RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");

        FileChannel fileChannel = raf1.getChannel();

        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(10240);

        ByteBuffer[] bufs ={buffer1, buffer2};
        fileChannel.read(bufs);

        for (ByteBuffer buffer : bufs) {
            buffer.flip();
        }

        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel raf2Channel = raf2.getChannel();
        raf2Channel.write(bufs);

    }
}