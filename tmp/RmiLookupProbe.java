import common.DangNhapRemote;

import java.rmi.Naming;

public class RmiLookupProbe {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String username = args.length > 2 ? args[2] : "__rmi_probe__";

        DangNhapRemote remote = (DangNhapRemote) Naming.lookup(
                "rmi://" + host + ":" + port + "/DangNhapRemote");
        boolean exists = remote.isUsernameExist(username);

        System.out.println("LOOKUP_OK rmi://" + host + ":" + port + "/DangNhapRemote");
        System.out.println("METHOD_OK isUsernameExist(" + username + ")=" + exists);
    }
}
