package util;

/**
 * Created by SkyDream on 2017/3/6.
 */
public interface Contracts {
    String EDGE_PATH = Contracts.class.getClassLoader().getResource("").getPath() + "edges";
    String HOST = "133.133.61.112";
    int PORT = 6123;
}
