import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements RemoteInterface {
    public String sayHello(String name) {
        return "Hello, " + name + " from the server!";
    }

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "10.100.200.104");

            Server obj = new Server();
            RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(1099); // default RMI port
            registry.rebind("HelloService", stub);

            System.out.println("Server ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
