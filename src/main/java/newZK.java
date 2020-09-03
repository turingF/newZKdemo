

public class newZK {

    public static void main(String[] args) {


        //使用两个线程来分别运行业务总线和管理总线
        ManageBus mbus = new ManageBus();

        //假设新建一个module
        ZkModule newModule = new ZkModule("demo7");

        mbus.adminControl("demo7",2);
    }
}
